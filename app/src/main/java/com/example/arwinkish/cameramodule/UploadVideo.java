package com.example.arwinkish.cameramodule;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;


/**
 * Created by Arwin Kish on 3/9/2016.
 */
public class UploadVideo extends AppCompatActivity {
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
    }
    public void Clicked(View view)
    {
        Bundle childCurrent = getIntent().getExtras();
        if (childCurrent == null) {
            return;
        }
        String rest = childCurrent.getString("child");
        File file = new File(rest);
        Toast.makeText(getApplicationContext(),rest, Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("video/*");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Title_text");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        startActivity(Intent.createChooser(intent,"Upload video via:"));
    }
}
