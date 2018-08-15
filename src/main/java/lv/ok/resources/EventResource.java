package lv.ok.resources;

import lv.ok.models.Event;
import lv.ok.service.EventServiceImpl;
import lv.ok.service.IEventService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("event")
public class EventResource {

    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAll() {
        IEventService eventService = new EventServiceImpl();
        return Response.status(200).
                entity(eventService.getAllEvents()).
                header("Access-Control-Allow-Origin", "*").build();
    }

    @POST
    @Path("add")
    @Produces("application/json")
    public Response post(Event event) {
        IEventService eventService = new EventServiceImpl();
        eventService.addEvent(event);
        return Response.ok().entity(event).
                header("Access-Control-Allow-Origin", "*").build();
    }

    @DELETE
    @Path("delete/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response delete(@PathParam("id") String catId) {
        IEventService catService = new EventServiceImpl();
        catService.deleteEvent(catId);
        return Response.ok().entity("Event was deleted").header("Access-Control-Allow-Origin", "*").build();
    }

    @PUT
    @Path("update/{id}")
    @Produces("application/json")
    public Response update(@PathParam("id") String eventId, Event event) {
        IEventService eventService = new EventServiceImpl();
        eventService.updateEvent(eventId, event);
        return Response.ok().entity(event).header("Access-Control-Allow-Origin", "*").build();
    }
}
