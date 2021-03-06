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
        public int invested; // for getting invesments
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
        public String shareBought;
        public String licenseNumber;
        public String shortDescription;
        public Bitmap logo;
        public int purchasedPercent;
        public String logoPath;
        public int phase;
    }

    public static class Investor {
        public int userId;
        public int sharePercent;
        public String userGender;
        public String username;
        public String userPicPath;
        public Bitmap userPic;
    }

    public static class ImageAndId {
        public Bitmap image;
        public String image_id; // for you to set which id is it for
    }

    public static class SimpleForumPost {
        public int postId;
        public String imagePath;
        public Bitmap pic;
        public String username;
        public String postTitle;
        public String postContent;
        public String postDate;
        public int postViewCount;
        public int postVoteCount;
    }

    public static class SimpleForumReply {
        public String imagePath;
        public Bitmap pic;
        public int replyId;
        public String username;
        public String replyContent;
        public String replyDate;
    }

    public static class EWalletBalance {
        public String balance;
    }
}
