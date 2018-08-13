package lv.ok.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Properties;

public class ApplicationProperties {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationProperties.class);
    private static HashMap<String, Properties> DEFAULT_VALUES = new HashMap<String, Properties>() {
        {
            put("default", new Properties() {
                {
                    setProperty(ApplicationProperty.APP_BASE_URL.name, "http://localhost:8080/myapp/");
                    setProperty(ApplicationProperty.DB_HOST.name, "ds249025.mlab.com");
                    setProperty(ApplicationProperty.DB_PORT.name, "49025");
                    setProperty(ApplicationProperty.DB_USER.name, "user1");
                    setProperty(ApplicationProperty.DB_PASSWORD.name, "user1");
                    setProperty(ApplicationProperty.DB_NAME.name, "sh");

                }
            });
            put("dev-ci", new Properties() {
                {
                }
            });
        }

    };

    private static String getString(String propertyName) {
        String currentEnv = System.getProperties().getProperty(
                ApplicationProperty.APP_ENV.name, System.getenv(ApplicationProperty.APP_ENV.name.toUpperCase().replace('.', '_')));

        if (System.getProperties().containsKey(propertyName)) {
            return System.getProperties().getProperty(propertyName);
        }
        if (currentEnv != null) {
            if (DEFAULT_VALUES.get(currentEnv).containsKey(propertyName)) {
                return DEFAULT_VALUES.get(currentEnv).getProperty(propertyName);
            }
        }
        if (DEFAULT_VALUES.get("default").containsKey(propertyName)) {
            return DEFAULT_VALUES.get("default").getProperty(propertyName);
        }

        logger.warn("Unknown application property: " + propertyName);
        throw new RuntimeException("Unknown application property: " + propertyName);
    }

    public static String getString(ApplicationProperty property) {
        return getString(property.name);
    }

    public static Integer getInteger(ApplicationProperty property) {
        return Integer.valueOf(getString(property));
    }


    public static boolean getBoolean(ApplicationProperty property) {
        return Boolean.valueOf(getString(property));
    }

    public enum ApplicationProperty {

        APP_ENV("application.env"),
        APP_BASE_URL("application.url"),
        DB_HOST("db.host"),
        DB_PORT("db.port"),
        DB_NAME("db.name"),
        DB_USER("db.user"),
        DB_PASSWORD("db.password");

        private String name;

        ApplicationProperty(String name) {
            this.name = name;
        }
    }
}