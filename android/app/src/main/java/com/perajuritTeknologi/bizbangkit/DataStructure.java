package com.perajuritTeknologi.bizbangkit;

public class DataStructure {
    // stores structures without methods
    public static class UserCredentials {
        public String userId;
        public String token;
    }

    public static class UserProfileDetails {
        public String name;
        public String nric;
        public String phoneNumber;
        public String username;
        public String email;
        public String password;

        UserProfileDetails(String name, String nric, String phoneNumber, String username, String email, String password) {
            this.name = name;
            this.nric = nric;
            this.phoneNumber = phoneNumber;
            this.username = username;
            this.email = email;
            this.password = password;
        }
    }
}
