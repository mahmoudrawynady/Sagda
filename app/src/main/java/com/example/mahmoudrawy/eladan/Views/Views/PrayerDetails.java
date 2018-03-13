package com.example.mahmoudrawy.eladan.Views.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.mahmoudrawy.eladan.R;
import com.example.mahmoudrawy.eladan.Views.PrayerInstances.Data;
import com.example.mahmoudrawy.eladan.Views.PrayerInstances.Date;
import com.example.mahmoudrawy.eladan.Views.PrayerInstances.Hijri;
import com.example.mahmoudrawy.eladan.Views.PrayerInstances.Timings;
import com.example.mahmoudrawy.eladan.Views.Utilities.DataUtilities;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 Prayer Details Activity used to display times, country Name and Hijry dat.
 */


public class PrayerDetails extends AppCompatActivity {
    ///////////////////////////////////////////////////////////////////////////////////////
    //Bind Views using Butter Knife.
    @BindView(R.id.TXV_countryName)
    TextView countryName;
    @BindView(R.id.TXV_hijryDate)
    TextView hijryDate;
    @BindView(R.id.TXV_elFajrTime)
    TextView fajrTime;
    @BindView(R.id.TXV_elDohrTime)
    TextView dohrTime;
    @BindView(R.id.TXV_elAsrTime)
    TextView asrTime;
    @BindView(R.id.TXV_elMaghrebTime)
    TextView maghrebTime;
    @BindView(R.id.TXV_elAishTime)
    TextView aishaTime;
    ///////////////////////////////////////////////////////////////////////////////////////
    private Data data; //Contains prayer times and Hijry details;
    private String countryNameString;
    private Timings timings; // this contains Times.
    private Date date;// Data Object for current date;
    private Hijri hijri; //Contains Hijry Details.
    ///////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer_details);
        ButterKnife.bind(this);// Bind Views Using Butter Knife
        DataUtilities.setActivityActionBar(getSupportActionBar(),
                getString(R.string.activity_title));
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(DataUtilities.BUNDLE_TAG);
        data = (Data) bundle.getSerializable(DataUtilities.DATA_TAG);
        timings = data.getTimings();
        date = data.getDate();
        hijri = date.getHijri();
        countryNameString = bundle.getString(DataUtilities.COUNTRY_TAG);
        fillViewsWithData();
    }
    ///////////////////////////////////////////////////////////////////////////////////////

    /*
  To allow back from the top of the Screen.
   */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);//Back using Animation.
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    ///////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);//Back using Animation.
    }

    ///////////////////////////////////////////////////////////////////////////////////////

    /*
    to fill views with data.
     */
    private void fillViewsWithData() {
        countryName.setText(countryNameString);
        hijryDate.setText(hijri.getDate());
        fajrTime.setText(timings.getFajr());
        dohrTime.setText(timings.getDhuhr());
        asrTime.setText(timings.getAsr());
        maghrebTime.setText(timings.getMaghrib());
        aishaTime.setText(timings.getIsha());
    }
    ///////////////////////////////////////////////////////////////////////////////////////


}
