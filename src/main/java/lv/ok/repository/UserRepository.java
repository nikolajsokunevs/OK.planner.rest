package lv.ok.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.eq;

import lv.ok.models.User;
import lv.ok.repository.codecs.UserCodec;
import lv.ok.utils.Constants;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;


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
        MongoCollection<User> collection = db.getCollection(Constants.USERS, User.class);
        collection.insertOne(user);
    }

    public void deleteUser(String id) {
        MongoCollection<User> collection = db.getCollection(Constants.USERS, User.class);
        BasicDBObject searchObject = new BasicDBObject();
        searchObject.put(Constants._ID, id);
        collection.deleteOne(searchObject);
    }

    public boolean checkIfUsernameExists(String usernameValue) {
        MongoCollection<User> collection = db.getCollection(Constants.USERS, User.class);
        User user = collection.find(eq(Constants.USERNAME, usernameValue)).first();
        if (user == null)
            return false;
        else
            return true;
    }

    public String getPasswordHash(String usernameValue) {
        MongoCollection<User> collection = db.getCollection(Constants.USERS, User.class);
        User user = collection.find(eq(Constants.USERNAME, usernameValue)).first();
        return user.getPassword();
    }

    public boolean verifyEmailHash(String usernameValue, String hashValue) {
        MongoCollection<User> collection = db.getCollection(Constants.USERS, User.class);
        User user = collection.find(eq(Constants.USERNAME, usernameValue)).first();
        if(user.getEmailVerificationHash() == hashValue) {
            return true;
        }
        else {
            return false;
        }
    }




}
