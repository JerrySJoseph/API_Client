package com.jstechnologies.androidpostman.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.jstechnologies.androidpostman.Models.FCMRequests;
import com.jstechnologies.androidpostman.Models.RequestModel;
import com.jstechnologies.androidpostman.R;
import com.jstechnologies.androidpostman.ui.NewRequest.NewRequestFragment;
import com.jstechnologies.androidpostman.ui.Recents.RecentsFragment;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecentsAdapter extends RecyclerView.Adapter<RecentsAdapter.RecentHolder> {

    ArrayList<Object>models;
    Context context;
    ItemClickListener itemClickListener;


    public interface ItemClickListener{
        void onItemClicked(Object request);
    }

    public RecentsAdapter(ArrayList<Object> models) {
        this.models = models;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    @NonNull
    @Override
    public RecentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context=parent.getContext();
        return new RecentHolder(
                LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.item_recent,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecentHolder holder, int position) {
        holder.Bind(models.get(position));
    }
    public void removeItem(int position) {
        models.remove(position);
        notifyItemRemoved(position);
    }

    public ArrayList<Object> getModels() {
        return models;
    }

    public void restoreItem(Object item, int position) {
        models.add(position, item);
        notifyItemInserted(position);
    }
    @Override
    public int getItemCount() {
        return models.size();
    }

    public class RecentHolder extends RecyclerView.ViewHolder{
        TextView method,url,timestamp;
        ImageView use;

        public RecentHolder(@NonNull View itemView) {
            super(itemView);
            method=itemView.findViewById(R.id.method);
            url=itemView.findViewById(R.id.url);
            timestamp=itemView.findViewById(R.id.timestamp);
            use=itemView.findViewById(R.id.use);
        }
        public void Bind(final Object model)
        {
           if(model instanceof RequestModel)
           {

               final String _method;
               switch (((RequestModel)model).getRequestMethod())
               {
                   case Request.Method.GET:_method="GET";break;
                   case Request.Method.PUT:_method="PUT";break;
                   case Request.Method.POST:_method="POS";break;
                   case Request.Method.DELETE:_method="DEL";break;
                   default: _method="NON";
               }
               this.method.setText(_method);
               url.setText(((RequestModel)model).getUrl());
               Date date=new Date(((RequestModel)model).getTimestamp());
               SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
               timestamp.setText(format.format(date));
               use.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       if(itemClickListener!=null)
                           itemClickListener.onItemClicked(model);
                   }
               });
           }
           if(model instanceof FCMRequests)
           {
               this.method.setText("FCM");
               url.setText(((FCMRequests)model).getServerKey().substring(0,25)+"...");
               if(((FCMRequests)model).getToken()!=null)
                    timestamp.setText(((FCMRequests)model).getToken().substring(0,25)+"...");
               else
                   timestamp.setText(((FCMRequests)model).getTopics().substring(0,25)+"...");
               use.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       if(itemClickListener!=null)
                           itemClickListener.onItemClicked(model);
                   }
               });
           }
        }
    }
}
