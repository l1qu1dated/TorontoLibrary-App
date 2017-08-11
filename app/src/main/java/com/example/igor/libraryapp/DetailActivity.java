package com.example.igor.libraryapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;

import java.util.ArrayList;

/**
 * Created by Igor on 7/30/2017.
 */

public class DetailActivity extends AppCompatActivity{

    private MapView mapView;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();
        ArrayList<String> data;
        data = bundle.getStringArrayList("data");

        TextView address = (TextView) findViewById(R.id.address);
        TextView telephone = (TextView) findViewById(R.id.telephone);
        TextView monday = (TextView) findViewById(R.id.monday_time);
        TextView tuesday = (TextView) findViewById(R.id.tuesday_time);
        TextView wednesday = (TextView) findViewById(R.id.wednesday_time);
        TextView thursday = (TextView) findViewById(R.id.thursday_time);
        TextView friday = (TextView) findViewById(R.id.friday_time);
        TextView saturday = (TextView) findViewById(R.id.saturday_time);
        TextView sunday = (TextView) findViewById(R.id.sunday_time);

        address.setText("Address: " + data.get(1) + " " + data.get(3));
        telephone.setText("Telephone: " + data.get(2));
        monday.setText(data.get(4));
        tuesday.setText(data.get(5));
        wednesday.setText(data.get(6));
        thursday.setText(data.get(7));
        friday.setText(data.get(8));
        saturday.setText(data.get(9));
        sunday.setText(data.get(10));

        getSupportActionBar().setTitle(data.get(0));

    }




}
