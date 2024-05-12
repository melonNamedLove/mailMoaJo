package com.melon.outlooktest;

import android.app.Activity;
import android.content.Context;

import com.microsoft.identity.client.AuthenticationResult;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.exception.MsalClientException;

public class AuthenticationController {
    private final String TAG = AuthenticationController.class.getSimpleName();
    private static AuthenticationController INSTANCE;
    private static PublicClientApplication mApplication;
    private AuthenticationResult mAuthResult;

    private static Context context;

    private MSALAuthenticationCallback mActivityCallback;

    private AuthenticationController(){

    }
    public static synchronized AuthenticationController getInstance(Context ctx) throws MsalClientException {
        context = ctx;

        if(INSTANCE ==null){
            INSTANCE = new AuthenticationController();
            if(mApplication==null){
                mApplication = new PublicClientApplication(context, Constants.CLIENT_ID);
            }
        }
        return INSTANCE;
    }

    public static synchronized void resetInstance(){INSTANCE = null;}

        public String getAccessToken(){
        return mAuthResult.getAccessToken();
        }

        public PublicClientApplication getPublicClient(){return mApplication;}

        public void doAcquireToken (Activity activity, final MSALAuthenticationCallback msalCallback){
        mActivityCallback = msalCallback;
        mApplication.acquireToken(activity, Constants.SCOPES, getAuthInteractiveCallback());
        }
}
