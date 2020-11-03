package com.jstechnologies.androidpostman.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.jstechnologies.androidpostman.Helpers.TypeConverter;

import java.util.Map;

@Entity(tableName = "request_data")
public class RequestModel {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "rmethod")
    int requestMethod;
    @ColumnInfo(name = "url")
    String Url;
    @ColumnInfo(name = "timestamp")
    long timestamp;
    @ColumnInfo(name = "fav")
    boolean isfav;
    @ColumnInfo(name = "headers")
   public String headers;
    @ColumnInfo(name = "params")
   public String params;

    public RequestModel() {
    }

    public RequestModel( int requestMethod, String url, long timestamp) {
        this.requestMethod = requestMethod;
        Url = url;
        this.timestamp = timestamp;
    }
    public RequestModel( int requestMethod, String url, long timestamp,Map<String,String>headers,Map<String,String>params) {
        this.requestMethod = requestMethod;
        Url = url;
        this.timestamp = timestamp;
        setHeaders(headers);
        setParams(params);
    }

    public String getHeaderasString()
    {
        return headers;
    }
    public String getParamsasString()
    {
        return params;
    }
    public Map<String,String> getHeaders() {

        return TypeConverter.getMapFromJson(headers);
    }

    public void setHeaders(Map<String,String> headers) {
        this.headers = TypeConverter.getJsonFromMap(headers);
    }

    public Map<String,String> getParams() {
        return TypeConverter.getMapFromJson(params);
    }

    public void setParams(Map<String,String> params) {
        this.params = TypeConverter.getJsonFromMap(params);
    }

    public boolean isIsfav() {
        return isfav;
    }

    public void setIsfav(boolean isfav) {
        this.isfav = isfav;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(int requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
