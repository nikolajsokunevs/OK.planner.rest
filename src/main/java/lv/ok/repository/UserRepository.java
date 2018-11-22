package lv.ok.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.eq;
import lv.ok.models.User;
import lv.ok.repository.codecs.UserCodec;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;


public class UserRepository extends BaseRepository{

    public static UserRepository instance;

    private UserRepository() {

        Codec<Document> defaultDocumentCodec = MongoClient.getDefaultCodecRegistry().get(Document.class);
        UserCodec userCodec = new UserCodec(defaultDocumentCodec);
        codecRegistry = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(), CodecRegistries.fromCodecs(userCodec));

        MongoClientURI uri = new MongoClientURI(getConnectionURL());
        MongoClient mongoClient = new MongoClient(uri);
        db = mongoClient.getDatabase(DB_NAME).withCodecRegistry(codecRegistry);
    }

    public static UserRepository getInstance() {
        if (instance != null) return instance;
        instance = new UserRepository();
        return instance;
    }

    public void insertUser(User user) {
        MongoCollection<User> collection = db.getCollection("users", User.class);
        collection.insertOne(user);
    }

    public void deleteUser(String id) {
        MongoCollection<User> collection = db.getCollection("users", User.class);
        BasicDBObject searchObject = new BasicDBObject();
        searchObject.put("_id", id);
        collection.deleteOne(searchObject);
    }

    public boolean checkIfUsernameExists(String usernameValue) {
        MongoCollection<User> collection = db.getCollection("users", User.class);
        User user = collection.find(eq("username", usernameValue)).first();
        if (user == null)
            return false;
        else
            return true;
    }

    public String getPasswordHash(String usernameValue) {
        MongoCollection<User> collection = db.getCollection("users", User.class);
        User user = collection.find(eq("username", usernameValue)).first();
        return user.getPassword();
    }




}
