/*
 * Copyright (c) 2016, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.demo.credit.service;

import org.demo.credit.dao.CreditDAO;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * This is the Microservice resource class.
 * See <a href="https://github.com/wso2/msf4j#getting-started">https://github.com/wso2/msf4j#getting-started</a>
 * for the usage of annotations.
 *
 * @since 0.1-SNAPSHOT
 */
@Path("/creditservice")
public class CreditService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreditService.class);

    @GET
    @Path("/{id}")
    public Response getOutstandingBalance(@PathParam("id") String id) {

        LOGGER.info("getOutstandingBalance invoked.");
        CreditDAO creditDAO = new CreditDAO();
        double totalOutstandingBalance = creditDAO.getCustomerOutstandingBalance(id);

        JSONObject returnObject = new JSONObject();
        LOGGER.info("OutstandingBalance : " + totalOutstandingBalance);
        returnObject.put("outstandingbalance", totalOutstandingBalance);
        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }

}

