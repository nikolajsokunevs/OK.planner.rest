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
    @Produces("application/json")
    public List<Event> getAll(){
        IEventService eventService=new EventServiceImpl();
        return eventService.getAllEvents();
    }

    @POST
    @Path("add")
    @Produces("application/json")
    public Response post(Event event){
        IEventService eventService=new EventServiceImpl();
        eventService.addEvent(event);
        return Response.ok().entity(event).build();
    }

    @DELETE
    @Path("delete/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response delete(@PathParam( "id" ) String catId){
        IEventService catService=new EventServiceImpl();
        catService.deleteEvent(catId);
        return Response.ok().entity("Event was deleted").build();
    }

    @PUT
    @Path("update/{id}")
    @Produces("application/json")
    public Response update(@PathParam( "id" ) String eventId, Event event){
        IEventService eventService=new EventServiceImpl();
        eventService.updateEvent(eventId, event);
        return Response.ok().entity(event).build();
    }
}
