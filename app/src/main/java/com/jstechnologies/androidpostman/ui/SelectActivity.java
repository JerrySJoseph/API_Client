package com.jstechnologies.androidpostman.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.jstechnologies.androidpostman.Adapters.RecentsAdapter;
import com.jstechnologies.androidpostman.Helpers.ItemSwipeCallback;
import com.jstechnologies.androidpostman.Helpers.RequestDatabase;
import com.jstechnologies.androidpostman.Models.FCMRequests;
import com.jstechnologies.androidpostman.Models.RequestModel;
import com.jstechnologies.androidpostman.R;

import java.util.ArrayList;

public class SelectActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecentsAdapter adapter;
    ArrayList<Object> models= new ArrayList<>();
    RequestDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Recent requests");
        String type=getIntent().getStringExtra("type");
        LoadData(type);
        recyclerView=findViewById(R.id.recycler);
        adapter=new RecentsAdapter(models);
        adapter.setItemClickListener(new RecentsAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(Object request) {
                Intent intent= new Intent();
                if(request instanceof RequestModel)
                {
                    intent.putExtra("url",((RequestModel)request).getUrl());
                    intent.putExtra("method",((RequestModel)request).getRequestMethod());
                    intent.putExtra("timestamp",((RequestModel)request).getTimestamp());
                    intent.putExtra("header",((RequestModel)request).getHeaderasString());
                    intent.putExtra("params",((RequestModel)request).getParamsasString());
                }
                if(request instanceof FCMRequests)
                {
                    intent.putExtra("serverkey",((FCMRequests)request).getServerKey());
                    intent.putExtra("topic",((FCMRequests)request).getTopics());
                    intent.putExtra("token",((FCMRequests)request).getToken());
                    intent.putExtra("payload",((FCMRequests)request).getPayloadasJson());
                }
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        enableSwipeToDeleteAndUndo();
    }
    void LoadData(String type)
    {
        if(type.equals("network"))
        {
            RequestDatabase.getInstance(this).ListNetworkRequests(new RequestDatabase.DataBaseReadListener() {
                @Override
                public void onListrecieved(ArrayList<Object> list) {
                    models.clear();
                    models.addAll(list);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(SelectActivity.this,"size:"+list.size(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onException(Exception e) {

                }
            });
        }
        else if(type.equals("fcm"))
        {
            RequestDatabase.getInstance(this).ListFCMRequests(new RequestDatabase.DataBaseReadListener() {
                @Override
                public void onListrecieved(ArrayList<Object> list) {
                    models.clear();
                    models.addAll(list);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(SelectActivity.this,"size:"+list.size(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onException(Exception e) {

                }
            });
        }
        else
            RequestDatabase.getInstance(this).ListAllRequests(new RequestDatabase.DataBaseReadListener() {
                @Override
                public void onListrecieved(ArrayList<Object> list) {
                    models.clear();
                    models.addAll(list);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(SelectActivity.this,"size:"+list.size(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onException(Exception e) {

                }
            });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void enableSwipeToDeleteAndUndo() {
        ItemSwipeCallback swipeToDeleteCallback = new ItemSwipeCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final RequestModel request =(RequestModel)adapter.getModels().get(position);

                if(i==ItemTouchHelper.RIGHT)
                {
                    Intent intent= new Intent();
                    intent.putExtra("url",request.getUrl());
                    intent.putExtra("method",request.getRequestMethod());
                    intent.putExtra("timestamp",request.getTimestamp());
                    intent.putExtra("header",request.getHeaderasString());
                    intent.putExtra("params",request.getParamsasString());
                    setResult(Activity.RESULT_OK,intent);
                    finish();

                }


            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }
}