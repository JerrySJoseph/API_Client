package com.jstechnologies.androidpostman.ui.NewRequest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jstechnologies.androidpostman.Adapters.RecentsAdapter;
import com.jstechnologies.androidpostman.Helpers.AppRequestQueue;
import com.jstechnologies.androidpostman.Helpers.PrefManager;
import com.jstechnologies.androidpostman.Helpers.RequestDatabase;
import com.jstechnologies.androidpostman.Helpers.TypeConverter;
import com.jstechnologies.androidpostman.Models.RequestModel;
import com.jstechnologies.androidpostman.Views.DropDownView;
import com.jstechnologies.androidpostman.R;
import com.jstechnologies.androidpostman.ui.Response.ResponseScreen;
import com.jstechnologies.androidpostman.ui.SelectActivity;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NewRequestFragment extends Fragment {

    DropDownView headers,params;
    FloatingActionButton send;
    RadioGroup radioGroup;
    EditText url;
    ProgressDialog dialog;
    int method=Request.Method.GET;
    RequestDatabase database;
    public NewRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_request, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        if(savedInstanceState!=null)
        {
            boolean hasdata=savedInstanceState.getBoolean("HASDATA",false);
            if(hasdata)
                Toast.makeText(getContext(),savedInstanceState.getString("URL"),Toast.LENGTH_SHORT).show();
        }

        database=RequestDatabase.getInstance(getContext());
        send=view.findViewById(R.id.fab_send);
        url=view.findViewById(R.id.url);
        headers=view.findViewById(R.id.headers);
        params=view.findViewById(R.id.params);
        radioGroup=view.findViewById(R.id.radiogroup);
        headers.setDisplayTitle("Headers");
        headers.AddEntry("content-type","multipart/form-data");
        params.setDisplayTitle("Parameters");
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id=radioGroup.getCheckedRadioButtonId();
                switch (id)
                {
                    case R.id.get:method=Request.Method.GET;break;
                    case R.id.post:method=Request.Method.POST;break;
                    case R.id.put:method=Request.Method.PUT;break;
                    case R.id.delete:method=Request.Method.DELETE;break;
                }
                SendRequest();

            }
        });

    }
    void SendRequest()
    {
        dialog= new ProgressDialog(getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Waiting for Response.....");
        dialog.show();
        final String _url=url.getText().toString().trim();
        final StringRequest request=new StringRequest(method,_url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

               // List<RequestModel> models=database.requestDAO().listallRequests();
                //Toast.makeText(getContext(),"Saved requests:"+models.size(),Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                if(dialog!=null && dialog.isShowing())
                    dialog.dismiss();

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers.getData();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params.getData();
            }

            @Override
            protected Response parseNetworkResponse(NetworkResponse response) {
                String body=new String(response.data, StandardCharsets.UTF_8);
                long time =response.networkTimeMs;
                int scode=response.statusCode;
                SaveToDB(method,_url);
                if(dialog!=null && dialog.isShowing())
                    dialog.dismiss();
                HashMap<String,String>_headers=new HashMap<String, String>(response.headers);
                Intent intent=new Intent(getContext(), ResponseScreen.class);
                intent.putExtra("BODY",body);
                intent.putExtra("HEADERS",_headers);
                intent.putExtra("TIME",time);
                intent.putExtra("STATUSCODE",scode);
                getContext().startActivity(intent);
              //  Toast.makeText(getContext(),"response recieved",Toast.LENGTH_LONG).show();


                return super.parseNetworkResponse(response);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };
        AppRequestQueue.getInstance(getContext()).addToRequestQueue(request);

    }
    void SaveToDB(int method,String _url)
    {
        if(PrefManager.getnetworkbackupenabled(getContext()))
        {

            RequestModel model= new RequestModel(method,_url,System.currentTimeMillis(),headers.getData(),params.getData());
            database.Insert(model, new RequestDatabase.DatabaseWriteListener() {
                @Override
                public void onJobComplete() {
                    Toast.makeText(getContext(),"Write Success",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onException(Exception e) {
                    Toast.makeText(getContext(),"Write Error :"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }

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
            intent.putExtra("type", "network");
            startActivityForResult(intent,101);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101 && resultCode== Activity.RESULT_OK)
        {

            headers.setExpanded(false);
            params.setExpanded(false);
            headers.ClearAll();
            params.ClearAll();
            String url=data.getStringExtra("url");
            int method=data.getIntExtra("method",Request.Method.GET);
            String jsonheaders=data.getStringExtra("header");
            String paramheaders=data.getStringExtra("params");
            Map<String,String>headermap=TypeConverter.getMapFromJson(jsonheaders);
            for(Map.Entry<String,String>entry:headermap.entrySet())
            {

                headers.AddEntry(entry.getKey(),entry.getValue());
            }
            Map<String,String>paramsmap=TypeConverter.getMapFromJson(paramheaders);
            for(Map.Entry<String,String>entry:paramsmap.entrySet())
            {

                params.AddEntry(entry.getKey(),entry.getValue());
            }
            this.url.setText(url);
            ((RadioButton)radioGroup.getChildAt(method)).setChecked(true);
            Toast.makeText(getContext(),url,Toast.LENGTH_SHORT).show();
        }
    }
}