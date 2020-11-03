package com.jstechnologies.androidpostman.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.jstechnologies.androidpostman.Models.FCMRequests;
import com.jstechnologies.androidpostman.Models.RequestModel;

import java.util.List;

@Dao
public interface RequestDAO {

    @Query("SELECT * FROM 'request_data'")
    List<RequestModel> listallRequests();



    @Insert
    void insertRequest(RequestModel model);

    @Delete
    void deleteRequest(RequestModel model);


}
