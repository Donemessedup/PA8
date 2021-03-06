/**
 * This file houses the activity for the place details screen which pulls data using the place details
 * API calls
 * CPSC 312-01, Fall 2021
 * Programming Assignment #8
 * No sources to cite.
 *
 * @author Aaron Miller
 * @author Wesley Muehlhausen
 * @version v1.0 11/24/21
 */

package com.example.pa8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class PlaceDetailsActivity extends AppCompatActivity {

    private static final int LOCATION_REQUEST_CODE = 1;
    static final String TAG = "PlaceDetailsActivity:";
    private PlaceDetailsActivity.GetDetailsLocationTask task;
    private static final String URI = "https://maps.googleapis.com/maps/api/place/details/";
    private static final String API = "AIzaSyBMvojmOv9vKtKfYiEd-lWvgbZWdGCvKCA";
    double latitude;
    double longitude;
    private String name;
    private String phoneNumber;
    private Boolean open;
    private String review;
    private PlaceDetailsActivity.GetPlaceLocationTask placeTask;

    private Place detailPlace;
    private ConstraintLayoutPlaceDetails layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_place_details);
        layout = new ConstraintLayoutPlaceDetails(this);
        setContentView(layout);


        Intent intent = getIntent();
        name = intent.getStringExtra("name");
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

    // builds a search URL for the API call by details
    private String buildSearchURL(String key) {
        String searchURL = URI;
        searchURL += "json?place_id=" + key + "&key=" + API;
        Log.d(TAG, "constructSearchURL: " + searchURL);
        return searchURL;
    }

    private String buildPhotoURL(String photoReference) {
        String photoURL = "https://maps.googleapis.com/maps/api/place/photo\n" +
                "  ?maxwidth=400";
        photoURL += "&photo_reference=" + photoReference + "&key=" + API;
        Log.d(TAG, "photoSearchURL: " + photoURL);
        return photoURL;
    }

    /*
    function parses jsonObject data to construct a place object
    @param placeJSON: the json data that the place object will use
    @return returns the place object created from the json data
     */
    private Place parsePlace(JSONObject placeJSON) {
        Place place = null;
        try {

            String id = placeJSON.getString("place_id");
            String name = placeJSON.getString("name");
            String vicinity = placeJSON.getString("vicinity");
            Double rating = placeJSON.getDouble("rating");
            JSONArray placesJSONArray = placeJSON.getJSONArray("photos");
            String photoReference = placesJSONArray.getJSONObject(0).getString("photo_reference");
            phoneNumber = placeJSON.getString("formatted_phone_number");
            open = placeJSON.getJSONObject("opening_hours").getBoolean("open_now");
            placesJSONArray = placeJSON.getJSONArray("reviews");
            review = placesJSONArray.getJSONObject(0).getString("text");
            place = new Place(id, name, vicinity, rating, "");
            Log.d(TAG, "parsePlace: " + id + " " + name + " " + vicinity + " " + rating + " " + phoneNumber + " " +
                    photoReference + " " + open + " " + review);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return place;
    }

// class for fetching place details data using API call
    class GetDetailsLocationTask extends AsyncTask<String, Void, Place> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //TODO: Progress bar setup
        }

        @Override
        protected Place doInBackground(String... strings) {
            Place place1 = null;
            String urlString = strings[0];

            try {
                URL url = new URL(urlString);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();

                InputStream in = httpsURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);
                int info = inputStreamReader.read();
                String jsonData = "";
                while (info != -1) {
                    jsonData += (char) info;
                    info = inputStreamReader.read();
                }

                Log.d(TAG, "doInBackground: " + jsonData);
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                place1 = parsePlace(jsonObject1);
            } catch (MalformedURLException exception) {

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return place1;
        }


        @Override
        protected void onPostExecute(Place place) {
            super.onPostExecute(place);

            detailPlace = place;

//            TextView nameTextView = findViewById(R.id.textView);
//            TextView addressTextView = findViewById(R.id.textView2);
//            TextView phoneTextView = findViewById(R.id.textView3);
//            TextView openTextView = findViewById(R.id.textView4);
//            TextView reviewTextView = findViewById(R.id.textView5);
//
//            Log.d(TAG, "parsePlace: " + detailPlace.getId() + " " + detailPlace.getName() + " " +
//                    detailPlace.getVicinity() + " " + detailPlace.getRating() + " " +
//                    phoneNumber + " " + detailPlace.getPhotoReference() + " " + open + " " + review);
//            phoneTextView.setText(phoneNumber);
//            openTextView.setText(open.toString());
//            reviewTextView.setText(review);
//            nameTextView.setText(detailPlace.getName());
//            addressTextView.setText(detailPlace.getVicinity());
            layout.nT(detailPlace.getName());
            layout.aT(detailPlace.getVicinity());
            layout.pT(phoneNumber);
            layout.oT(open.toString());
            layout.rT(review);
            //layout.iV();

            String photoURL = buildPhotoURL(detailPlace.getPhotoReference());
            placeTask = new PlaceDetailsActivity.GetPlaceLocationTask();
            placeTask.execute(photoURL);


            //TODO: Set up progress bar
        }
    }


    public void receivedPhotoBitmap(Bitmap bitmap) {
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);
    }



    class GetPlaceLocationTask extends AsyncTask<String, Void, Place> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //TODO: Progress bar setup
        }

        @Override
        protected Place doInBackground(String... strings) {
            String urlString = strings[0];

            try {
                URL url = new URL(urlString);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();

                InputStream in = httpsURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);
                int info = inputStreamReader.read();
                Bitmap bitmap;



                Log.d(TAG, "making an image");

            } catch (MalformedURLException exception) {

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Place place) {
            super.onPostExecute(place);

            detailPlace = place;




            //TODO: Set up progress bar
        }
    }
}
