package com.jstechnologies.androidpostman.ui.Response;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jstechnologies.androidpostman.R;

public class FcmResponse extends AppCompatActivity {

    String body;
    TextView response;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcm_response);
        getSupportActionBar().setTitle("FCM Response");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        response=findViewById(R.id.response);
        body=getIntent().getStringExtra("RESPONSE");
        Gson gson=new GsonBuilder().setPrettyPrinting().create();
        try {
            JsonElement jsonElement = new JsonParser().parse(body);
            response.setText(gson.toJson(jsonElement));
        }catch (Exception e)
        {
            response.setText("Error encountered parsing the response.\n"+e.getMessage());
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }
}