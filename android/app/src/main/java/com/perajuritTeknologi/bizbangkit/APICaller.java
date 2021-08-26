package com.perajuritTeknologi.bizbangkit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.perajuritTeknologi.bizbangkit.event.ImageEvent;
import com.perajuritTeknologi.bizbangkit.event.LogInEvent;
import com.perajuritTeknologi.bizbangkit.event.ProfileEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

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

    public static void getProfile(String userId) {
        new ProfileTask().execute(userId);
    }

    private static class ProfileTask extends AsyncTask<String, Integer, DataStructure.UserProfileDetails> {
        @Override
        protected DataStructure.UserProfileDetails doInBackground(String... details) {
            Request request = new Request.Builder().
                    url(baseUrl + "profile/" + details[0]).build();
            try (Response response = client.newCall(request).execute()) {
                Log.d("ShenYien", "entered");
                return parseUserProfile(response.body().string());
            } catch (IOException e) {
                Log.d("ShenYien", e.toString());
                return parseUserProfile("");
            }
        }
        @Override
        protected void onPostExecute(DataStructure.UserProfileDetails result) {
            Log.d("ShenYien", result.email);
            EventBus.getDefault().post(new ProfileEvent(result));
        }
    }

    public static void getImg(String path, String imageId, String eventId) {
        new ImageTask().execute(path, imageId, eventId);
    }

    private static class ImageTask extends AsyncTask<String, Integer, DataStructure.ImageAndId> {
        private String eventId;
        @Override
        protected DataStructure.ImageAndId doInBackground(String... strings) {
            String path = strings[0];
            String imageId = strings[1];
            eventId = strings[2];

            Request request = new Request.Builder().
                    url(baseUrl + "image/" + path).build();

            try (Response response = client.newCall(request).execute()) {
                Bitmap bitmap = BitmapFactory.decodeStream(Objects.requireNonNull(response.body()).byteStream());
                DataStructure.ImageAndId image = new DataStructure.ImageAndId();
                image.image = bitmap;
                image.image_id = imageId;
                return image;

            } catch (IOException e) {
                return null;
            }
        }
        @Override
        protected void onPostExecute(DataStructure.ImageAndId result) {
            EventBus.getDefault().post(new ImageEvent(result, eventId));
        }
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

    private static DataStructure.UserProfileDetails parseUserProfile(String response) {
        DataStructure.UserProfileDetails profile = new DataStructure.UserProfileDetails();

        try {
            JSONObject jsonObject = new JSONObject(response);
            profile.username = jsonObject.get("user_username").toString();
            profile.userId = jsonObject.get("user_id").toString();
            profile.name = jsonObject.get("user_fullname").toString();
            profile.nric = jsonObject.get("user_ic_no").toString();
            profile.email = jsonObject.get("user_email").toString();
            profile.gender = jsonObject.get("user_gender").toString();
            profile.phoneNumber = jsonObject.get("user_phone").toString();
            profile.profilePicture = jsonObject.get("user_fpath_profilepic").toString();
            profile.aboutme = jsonObject.get("user_aboutme").toString();
            profile.dob = jsonObject.get("user_dob").toString();
            profile.address = jsonObject.get("user_fulladdress").toString();
            Log.d("ShenYien", profile.email);

        } catch (JSONException e) {
            profile.userId = "error";
        }
        return profile;
    }
}
