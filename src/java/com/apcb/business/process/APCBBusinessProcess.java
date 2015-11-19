/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.apcb.business.process;

import com.apcb.ticketshandler.services.TicketHandlerServices;
import com.apcb.utils.conection.ServiceGenerator;
import com.apcb.utils.entities.Message;
import com.apcb.utils.entities.PropertiesReader;
import com.apcb.utils.entities.Request;
import com.apcb.utils.entities.Response;
import com.apcb.utils.ticketsHandler.Enums.MessagesTypeEnum;
import com.google.gson.Gson;
import java.io.IOException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Demian
 */
public class APCBBusinessProcess {
    private static final Logger log = LogManager.getLogger(APCBBusinessProcess.class);
    private final Gson gson = new Gson(); 

    public Response ticketAirAvailAndPrice(Request request) throws IOException, Exception {
 
        TicketHandlerServices ticketHandlerServices = ServiceGenerator.ServiceGenerator(TicketHandlerServices.class);
        Response response = gson.fromJson(ticketHandlerServices.ticketAirAvail(gson.toJson(request)),Response.class);

        return response;
    }
    
}