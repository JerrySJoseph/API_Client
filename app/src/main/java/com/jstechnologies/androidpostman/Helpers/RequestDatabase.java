package com.jstechnologies.androidpostman.Helpers;

import android.app.PendingIntent;
import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.jstechnologies.androidpostman.DAO.RequestDAO;
import com.jstechnologies.androidpostman.DAO.fcmDAO;
import com.jstechnologies.androidpostman.Models.FCMRequests;
import com.jstechnologies.androidpostman.Models.RequestModel;

import java.util.ArrayList;

@Database(entities = {RequestModel.class,FCMRequests.class},exportSchema = false,version = 2)
public abstract class RequestDatabase extends RoomDatabase {
    private static final String DB_NAME="requests_db";
    private static RequestDatabase mInstance;
    static Context _context;

    public static synchronized RequestDatabase getInstance(Context context)
    {
        if(mInstance==null)
        {
            _context=context;
            mInstance= Room.databaseBuilder(context.getApplicationContext(),RequestDatabase.class,DB_NAME).
                    fallbackToDestructiveMigration().build();
        }
        return mInstance;
    }


    public abstract RequestDAO requestDAO();
    public abstract fcmDAO fcmDAO();

    public void Insert(final Object model, final DatabaseWriteListener mlistener)
    {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                try{
                    if(model instanceof RequestModel)
                        requestDAO().insertRequest((RequestModel) model);
                    if(model instanceof FCMRequests)
                        fcmDAO().insertFCM((FCMRequests) model);

                }catch (Exception e)
                {
                    mlistener.onException(e);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mlistener.onJobComplete();
                super.onPostExecute(aVoid);
            }
        }.execute();

    }
    public void Delete(final Object model, final DatabaseWriteListener mlistener)
    {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                try{
                    if(model instanceof RequestModel)
                        requestDAO().deleteRequest((RequestModel)model);
                    if(model instanceof FCMRequests)
                        fcmDAO().deleteFCM((FCMRequests)model);

                }catch (Exception e)
                {
                    mlistener.onException(e);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mlistener.onJobComplete();
                super.onPostExecute(aVoid);
            }
        }.execute();

    }
    public void ListAllRequests(final DataBaseReadListener mlistener )
    {
        new AsyncTask<Void,Void,Void>(){
            ArrayList<Object> models=new ArrayList<>();
            @Override
            protected Void doInBackground(Void... voids) {
                try{
                    models.clear();
                    models.addAll(requestDAO().listallRequests());
                    models.addAll(fcmDAO().listallFCMRequests());

                }catch (Exception e)
                {
                    mlistener.onException(e);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mlistener.onListrecieved(models);
                super.onPostExecute(aVoid);
            }
        }.execute();
    }
    public void ListNetworkRequests(final DataBaseReadListener mlistener )
    {
        new AsyncTask<Void,Void,Void>(){
            ArrayList<Object> models=new ArrayList<>();
            @Override
            protected Void doInBackground(Void... voids) {
                try{
                    models.clear();
                    models.addAll(requestDAO().listallRequests());

                }catch (Exception e)
                {
                    mlistener.onException(e);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mlistener.onListrecieved(models);
                super.onPostExecute(aVoid);
            }
        }.execute();
    }
    public void ListFCMRequests(final DataBaseReadListener mlistener )
    {
        new AsyncTask<Void,Void,Void>(){
            ArrayList<Object> models=new ArrayList<>();
            @Override
            protected Void doInBackground(Void... voids) {
                try{
                    models.clear();
                    models.addAll(fcmDAO().listallFCMRequests());

                }catch (Exception e)
                {
                    mlistener.onException(e);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mlistener.onListrecieved(models);
                super.onPostExecute(aVoid);
            }
        }.execute();
    }
    public interface DatabaseWriteListener
    {
        void onJobComplete();
        void onException(Exception e);
    }
    public interface DataBaseReadListener
    {
        void onListrecieved(ArrayList<Object>list);
        void onException(Exception e);
    }

}
