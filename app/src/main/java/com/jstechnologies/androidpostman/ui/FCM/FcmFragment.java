package com.jstechnologies.androidpostman.ui.FCM;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.jstechnologies.androidpostman.Helpers.AppRequestQueue;
import com.jstechnologies.androidpostman.Helpers.PrefManager;
import com.jstechnologies.androidpostman.Helpers.RequestDatabase;
import com.jstechnologies.androidpostman.Helpers.TypeConverter;
import com.jstechnologies.androidpostman.Models.FCMRequests;
import com.jstechnologies.androidpostman.Models.RequestModel;
import com.jstechnologies.androidpostman.R;
import com.jstechnologies.androidpostman.Views.DropDownView;
import com.jstechnologies.androidpostman.ui.Response.FcmResponse;
import com.jstechnologies.androidpostman.ui.SelectActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FcmFragment extends Fragment {

    DropDownView map;
    FloatingActionButton send;
    String[] tokens,topics;
    EditText serverkey_et,tokens_et,topics_et;
    public static String FCM_PUSH = "https://fcm.googleapis.com/fcm/send";
    public static String FCM_AUTH_KEY;
    RadioGroup radioGroup;
    ProgressDialog dialog;
    RequestDatabase database;
    public FcmFragment() {
        // Required empty public constructor
    }
    RadioButton enabletoken,enabletopic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fcm, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String savedfcmurl= PrefManager.getFCMurl(view.getContext());
        setHasOptionsMenu(true);
        FCM_PUSH =savedfcmurl!=null?savedfcmurl:"https://fcm.googleapis.com/fcm/send";
        map=view.findViewById(R.id.map);
        database=RequestDatabase.getInstance(getContext());
        send=view.findViewById(R.id.fab_send);
        serverkey_et=view.findViewById(R.id.serverkey);
        topics_et=view.findViewById(R.id.topic);
        tokens_et=view.findViewById(R.id.token);
        radioGroup=view.findViewById(R.id.radiogroup);
        enabletoken=view.findViewById(R.id.enabletoken);
        enabletopic=view.findViewById(R.id.enabletopic);
        map.setDisplayTitle("Notification Payload");
        map.AddEntry("title","Postman");
        map.AddEntry("message","Postman says Hi!");
        map.setBtndisabled(true);
        map.setExpanded(true);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
            }
        });
        enabletopic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                 enabletoken.setChecked(false);
            }
        });
        enabletoken.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    enabletopic.setChecked(false);
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i)
                {
                    case R.id.notification:notification();break;
                    case R.id.data:Data();break;
                    case R.id.both:Both();break;

                }
            }
        });
        Toast.makeText(getContext(), PrefManager.getFCMurl(getContext()),Toast.LENGTH_SHORT).show();
    }
    void sendRequest()
    {
        String serverKey=serverkey_et.getText().toString().trim();
        FCM_AUTH_KEY="key="+serverKey;
        Map<String,String>vars=map.getData();
        fetchAllTokens();
        JSONObject mainobject= new JSONObject();
        JSONObject data= new JSONObject();
        try{

            for(Map.Entry<String,String> entry:vars.entrySet())
            {
                data.put(entry.getKey(),entry.getValue());
            }
            mainobject.put("data",data);
            JSONArray regid=new JSONArray(tokens);
            if(enabletopic.isChecked())
                mainobject.put("to",topics_et.getText().toString().trim());
            if(enabletoken.isChecked())
                mainobject.put("registration_ids",regid);
            Send(getContext(),mainobject);
        }catch (Exception e)
        {
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }


    }
    void fetchAllTokens()
    {
        String input=tokens_et.getText().toString().trim();
        if(input.contains(","))
            tokens=input.split(",");
        else
            tokens=new String[]{input};

    }

    private void Send(Context c, JSONObject js) {
        dialog= new ProgressDialog(getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Waiting for Response.....");
        dialog.show();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, FCM_PUSH, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(dialog!=null && dialog.isShowing())
                    dialog.dismiss();
                SaveToDB(serverkey_et.getText().toString().trim(),
                        tokens_et.getText().toString().trim(),
                        topics_et.getText().toString().trim());
                Intent intent= new Intent(getContext(), FcmResponse.class);
                intent.putExtra("RESPONSE",response.toString());
                startActivity(intent);
                Log.e("FCM RESPONSE",response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(dialog!=null && dialog.isShowing())
                    dialog.dismiss();
                Toast.makeText(getContext(),"Error: "+error.getMessage(),Toast.LENGTH_LONG).show();
               // Log.e("FCM RESPONSE ERROR",error.getMessage());
                return;
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", FCM_AUTH_KEY);
                return headers;
            }
        };
        AppRequestQueue.getInstance(c.getApplicationContext()).addToRequestQueue(request);


    }
    void SaveToDB(String serverkey,String token,String topic)
    {
        if(PrefManager.getFCMbackupenabled(getContext()))
        {
            FCMRequests fcm=new FCMRequests(serverkey,topic,token);
            fcm.setPayload(map.getData());
          //  RequestModel model= new RequestModel(method,_url,System.currentTimeMillis(),headers.getData(),params.getData());
            database.Insert(fcm, new RequestDatabase.DatabaseWriteListener() {
                @Override
                public void onJobComplete() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"Write Success",Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                @Override
                public void onException(final Exception e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"Write Error :"+e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });

                }
            });
        }

    }
    void notification()
    {
        map.removeEntry("");
        if(!map.getData().containsKey("title"))
            map.AddEntry("title","Postman");
        if(!map.getData().containsKey("message"))
            map.AddEntry("message","Postman says Hi!");
        map.setBtndisabled(true);
    }
    void Data()
    {
        map.removeEntry("title");
        map.removeEntry("message");
        map.AddEntry("","");
        map.setBtndisabled(false);
    }
    void Both()
    {
        if(!map.getData().containsKey("title"))
        map.AddEntry("title","Postman");
        if(!map.getData().containsKey("message"))
            map.AddEntry("message","Postman says Hi!");
        map.AddEntry("","");
        map.setBtndisabled(false);
    }
    void custom()
    {
        if(!map.getData().containsKey("title"))
            map.AddEntry("title","Postman");
        if(!map.getData().containsKey("message"))
            map.AddEntry("message","Postman says Hi!");
        map.AddEntry("","");
        map.setBtndisabled(false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_response,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.history)
        {
            Intent intent= new Intent(getContext(), SelectActivity.class);
            intent.putExtra("type", "fcm");
            startActivityForResult(intent,101);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101 && resultCode== Activity.RESULT_OK)
        {

            String serverkey=data.getStringExtra("serverkey");
            String topics=data.getStringExtra("topic");
            String token=data.getStringExtra("token");
            String payload=data.getStringExtra("payload");
            Map<String,String>payloadmap= TypeConverter.getMapFromJson(payload);
            for(Map.Entry<String,String>entry:payloadmap.entrySet())
            {

                map.AddEntry(entry.getKey(),entry.getValue());
            }
            this.serverkey_et.setText(serverkey);
            tokens_et.setText(token);
            topics_et.setText(topics);

        }
    }

}