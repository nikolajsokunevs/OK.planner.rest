package lv.ok.repository.codecs;

import lv.ok.models.User;
import lv.ok.utils.Constants;
import lv.ok.utils.HashPassword;
import org.bson.*;
import org.bson.codecs.*;
import org.bson.types.ObjectId;

import java.util.Date;


public class UserCodec implements CollectibleCodec<User> {


    private Codec<Document> documentCodec;

    public UserCodec(Codec<Document> codec) {
        this.documentCodec = codec;
    }


    public Document deviceToDocument(User user) {
        Document document = new Document();
        String id = user.getId();
        String username = user.getUsername();
        String password = HashPassword.createHash(user.getPassword());
        String company = user.getCompany();
        Date dateCreated = user.getDateCreated();


        if (null != id) document.put(Constants._ID, id);
        if (null != username) document.put(Constants.USERNAME, username);
        if (null != password) document.put(Constants.PASSWORD, password);
        if (null != company) document.put(Constants.COMPANY, company);
        if (null != dateCreated) document.put("dateCreated", dateCreated);

        return document;
    }

    public User documentToUser(Document document){
        User user = new User();
        user.setId(document.getString(Constants._ID));
        user.setUsername(document.getString(Constants.USERNAME));
        user.setPassword(document.getString(Constants.PASSWORD));
        user.setCompany(document.getString(Constants.COMPANY));
        user.setDateCreated();

        return user;
    }

    @Override
    public Class<User> getEncoderClass() {
        return User.class;
    }

    @Override
    public void encode(BsonWriter writer, User value,
                       EncoderContext encoderContext) {
        Document document = deviceToDocument(value);
        documentCodec.encode(writer, document, encoderContext);
    }

    @Override
    public User decode(BsonReader reader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(reader, decoderContext);
        System.out.println(Constants.DOCUMENT + " " + document);
        User user = documentToUser(document);
        return user;
    }
    @Override
    public User generateIdIfAbsentFromDocument(User document) {
        User userWithId = document;
        if (userWithId.getId() == null) userWithId.setId(new ObjectId().toString());
        return userWithId;
    }

    @Override
    public boolean documentHasId(User document) {
        return null == document.getId();
    }

    @Override
    public BsonValue getDocumentId(User document) {
        if (!documentHasId(document)) {
            throw new IllegalStateException("The document does not contain an _id");
        }

        return new BsonString(document.getId());
    }
}
