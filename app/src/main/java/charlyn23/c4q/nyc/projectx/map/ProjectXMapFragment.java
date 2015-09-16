package charlyn23.c4q.nyc.projectx.map;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import charlyn23.c4q.nyc.projectx.Constants;
import charlyn23.c4q.nyc.projectx.R;
import charlyn23.c4q.nyc.projectx.shames.MarkerListener;
import charlyn23.c4q.nyc.projectx.shames.Shame;
import charlyn23.c4q.nyc.projectx.shames.ShameDialogs;

public class ProjectXMapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, MarkerListener, ResultCallback<Status> {
    private static final LatLngBounds BOUNDS = new LatLngBounds(new LatLng(40.498425, -74.250219), new LatLng(40.792266, -73.776434));
    private SharedPreferences preferences;
    private PlaceAutocompleteAdapter mAdapter;
    private GoogleApiClient client;
    private boolean isDropped, geofenceEnabled;
    private View view;
    private GoogleMap map;
    private Marker new_marker;
    private Location currentLocation;
    private FloatingActionButton addShame;
    private AutoCompleteTextView search;
    private LatLng searchLocation;
    private Button filter;
    private ViewPager viewPager;
    private OnDataPass dataPasser;
    private List<LatLng> woman_loc = new ArrayList<>(),
            minor_loc = new ArrayList<>(),
            lgbtq_loc = new ArrayList<>(),
            poc_loc = new ArrayList<>(),
            other_loc = new ArrayList<>();
    private Integer[] filter_chosen = new Integer[]{0, 1, 2, 3, 4};
    private PendingIntent mGeofencePendingIntent = null;
    private HashMap<String, Boolean> identity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.map_fragment, container, false);
        initializeViews();
        setCustomFont();

        preferences = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE);
        geofenceEnabled = preferences.getBoolean(Constants.ALLOW_GEOFENCE, false);
        if (!preferences.getBoolean(Constants.IS_CONNECTED, false))
            loadData();

        identity = new HashMap<>();
        identity.put(Constants.MAN, preferences.getBoolean(Constants.MAN, false));
        identity.put(Constants.WOMAN, preferences.getBoolean(Constants.WOMAN, false));
        identity.put(Constants.POC, preferences.getBoolean(Constants.POC, false));
        identity.put(Constants.MINOR, preferences.getBoolean(Constants.MINOR, false));
        identity.put(Constants.OTHER, preferences.getBoolean(Constants.OTHER, false));
        boolean lgbtq_check = (preferences.getBoolean(Constants.TRANS, false) || preferences.getBoolean(Constants.GAY, false) ||
                preferences.getBoolean(Constants.BISEXUAL, false) || preferences.getBoolean(Constants.QUEER, false) ||
                preferences.getBoolean(Constants.LESBIAN, false));
        identity.put(Constants.LGBTQ, lgbtq_check);

        filter.setOnClickListener(filterClick);
        addShame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportShame();
            }
        });

        // Connects to Geolocation API to make current location request & load map
        buildGoogleApiClient(view.getContext());
        addMapFragment();

        // Autocomplete Places setup
        search.setOnItemClickListener(mAutocompleteClickListener);
        mAdapter = new PlaceAutocompleteAdapter(view.getContext(), android.R.layout.simple_list_item_1,
                client, BOUNDS, null);
        search.setAdapter(mAdapter);
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    searchLocation = getLocationFromAddress(search.getText().toString());
                    setViewToLocation(searchLocation);
                    handled = true;
                }
                return handled;
            }
        });

        addSubmittedMarker();
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.map_fragment, container, false);
        } catch (InflateException e) {
            //map is already there, just return view as it is
        }
        return view;
    }

    // adds Google MapFragment to the existing xml and set listeners
    public void addMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        map = mapFragment.getMap();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i("MapFragment", "onmapready");

        map.getUiSettings().setMapToolbarEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(true);
        map.getUiSettings().setTiltGesturesEnabled(true);
        map.getUiSettings().setScrollGesturesEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(true);
        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setOnMapClickListener(mapClickListener);
        map.setOnMarkerClickListener(markerClickListener);

        //populates map with shames that occurred within the last two months
        ParseQuery<Shame> query = ParseQuery.getQuery(Constants.SHAME);
        String lastUpdate = preferences.getString(Constants.LAST_UPDATE, "00000000_0000");
        query.whereGreaterThanOrEqualTo(Constants.SHAME_TIME_COLUMN, lastUpdate);
        query.findInBackground(new FindCallback<Shame>() {
            public void done(final List<Shame> results, ParseException e) {
                if (e == null) {
                    if (results.size() > 0) {
                        insertDatatoSQLite(results);
                        Calendar cal = Calendar.getInstance();
                        preferences.edit().putString(Constants.LAST_UPDATE, new SimpleDateFormat("yyyyMMdd_HHmmss").format(cal.getTime())).apply();
                    }

                    loadData();
                    Log.d("List of Shames", "Inserted " + results.size() + " Shames");
                } else {
                    Log.d("List of Shames", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void loadData() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void[] params) {
                List<Shame> active_list = loadFromSQLite();
                Log.i("SQLite Shames loaded", String.valueOf(active_list.size()));
                for (Shame incident : active_list) {
                    double latitude = incident.getLatitude();
                    double longitude = incident.getLongitude();
                    LatLng location = new LatLng(latitude, longitude);
                    String shame_group = incident.getGroup();
                    if (shame_group != null) {
                        switch (shame_group) {
                            case Constants.WOMAN:
                                woman_loc.add(location);
                                break;
                            case Constants.MINOR:
                                minor_loc.add(location);
                                break;
                            case Constants.POC:
                                poc_loc.add(location);
                                break;
                            case Constants.LGBTQ:
                                lgbtq_loc.add(location);
                                break;
                            case Constants.OTHER:
                                other_loc.add(location);
                                break;
                        }
                    }
                }
                return "All";
            }

            @Override
            protected void onPostExecute(String all) {
                populateMap(all);
                Log.i("MapFragment", "Populating map");
            }
        }.execute();
    }

    // load incidents from past 2 months
    public List<Shame> loadFromSQLite() {
        ShameSQLiteHelper helper = ShameSQLiteHelper.getInstance(view.getContext());
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 2);
        return helper.loadData(new String[]{new SimpleDateFormat("yyyyMMdd").format(cal.getTime())});
    }

    public void insertDatatoSQLite(List<Shame> results) {
        ShameSQLiteHelper helper = ShameSQLiteHelper.getInstance(view.getContext());
        helper.insertData(results);
    }

    //directs the user to SignUp Fragment if not logged in or to Dialogs if logged in when FAB is clicked
    public void reportShame() {
        boolean isLoggedIn = preferences.getBoolean(Constants.LOGGED_IN, false);
        if (isLoggedIn) {
            ShameDialogs dialogs = new ShameDialogs();
            Log.i(Constants.TAG, new_marker.getPosition().latitude + " " + new_marker.getPosition().longitude);
            dialogs.setListener(this);
            dialogs.initialDialog(view.getContext(), new_marker.getPosition().latitude, new_marker.getPosition().longitude, new_marker, addShame);
        } else {
            viewPager.setCurrentItem(Constants.LOG_IN_VIEW);
            Toast.makeText(view.getContext(), "Please log in to report a new incident", Toast.LENGTH_LONG).show();
        }
    }

    //drops a marker in any place on the map
    private GoogleMap.OnMapClickListener mapClickListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng point) {
            map.setOnMyLocationChangeListener(null);
            if (!isDropped) {
                new_marker = map.addMarker(new MarkerOptions()
                        .position(point).icon(BitmapDescriptorFactory.fromResource(R.drawable.smallredlogo)).draggable(true));
                addShame.setVisibility(View.VISIBLE);
                isDropped = true;
                preferences.edit().putBoolean(Constants.MARKER_DROPPED, true).apply();
            } else {
                new_marker.remove();
                new_marker = map.addMarker(new MarkerOptions()
                        .position(point).icon(BitmapDescriptorFactory.fromResource(R.drawable.smallredlogo)).draggable(true));
                addShame.setVisibility(View.VISIBLE);
            }

            if (map != null)
                map.animateCamera(CameraUpdateFactory.newLatLng(point));
            long lat = Double.doubleToRawLongBits(new_marker.getPosition().latitude);
            long longit = Double.doubleToRawLongBits(new_marker.getPosition().longitude);
            preferences.edit().putBoolean(Constants.IS_DROPPED, true).apply();
            preferences.edit().putLong(Constants.LATITUDE_PREFERENCE, lat).apply();
            preferences.edit().putLong(Constants.LONGITUDE_PREFERENCE, longit).apply();
        }
    };

    protected GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(final Marker marker) {
            if (marker.equals(new_marker)) {
                Snackbar.make(view, "Click \"+\" to report new activity", Snackbar.LENGTH_LONG)
                        .setAction(R.string.snackbar_delete, snackBarDelete)
                        .show();
            } else {
                ParseQuery<Shame> query = ParseQuery.getQuery(Constants.SHAME);
                query.whereEqualTo(Constants.SHAME_LATITUDE_COLUMN, marker.getPosition().latitude);
                query.whereEqualTo(Constants.SHAME_LONGITUDE_COLUMN, marker.getPosition().longitude);
                query.getFirstInBackground(new GetCallback<Shame>() {
                    @Override
                    public void done(Shame shame, ParseException e) {
                        if (shame != null && shame.getString(Constants.GROUP_COLUMN) != null && shame.getString(Constants.SHAME_TIME_COLUMN) != null) {
                            String readableTime = convertToReadableTime(shame.getString(Constants.SHAME_TIME_COLUMN));
                            String when = shame.getString(Constants.SHAME_TIME_COLUMN);
                            String who = shame.getString(Constants.GROUP_COLUMN);
                            String type = shame.getString(Constants.SHAME_TYPE_COLUMN);
                            String group = shame.getString(Constants.GROUP_COLUMN);
                            if (group.equals(Constants.OTHER))
                                group = Constants.PERSON;
                            Snackbar.make(view, "A " + group + " got harassed on " + readableTime, Snackbar.LENGTH_LONG)
                                    .setAction(R.string.snackbar_action, new snackbarDetail(marker.getPosition().latitude, marker.getPosition().longitude, type, who, when))
                                    .show();

                            Log.i("current shame lat : ", String.valueOf(marker.getPosition().latitude));
                            Log.i("current shame long : ", String.valueOf(marker.getPosition().longitude));
                            Log.i("current shame date : ", String.valueOf(readableTime));
                        }
                    }
                });
            }
            return true;
        }
    };

    //converts timestamp to a readable format for snackbar display
    private String convertToReadableTime(String time) {
        String year = time.substring(0, 4);
        String month = time.substring(4, 6);
        String day = time.substring(6, 8);
        String hour = time.substring(9, 11);
        String minute = time.substring(11, 13);
        return month + "/" + day + "/" + year + "  " + hour + ":" + minute;
    }

    @Override
    public void setMarker(double latitude, double longitude) {
        map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.smallredlogo)).draggable(true));
    }

    @Override
    public void onResult(Status status) {
        if (status.isSuccess()) {
            Log.d("MapFragment - Geofence", "Geofence successfully added");
        } else {
            String errorMessage;
            switch (status.getStatusCode()) {
                case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                    errorMessage = "Geofence service is not available now";
                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                    errorMessage = "Your app has registered too many geofences";
                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                    errorMessage = "You have provided too many PendingIntents to the addGeofences() call";
                default:
                    errorMessage = "Unknown error";
            }

            Log.e("MapFragment - Geofence", errorMessage);
        }
    }

    public class snackbarDetail implements View.OnClickListener {
        double lat, lon;
        String type, who, when;

        public snackbarDetail(double lat, double lon, String type, String who, String when) {
            this.lat = lat;
            this.lon = lon;
            this.type = type;
            this.who = who;
            this.when = when;
        }

        @Override
        public void onClick(View v) {
            dataPasser.onDataPass(lat, lon, when, who, type);


        }
    }

    private View.OnClickListener snackBarDelete = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new_marker.remove();
            isDropped = false;
            preferences.edit().putBoolean(Constants.MARKER_DROPPED, false).apply();
            addShame.setVisibility(View.INVISIBLE);
        }
    };

    View.OnClickListener filterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new MaterialDialog.Builder(view.getContext())
                    .title(R.string.filter)
                    .content(R.string.filter_content)
                    .items(R.array.filter_types)
                    .itemsCallbackMultiChoice(filter_chosen, new MaterialDialog.ListCallbackMultiChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                            map.clear();
                            filter_chosen = new Integer[which.length];
                            for (int i = which.length - 1; i >= 0; --i) {
                                if (which[i] == 0)
                                    populateMap(Constants.WOMAN);
                                else if (which[i] == 1)
                                    populateMap(Constants.POC);
                                else if (which[i] == 2)
                                    populateMap(Constants.LGBTQ);
                                else if (which[i] == 3)
                                    populateMap(Constants.MINOR);
                                else if (which[i] == 4)
                                    populateMap(Constants.OTHER);
                                filter_chosen[i] = which[i];
                            }
                            return true;
                        }
                    })
                    .positiveText(R.string.done)
                    .negativeText(R.string.cancel)
                    .autoDismiss(false)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            dialog.cancel();
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            dialog.cancel();
                        }
                    }).show();
        }
    };

    public void populateMap(String group) {
        switch (group) {
            case Constants.WOMAN:
                for (LatLng loc : woman_loc) {
                    map.addMarker(new MarkerOptions().position(loc).icon(BitmapDescriptorFactory.fromResource(R.drawable.smallredlogo)));
                }
                break;
            case Constants.MINOR:
                for (LatLng loc : minor_loc) {
                    map.addMarker(new MarkerOptions().position(loc).icon(BitmapDescriptorFactory.fromResource(R.drawable.smallkidlogo)));
                }
                break;
            case Constants.LGBTQ:
                for (LatLng loc : lgbtq_loc) {
                    map.addMarker(new MarkerOptions().position(loc).icon(BitmapDescriptorFactory.fromResource(R.drawable.smallgaylogo)));
                }
                break;
            case Constants.POC:
                for (LatLng loc : poc_loc) {
                    map.addMarker(new MarkerOptions().position(loc).icon(BitmapDescriptorFactory.fromResource(R.drawable.smallpoclogo)));
                }
                break;
            case Constants.OTHER:
                for (LatLng loc : other_loc) {
                    map.addMarker(new MarkerOptions().position(loc).icon(BitmapDescriptorFactory.fromResource(R.drawable.smallotherlogo)));
                }
                break;
            default:
                for (LatLng loc : woman_loc) {
                    map.addMarker(new MarkerOptions().position(loc).icon(BitmapDescriptorFactory.fromResource(R.drawable.smallredlogo)));
                }
                for (LatLng loc : minor_loc) {
                    map.addMarker(new MarkerOptions().position(loc).icon(BitmapDescriptorFactory.fromResource(R.drawable.smallkidlogo)));
                }
                for (LatLng loc : lgbtq_loc) {
                    map.addMarker(new MarkerOptions().position(loc).icon(BitmapDescriptorFactory.fromResource(R.drawable.smallgaylogo)));
                }
                for (LatLng loc : poc_loc) {
                    map.addMarker(new MarkerOptions().position(loc).icon(BitmapDescriptorFactory.fromResource(R.drawable.smallpoclogo)));
                }
                for (LatLng loc : other_loc) {
                    map.addMarker(new MarkerOptions().position(loc).icon(BitmapDescriptorFactory.fromResource(R.drawable.smallotherlogo)));
                }
                break;
        }
    }

    // Listener that handles selections from suggestions from the AutoCompleteTextView
    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceAutocompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(Constants.TAG, "Autocomplete item selected: " + item.description);

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(client, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(Constants.TAG, "Called getPlaceById to get Place details for " + item.placeId);

            LatLng search_location = getLocationFromAddress(item.toString());
            setViewToLocation(search_location);
        }
    };

    // Callback for results from a Places Geo Data API query that shows the first place result in
    // the details view on screen.
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e(Constants.TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            places.release();
        }
    };

    @Override
    public void onConnected(Bundle bundle) {
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(client);
        if (currentLocation == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, createLocationRequest(), new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    setViewToLocation(new LatLng(location.getLatitude(), location.getLongitude()));
                }
            });
        } else {
            setViewToLocation(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));

            // geofence setup - fetch if geofence is enabled && location hasn't change && did not fetch in past 2 days
            Calendar cal = Calendar.getInstance();
            Location lastFetchLocation = LocationServices.FusedLocationApi.getLastLocation(client);
            float distance = currentLocation.distanceTo(lastFetchLocation);
            Log.d("Geofence enabled", String.valueOf(geofenceEnabled));
            Log.d("Geofence time", String.valueOf(distance >= Constants.FIFTY_METERS));
            Log.d("Geofence location", String.valueOf(preferences.getLong(Constants.LAST_GEOFENCE_FETCH, cal.getTimeInMillis() - Constants.MILLI_48HOURS) <= cal.getTimeInMillis() - Constants.MILLI_48HOURS));

            if (geofenceEnabled && preferences.getLong(Constants.LAST_GEOFENCE_FETCH, cal.getTimeInMillis() - Constants.MILLI_48HOURS) <= cal.getTimeInMillis() - Constants.MILLI_48HOURS) {
                fetchGeofenceFromParse(cal);
                Log.d("Geofence", "Fetching data");
            } else if (distance >= Constants.FIFTY_METERS)
                fetchGeofenceFromParse(cal);
        }
    }

    private LocationRequest createLocationRequest() {
        return new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(1);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    protected synchronized void buildGoogleApiClient(Context context) {
        client = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        Log.d("Map", "Connected to Google API Client");
    }

    private void setViewToLocation(LatLng latLng) {
        if (map != null && latLng != null)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
    }

    // Called when the Activity could not connect to Google Play services and the auto manager
    // could resolve the error automatically.
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(Constants.TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    public LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(getActivity());
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null)
                return null;

            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return p1;
    }

    // get geofence landmarks from parse db & save to local
    public void fetchGeofenceFromParse(final Calendar cal) {
        ParseQuery<ShameGeofence> db_geofences = ParseQuery.getQuery(Constants.GEOFENCE_NAME);
        Calendar before = Calendar.getInstance();
        before.set(Calendar.YEAR, cal.get(Calendar.HOUR) - 24);
        String yesterday = new SimpleDateFormat("yyyyMMdd_HHmmss").format(before.getTime());
        db_geofences.whereGreaterThanOrEqualTo(Constants.TIMESTAMP, yesterday);
        db_geofences.whereWithinMiles(Constants.LOCATION, new ParseGeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude()), 50);
        db_geofences.addDescendingOrder(Constants.TIMESTAMP);
        db_geofences.findInBackground(new FindCallback<ShameGeofence>() {
            public void done(List<ShameGeofence> results, ParseException e) {
                if (e == null) {
                    ArrayList<Geofence> active_geofence = new ArrayList<>();
                    for (ShameGeofence geo : results) {
                        String group = geo.getString(Constants.GROUP_COLUMN);
                        if (identity.get(group)) {
                            active_geofence.add(new Geofence.Builder()
                                    .setRequestId(geo.getObjectId())
                                    .setCircularRegion(
                                            geo.getDouble(Constants.SHAME_LATITUDE_COLUMN),
                                            geo.getDouble(Constants.SHAME_LONGITUDE_COLUMN),
                                            Constants.GEOFENCE_RADIUS_IN_METER) // 1/2 mile
                                    .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS) // 24 hours
                                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                                    .build());
                        }

                        setUpGeofence(active_geofence, cal);
                    }
                    Log.d("Active Geofence loc", "Retrieved " + results.size() + " Shames");
                } else {
                    Log.d("Active Geofence loc", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void setUpGeofence(ArrayList<Geofence> active_geofence, Calendar cal) {
        if (!client.isConnected()) {
            Log.d("MapFragment - Geofence", "GoogleAPIClient Not connected");
            return;
        }

        if (active_geofence.size() != 0) {
            try {
                LocationServices.GeofencingApi.addGeofences(
                        client,
                        getGeofencingRequest(active_geofence),
                        getGeofencePendingIntent()
                ).setResultCallback(this); // Result processed in onResult().
                preferences.edit().putLong(Constants.LAST_GEOFENCE_FETCH, cal.getTimeInMillis()).apply();
            } catch (SecurityException securityException) {
                Log.d("MapFragment - Geofence", "Error on ACCESS_FINE_LOCATION", securityException);
            }
        }
    }

    private GeofencingRequest getGeofencingRequest(ArrayList<Geofence> active_geofence) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(active_geofence);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        } else {
            Intent intent = new Intent(getActivity(), GeofenceIntentService.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP | Notification.FLAG_AUTO_CANCEL);
            return PendingIntent.getService(view.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }

    public void initializeViews() {
        viewPager = (ViewPager) getActivity().findViewById(R.id.view_pager);
        addShame = (FloatingActionButton) view.findViewById(R.id.add_shame);
        search = (AutoCompleteTextView) view.findViewById(R.id.search);
        filter = (Button) view.findViewById(R.id.filter);
    }

    public void setCustomFont() {
        Typeface questrial = Typeface.createFromAsset(getActivity().getAssets(), "questrial.ttf");
        search.setTypeface(questrial);
        filter.setTypeface(questrial);
    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        client.disconnect();
    }

    public interface OnDataPass {
        void onDataPass(double latitude, double longitude, String when, String who, String type);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            dataPasser = (ProjectXMapFragment.OnDataPass) activity;
            Log.i("datapasser", "works!");
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnDataPass");
        }
    }

    //brings up a survey dialog group and saves a permanent marker on the map
    public void addSubmittedMarker() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            boolean dialog = bundle.getBoolean(Constants.SHOW_DIALOG, false);
            boolean isDropped = preferences.getBoolean(Constants.IS_DROPPED, false);
            if (dialog && isDropped) {
                long lat = preferences.getLong(Constants.LATITUDE_PREFERENCE, 0);
                long longi = preferences.getLong(Constants.LONGITUDE_PREFERENCE, 0);
                double latitude = Double.longBitsToDouble(lat);
                double longitude = Double.longBitsToDouble(longi);
                ShameDialogs dialogs = new ShameDialogs();
                dialogs.setListener(this);
                dialogs.initialDialog(getActivity(), latitude, longitude, null, null);
                bundle.clear();
            }
        }
    }
}
