package com.perajuritTeknologi.bizbangkit;

import org.json.JSONException;
import org.json.JSONObject;

public class APICaller {
    public static DataStructure.UserCredentials logIn(String email, String password) throws LogInFails{
        if (email.compareTo("johndoe@gmail.com") == 0 &&
                password.compareTo("asdfasdf") == 0) {
            return parseCredential("{'ID':'0001', 'token': 'something something'}");
        }
        else {
            throw new LogInFails("Incorrect email or password");
        }
    }

    public static String getUserName(String userID) {
        return userID + "JohnDoe";
    }

    //--------API exceptions
    static class LogInFails extends Exception{
        public LogInFails(String errorMessage) {
            super(errorMessage);
        }
    }

    private static DataStructure.UserCredentials parseCredential(String response) {
        DataStructure.UserCredentials credentials = new DataStructure.UserCredentials();
        try {
            JSONObject jObject = new JSONObject(response);
            credentials.userId = jObject.get("ID").toString();
            credentials.token = jObject.get("token").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return credentials;
    }
}
