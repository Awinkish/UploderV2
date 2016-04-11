package com.example.arwinkish.cameramodule;

/**
 * I've combined location api with google login api v2
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

public class GooglePlusLoginUtils implements ConnectionCallbacks, OnConnectionFailedListener,View.OnClickListener {
    private String TAG = "GooglePlusLoginUtils";
    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PHOTO = "photo";
    public static final String PROFILE= "profile";

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    private Location mLastLocation;

    private SignInButton btnSignIn;
    private Context ctx;
    private LocationRequest mLocationRequest;
    double latitude;
    double longitude;
    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    private GPlusLoginStatus loginstatus;
    public interface GPlusLoginStatus{
        public void OnSuccessGPlusLogin(Bundle profile);
    }

    public GooglePlusLoginUtils(Context ctx,int btnRes){
        Log.i(TAG, "GooglePlusLoginUtils");
        this.ctx= ctx;
        this.btnSignIn =(SignInButton) ((Activity)ctx).findViewById(btnRes);
        btnSignIn.setOnClickListener(this);
        // Initializing google plus api client
        mGoogleApiClient = new GoogleApiClient.Builder(ctx)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(LocationServices.API)
                .addApi(ActivityRecognition.API)
                .build();

            createLocationRequest();
    }

    public void setLoginStatus(GPlusLoginStatus loginStatus){
        this.loginstatus = loginStatus;
    }
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "onConnectionFailed");
        Log.i(TAG,"Error Code "+ result.getErrorCode());
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), (Activity)ctx,0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }
    public void setSignInClicked(boolean value){
        mSignInClicked  =value;
    }
    public void setIntentInProgress(boolean value){
        mIntentInProgress = value;
    }
    public void connect(){
        mGoogleApiClient.connect();
    }
    public void reconnect(){
        if (!mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }
    }
    public void disconnect(){
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
    private void signInWithGplus() {
        Log.i(TAG, "signInWithGplus");
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }
    /**
     * Creating location request object
     * */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }
    private void resolveSignInError() {
        Log.i(TAG, "resolveSignInError");
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult((Activity)ctx, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }
    @Override
    public void onConnected(Bundle arg0) {
        Log.i(TAG, "onConnected");
        mSignInClicked = false;

        // Get user's information
        getProfileInformation();

    }
    @Override
    public void onConnectionSuspended(int arg0) {
        Log.i(TAG, "onConnectionSuspended");
        mGoogleApiClient.connect();
    }

    private void getProfileInformation() {
        Log.i(TAG, "getProfileInformation");
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
//                Person currentPerson = Plus.PeopleApi
//                        .getCurrentPerson(mGoogleApiClient);

                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                double longi = displayVectorLong();
                double lat = displayVectorLat();
                Intent intent = new Intent(ctx, MainActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("latitude", lat);
                intent.putExtra("longitude", longi);
                ctx.startActivity(intent);

                Bundle profile = new Bundle();
                profile.putString(EMAIL, email);


            } else {
                Toast.makeText(ctx,
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double displayVectorLat() {

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
        }
        return latitude;
    }

    private double displayVectorLong() {

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            longitude = mLastLocation.getLatitude();
        }
        return longitude;
    }

    @Override
    public void onClick(View v) {
        signInWithGplus();
    }

    public void onActivityResult(int requestCode,int responseCode,Intent intent){
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != ((Activity)ctx).RESULT_OK) {
                setSignInClicked(false);
            }
            setIntentInProgress(false);
            reconnect();
        }
    }
}

