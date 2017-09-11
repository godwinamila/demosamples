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

package org.demo.loan.service;

import io.swagger.annotations.ApiParam;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.demo.loan.ApplicationStatus;
import org.demo.loan.bean.ApplicationBean;
import org.demo.loan.dao.LoanApplicationDAO;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
@Path("/loanservice")
public class LoanService {

    private static Log LOGGER = LogFactory.getLog(LoanService.class);

    @GET
    @Path("/")
    public Response getAllLoanApplication() {

        LOGGER.info("Get all loan application invoked");
        LoanApplicationDAO applicationDAO = new LoanApplicationDAO();
        List<ApplicationBean> applicationBeanList = applicationDAO.getAllLaonApplications();

        JSONArray jsonArray = new JSONArray(applicationBeanList.toArray());
        JSONObject returnObject = new JSONObject();
        returnObject.put("applications", jsonArray);
        return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
    }

    @GET
    @Path("/status/{referenceNumber}")
    public Response status(@PathParam("referenceNumber") String referenceNumber) {

        LOGGER.info("Get loan status invoked for reference number : " + referenceNumber);
        LoanApplicationDAO applicationDAO = new LoanApplicationDAO();
        String status = applicationDAO.getApplicationStatus(referenceNumber);
        JSONObject returnObject = new JSONObject();
        if (status != null) {
            returnObject.put("status", status);
            return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }
    }

    @POST
    @Consumes("application/json")
    @Path("/")
    public Response create(@ApiParam(value = "Application object", required = true) ApplicationBean application) {

        LOGGER.info("Application creation invoked.");
        LoanApplicationDAO applicationDAO = new LoanApplicationDAO();
        String referenceNumber = applicationDAO.createApplication(application);
        JSONObject returnObject = new JSONObject();

        if (referenceNumber != null) {
            returnObject.put("referencenumber", referenceNumber);
            return Response.status(Response.Status.OK).entity(returnObject.toString()).build();
        } else {
            returnObject.put("referencenumber", "");
            return Response.status(Response.Status.NOT_FOUND).entity(returnObject.toString()).build();
        }
    }

    @POST
    @Path("/approve/{referenceNumber}")
    public Response approve(@PathParam("referenceNumber") String referenceNumber) {

        LOGGER.info("Application approve invoked for reference number : " + referenceNumber);
        LoanApplicationDAO applicationDAO = new LoanApplicationDAO();
        boolean status = applicationDAO.UpdateStatus(referenceNumber, ApplicationStatus.APPROVED.toString());

        if (status) {
            return Response.status(Response.Status.OK).entity("").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }
    }

    @POST
    @Path("/reject/{referenceNumber}")
    public Response reject(@PathParam("referenceNumber") String referenceNumber) {

        LOGGER.info("Application reject invoked for reference number : " + referenceNumber);
        LoanApplicationDAO applicationDAO = new LoanApplicationDAO();
        boolean status = applicationDAO.UpdateStatus(referenceNumber, ApplicationStatus.REJECTED.toString());

        if (status) {
            return Response.status(Response.Status.OK).entity("").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }
    }
}
