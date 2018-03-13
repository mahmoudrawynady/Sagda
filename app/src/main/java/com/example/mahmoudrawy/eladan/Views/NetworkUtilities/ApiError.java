package com.example.mahmoudrawy.eladan.Views.NetworkUtilities;


public class ApiError {
    ///////////////////////////////////////////////////////////////////////////////////
    public static final int NOT_FOUND_STATUS_CODE = 404;
    public static final int CONNECTION_TIME_OUT_STATUS_CODE = 409;
    public static final int INTERNAL_SERVER_ERROR_STATUS_CODE = 500;
    public static final int SERVICE_UNAVAILABLE_STATUS_CODE = 503;
    //////////////////////////////////////////////////////////////////////////////////
    private int statusCode;
    private String message;

    public int status() {
        return statusCode;
    }

    public String message() {
        return message;
    }
    ///////////////////////////////////////////////////////////////////////////////////
}
