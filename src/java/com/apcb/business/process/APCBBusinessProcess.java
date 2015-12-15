/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.apcb.business.process;

import com.apcb.ticketshandler.services.TicketsHandlerServices;
import com.apcb.utils.conection.ServiceGenerator;
import com.apcb.utils.entities.Message;
import com.apcb.utils.entities.Request;
import com.apcb.utils.entities.Response;
import com.apcb.utils.ticketsHandler.Enums.MessagesTypeEnum;
import com.apcb.utils.ticketsHandler.entities.Travel;
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

    public Response ticketAirAvailAndPrice(Request request) throws IOException, Exception {
        Response response = new Response();
        TicketsHandlerServices ticketHandlerServices = ServiceGenerator.ServiceGenerator(TicketsHandlerServices.class);
        if (ticketHandlerServices==null){
            response.setMessage(new Message(MessagesTypeEnum.ErrorAccess_Business));
            return response;
        }
        response = gson.fromJson(ticketHandlerServices.ticketAirAvail(gson.toJson(request)),Response.class); 
        
        request.setBeam(response.getBeam());
        
        Travel travel = gson.fromJson(response.getBeam().getObjectStr(),Travel.class);
        
        if (travel.getItinerary().length>0){
            response = gson.fromJson(ticketHandlerServices.ticketAirPrice(gson.toJson(request)),Response.class); 
        }

        return response;
    }
    
}
