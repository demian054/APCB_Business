/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.apcb.business.services;

import com.apcb.business.process.APCBBusinessProcess;
import com.apcb.ticketshandler.services.TicketHandlerServices;
import com.apcb.ticketshandler.services.TicketHandlerServices_Service;
import com.apcb.utils.conection.ConectionHttpsURL;
import com.apcb.utils.conection.ServiceGenerator;
import com.apcb.utils.entities.Message;
import com.apcb.utils.entities.PropertiesReader;
import com.apcb.utils.entities.Request;
import com.apcb.utils.entities.Response;
import com.apcb.utils.ticketsHandler.Enums.MessagesTypeEnum;
import com.google.gson.Gson;
import java.io.IOException;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author Demian
 */
@WebService(serviceName = "APCBBusinessServices")
public class APCBBusinessServices {
    private final Gson gson = new Gson();
    private final static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(APCBBusinessServices.class);
    /**
     * This is a sample web service operation
     * @param strRequest
     * @return 
     */
    @WebMethod(operationName = "ticketAirAvailAndPrice")
    public String ticketAirAvailAndPrice(@WebParam(name = "request") String strRequest) {
        Response response = new Response();
        try {
            Request request = new Request(strRequest); 
            APCBBusinessProcess process = new APCBBusinessProcess();
            response = process.ticketAirAvailAndPrice(request);
        } catch (Exception e) {
            response.setMessage(new Message(MessagesTypeEnum.AplicationErrorNotHandler));
            log.error(response.getMessage().getMsgDesc(), e);
        }
        return gson.toJson(response);
    }
}
