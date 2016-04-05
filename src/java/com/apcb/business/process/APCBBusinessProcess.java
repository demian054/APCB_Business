/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.apcb.business.process;


import com.apcb.paymentshandler.services.PaymentsHandlerServices;
import com.apcb.ticketshandler.services.TicketsHandlerServices;
import com.apcb.utils.conection.ServiceGenerator;
import com.apcb.utils.entities.Message;
import com.apcb.utils.entities.Request;
import com.apcb.utils.entities.Response;
import com.apcb.utils.paymentHandler.enums.PaymentTypeEnum;
import com.apcb.utils.ticketsHandler.entities.APCB_Itinerary;
import com.apcb.utils.ticketsHandler.entities.APCB_ItineraryOption;
import com.apcb.utils.ticketsHandler.entities.APCB_Tax;
import com.apcb.utils.ticketsHandler.enums.MessagesTypeEnum;
import com.apcb.utils.ticketsHandler.entities.APCB_Travel;
import com.google.gson.Gson;
import java.io.IOException;
import java.math.BigDecimal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Demian
 */
public class APCBBusinessProcess {
    private Logger log = LogManager.getLogger(APCBBusinessProcess.class);
    private Gson gson = new Gson(); 

    public Response ticketAvail(Request request) throws IOException, Exception  {
        log.info("APCBBusinessProcess -> ticketAvail ini");
        Response response = new Response(request.getSesionId());
         TicketsHandlerServices ticketHandlerServices = ServiceGenerator.ServiceGenerator(TicketsHandlerServices.class);
        if (ticketHandlerServices==null){
            response.setMessage(new Message(MessagesTypeEnum.ErrorAccess_TicketsHandler));
            return response;
        }
        response = gson.fromJson(ticketHandlerServices.ticketAirAvail(gson.toJson(request)),Response.class);  
        return response;
    }

    public Response ticketPrice(Request request) throws IOException, Exception  {
        log.info("APCBBusinessProcess -> ticketPrice ini");
        Response response = new Response(request.getSesionId());
         TicketsHandlerServices ticketHandlerServices = ServiceGenerator.ServiceGenerator(TicketsHandlerServices.class);
        if (ticketHandlerServices==null){
            response.setMessage(new Message(MessagesTypeEnum.ErrorAccess_TicketsHandler));
            return response;
        }
        response = gson.fromJson(ticketHandlerServices.ticketAirPrice(gson.toJson(request)),Response.class);  
        return response;
    }
    

    public Response ticketReserv(Request request) throws IOException, Exception  {
        log.info("APCBBusinessProcess -> ticketReserv ini");
        Response response = new Response(request.getSesionId());
         TicketsHandlerServices ticketHandlerServices = ServiceGenerator.ServiceGenerator(TicketsHandlerServices.class);
        if (ticketHandlerServices==null){
            response.setMessage(new Message(MessagesTypeEnum.ErrorAccess_TicketsHandler));
            return response;
        }
        response = gson.fromJson(ticketHandlerServices.ticketAirReserv(gson.toJson(request)),Response.class);  
        return response;
    }
    
    public Response ticketDemand(Request request) throws IOException, Exception  {
        log.info("APCBBusinessProcess -> ticketDemand ini");
        Response response = new Response(request.getSesionId());
         TicketsHandlerServices ticketHandlerServices = ServiceGenerator.ServiceGenerator(TicketsHandlerServices.class);
        if (ticketHandlerServices==null){
            response.setMessage(new Message(MessagesTypeEnum.ErrorAccess_TicketsHandler));
            return response;
        }
        response = gson.fromJson(ticketHandlerServices.ticketAirDemand(gson.toJson(request)),Response.class);  
        return response;
    }
    
    public Response ticketCancel(Request request) throws IOException, Exception  {
        log.info("APCBBusinessProcess -> ticketCancel ini");
        Response response = new Response(request.getSesionId());
         TicketsHandlerServices ticketHandlerServices = ServiceGenerator.ServiceGenerator(TicketsHandlerServices.class);
        if (ticketHandlerServices==null){
            response.setMessage(new Message(MessagesTypeEnum.ErrorAccess_TicketsHandler));
            return response;
        }
        response = gson.fromJson(ticketHandlerServices.ticketAirCancel(gson.toJson(request)),Response.class);  
        return response;
    }
    
     public Response ticketConsultReserv(Request request)throws  IOException, Exception  {
         log.info("APCBBusinessProcess -> ticketConsult ini");
        Response response = new Response(request.getSesionId());
         TicketsHandlerServices ticketHandlerServices = ServiceGenerator.ServiceGenerator(TicketsHandlerServices.class);
        if (ticketHandlerServices==null){
            response.setMessage(new Message(MessagesTypeEnum.ErrorAccess_TicketsHandler));
            return response;
        }
        response = gson.fromJson(ticketHandlerServices.ticketAirConsultReserv(gson.toJson(request)),Response.class);  
        return response;
    }
     
    public Response ticketConsultTickets(Request request)throws  IOException, Exception  {
         log.info("APCBBusinessProcess -> ticketConsult ini");
        Response response = new Response(request.getSesionId());
         TicketsHandlerServices ticketHandlerServices = ServiceGenerator.ServiceGenerator(TicketsHandlerServices.class);
        if (ticketHandlerServices==null){
            response.setMessage(new Message(MessagesTypeEnum.ErrorAccess_TicketsHandler));
            return response;
        }
        response = gson.fromJson(ticketHandlerServices.ticketAirConsultTickets(gson.toJson(request)),Response.class);  
        return response;
    }
        
    
    public Response payCreate(Request request) throws  IOException, Exception  {
         log.info("APCBBusinessProcess -> payCreate ini");
        Response response = new Response(request.getSesionId());
        PaymentsHandlerServices paymentsHandlerServices = ServiceGenerator.ServiceGenerator(PaymentsHandlerServices.class);
        if (paymentsHandlerServices==null){
            response.setMessage(new Message(MessagesTypeEnum.ErrorAccess_PaymentHandler));
            return response;
        }
        response = gson.fromJson(paymentsHandlerServices.createPay(gson.toJson(request)),Response.class);  
        return response;
    }
    
    public Response payComplete(Request request) throws  IOException, Exception {
        log.info("APCBBusinessProcess -> payComplete ini");
        Response response = new Response(request.getSesionId());
        PaymentsHandlerServices paymentsHandlerServices = ServiceGenerator.ServiceGenerator(PaymentsHandlerServices.class);
        if (paymentsHandlerServices==null){
            response.setMessage(new Message(MessagesTypeEnum.ErrorAccess_PaymentHandler));
            return response;
        }
        response = gson.fromJson(paymentsHandlerServices.completePay(gson.toJson(request)),Response.class);  
        return response;
    }
        
    public Response payAnnular(Request request)throws IOException, Exception  {
        log.info("APCBBusinessProcess -> payComplete ini");
        Response response = new Response(request.getSesionId());
        PaymentsHandlerServices paymentsHandlerServices = ServiceGenerator.ServiceGenerator(PaymentsHandlerServices.class);
        if (paymentsHandlerServices==null){
            response.setMessage(new Message(MessagesTypeEnum.ErrorAccess_PaymentHandler));
            return response;
        }
        response = gson.fromJson(paymentsHandlerServices.annularPay(gson.toJson(request)),Response.class);  
        return response;
    }

    public boolean validateCostAndDisponibility(Request request) throws IOException, Exception  {
        Response responsePrice = ticketPrice(request);
        BigDecimal totalAmountNeto = new BigDecimal(0);
        BigDecimal totalTaxAmount = new BigDecimal(0);
        //BigDecimal total = new BigDecimal(0); 
        if(responsePrice.getMessage().getMsgCode().equals(MessagesTypeEnum.Ok.getCode())){
            for (APCB_Itinerary itinerary : responsePrice.getTravelInfo().getItinerary()) {
                totalAmountNeto = totalAmountNeto.add(BigDecimal.valueOf(itinerary.getTotalCost().getTotalAmount()));
                for ( APCB_Tax tax:itinerary.getTotalCost().getTaxes()){
                    totalTaxAmount = totalTaxAmount.add(BigDecimal.valueOf(tax.getAmount()));
                }
            }

            //total = totalTaxAmount.add(totalAmountNeto);
            if (totalAmountNeto.compareTo(BigDecimal.valueOf(request.getPayMainInfo().getAmount()))!=0){
               log.error("Found Anomali in totalCost from TicketHandler="+totalAmountNeto+" From Client="+request.getPayMainInfo().getAmount());
               // TODO alertar con correo para investigacion por hack;
               return false;
            }
            
            if (totalTaxAmount.compareTo(BigDecimal.valueOf(request.getPayMainInfo().getTaxesAmount()))!=0){
               log.error("Found Anomali in totalTaxAmount from TicketHandler="+totalTaxAmount+" From Client="+request.getPayMainInfo().getTaxesAmount());
               // TODO alertar con correo para investigacion por hack;
               return false;
            }

            //validamos los precios
            return true;
        } else {
            responsePrice.setMessage(new Message(MessagesTypeEnum.ErrorValidate_CostOrDisponibility));
            log.error(responsePrice.getMessage().getMsgDesc());
            return false;
        }
    }

      
}
