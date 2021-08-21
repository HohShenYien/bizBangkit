package com.perajuritTeknologi.bizbangkit;

import android.os.AsyncTask;
import android.util.Log;

import com.perajuritTeknologi.bizbangkit.event.LogInEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class APICaller {
    private static final OkHttpClient client = new OkHttpClient();
    // just append the route behind
    private static final String baseUrl = "http://bizbangkit.pythonanywhere.com/";

    public static void logIn(String email, String password) {
        String hashed = Utils.hash(password);
        String credential = Credentials.basic(email, password);

        Request request = new Request.Builder().
                url(baseUrl + "login").header("Authorization", credential).build();

        new logInTask().execute(request);
    }

    private static class logInTask extends AsyncTask<Request, Integer, DataStructure.UserCredentials> {
        @Override
        protected DataStructure.UserCredentials doInBackground(Request... requests) {
            Request request = requests[0];

            try (Response response = client.newCall(request).execute()) {
                return parseCredential(response.body().string());
            } catch (IOException e) {
                return parseCredential("");
            }
        }
        @Override
        protected void onPostExecute(DataStructure.UserCredentials result) {
            EventBus.getDefault().post(new LogInEvent(result));
        }
    }

    public static String getUserName(String userID) {
        return userID + "JohnDoe";
    }

    private static DataStructure.UserCredentials parseCredential(String response) {
        DataStructure.UserCredentials credentials = new DataStructure.UserCredentials();
        try {
            JSONObject jObject = new JSONObject(response);
            credentials.userId = jObject.get("user_id").toString();
            credentials.token = jObject.get("authkey").toString();
        } catch (JSONException e) {
            credentials.userId = "error";
        }

        return credentials;
    }
}
