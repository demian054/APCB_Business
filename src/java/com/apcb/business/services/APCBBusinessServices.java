/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.apcb.business.services;

import com.apcb.business.process.APCBBusinessProcess;
import com.apcb.utils.entities.Message;
import com.apcb.utils.entities.Request;
import com.apcb.utils.entities.Response;
import com.apcb.utils.ticketsHandler.enums.MessagesTypeEnum;
import com.apcb.utils.utils.PropertiesReader;
import com.google.gson.Gson;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Demian
 */
@WebService(serviceName = "APCBBusinessServices")
public class APCBBusinessServices {
    private Logger log = LogManager.getLogger(APCBBusinessServices.class);
    private Gson gson = new Gson();
    /**
     * This is a sample web service operation
     * @param strRequest
     * @return 
     */
    @WebMethod(operationName = "ticketAirAvail")
    public String ticketAirAvail(@WebParam(name = "request") String strRequest) {
        log.info("APCBBusinessServices -> ticketAirAvailAndPrice ini -> "+gson.toJson(strRequest));
        Request request = new Request(strRequest); 
        Response response;
        try {
            APCBBusinessProcess process = new APCBBusinessProcess();
            response = process.ticketAvail(request);
            /*if (response.getMessage().getMsgCode().equals("200")){
                if (response.getTravelInfo().getItinerary().length>0){
                    request.setTravelInfo(response.getTravelInfo());
                    response = process.ticketPrice(request);
                } else {
                    log.info("Dont call ticketPrice by itineraryList.length > 0");
                    response = new Response(request.getSesionId());
                    response.setMessage(new Message(MessagesTypeEnum.ErrorValidate_NoItineraryOptionsAvailable));
                    log.error(response.getMessage().getMsgDesc());
                }
            }*/
        } catch (Exception e) {
            response = new Response(request.getSesionId());
            response.setMessage(new Message(MessagesTypeEnum.Error_AplicationErrorNotHandler));
            log.error(response.getMessage().getMsgDesc(), e);
        }
        String strResponse = gson.toJson(response);
        log.info("APCBBusinessServices -> ticketAirAvailAndPrice end -> "+strResponse);
        return strResponse;
    }
    
    @WebMethod(operationName = "ticketReservAndPay")
    public String ticketReservAndPay(@WebParam(name = "request") String strRequest) {
        log.info("APCBBusinessServices -> ticketReservAndPay ini");
        Request request = new Request(strRequest); 
        Response response = new Response(request.getSesionId());
        Response responseTicketsHandler = new Response(request.getSesionId());
        Response responsePaymentsHandler = new Response(request.getSesionId());
        Response responseReverse;
        try {
            PropertiesReader prop = new PropertiesReader("BusinessConfiguration");
            APCBBusinessProcess process = new APCBBusinessProcess();
            log.info("APCBBusinessProcess -> ticketReservAndPay ini");
            boolean error = false;
            
           //Validamo si todavia existe el boleto a reservar y si los montos son correctos acobrar
            if (prop.getProperty("ValidateAmountOfPay", false).equalsIgnoreCase("true") 
                    && !process.validateCostAndDisponibility(request)){
               response.setMessage(new Message(MessagesTypeEnum.ErrorValidate_CostOrDisponibility));
               log.error(response.getMessage().getMsgDesc());
            } else {
                //Reserva de voleto (Kiu)
                //request.setTravelInfo(responseTicketsHandler.getTravelInfo());
                responseTicketsHandler = process.ticketReserv(request);
                log.info("responseTicketsHandler -> ticketReserv ->"+gson.toJson(responseTicketsHandler));
                //request.setTravelInfo(responseTicketsHandler.getTravelInfo());

                // Consultar reserva (Kiu)
                if (responseTicketsHandler.getMessage().getMsgCode().equals("200") && 
                        prop.getProperty("ConsultReservBeforePay", false).equalsIgnoreCase("true")){
                    request.setTravelInfo(responseTicketsHandler.getTravelInfo());
                    responseTicketsHandler = process.ticketConsultReserv(request);
                    log.info("*************----------responseTicketsHandler -> ticketConsult ->"+gson.toJson(responseTicketsHandler));

                }
                // Crear Pago 
                if (responseTicketsHandler.getMessage().getMsgCode().equals("200")){
                    //request.setTravelInfo(response.getTravelInfo());
                    responsePaymentsHandler = process.payCreate(request);
                    log.info("*************----------responseTicketsHandler -> payCreate ->"+gson.toJson(responsePaymentsHandler));

                    // Crear Voleto (Kiu)
                    if (responseTicketsHandler.getMessage().getMsgCode().equals("200") && responsePaymentsHandler.getMessage().getMsgCode().equals("200")){
                        request.setTravelInfo(responseTicketsHandler.getTravelInfo());
                        request.setPayMainInfo(responsePaymentsHandler.getPayMainInfo());
                        responseTicketsHandler = process.ticketDemand(request);
                        log.info("*************----------responseTicketsHandler -> ticketDemand ->"+gson.toJson(responseTicketsHandler));

                    }
                    // Consultar Voleto (Kiu)
                    if (responseTicketsHandler.getMessage().getMsgCode().equals("200") && responsePaymentsHandler.getMessage().getMsgCode().equals("200")){
                        request.setTravelInfo(responseTicketsHandler.getTravelInfo());
                        request.setPayMainInfo(responsePaymentsHandler.getPayMainInfo());
                        responseTicketsHandler = process.ticketConsultTickets(request);
                        log.info("*************----------responseTicketsHandler -> ticketConsult ->"+gson.toJson(responseTicketsHandler));

                    }
                    // Completar Pago
                    if (responseTicketsHandler.getMessage().getMsgCode().equals("200") && responsePaymentsHandler.getMessage().getMsgCode().equals("200")){
                        request.setTravelInfo(responseTicketsHandler.getTravelInfo());
                        request.setPayMainInfo(responsePaymentsHandler.getPayMainInfo());
                        responsePaymentsHandler = process.payComplete(request);
                        log.info("*************----------responseTicketsHandler -> payComplete ->"+gson.toJson(responsePaymentsHandler));
                    }
                    // Cancelar ticket (Kiu)
                    if (!responseTicketsHandler.getMessage().getMsgCode().equals("200") || !responsePaymentsHandler.getMessage().getMsgCode().equals("200")){
                        
                        if (!responseTicketsHandler.getMessage().getMsgCode().equals("200")){
                            request.getPayMainInfo().setDescription(request.getPayMainInfo().getDescription()+" "
                                    +responseTicketsHandler.getMessage().getMsgCode()+"-"
                                    +responseTicketsHandler.getMessage().getMsgDesc());
                        } else if (!responsePaymentsHandler.getMessage().getMsgCode().equals("200")){
                             request.getPayMainInfo().setDescription(request.getPayMainInfo().getDescription()+" "
                                    +responsePaymentsHandler.getMessage().getMsgCode()+"-"
                                    +responsePaymentsHandler.getMessage().getMsgDesc());
                        }
                        
                        //request = new Request();
                        error = true;
                        responseReverse = process.ticketCancel(request);
                        log.info("*************----------responseTicketsHandler -> ticketCancel ->"+gson.toJson(responseReverse));

                        if (responseReverse.getMessage().getMsgCode().equals("200")){
                            // Cancelar Pago
                            responseReverse = process.payAnnular(request);
                            log.info("*************----------responseReverse -> payAnnular ->"+gson.toJson(responseReverse));
                        }
                    } else {
                        if (prop.getProperty("CancelAllTickets", false).equalsIgnoreCase("true")){
                            responseReverse = process.ticketCancel(request);
                        }
                        if (prop.getProperty("CancelAllPays", false).equalsIgnoreCase("true")){
                            responseReverse = process.payAnnular(request);
                        }                
                    }
                }
                if (!responseTicketsHandler.getMessage().getMsgCode().equals("200")){
                    log.error("responseTicketsHandler --> error --> "+responseTicketsHandler.getMessage().getMsgCode());
                    response.setMessage(responseTicketsHandler.getMessage());
                } else if (!responsePaymentsHandler.getMessage().getMsgCode().equals("200")){
                     log.error("responsePaymentsHandler --> error --> "+responsePaymentsHandler.getMessage().getMsgCode());
                     response.setMessage(responsePaymentsHandler.getMessage());
                } else {
                    response.setSesionId(request.getSesionId());
                    response.setTravelInfo(responseTicketsHandler.getTravelInfo());
                    response.setPayMainInfo(responsePaymentsHandler.getPayMainInfo());
                }   
            }
            
        } catch (Exception e) {
            response = new Response(request.getSesionId());
            response.setMessage(new Message(MessagesTypeEnum.Error_AplicationErrorNotHandler));
            log.error(response.getMessage().getMsgDesc(), e);
        }
        String strResponse = gson.toJson(response);
        log.info("APCBBusinessServices -> ticketReservAndPay end -> "+strResponse);
        return strResponse;
    }
    
     
    @WebMethod(operationName = "consultReserv")
    public String consultReserv(@WebParam(name = "request") String strRequest) {
        log.info("APCBBusinessServices -> consultReserv ini");
        Request request = new Request(strRequest); 
        Response response;
        try {
            APCBBusinessProcess process = new APCBBusinessProcess();
            response = process.ticketConsultReserv(request);
        } catch (Exception e) {
            response = new Response(request.getSesionId());
            response.setMessage(new Message(MessagesTypeEnum.Error_AplicationErrorNotHandler));
            log.error(response.getMessage().getMsgDesc(), e);
        }
        log.info("APCBBusinessServices -> consultReserv end");
        return gson.toJson(response);  
    }
    
        @WebMethod(operationName = "consultTicket")
    public String consultTicket(@WebParam(name = "request") String strRequest) {
        log.info("APCBBusinessServices -> consultTicket ini");
        Request request = new Request(strRequest); 
        Response response;
        try {
            APCBBusinessProcess process = new APCBBusinessProcess();
            response = process.ticketConsultTickets(request);
        } catch (Exception e) {
            response = new Response(request.getSesionId());
            response.setMessage(new Message(MessagesTypeEnum.Error_AplicationErrorNotHandler));
            log.error(response.getMessage().getMsgDesc(), e);
        }
        log.info("APCBBusinessServices -> consultTicket end");
        return gson.toJson(response);  
    }
    
    
    @WebMethod(operationName = "ticketAirPrice")
    public String ticketAirPrice(@WebParam(name = "request") String strRequest) {
        log.info("APCBBusinessServices -> ticketAirPrice ini -> "+gson.toJson(strRequest));
        Request request = new Request(strRequest); 
        Response response;
        try {
            APCBBusinessProcess process = new APCBBusinessProcess();
            response = process.ticketPrice(request);
        } catch (Exception e) {
            response = new Response(request.getSesionId());
            response.setMessage(new Message(MessagesTypeEnum.Error_AplicationErrorNotHandler));
            log.error(response.getMessage().getMsgDesc(), e);
        }
        String strResponse = gson.toJson(response);
        log.info("APCBBusinessServices -> ticketAirPrice end -> "+strResponse);
        return strResponse;
    }
}
