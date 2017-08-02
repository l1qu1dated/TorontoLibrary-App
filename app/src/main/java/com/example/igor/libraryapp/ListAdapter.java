package com.example.igor.libraryapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Igor on 7/30/2017.
 */

public class ListAdapter extends ArrayAdapter{
    ArrayList<String> branches = new ArrayList<>();
    ArrayList<String> hours = new ArrayList<>();
    ArrayList<String> branchFilter = new ArrayList<>();
    ArrayList<String> hoursFilter = new ArrayList<>();
    Context context;

    public ListAdapter(Activity context, ArrayList<String> branch, ArrayList<String> time){
        super(context, R.layout.custom_list, branch);

        this.context = context;
        this.branches = branch;
        this.hours = time;
    }

    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflator.inflate(R.layout.custom_list, null, true);

        TextView br = (TextView)rowView.findViewById(R.id.br);
        TextView hr = (TextView)rowView.findViewById(R.id.hr);
        branchFilter.addAll(branches);
        hoursFilter.addAll(hours);


        br.setText(branchFilter.get(position));
        hr.setText(hoursFilter.get(position));
        return rowView;
    }


}
