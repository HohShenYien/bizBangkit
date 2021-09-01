package com.perajuritTeknologi.bizbangkit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.perajuritTeknologi.bizbangkit.event.BusinessOwnerEvent;
import com.perajuritTeknologi.bizbangkit.event.GetBusinessDetails;
import com.perajuritTeknologi.bizbangkit.event.GetBusinessListEvent;
import com.perajuritTeknologi.bizbangkit.event.GetInvestors;
import com.perajuritTeknologi.bizbangkit.event.GetPersonalBusinessDetails;
import com.perajuritTeknologi.bizbangkit.event.GetWalletBalance;
import com.perajuritTeknologi.bizbangkit.event.ImageEvent;
import com.perajuritTeknologi.bizbangkit.event.LogInEvent;
import com.perajuritTeknologi.bizbangkit.event.ProfileEvent;
import com.perajuritTeknologi.bizbangkit.event.SaveProfileResponse;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
                return parseUserProfile(response.body().string());
            } catch (IOException e) {
                return parseUserProfile("");
            }
        }
        @Override
        protected void onPostExecute(DataStructure.UserProfileDetails result) {
            EventBus.getDefault().post(new ProfileEvent(result));
        }
    }

    public static void getBusinessOwner(int userId) {
        new BusinessOwnerTask().execute(userId);
    }

    private static class BusinessOwnerTask extends AsyncTask<Integer, Integer, DataStructure.UserProfileDetails> {
        @Override
        protected DataStructure.UserProfileDetails doInBackground(Integer... details) {
            Request request = new Request.Builder().
                    url(baseUrl + "business/owner/" + details[0]).build();
            try (Response response = client.newCall(request).execute()) {
                return parseUserProfile(response.body().string());
            } catch (IOException e) {
                return parseUserProfile("");
            }
        }
        @Override
        protected void onPostExecute(DataStructure.UserProfileDetails result) {
            EventBus.getDefault().post(new BusinessOwnerEvent(result));
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

    public static void saveProfile(DataStructure.UserProfileDetails user) {
        MultipartBody.Builder builder
                = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("fullname", user.name)
                .addFormDataPart("ic_number", user.nric)
                .addFormDataPart("username", user.username)
                .addFormDataPart("dob", user.dob)
                .addFormDataPart("phone_num", user.phoneNumber)
                .addFormDataPart("email", user.email)
                .addFormDataPart("fulladdress", user.address)
                .addFormDataPart("aboutme", user.aboutme)
                .addFormDataPart("gender", user.gender);

        if (user.profilePicture != null) {
            builder.addFormDataPart("file", ("profile_pic" + user.profilePictureType), RequestBody.create(user.profilePicture, MediaType.parse(user.profilePictureMimeType)));
            Log.d("ShenYien111", "entered");
        }

        RequestBody requestBody = builder.build();
        Log.d("ShenYiennn", user.aboutme + "-----" + user.profilePictureType);

        Request request
                = new Request.Builder()
                .url(baseUrl + "update/profile/" + user.userId)
                .put(requestBody)
                .build();

        new UpdateTask().execute(request);
    }

    private static class UpdateTask extends AsyncTask<Request, Integer, Integer> {

        @Override
        protected Integer doInBackground(Request... requests) {
            try (Response response = client.newCall(requests[0]).execute()) {
                Log.d("ShenYien", response.message());
                return 1;
            } catch (IOException e) {
                Log.d("ShenYien", "UpdateError");
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            EventBus.getDefault().post(new SaveProfileResponse(result));
        }
    }

    public static void getBusinessList(int start, int num) {
        Request request = new Request.Builder().
                url(baseUrl + "business/list?n=" + num + "&q=" + start).build();
        new BusinessListTask().execute(request);
    }

    private static class BusinessListTask extends AsyncTask<Request, Integer, ArrayList<DataStructure.SimpleBusiness>> {

        @Override
        protected ArrayList<DataStructure.SimpleBusiness> doInBackground(Request... requests) {
            try (Response response = client.newCall(requests[0]).execute()) {
                return parseBusinessList(response.body().string());
            } catch (IOException e) {
                Log.e("ShenYien", "Shouldn't have an error here");
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<DataStructure.SimpleBusiness> result) {
            EventBus.getDefault().post(new GetBusinessListEvent(result));
        }
    }

    public static void getPersonalBusinessDetails(String userID) {
        Request request =
                new Request.Builder()
                .url(baseUrl + "business/info/user/" + userID)
                .build();
        new GetPersonalBusinessTask().execute(request);
    }

    private static class GetPersonalBusinessTask extends AsyncTask<Request, Integer, DataStructure.BusinessProfileDetails> {
        @Override
        protected DataStructure.BusinessProfileDetails doInBackground(Request... requests) {
            DataStructure.BusinessProfileDetails profileDetails = new DataStructure.BusinessProfileDetails();
            try (Response response = client.newCall(requests[0]).execute()) {
                return parsePersonalBusinessDetails(response.body().string());
            } catch (IOException e) {
                Log.e("RuiJun", "Request to server problem", e);
                profileDetails.name = "ServerFailedUs";
                return profileDetails;
            }
        }

        @Override
        protected void onPostExecute(DataStructure.BusinessProfileDetails result) {
            EventBus.getDefault().post(new GetPersonalBusinessDetails(result));
        }
    }

    private static DataStructure.BusinessProfileDetails parsePersonalBusinessDetails(String details) {
        DataStructure.BusinessProfileDetails profileDetails = new DataStructure.BusinessProfileDetails();
        try {
            JSONObject jsonObject = new JSONObject(details);
            try {
                String noBusiness = jsonObject.get("Error").toString();
                Log.d("RuiJun","No business exists");
                profileDetails.name = "NO_EXISTING_BUSINESS";
                return profileDetails;
            } catch (JSONException e) {
                Log.d("RuiJun","Business does exists for this user");
                profileDetails.type = jsonObject.get("bus_lic_no").toString();
                profileDetails.name = jsonObject.get("bus_name").toString();
                profileDetails.phase = jsonObject.getInt("bus_phase");
                profileDetails.valuation = jsonObject.get("bus_valuation").toString();
                profileDetails.commencementDate = jsonObject.get("bus_start_date").toString();  // this is actually the proposed date, put into commencement date as data holder only
                profileDetails.shareBought = String.format(Locale.getDefault(),"%d",Math.round(Float.parseFloat(profileDetails.valuation)
                        * jsonObject.getInt("share_bought")/100f));
                Log.d("RuiJun share bought", profileDetails.shareBought);
                return profileDetails;
            }

        } catch (JSONException e) {
            Log.d("RuiJun", "JSON parsing error", e);
            return null;
        }
    }

    public static void getBusinessDetails(int busId) {
        Request request = new Request.Builder().
                url(baseUrl + "business/info/" + busId).build();
        new GetBusinessTask().execute(request);
    }

    private static class GetBusinessTask extends AsyncTask <Request, Integer, DataStructure.BusinessProfileDetails>{

        @Override
        protected DataStructure.BusinessProfileDetails doInBackground(Request... requests) {
            try (Response response = client.newCall(requests[0]).execute()) {
                return parseBusinessDetails(response.body().string());
            } catch (IOException e) {
                Log.e("ShenYien", e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(DataStructure.BusinessProfileDetails result) {
            EventBus.getDefault().post(new GetBusinessDetails(result));
        }
    }

    public static void getInvestors(int busId) {
        Request request = new Request.Builder().
                url(baseUrl + "business/investors/" + busId).build();
        new GetInvestorTask().execute(request);
    }

    private static class GetInvestorTask extends AsyncTask <Request, Integer, ArrayList<DataStructure.Investor>> {
        @Override
        protected ArrayList<DataStructure.Investor> doInBackground(Request... requests) {
            try (Response response = client.newCall(requests[0]).execute()) {
                return parseInvestor(response.body().string());
            } catch (IOException e) {
                Log.e("ShenYien", e.toString());
                return parseInvestor("");
            }
        }

        @Override
        protected void onPostExecute(ArrayList<DataStructure.Investor> result) {
            EventBus.getDefault().post(new GetInvestors(result));
        }
    }

    private static ArrayList<DataStructure.Investor> parseInvestor(String response) {
        ArrayList<DataStructure.Investor> investors = new ArrayList<>();
        try {
            JSONArray investorList = new JSONArray(response);
            for (int i = 0; i < investorList.length(); i++) {
                JSONObject investorData = investorList.getJSONObject(i);
                DataStructure.Investor investor = new DataStructure.Investor();
                investor.sharePercent = investorData.getInt("share_percent");
                investor.userGender = investorData.getString("user_Gender");
                investor.userPicPath = investorData.getString("user_fpath_profilepic");
                investor.userId = investorData.getInt("user_id");
                investor.username = investorData.getString("username");
                investors.add(investor);
            }
        } catch (JSONException e) {
            Log.e("ShenYien", "Oh no");
            Log.e("ShenYien", e.toString());
        }
        return investors;
    }


    private static DataStructure.BusinessProfileDetails parseBusinessDetails(String details) {
        DataStructure.BusinessProfileDetails business = new DataStructure.BusinessProfileDetails();
        try {
            JSONObject result = new JSONObject(details);
            business.businessId = result.getInt("bus_id");
            business.name = result.getString("bus_name");
            business.businessType = result.getString("bus_type");
            business.valuation = Integer.toString(result.getInt("bus_valuation"));
            business.principalAddress = result.getString("bus_loc_address_city");
            business.logoPath = result.getString("bus_fpath_logo");
            business.commencementDate = result.getString("bus_day");
            business.purchasedPercent = result.getInt("share_bought");
            business.phase = result.getInt("bus_phase");
            return business;

        } catch (JSONException e) {
            Log.e("ShenYien", e.toString());
            return null;
        }
    }

    private static ArrayList<DataStructure.SimpleBusiness> parseBusinessList(String response) {
        ArrayList<DataStructure.SimpleBusiness> arrayList = new ArrayList<>();
        try {
            JSONArray businessList = new JSONArray(response);
            for (int i = 0; i < businessList.length(); i++) {
                JSONObject business = businessList.getJSONObject(i);
                DataStructure.SimpleBusiness theBusiness = new DataStructure.SimpleBusiness();
                theBusiness.businessId = business.getInt("bus_id");
                theBusiness.businessName = business.getString("bus_name");
                theBusiness.phase = business.getInt("bus_share_phase");
                theBusiness.type = business.getString("bus_type");
                theBusiness.valuation = business.getInt("bus_valuation");
                theBusiness.logoPath = business.getString("bus_fpath_logo");
                theBusiness.purchasedPercent = business.getInt("puchased_percent");
                arrayList.add(theBusiness);
            }
        } catch (JSONException e) {
            Log.e("ShenYien", "Oh no");
            Log.e("ShenYien", e.toString());
        }
        return arrayList;
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
            profile.picturePath = jsonObject.get("user_fpath_profilepic").toString();
            profile.aboutme = jsonObject.get("user_aboutme").toString();
            profile.dob = jsonObject.get("user_dob").toString();
            profile.address = jsonObject.get("user_fulladdress").toString();
            Log.d("ShenYien", profile.email);

        } catch (JSONException e) {
            profile.userId = "error";
        }
        return profile;
    }

    public static void getWalletBalance(String userID) {
        Request request =
                new Request.Builder()
                        .url(baseUrl + "wallet/balance/" + userID)
                        .build();
        new GetWalletBalanceTask().execute(request);
    }

    private static class GetWalletBalanceTask extends AsyncTask<Request, Integer, DataStructure.EWalletBalance> {
        @Override
        protected DataStructure.EWalletBalance doInBackground(Request... requests) {
            DataStructure.EWalletBalance balance = new DataStructure.EWalletBalance();
            try (Response response = client.newCall(requests[0]).execute()) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    balance.balance = jsonObject.get("balance").toString();
                    return balance;
                } catch (JSONException e) {
                    Log.d("RuiJun", "JSON parsing error", e);
                    balance.balance = "ERROR";
                    return balance;
                }
            } catch (IOException e) {
                Log.e("RuiJun", "Request to server problem", e);
                balance.balance = "ERROR";
                return balance;
            }
        }

        @Override
        protected void onPostExecute(DataStructure.EWalletBalance result) {
            EventBus.getDefault().post(new GetWalletBalance(result));
        }
    }

    public static void changeWalletBalance(String userToken) {
        Request request =
                new Request.Builder()
                .url(baseUrl + "wallet/" + userToken)
                .build();
        new
    }

    private static class ChangeWalletBalanceTask extends AsyncTask<>
}
