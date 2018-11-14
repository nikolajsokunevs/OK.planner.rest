package lv.ok.repository.codecs;

import lv.ok.models.User;
import org.bson.*;
import org.bson.codecs.*;
import org.bson.types.ObjectId;

public class UserCodec implements CollectibleCodec<User> {


    private Codec<Document> documentCodec;

    public UserCodec() {
        this.documentCodec = new DocumentCodec();
    }

    public UserCodec(Codec<Document> codec) {
        this.documentCodec = codec;
    }

    public Document deviceToDocument(User user) {
        Document document = new Document();
        String id = user.getId();
        String username = user.getUsername();
        String password = user.getPassword();
        String company = user.getCompany();


        if (null != id) document.put("_id", id);
        if (null != username) document.put("username", username);
        if (null != password) document.put("password", password);
        if (null != company) document.put("company", company);

        return document;
    }

    public User documentToUser(Document document){
        User user = new User();
        user.setId(document.getString("_id"));
        user.setUsername(document.getString("username"));
        user.setPassword(document.getString("password"));
        user.setCompany(document.getString("company"));

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
        System.out.println("document " + document);
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
