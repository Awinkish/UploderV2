package com.example.arwinkish.cameramodule;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Arwin Kish on 3/9/2016.
 */
public class GpsCord extends AppCompatActivity {

    Button btnShowLocation;

    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_location);

        btnShowLocation = (Button) findViewById(R.id.show_location);

        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gps = new GPSTracker(GpsCord.this);

                if(gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    Toast.makeText(
                            getApplicationContext(),
                            "My Location is -\nLat: " + latitude + "\nLong: "
                                    + longitude, Toast.LENGTH_LONG).show();
                    String location = "My Location is -\nLat: " + latitude + "\nLong: "
                            + longitude;

                    ConnectivityManager connMgr = (ConnectivityManager)
                            getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, location);
                        sendIntent.setType("text/plain");
                        Intent sessionStart = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(sessionStart);
                        startActivity(Intent.createChooser(sendIntent, "Upload Coordinates to Drive:"));

                    } else {
                        Toast.makeText(getApplicationContext(), "Unable to upload. Please check your internet connection",Toast.LENGTH_LONG).show();
                    }

                } else {
                    gps.showSettingsAlert();
                }
            }
        });
    }
}
