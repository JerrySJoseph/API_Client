package com.jstechnologies.androidpostman.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.jstechnologies.androidpostman.Models.FCMRequests;

import java.util.List;

@Dao
public interface fcmDAO {

    @Query("SELECT * FROM 'fcm_request_data'")
    List<FCMRequests> listallFCMRequests();

    @Insert
    void insertFCM(FCMRequests model);

    @Delete
    void deleteFCM(FCMRequests model);
}
