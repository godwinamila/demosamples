/*
 * Copyright (c) 2017, WSO2 Inc. (http://wso2.com) All Rights Reserved.
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.SwaggerDefinition;
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
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * This is the Microservice resource class.
 * See <a href="https://github.com/wso2/msf4j#getting-started">https://github.com/wso2/msf4j#getting-started</a>
 * for the usage of annotations.
 *
 * @since 0.1-SNAPSHOT
 */
@Api(value = "loanservice")
@SwaggerDefinition(
        info = @Info(
                title = "Loan service Swagger Definition", version = "1.0",
                description = "The endpoint which is used to manage loan application",
                license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0")
        )
)
@Path("/loanservice")
public class LoanService {

    private static Log LOGGER = LogFactory.getLog(LoanService.class);

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Return all the loan applications. ",
            notes = "Returns HTTP 500 if an internal error occurs at the server")
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "{\"applications\":[{att1name:att1val,att2name:att2name},{att1name:att1val,att2name:att2name}]}"),
            @ApiResponse(code = 500, message = "Particular exception message") })
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
    @ApiOperation(
            value = "Return Laon application status for the reference number. ",
            notes = "Returns HTTP 404 if user doesn't exist")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "{status:NEW}"),
            @ApiResponse(code = 404, message = "Particular exception message")})
    public Response status(@ApiParam(value = "referenceNumber", required = true)
                           @PathParam("referenceNumber") String referenceNumber) {

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
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    @ApiOperation(
            value = "Create loan application and return loan reference number. ",
            notes = "Returns HTTP 404 if user doesn't exist")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "{referencenumber:HOU2017000012}"),
            @ApiResponse(code = 404, message = "Particular exception message")})
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
    @ApiOperation(
            value = "Approve loan application.",
            notes = "Returns HTTP 404 if loan application doesn't exist")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = ""),
            @ApiResponse(code = 404, message = "")})
    public Response approve(@ApiParam(value = "referenceNumber", required = true)
                            @PathParam("referenceNumber") String referenceNumber) {

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
    @ApiOperation(
            value = "Reject loan application.",
            notes = "Returns HTTP 404 if loan application doesn't exist")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = ""),
            @ApiResponse(code = 404, message = "")})
    public Response reject(@ApiParam(value = "referenceNumber", required = true)
                           @PathParam("referenceNumber") String referenceNumber) {

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
