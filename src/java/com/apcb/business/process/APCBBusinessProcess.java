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
import com.apcb.utils.ticketsHandler.enums.MessagesTypeEnum;
import com.apcb.utils.ticketsHandler.entities.APCB_Travel;
import com.google.gson.Gson;
import java.io.IOException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

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
    
     public Response ticketConsult(Request request)throws  IOException, Exception  {
         log.info("APCBBusinessProcess -> ticketConsult ini");
        Response response = new Response(request.getSesionId());
         TicketsHandlerServices ticketHandlerServices = ServiceGenerator.ServiceGenerator(TicketsHandlerServices.class);
        if (ticketHandlerServices==null){
            response.setMessage(new Message(MessagesTypeEnum.ErrorAccess_TicketsHandler));
            return response;
        }
        response = gson.fromJson(ticketHandlerServices.ticketAirConsult(gson.toJson(request)),Response.class);  
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
        if(responsePrice.getMessage().getMsgCode().equals(MessagesTypeEnum.Ok.getCode())){
            //validamos los precios
            return true;
        } else {
            // no hay dispibilidad
            return false;
        }
    }

      
}
