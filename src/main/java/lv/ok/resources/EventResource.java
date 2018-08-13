package lv.ok.resources;

import lv.ok.models.Event;
import lv.ok.service.EventServiceImpl;
import lv.ok.service.IEventService;

import javax.ws.rs.*;
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
}
