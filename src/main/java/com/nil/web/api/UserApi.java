package com.nil.web.api;

import com.google.inject.Inject;
import com.nil.backend.user.UserService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("/api/user")
public class UserApi {

    private UserService userService;

    @Inject
    public UserApi(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Path("/list")
    @Produces({MediaType.APPLICATION_JSON})
    public Response list() {

        List<String> users = userService.getUsers();

        return Response.status(200).entity(users).build();

    }

    @GET
    @Path("/add")
    public Response add() {
        return Response.status(200).entity("safe?").build();
    }

}
