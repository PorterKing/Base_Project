package com.porterking.netlibrary.work;

public class RxException {


    public static class ServerException extends Exception {

        public ServerException(String msg) {
            super(msg);
        }
    }

    public static class LoginFailException extends Exception {

        public LoginFailException(String msg) {
            super(msg);
        }
    }

    public static class LowVersionException extends Exception {

        public LowVersionException(String msg) {
            super(msg);
        }
    }



}
