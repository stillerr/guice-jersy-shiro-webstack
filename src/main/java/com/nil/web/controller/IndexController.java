package com.nil.web.controller;

import com.sun.jersey.api.view.Viewable;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;


@Path("/")
public class IndexController {

    @Context
    SecurityContext sc;

    @GET
    public Viewable renderPage() {
        return new Viewable("/index");
    }

    @GET
    @Path("/login")
    public Response login() {

        Subject currentUser = SecurityUtils.getSubject();

        if (!currentUser.isAuthenticated()) {

            UsernamePasswordToken token = new UsernamePasswordToken("root", "admin");
            token.setRememberMe(true);

            try {
                currentUser.login(token);
            }
            catch (AuthenticationException ae) {
                System.out.println(ae);
            }
        }

        return Response.status(200).entity("logined").build();
    }

    @GET
    @Path("/logout")
    public Response logout() {

        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();

        return Response.status(200).entity("logged out").build();
    }

    @GET
    @Path("/protected")
    public Response protectedPage() {
        Subject currentUser = SecurityUtils.getSubject();

        if(currentUser.isAuthenticated()) {
            return Response.status(200).entity("ok").build();
        }

        return Response.status(Response.Status.FORBIDDEN).build();
    }

}
