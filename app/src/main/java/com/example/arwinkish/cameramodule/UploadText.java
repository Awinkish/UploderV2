package com.example.arwinkish.cameramodule;

/**
 * Created by Arwin Kish on 3/9/2016.
 */
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class UploadText extends AppCompatActivity {
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.textupload);
        editText = (EditText)findViewById(R.id.editText);
    }

    public void Clicked(View view)
    {
        String textDesc = editText.getText().toString();

        if(textDesc.length() <= 0) {
            Toast.makeText(getApplicationContext(), "Please describe the video!", Toast.LENGTH_LONG).show();
        }else{
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Description.txt");
                sendIntent.putExtra(Intent.EXTRA_TEXT, textDesc);
                Intent sessionStart = new Intent(getApplicationContext(), GpsCord.class);
                startActivity(sessionStart);
                editText.setText("");
                startActivity(Intent.createChooser(sendIntent, "Upload description to Drive:"));
            } else {
                Toast.makeText(getApplicationContext(), "Unable to upload. Please check your internet connection", Toast.LENGTH_LONG).show();
            }
        }
    }
}

