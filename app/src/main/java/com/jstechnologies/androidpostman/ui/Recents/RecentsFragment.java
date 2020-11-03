package com.jstechnologies.androidpostman.ui.Recents;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.android.material.snackbar.Snackbar;
import com.jstechnologies.androidpostman.Adapters.RecentsAdapter;
import com.jstechnologies.androidpostman.Helpers.ItemSwipeCallback;
import com.jstechnologies.androidpostman.Helpers.RequestDatabase;
import com.jstechnologies.androidpostman.Models.FCMRequests;
import com.jstechnologies.androidpostman.Models.RequestModel;
import com.jstechnologies.androidpostman.R;

import java.util.ArrayList;
import java.util.List;

public class RecentsFragment extends Fragment {

    RecyclerView recyclerView;
    RecentsAdapter adapter;
    ArrayList<Object>models= new ArrayList<>();
    RequestDatabase database;
    public RecentsFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recents, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database=RequestDatabase.getInstance(getContext());
        LoadData();
        recyclerView=view.findViewById(R.id.recycler);
        adapter=new RecentsAdapter(models);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        enableSwipeToDeleteAndUndo();
    }

    void LoadData()
    {

        RequestDatabase.getInstance(getContext()).ListAllRequests(new RequestDatabase.DataBaseReadListener() {
            @Override
            public void onListrecieved(ArrayList<Object> list) {
                models.clear();
                models.addAll(list);
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(),"size:"+list.size(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(Exception e) {

            }
        });
    }
    void Addmodels()
    {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPostExecute(Void aVoid) {
                adapter.notifyDataSetChanged();
                super.onPostExecute(aVoid);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                models.clear();
                models.addAll(database.requestDAO().listallRequests());
                return null;
            }
        }.execute();
    }
    private void enableSwipeToDeleteAndUndo() {
        ItemSwipeCallback swipeToDeleteCallback = new ItemSwipeCallback(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final Object item =  adapter.getModels().get(position);

                if(i==ItemTouchHelper.LEFT )
                {
                    //Delete Sequence
                    adapter.removeItem(position);
                    RequestDatabase.getInstance(getContext()).Delete(item, new RequestDatabase.DatabaseWriteListener() {
                        @Override
                        public void onJobComplete() {
                            Snackbar snackbar = Snackbar
                                    .make(getView(), "Item was removed from the list.", Snackbar.LENGTH_LONG);
                            snackbar.setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    RequestDatabase.getInstance(getContext()).Insert(item, new RequestDatabase.DatabaseWriteListener() {
                                        @Override
                                        public void onJobComplete() {
                                            adapter.restoreItem(item, position);
                                            recyclerView.scrollToPosition(position);
                                        }

                                        @Override
                                        public void onException(Exception e) {

                                        }
                                    });

                                }
                            });

                            snackbar.setActionTextColor(Color.YELLOW);
                            snackbar.show();
                        }

                        @Override
                        public void onException(Exception e) {

                        }
                    });

                }

                if(i==ItemTouchHelper.RIGHT)
                {
                    //Open url Sequence


                }


            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_response,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}