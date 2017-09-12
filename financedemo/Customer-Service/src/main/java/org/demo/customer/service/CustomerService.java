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

package org.demo.customer.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.demo.customer.bean.CustomerBean;
import org.demo.customer.dao.CustomerDAO;
import org.json.JSONObject;

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
@Path("/customerservice")
public class CustomerService {

    private static Log LOGGER = LogFactory.getLog(CustomerService.class);
    @GET
    @Path("/{id}")
    public Response getCustomer(@PathParam("id") String id) {

        LOGGER.info("Get customer invoked.");
        CustomerDAO customerDAO = new CustomerDAO();
        CustomerBean customerBean = customerDAO.getCustomer(id);

        if (customerBean != null) {
            JSONObject returnObject = new JSONObject();
            returnObject.put("id", customerBean.getId());
            returnObject.put("firstname", customerBean.getFname());
            returnObject.put("lastname", customerBean.getLname());
            returnObject.put("addrress", customerBean.getAddress());
            returnObject.put("state", customerBean.getState());
            returnObject.put("postalcode", customerBean.getPostalcode());
            //returnObject.put("country", customerBean.getCountry());
            return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }
    }

}
