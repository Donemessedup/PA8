package com.example.pa8;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


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

public class PlaceSearchActivity extends AppCompatActivity {

    //NAME: WES AND AARON
    //PROJECT: PA8
    //DATE: 12/9/2021
    //TITLE: WEB SERVICES LOCATION

    //PRIVATE VARIABLES FOR API
    private static final String URI = "https://maps.googleapis.com/maps/api/place/nearbysearch";
    private static final String API = "AIzaSyBMvojmOv9vKtKfYiEd-lWvgbZWdGCvKCA";
    private static final int LOCATION_REQUEST_CODE = 1;
    static final String TAG = "PlaceSearchActivity:";//For debugging in logcat

    //PRIVATE MEMBER VARIABLES
    private List<Place> placeList = new ArrayList<>();//arraylist of places
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GetNearMeLocationTask task;//gets location
    double latitude;
    double longitude;
    CustomAdapter adapter;
    ProgressBar progressBar;
    ActivityResultLauncher<Intent> launcher;

    //ON CREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_search);

        EditText searchBar = findViewById(R.id.searchBar);//object for search bar

        //LAUNCHER TO SEND INTENT TO NEXT SCREEN
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Log.d(TAG, "onActivityResult: ");
                    }
                });

        //HIT THE SEARCH BUTTON
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                //call search method
                if(i == EditorInfo.IME_ACTION_SEARCH) {
                    String searchURL = buildSearchURL(searchBar.getText().toString());
                    task = new GetNearMeLocationTask();
                    task.execute(searchURL);
                    return true;
                }
                return false;
            }
        });

        //HIT THE X BUTTON
        searchBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    //TODO: Get X button functionality
                    searchBar.setText("");
                    placeList = null;
                    adapter.notifyDataSetChanged();
                }
                return false;
            }
        });

        //ENABLE LOCATION
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        enableMyLocation();

        //SET UP RECYCLER VIEW
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //SET UP CUSTOM ADAPTER
        adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);

    }

    //INFLATE MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.place_search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //WHEN ONE OF THE MENU ITEMS ARE SELECTED
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        switch(itemID) {
            case R.id.fetchLocationItem:
                Toast.makeText(this, "Fetch Location", Toast.LENGTH_SHORT).show();
                enableMyLocation();
                break;
            case R.id.searchItem:
                Toast.makeText(this, "Searched Button", Toast.LENGTH_SHORT).show();
                EditText searchBar = findViewById(R.id.searchBar);
                String searchURL = buildSearchURL(searchBar.getText().toString());
                task = new GetNearMeLocationTask();
                task.execute(searchURL);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //MAKE SURE WE CAN USE API SAFELY
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();

            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }

    }

    //CREATE SEARCH URL
    private String buildSearchURL(String key) {
        String searchURL = URI;
        searchURL +=
                "/json?location=" +
                        latitude + "," +
                        longitude +
                        "&radius=3000&rankBy=distance&keyword="
                        + key + "&key=" +
                        API;
        Log.d(TAG, "constructSearchURL: " + searchURL);
        return searchURL;
    }

    //ENABLE THE PHONE'S LOCATION
    private void enableMyLocation() {
        Toast.makeText(this, "Enabling Location", Toast.LENGTH_SHORT).show();
        //TRY TO GET USERS FINE LOCATION
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();//HERE
                    }
                }
            });

        }
        else {//WE DONE HAVE PERMISSION, SO TRY TO GET PERMISSION
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE);
        }
    }

    //GET DATA FROM JSON FILE
    private Place parsePlace(JSONObject placeJSON) {
        Place place = null;
        try {
            String id = placeJSON.getString("place_id");
            String name = placeJSON.getString("name");
            String vicinity = placeJSON.getString("vicinity");
            Double rating = placeJSON.getDouble("rating");
            place = new Place(id, name, vicinity, rating, "");
            Log.d(TAG, "parsePlace: " + id + " " + name + " " + vicinity + " " + rating);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return place;
    }

    //GET STUFF NEAR ME BASED ON LOCATION
    class GetNearMeLocationTask extends AsyncTask<String, Void, List<Place>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        //MAIN
        @Override
        protected List<Place> doInBackground(String... strings) {
            List<Place> placeList1 = new ArrayList<>();
            String urlString = strings[0];

            try {
                URL url = new URL(urlString);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                //get json data
                InputStream in = httpsURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);
                int info = inputStreamReader.read();
                String jsonData = "";
                while(info != -1) {
                    jsonData += (char) info;
                    info = inputStreamReader.read();
                }

                Log.d(TAG, "doInBackground: " + jsonData);
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray placesJSONArray = jsonObject.getJSONArray("results");
                //go through all places
                Log.d(TAG, "Number of Hits: " + placesJSONArray.length());
                for(int i = 0; i < placesJSONArray.length(); i++) {
                    JSONObject placeObject = placesJSONArray.getJSONObject(i);
                    Place place = parsePlace(placeObject);
                    if(place != null) {
                        placeList1.add(place);
                    }
                    else {
                        Log.d(TAG, "Wes likes tigers");
                    }
                }

            }
            catch(MalformedURLException exception) {
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return placeList1;
        }


        @Override
        protected void onPostExecute(List<Place> places) {
            super.onPostExecute(places);

            //update the adapter
            placeList = places;
            adapter.notifyDataSetChanged();
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);

            //TODO: Set up progress bar
        }
    }

    //  CREATE CUSTOM ADAPTER
    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
        CardView cardView1;
        class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView textView;
            TextView textView2;
            public CustomViewHolder(@NonNull View itemView) {
                super(itemView);
                cardView1 = itemView.findViewById(R.id.myCardView1);
                textView = itemView.findViewById(R.id.MyText1);
                textView2 = itemView.findViewById(R.id.MyText2);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlaceSearchActivity.this, PlaceDetailsActivity.class);
                intent.putExtra("name", placeList.get(getAdapterPosition()).getId());
                launcher.launch(intent);
            }

            public void updateView(String title, String vicinity) {
                cardView1.setCardBackgroundColor(getResources().getColor(R.color.white));
                Log.d(TAG, "Vicinity is " + vicinity);
                textView.setText(title);
                textView2.setText(vicinity);
            }

        }

        @NonNull
        @Override
        public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if(placeList.size() != 0) {
                View view = LayoutInflater.from(PlaceSearchActivity.this).inflate(R.layout.card_view_place_item, parent, false);
                return new CustomViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
            String text;
            String vicinity;
            if(placeList != null){
                text = "" + placeList.get(position).getName() + "(" + placeList.get(position).getRating() + ")";
                vicinity = "" + placeList.get(position).getVicinity();
            }
            else{
                text = "Null Value Caught. Fix it chief";
                vicinity = "fix it chief";
            }

            holder.updateView(text, vicinity);
        }

        @Override
        public int getItemCount() {
            if(placeList != null)
                return placeList.size();
            else
                return 0;
        }
    }





}
