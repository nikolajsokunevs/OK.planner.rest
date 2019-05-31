package lv.ok.utils;

import java.util.UUID;

public class Utilities {

    public String generateRandomString() {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }
}
