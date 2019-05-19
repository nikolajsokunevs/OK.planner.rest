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

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
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

    public void sendAuthenticationEmail(String usernameValue) {
        try {
            String host = "smtp.gmail.com";
            String from = "mark.gusman11@gmail.com";
            String to = usernameValue;
//                String user2 = "xakim@inbox.lv";
//                String from = "xakim@inbox.lv";
//                String host = "mail.inbox.lv";
            String database = "planitnow";

            Properties properties = System.getProperties();
            properties.put("mail.smtp.starttls.enable", Constants.TRUE);
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", Constants.TRUE);
            properties.put("mail.smtp.starttls.required", Constants.TRUE);

            java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

            Session mailSession = Session.getDefaultInstance(properties, null);
            mailSession.setDebug(false);
            Message msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject("Verify your Planit email");
            msg.setSentDate(new Date());
            msg.setText("Please navigate to " + "<url>" + "to verify your email address");
            Transport transport = mailSession.getTransport("smtp");
            transport.connect(host, from, database);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
        }
        catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }




}
