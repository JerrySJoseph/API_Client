package com.jstechnologies.androidpostman.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.jstechnologies.androidpostman.Helpers.TypeConverter;

import java.util.Map;

@Entity(tableName = "fcm_request_data")
public class FCMRequests {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "server_key")
    String serverKey;
    @ColumnInfo(name = "topics")
    String topics;
    @ColumnInfo(name = "token")
    String token;
    @ColumnInfo(name = "payload")
    public String payload;

    public FCMRequests() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FCMRequests(String serverKey, String topics, String token) {
        this.serverKey = serverKey;
        this.topics = topics;
        this.token = token;
    }

    public String getPayloadasJson() {
        return payload;
    }
    public Map<String,String> getPayload() {
        return TypeConverter.getMapFromJson(payload);
    }
    public void setPayload(Map<String,String> payload) {
        this.payload = TypeConverter.getJsonFromMap(payload);
    }

    public String getServerKey() {
        return serverKey;
    }

    public void setServerKey(String serverKey) {
        this.serverKey = serverKey;
    }

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
