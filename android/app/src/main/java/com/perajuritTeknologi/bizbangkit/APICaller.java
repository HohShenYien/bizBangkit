package com.perajuritTeknologi.bizbangkit;

public class APICaller {
    public static String logIn(String email, String password) throws LogInFails{
        return "{'ID':'0001', 'token': 'something something'}";
    }

    //--------API exceptions
    private static class LogInFails extends Exception{
        public LogInFails(String errorMessage) {
            super(errorMessage);
        }
    }
}
