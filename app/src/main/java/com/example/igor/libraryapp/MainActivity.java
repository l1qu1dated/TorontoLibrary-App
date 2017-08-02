package com.example.igor.libraryapp;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DBHelper myDB;
    private static ArrayList<String> branches;
    private static ArrayList<String> branch_hours;
    private static ArrayList<Integer> ids;
    private Cursor res;
    private ListView myList;
    //private ListAdapter adapter;
    public ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize variables
        Calendar calendar = GregorianCalendar.getInstance();
        myList = (ListView)findViewById(R.id.listView);
        myDB = new DBHelper(this);
        res = myDB.getAllData();
        branches = new ArrayList<>();
        branch_hours = new ArrayList<>();
        ids = new ArrayList<>();

        int checkDB = checkIfEmpty();
        int day = calendar.get(GregorianCalendar.DAY_OF_WEEK);

        if(checkDB <= 0){ readCSV(); } //only run if Database is empty

        if (res.getCount() == 0){ return; }

        while(res.moveToNext()){
            String name = res.getString(1);
            int id = Integer.parseInt(res.getString(0));
            String hours = "";
            //String hours = res.getString(day + 3);
            switch(day){
                case GregorianCalendar.MONDAY:
                    hours = res.getString(5);
                    break;
                case GregorianCalendar.TUESDAY:
                    hours = res.getString(6);
                    break;
                case GregorianCalendar.WEDNESDAY:
                    hours = res.getString(7);
                    break;
                case GregorianCalendar.THURSDAY:
                    hours = res.getString(8);
                    break;
                case GregorianCalendar.FRIDAY:
                    hours = res.getString(9);
                    break;
                case GregorianCalendar.SATURDAY:
                    hours = res.getString(10);
                    break;
                case GregorianCalendar.SUNDAY:
                    hours = res.getString(11);
                    break;
            }

            branches.add(name);
            branch_hours.add(hours);
            ids.add(id);
        }

        //adapter = new ListAdapter(this, branches, branch_hours);
        /*adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_2, android.R.id.text1,  branches){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                Log.d("view2", "testing: " + branches.get(position)+ " " + branch_hours.get(position));

                View view = super.getView(position, convertView, parent);

                TextView text1 = (TextView)view.findViewById(android.R.id.text1);
                text1.setText(branches.get(position));
                text1.setTextSize(18);
                text1.setTypeface(null, Typeface.BOLD);
                //TextView text2 = (TextView)view.findViewById(android.R.id.text2);
                //text2.setText(branch_hours.get(position));
                return view;
            }
        };*/
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, branches);
        myList.setAdapter(adapter);


        myList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String text = (String)adapterView.getItemAtPosition(i);
                Log.d("search", "test: " + text);

                itemSelected(i, text);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = (SearchView)item.getActionView();
        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                adapter.getFilter().filter(s);

                return true;
            }
        });

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.custom_list, R.id.br, branches);

        //myList.setAdapter(adapter);


        return super.onCreateOptionsMenu(menu);

    }

    //extracts data from csv file and saves it in database
    public void readCSV(){

        InputStream is = getResources().openRawResource(R.raw.libraryinfo);
        InputStream is2 = getResources().openRawResource(R.raw.libraryhours);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8")));
        BufferedReader reader2 = new BufferedReader(
                new InputStreamReader(is2, Charset.forName("UTF-8")));
        String line = "";
        String line2 = "";

        try {
            //skip headers
            reader.readLine();
            reader2.readLine();

            while ((line = reader.readLine()) != null && (line2 = reader2.readLine()) != null){

                //split
                String[] tokens = line.split(",");
                String[] tokens2 = line2.split(",");

                //read data

                myDB.insertData(tokens[0], tokens[1], tokens[3],
                        tokens[2], tokens2[1], tokens2[2], tokens2[3],
                        tokens2[4], tokens2[5], tokens2[6], tokens2[7]);

            }
        } catch (IOException e) {
            Log.wtf("My activity", "Error reading data file on line " + line + " or " + line2, e);
            e.printStackTrace();
        }
    }

    //returns the number of records in the database
    public int checkIfEmpty(){
        SQLiteDatabase db = myDB.getWritableDatabase();

        String query = "Select count(*) from branch_table";

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        return count;
    }

    private void itemSelected(int position, String name){

        for(int i = 0; i < branches.size(); i++){
            if(branches.get(i).contains(name)){
                position = i;
                break;
            }
        }

        String query = "SELECT * FROM branch_table WHERE id = " + ids.get(position).toString();
        SQLiteDatabase sql = myDB.getReadableDatabase();
        Cursor cur = sql.rawQuery(query, null);
        ArrayList<String> data = new ArrayList<>();
        if (cur.moveToFirst()){
            do{
                data.add(cur.getString(cur.getColumnIndex("name")));
                data.add(cur.getString(cur.getColumnIndex("address")));
                data.add(cur.getString(cur.getColumnIndex("telephone")));
                data.add(cur.getString(cur.getColumnIndex("postalCode")));
                data.add(cur.getString(cur.getColumnIndex("monday")));
                data.add(cur.getString(cur.getColumnIndex("tuesday")));
                data.add(cur.getString(cur.getColumnIndex("wednesday")));
                data.add(cur.getString(cur.getColumnIndex("thursday")));
                data.add(cur.getString(cur.getColumnIndex("friday")));
                data.add(cur.getString(cur.getColumnIndex("saturday")));
                data.add(cur.getString(cur.getColumnIndex("sunday")));
                data.add(cur.getString(cur.getColumnIndex("id")));
                data.add(cur.getString(cur.getColumnIndex("favorite")));

            }while(cur.moveToNext());
        }
        Intent resultIntent = new Intent(this, DetailActivity.class);

        resultIntent.putExtra("data", data);

        startActivity(resultIntent);


    }


}
