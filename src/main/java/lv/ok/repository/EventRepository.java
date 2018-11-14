package lv.ok.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lv.ok.config.ApplicationProperties;
import lv.ok.models.Event;
import lv.ok.repository.codecs.EventCodec;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;


import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

import static lv.ok.config.ApplicationProperties.getString;

public class EventRepository extends BaseRepository {

    public static EventRepository instance;


    private EventRepository() {

        Codec<Document> defaultDocumentCodec = MongoClient.getDefaultCodecRegistry().get(Document.class);
        EventCodec eventCodec = new EventCodec(defaultDocumentCodec);
        codecRegistry = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(), CodecRegistries.fromCodecs(eventCodec));

        MongoClientURI uri = new MongoClientURI(getConnectionURL());
        MongoClient mongoClient = new MongoClient(uri);
        db = mongoClient.getDatabase(DB_NAME).withCodecRegistry(codecRegistry);
    }

    public static EventRepository getInstance() {
        if (instance != null) return instance;
        instance = new EventRepository();
        return instance;
    }

    public void insertEvent(Event event) {
        MongoCollection<Event> collection = db.getCollection("events", Event.class);
        collection.insertOne(event);
    }

    public List<Event> getAllDevices() {
        MongoCollection<Event> collection = db.getCollection("events", Event.class);
        List<Event> foundDocument = collection.find().into(new ArrayList<Event>());
        return foundDocument;
    }

    public void deleteEvent(String id) {
        MongoCollection<Event> collection = db.getCollection("events", Event.class);
        BasicDBObject searchObject = new BasicDBObject();
        searchObject.put("_id", id);
        collection.deleteOne(searchObject);
    }

    public void updateEvent(String id, Event event){
        deleteEvent(id);
        event.setId(id);
        insertEvent(event);
    }
}