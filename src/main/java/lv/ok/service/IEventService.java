package lv.ok.service;

import lv.ok.models.Event;

import java.util.List;

public interface IEventService {

    Event addEvent(Event event);
    List<Event> getAllEvents();
}
