package lv.ok.service;

import lv.ok.models.Event;
import lv.ok.models.User;
import lv.ok.repository.EventRepository;

import java.util.List;

public class EventServiceImpl implements IEventService{

    private EventRepository eventRepository=EventRepository.getInstance();

    @Override
    public Event addEvent(Event device) {
        eventRepository.insertEvent(device);
        return null;
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.getAllDevices();
    }

    @Override
    public void deleteEvent(String id, String jwt) {
        eventRepository.deleteEvent(id, jwt);
    }

    @Override
    public void updateEvent(String id, Event event, String jwt, User user) {
        eventRepository.updateEvent(id, event, jwt, user);
    }
}
