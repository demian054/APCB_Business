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
import com.apcb.utils.ticketsHandler.entities.APCB_Travel;
import com.apcb.utils.ticketsHandler.enums.MessagesTypeEnum;
import com.google.gson.Gson;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author Demian
 */
@WebService(serviceName = "APCBBusinessServices")
public class APCBBusinessServices {
    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(APCBBusinessServices.class);
    private Gson gson = new Gson();
    /**
     * This is a sample web service operation
     * @param strRequest
     * @return 
     */
    @WebMethod(operationName = "ticketAirAvailAndPrice")
    public String ticketAirAvailAndPrice(@WebParam(name = "request") String strRequest) {
        log.info("APCBBusinessServices -> ticketAirAvailAndPrice ini");
        Response response = new Response();
        try {
            Request request = new Request(strRequest); 
            APCBBusinessProcess process = new APCBBusinessProcess();
            response = process.ticketAvail(request);
            if (response.getMessage().getMsgCode().equals("200")){
                APCB_Travel travel = response.getTravelInfo();
                if (travel.getItinerary().length>0){
                    request.setTravelInfo(response.getTravelInfo());
                    response = process.ticketPrice(request); 
                }
            }
        } catch (Exception e) {
            response.setMessage(new Message(MessagesTypeEnum.Error_AplicationErrorNotHandler));
            log.error(response.getMessage().getMsgDesc(), e);
        }
        log.info("APCBBusinessServices -> ticketAirAvailAndPrice end");
        return gson.toJson(response);
    }
    
    @WebMethod(operationName = "ticketReservAndPay")
    public String ticketReservAndPay(@WebParam(name = "request") String strRequest) {
        log.info("APCBBusinessServices -> ticketReservAndPay ini");
        Response response = new Response();
        try {
            Request request = new Request(strRequest); 
            APCBBusinessProcess process = new APCBBusinessProcess();
 
             log.info("APCBBusinessProcess -> ticketAirAvailAndPrice ini");
            boolean error = false;
           //Reserva de voleto
            response = process.ticketReserv(request);
            // Crear Pago
            if (response.getMessage().getMsgCode().equals("200")){
                response = process.payCreate(request);
            }
            // Crear Voleto
            if (response.getMessage().getMsgCode().equals("200")){
                response = process.ticketDemand(request);
            }
            // Completar Pago
            if (response.getMessage().getMsgCode().equals("200")){
                response = process.payComplete(request);
            }
            // Cancelar ticket
            if (!response.getMessage().getMsgCode().equals("200")){
                error = true;
                response = process.ticketCancel(request);

                if (response.getMessage().getMsgCode().equals("200")){
                    // Cancelar Pago
                    response = process.payAnnular(request);
                }
            }
            if (error){
                //TODO: enviar correo de error.
            }            
            
        } catch (Exception e) {
            response.setMessage(new Message(MessagesTypeEnum.Error_AplicationErrorNotHandler));
            log.error(response.getMessage().getMsgDesc(), e);
        }
        log.info("APCBBusinessServices -> ticketReservAndPay end");
        return gson.toJson(response);
    }
}
