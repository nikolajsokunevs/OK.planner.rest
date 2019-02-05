package lv.ok.repository.codecs;

import lv.ok.models.Event;

import lv.ok.utils.Constants;
import org.bson.*;
import org.bson.codecs.*;
import org.bson.types.ObjectId;

public class EventCodec implements CollectibleCodec<Event> {

    private Codec<Document> documentCodec;

    public EventCodec() {
        this.documentCodec = new DocumentCodec();
    }

    public EventCodec(Codec<Document> codec) {
        this.documentCodec = codec;
    }

    @Override
    public void encode(BsonWriter writer, Event value,
                       EncoderContext encoderContext) {
        Document document = deviceToDocument(value);
        documentCodec.encode(writer, document, encoderContext);
    }

    public Document deviceToDocument(Event event) {
        Document document = new Document();
        String id = event.getId();

        String title= event.getTitle();
        String clientName=event.getClientName();
        String clientLastName=event.getClientLastName();
        String cliectPhoneNumber=event.getCliectPhoneNumber();
        String master=event.getMaster();
        String start= event.getStart();
        String end= event.getEnd();
        Boolean allDay= event.isAllDay();
        String color= event.getColor();
        String tbackgroundColoritle= event.getTbackgroundColoritle();
        String borderColor= event.getBorderColor();
        String textColor= event.getTextColor();

        if (null != id) document.put(Constants._ID, id);
        if (null != title) document.put("title", title);
        if (null != clientName) document.put("clientName", clientName);
        if (null != clientLastName) document.put("clientLastName", clientLastName);
        if (null != cliectPhoneNumber) document.put("cliectPhoneNumber", cliectPhoneNumber);
        if (null != master) document.put("master", master);
        if (null != start) document.put("start", start);
        if (null != end) document.put("end", end);
        if (null != allDay) document.put("allDay", allDay);
        if (null != color) document.put("color", color);
        if (null != tbackgroundColoritle) document.put("tbackgroundColoritle", tbackgroundColoritle);
        if (null != borderColor) document.put("borderColor", borderColor);
        if (null != textColor) document.put("textColor", textColor);
        return document;
    }


    @Override
    public Class<Event> getEncoderClass() {
        return Event.class;
    }

    @Override
    public Event decode(BsonReader reader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(reader, decoderContext);
        System.out.println(Constants.DOCUMENT + " " + document);
        Event event = documentToEvent(document);
        return event;
    }

    public Event documentToEvent(Document document){
        Event event = new Event();
        event.setId(document.getString(Constants._ID));
        event.setTitle(document.getString("title"));
        event.setClientName(document.getString("clientName"));
        event.setClientLastName(document.getString("clientLastName"));
        event.setCliectPhoneNumber(document.getString("cliectPhoneNumber"));
        event.setMaster(document.getString("master"));
        event.setStart(document.getString("start"));
        event.setEnd(document.getString("end"));
        event.setAllDay(document.getBoolean("allDay"));
        event.setColor(document.getString("color"));
        event.setTbackgroundColoritle(document.getString("tbackgroundColoritle"));
        event.setBorderColor(document.getString("borderColor"));
        event.setTextColor(document.getString("textColor"));
        return event;
    }

    @Override
    public Event generateIdIfAbsentFromDocument(Event document) {
        Event eventWithId = document;
        if (eventWithId.getId() == null) eventWithId.setId(new ObjectId().toString());
        return eventWithId;
    }

    @Override
    public boolean documentHasId(Event document) {
        return null == document.getId();
    }

    @Override
    public BsonValue getDocumentId(Event document) {
        if (!documentHasId(document)) {
            throw new IllegalStateException("The document does not contain an _id");
        }

        return new BsonString(document.getId());
    }
}