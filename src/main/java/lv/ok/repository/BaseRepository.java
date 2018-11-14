package lv.ok.repository;

import com.mongodb.client.MongoDatabase;
import lv.ok.config.ApplicationProperties;
import org.bson.codecs.configuration.CodecRegistry;

import static java.lang.String.format;
import static lv.ok.config.ApplicationProperties.getString;

public class BaseRepository {

    protected static String HOST = getString(ApplicationProperties.ApplicationProperty.DB_HOST);
    protected static String PORT = getString(ApplicationProperties.ApplicationProperty.DB_PORT);
    protected static String DB_NAME = getString(ApplicationProperties.ApplicationProperty.DB_NAME);

    protected static String USER = getString(ApplicationProperties.ApplicationProperty.DB_USER);
    protected static String PASSWORD = getString(ApplicationProperties.ApplicationProperty.DB_PASSWORD);

    protected MongoDatabase db;
    protected CodecRegistry codecRegistry;

    protected static String getConnectionURL() {
        String url = format("mongodb://%s:%s@%s:%s/%s", USER, PASSWORD, HOST, PORT, DB_NAME);
        return url;
    }


}
