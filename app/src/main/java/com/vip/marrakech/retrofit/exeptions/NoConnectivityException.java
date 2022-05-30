package com.vip.marrakech.retrofit.exeptions;

import java.io.IOException;

public class NoConnectivityException extends IOException {



    @Override
    public String getMessage() {
        return "Check Internet Connection.";
    }
 
}