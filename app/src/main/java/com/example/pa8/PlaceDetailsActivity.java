package com.example.pa8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class PlaceDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_place_details);
        ConstraintLayoutPlaceDetails layout = new ConstraintLayoutPlaceDetails(this);
        setContentView(layout);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String searchURL = buildSearchURL(name);
        task = new PlaceDetailsActivity.GetDetailsLocationTask();
        task.execute(searchURL);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.place_details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        PlaceDetailsActivity.this.finish();
        return super.onOptionsItemSelected(item);
    }
}