package lv.ok.resources;

import lv.ok.models.User;
import lv.ok.resources.responses.LoginResponse;
import lv.ok.service.IUserService;
import lv.ok.service.UserServiceImpl;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("user")
public class UserResource {


    @POST
    @Path("signUp")
    @PermitAll
    @Produces({MediaType.APPLICATION_JSON})
    public Response createUser(User user) {
        IUserService userService = new UserServiceImpl();
        String result = userService.signUpUser(user);
        return Response.ok(result).build();
    }

    @DELETE
    @Path("delete/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response delete(@PathParam("id") String userId) {
        IUserService userService = new UserServiceImpl();
        userService.deleteUser(userId);
        return Response.ok().entity("User " + userId + " has been deleted").header("Access-Control-Allow-Origin", "*").build();
    }

    @POST
    @Path("signIn")
    @PermitAll
    @Produces({MediaType.APPLICATION_JSON})
    public Response login(User user) {
        IUserService userService = new UserServiceImpl();
        LoginResponse result = userService.signIn(user);
        return Response.ok(result).build();
    }
}
