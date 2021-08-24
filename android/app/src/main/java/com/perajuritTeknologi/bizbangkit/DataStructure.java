package com.perajuritTeknologi.bizbangkit;

import java.io.File;

public class DataStructure {
    // stores structures without methods
    public static class UserCredentials {
        public String userId;
        public String token;
    }

    public static class SimpleBusiness {
        public String businessName;
        public int phase;
        public String description;
        public int businessId;
    }

    public static class UserProfileDetails {
        public String name;
        public String nric;
        public String dob;
        public String phoneNumber;
        public String gender;
        public File profilePicture;
        public String profilePictureType;
        public String profilePictureMimeType;
        public String username;
        public String email;
        public String password;
    }

    public static class BusinessProfileDetails {
        public String type;
        public String name;
        public String commencementDate;
        public String principalAddress;
        public String branchAddress;
        public String partnerNric;
        public String businessType;
        public String valuation;
        public String shortDescription;

    }
}
