package www.mara.android.com.ActivitiesPackage;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import com.skyfishjy.library.RippleBackground;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import www.mara.android.com.DirectionHelpersPackage.FetchDownloadUrl;
import www.mara.android.com.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, OnMarkerClickListener {

    //Object related to google map
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;
    private Location userLastKnownLocation;
    private LocationCallback locationCallback;
    private RippleBackground rippleBg;


    //Oblects related to the xml views
    private MaterialSearchBar materialSearchBar;
    private Button btnLocateCenter, btnFindExpert;
    private View mapView;
    private Dialog aboutPopUpDialog;
    private  Button aboutPopUpButton;


    private final float DEFAULT_ZOOM = 13;

    private long backPressedTime;
    private Toast backPressedToast;
    public String key;

    private Marker rehubMarker;
    private LatLng currentUserLocation = new LatLng(-0.077059, 34.771637);

    private LatLng ksmCountyReferalHsp = new LatLng(-0.101537, 34.755620);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);


        //This code is for the drawer layout
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        /**ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();**/
        navigationView.setNavigationItemSelectedListener(this);


        //Setting bounderies to restrict the map to Kisumu county
       /** if (mMap != null)
        {
           double bottomBoundery =

        }**/

        //initializing the views
        materialSearchBar = (MaterialSearchBar) findViewById(R.id.placesSearchBar);
        btnLocateCenter = (Button)findViewById(R.id.btnLocateCenter);
        //rippleBg = (RippleBackground)findViewById(R.id.rippleBg);
        btnFindExpert = findViewById(R.id.btnContactExpert);

        //initializing the aboutPopUpDialog
        aboutPopUpDialog = new Dialog(this);


        //implementing the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_id);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        //Initializing objects related to googlemap by creating their instance
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Places.initialize(MainActivity.this, getString(R.string.google_api_key));
        placesClient = Places.createClient(this);
        final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        //Enabling the user to serch for places
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener()
        {
            @Override
            public void onSearchStateChanged(boolean enabled)
            {

            }

            @Override
            public void onSearchConfirmed(CharSequence text)
            {
                startSearch(text.toString(), true, null, true);
            }

            @Override
            public void onButtonClicked(int buttonCode)
            {
                //This code is for the functions of the button on the MaterialSearchBar
                if (buttonCode == MaterialSearchBar.BUTTON_NAVIGATION)
                {
                    DrawerLayout drawer = findViewById(R.id.drawer_layout);
                    NavigationView navigationView = findViewById(R.id.nav_view);

                    drawer.openDrawer(Gravity.LEFT);

                }
                else if (buttonCode == MaterialSearchBar.BUTTON_BACK)
                {
                    materialSearchBar.disableSearch();
                }

            }
        });
        //This functionality is for determining what happens when the user inputs text on the keybord
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                //Finding prediction request
                final FindAutocompletePredictionsRequest predictionsRequest = FindAutocompletePredictionsRequest.builder()
                        .setCountry("ke")
                        .setTypeFilter(TypeFilter.ADDRESS)
                        .setSessionToken(token)
                        .setQuery(s.toString())
                        .build();
                //passing the request object to the places client
                placesClient.findAutocompletePredictions(predictionsRequest).addOnCompleteListener(new OnCompleteListener<FindAutocompletePredictionsResponse>()
                {
                    @Override
                    public void onComplete(@NonNull Task<FindAutocompletePredictionsResponse> task)
                    {
                        if (task.isSuccessful())
                        {
                            //if the task is successful we'll find out the suggestions received from google
                            FindAutocompletePredictionsResponse predictionsResponse = task.getResult();
                            if (predictionsRequest != null)
                            {
                                //setting the result in an array list
                                predictionList = predictionsResponse.getAutocompletePredictions();
                                //converting the prediction results into a List type array for String
                                List<String> suggestionList = new ArrayList<>();
                                for (int i = 0; i < predictionList.size(); i++)
                                {
                                    AutocompletePrediction prediction = predictionList.get(i);
                                    suggestionList.add(prediction.getFullText(null).toString());
                                }
                                //Updating the suggestions on the search bar
                                materialSearchBar.updateLastSuggestions(suggestionList);
                                if (!materialSearchBar.isSuggestionsVisible())
                                {
                                    //if the suggestion is not vissible we'll show the suggestion list
                                    materialSearchBar.showSuggestionsList();
                                }

                            }




                        }
                        else
                        {
                            Log.i("myTag", "places fetching task unsuccessful");
                        }
                    }
                });

            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
        materialSearchBar.setSuggestionsClickListener(new SuggestionsAdapter.OnItemViewClickListener()
        {
            @Override
            public void OnItemClickListener(int position, View v)
            {
                if (position >= predictionList.size())
                {
                    return;
                }
                AutocompletePrediction selectedPrediction = predictionList.get(position);
                String suggestions = materialSearchBar.getLastSuggestions().get(position).toString();
                materialSearchBar.setText(suggestions);
            }

            @Override
            public void OnItemDeleteListener(int position, View v)
            {

            }
        });
        btnLocateCenter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showMarkersOnTheMap();


            }
        });
        btnFindExpert.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this, ConnectWithExpert.class));
            }
        });
        /*
        Adding a crash Button (Start)

        Button crashButton = new Button(this);
        crashButton.setText("Crash!");
        crashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //forcing a crash
                Crashlytics.getInstance().crash();


            }
        });

        addContentView(crashButton,  new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));


        adding a crash button (End)
         */

        /*setBasicCrashKey( key);
        enableRuntime();
        resetKey();*/


    }






    //Method for setting basic crashlytics key

    public void setBasicCrashKey(String key)
    {
        /* start crash set keys */
        Crashlytics.setString(key, "foo");
        Crashlytics.setBool(key, true);
        Crashlytics.setInt(key, 1);
        Crashlytics.setFloat(key, 1.0f);
        Crashlytics.setDouble(key, 1.0);
        /*end of crash keys*/

    }

    public void resetKey()
    {
        Crashlytics.setInt("current_level", 3);
        Crashlytics.setString("last_iu_action", "logged in");

    }

    public void enableRuntime()
    {
        Fabric.with(this, new Crashlytics());
    }





    //This method is for displaying markers on the map
    private void showMarkersOnTheMap()

    {
        /***  //Adding a marker at Kisumu county referral hospital
        LatLng ksmCountyReferalHsp = new LatLng(-0.101537, 34.755620);
        mMap.addMarker(new MarkerOptions()
            .position(ksmCountyReferalHsp)
            //.title("Kisumu County Referral Hospital")
            //.snippet("Contact : 0710286818")
            .icon(getBitmapDescriptor(R.drawable.ic_greenmarker))); **/


        //Adding a marker at Blue Cross Railways branch

        /**LatLng blueCrossRailwaysBranch = new LatLng(-0.077059, 34.771637);
        mMap.addMarker(new MarkerOptions()
             .position(blueCrossRailwaysBranch)
             .title("Blue Cross Railways Branch")
             .snippet("Contact : 0792965139")
             .icon(getBitmapDescriptor(R.drawable.ic_greenmarker))); **/

        //Adding marker at Lutheran Special School

        LatLng lutheraSpecialSchool = new LatLng(-0.106248, 34.768604);
        mMap.addMarker(new MarkerOptions()
            .position(lutheraSpecialSchool)
            .title("Lutheran Special School")
            .icon(getBitmapDescriptor(R.drawable.ic_greenmarker)));

        //Adding a marker at JOOORTH

        LatLng joorth = new LatLng(-0.088576, 34.771490);
        mMap.addMarker(new MarkerOptions()
            .position(joorth)
            .title("Jaramogi Oginga Odinga Teaching & Referal Hospital")
            .snippet("Contact : 0736662522")
            .icon(getBitmapDescriptor(R.drawable.ic_greenmarker)));


        //Adding a marker at International Felloship Kenya
        LatLng interfelk = new LatLng(-0.1068408,34.7612681);
        mMap.addMarker(new MarkerOptions()
            .position(interfelk)
            .title("International Felloship Kenya")
            .snippet("Contact : 0572022210")
            .icon(getBitmapDescriptor(R.drawable.ic_greenmarker)));

        //Adding a marker at Dove Rehabilitation Center

        LatLng doveRehabilitationCenter = new LatLng(-0.083407, 34.766203);
        mMap.addMarker(new MarkerOptions()
            .position(doveRehabilitationCenter)
            .title("Dove Rehabilitation Center")
            .snippet("Contact : 0720668417")
            .icon(getBitmapDescriptor(R.drawable.ic_greenmarker)));






        //Including the ripple background on the current user location
        LatLng currentMarkerPosition = mMap.getCameraPosition().target;
        //rippleBg.startRippleAnimation();

    }

    //Handling marker click events



    //Method for converting vector assets into Bitmap for use as marker
    private BitmapDescriptor getBitmapDescriptor(@DrawableRes int resId)
    {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(getResources(), resId, null);
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                                            vectorDrawable.getIntrinsicHeight(),
                                             Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }



    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
            {
           // super.onBackPressed();

                //Setting validation for the user to press back twice before exiting the app

                //This condition will compare the length of time betwin the first and second click
                if (backPressedTime + 2000 > System.currentTimeMillis())
                {
                    //Cancelling the toast before closing the app
                     backPressedToast.cancel();
                    //closing the app
                    super.onBackPressed();
                    return;
                }
                else
                {
                    //Displaying a toast to request the user to click again to exit
                    backPressedToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
                    backPressedToast.show();

                }

                backPressedTime = System.currentTimeMillis();
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

         if (id == R.id.nav_contactExpert)
         {
            startActivity(new Intent(MainActivity.this, ConnectWithExpert.class));

         }
         else if (id == R.id.nav_share)
         {
           //Sharing the app
             shareMara();

         }
         else if (id == R.id.nav_forum)
         {

         }
         else if (id == R.id.nav_about)
         {
             //Diaplaying about dialog
             aboutMaraDialog();
         }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    //Method for sharing the app
    private boolean shareMara()
    {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBodyText = "Hello, this mobile app is awesome, it promotes well-being by providing safe spaces and professional help to people suffering from mental health:" + shareDynamicLink()
                ;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));
        return true;
    }

    //Method for creating dynamic link

    private String shareDynamicLink()
    {
        return "https://mymara.page.link/mara-7SuK";
    }

    //Method for the pop up of about Mara

    private  void aboutMaraDialog()
    {
        aboutPopUpDialog.setContentView(R.layout.about_mara_popup);
        aboutPopUpDialog.getWindow().setBackgroundDrawableResource(R.color.greenColor);
        //showing the popup
        aboutPopUpDialog.show();

        //setting a function to the popup button

        aboutPopUpButton = (Button) aboutPopUpDialog.findViewById(R.id.about_popUp_button_id);
        aboutPopUpButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                //Sending the user back to the map
                Intent aboutIntent = new Intent(MainActivity.this, MainActivity.class);
                aboutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(aboutIntent);

            }
        });
    }







    /**
     * This method will be called when the map has been loaded and is ready for use
     * all the customerisation and functionalities of the map are in this method
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        //casting the map inside the google map variable
        mMap = googleMap;
        //Enabling the map to read the user location
        mMap.setMyLocationEnabled(true);
        //showing the location button on the map
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        //setting the location button to be at the botton-right of the app
        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null)
        {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0,0,40,180);
        }
        /**
         * Checking if the GPS is enabled on the device
         * if not request the user to enable it
         */
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        //requesting the user to enable gps
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(MainActivity.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(MainActivity.this, new OnSuccessListener<LocationSettingsResponse>()
        {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse)
            {
                //If the gps is enabled we'll get the device location
                getDeviceLocation();

                //Creating a URL TO GET REQUEST FROM GOOGLE MAPS DIRECTION API (From the user location to the rehubCenter)
                if (currentUserLocation != null)
                {
                    String directionUrl = getRequestUrl(currentUserLocation, ksmCountyReferalHsp, "driving");
                }
            }
        });
        task.addOnFailureListener(MainActivity.this, new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                //If the task failed then we'll check if the problem can be resolved
                if (e instanceof ResolvableApiException)
                {
                    ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                    try
                    {
                        //This line will show the user a dialog where the user can either accept the settings or not
                        resolvableApiException.startResolutionForResult(MainActivity.this, 101);
                    }
                    catch (IntentSender.SendIntentException e1)
                    {
                        e1.printStackTrace();
                    }
                }
            }
        });




        mMap.setOnMarkerClickListener(this);

        //Setting a listener to perform an action when the marker is clicked
       /***** mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker marker)
            {

                if (marker.equals(ksmCountyReferalHsp))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Kisumu County Referral Hospital");
                    builder.setMessage("Do you want to get direction to this place?")
                            .setNegativeButton("Cancel", null)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {

                                }
                            })
                            .show();
                    return true;
                }
                return false;
            }
        }); *****/

       mMap.addMarker(new MarkerOptions()
           .position(ksmCountyReferalHsp)
           .icon(getBitmapDescriptor(R.drawable.ic_greenmarker)));


        mMap.addMarker(new MarkerOptions()
                .position(currentUserLocation)
                .title("Me")
                .icon(getBitmapDescriptor(R.drawable.ic_userlocationicon)));



    }

    //Method for creating url to get direction from Google Maps Api
    private String getRequestUrl(LatLng origin, LatLng destination,  String directionMode)
    {
        //Getting the value of origin of the user
        String str_userOrigin = "origin=" + origin.latitude + "," + origin.longitude;

        //Getting the value of destination of the user
        String str_userDestination = "destination=" + destination.latitude + "," + destination.longitude;

        //User mode of travel
        String mode = "mode=" + directionMode;

        //setting sensors value
        String sensor = "sensor=false";

        //Building the full parameters for the web service
        String parameters = str_userOrigin +"&" + str_userDestination + "&" + mode +"&" + sensor ;
        //Building the output format
        String output = "json";
        //Creating the full url to request direction api
        String fullUrl = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_api_key);
        return fullUrl;


    }




    /**
     * Checking for the results
     * if the user accepted the request to turn on the location settings
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101)
        {
            if (resultCode == RESULT_OK)
            {
                /**
                 * if the user accepted the request
                 * if the user turned on the GPS in the device
                 * We'll proceed to find the current location of the users device
                 */
                getDeviceLocation();
            }
        }
    }

    private void getDeviceLocation()
    {
        //Getting the user last known location
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Location> task)
                    {
                        //if the task to get the lastKnowLocation is successful then we get the device location
                        if (task.isSuccessful())
                        {
                            userLastKnownLocation = task.getResult();
                            /**
                             * The task to get the device location might be successful but
                             * may be the location is null
                             * the following code will handle this condition
                             */
                            if (userLastKnownLocation != null)
                            {
                                //Getting the current user location
                                LatLng currentUserLocation = new LatLng(userLastKnownLocation.getLatitude(), userLastKnownLocation.getLongitude());

                                //Adding a marker to the current user location
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(currentUserLocation);
                                markerOptions.title("Me");
                                //markerOptions.icon(getBitmapDescriptor(R.drawable.ic_userlocationicon));
                                mMap.addMarker(markerOptions);
                                //Moving the camera to the device location
                               mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentUserLocation, DEFAULT_ZOOM));

                            }
                            else
                            {
                                //if the lastKnownLocation is null then we need to request for an updated location
                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(5000);
                                locationRequest.setFastestInterval(1000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                                locationCallback = new LocationCallback()
                                {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult)
                                    {
                                        super.onLocationResult(locationResult);
                                        //If the location result is not null then well update the userLastKnownLocation
                                        if (locationResult == null)
                                        {
                                            return;
                                        }
                                        else
                                        {
                                            userLastKnownLocation = locationResult.getLastLocation();

                                            LatLng userLocation = new LatLng(userLastKnownLocation.getLatitude(), userLastKnownLocation.getLongitude());

                                            //Adding a marker to the current user location
                                            MarkerOptions markerOptions = new MarkerOptions();
                                            markerOptions.position(userLocation);
                                            markerOptions.title("Me");
                                            //markerOptions.icon(getBitmapDescriptor(R.drawable.ic_userlocationicon));
                                            mMap.addMarker(markerOptions);
                                            //Moving the camera to the device location
                                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, DEFAULT_ZOOM));





                                            //Removing the location update from the callback
                                            mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                        }
                                    }
                                };
                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                            }
                        }
                        else
                        {
                            //If the task to get userLastKnownLocation is not successful then send the user a message
                            Toast.makeText(MainActivity.this, "Failed to get device location", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }


    @Override
    public boolean onMarkerClick(Marker marker)
    {

        if (marker.getPosition().equals(ksmCountyReferalHsp))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Kisumu County Referral Hospital");
            builder.setMessage("Do you want to get direction to this place?")
                    .setNegativeButton("NO", null)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            //Calling the functionality display direction
                            new FetchDownloadUrl(MainActivity.this).execute(getRequestUrl(currentUserLocation, ksmCountyReferalHsp, "driving"), "driving");
                        }
                    }).show();
            return true;


        }
        return false;
    }
}
