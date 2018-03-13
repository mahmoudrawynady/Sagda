
package com.example.mahmoudrawy.eladan.Views.PrayerInstances;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data implements Serializable {
    ///////////////////////////////////////////////////////////////////////////////////
    @SerializedName("timings")
    @Expose
    private Timings timings;
    @SerializedName("date")
    @Expose
    private Date date;

    ///////////////////////////////////////////////////////////////////////////////////
    public Timings getTimings() {
        return timings;
    }

    public void setTimings(Timings timings) {
        this.timings = timings;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
///////////////////////////////////////////////////////////////////////////////////


}
