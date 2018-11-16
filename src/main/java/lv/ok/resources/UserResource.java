package lv.ok.resources;

import lv.ok.models.User;
import lv.ok.service.IUserService;
import lv.ok.service.UserServiceImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("user")
public class UserResource {


    @POST
    @Path("add")
    @Produces({MediaType.APPLICATION_JSON})
    public Response createUser(User user) {
        IUserService userService = new UserServiceImpl();
        userService.addUser(user);
        return Response.ok("User " + user.getId() + " has been added successfully").build();
    }

    @DELETE
    @Path("delete/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response delete(@PathParam("id") String userId) {
        IUserService userService = new UserServiceImpl();
        userService.deleteUser(userId);
        return Response.ok().entity("User " + userId + " has been deleted").header("Access-Control-Allow-Origin", "*").build();

        // return Response.ok().entity(String.format("User %d has been deleted", userId)).header("Access-Control-Allow-Origin", "*").build();
    }
}
