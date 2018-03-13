
package com.example.mahmoudrawy.eladan.Views.PrayerInstances;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hijri implements Serializable {
    ///////////////////////////////////////////////////////////////////////////////////
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("format")
    @Expose
    private String format;
    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("year")
    @Expose
    private String year;
    @SerializedName("holidays")
    @Expose
    private List<Object> holidays = null;

    ///////////////////////////////////////////////////////////////////////////////////
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }


    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }


    public List<Object> getHolidays() {
        return holidays;
    }

    public void setHolidays(List<Object> holidays) {
        this.holidays = holidays;
    }
    ///////////////////////////////////////////////////////////////////////////////////
}
