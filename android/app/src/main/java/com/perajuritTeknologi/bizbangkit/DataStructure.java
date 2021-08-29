package com.perajuritTeknologi.bizbangkit;

import android.graphics.Bitmap;
import java.io.File;

public class DataStructure {
    // stores structures without methods
    public static class UserCredentials {
        public String userId;
        public String token;
    }

    public static class SimpleBusiness {
        public String businessName;
        public String logoPath;
        public int valuation;
        public int phase;
        public String type;
        public int businessId;
        public Bitmap logo;
        public int purchasedPercent;
    }

    public static class UserProfileDetails {
        public String userId;
        public String aboutme;
        public String name;
        public String nric;
        public String dob;
        public String phoneNumber;
        public String gender;
        public String picturePath;
        public File profilePicture;
        public String profilePictureType;
        public String profilePictureMimeType;
        public String username;
        public String email;
        public String password;
        public String address;
    }

    public static class BusinessProfileDetails {
        public int businessId;
        public String type;
        public String name;
        public String commencementDate;
        public String principalAddress;
        public String branchAddress;
        public String partnerNric;
        public String businessType;
        public String valuation;
        public String licenseNumber;
        public String shortDescription;
        public Bitmap logo;
        public int purchasedPercent;
        public String logoPath;
        public int phase;
    }

    public static class ImageAndId {
        public Bitmap image;
        public String image_id; // for you to set which id is it for
    }

    public static class SimpleForumPost {
        public String postId;
        public String postTitle;
        public String postContent;
        public String postDate;
        public int postViewCount;
        public int postVoteCount;
    }
}
