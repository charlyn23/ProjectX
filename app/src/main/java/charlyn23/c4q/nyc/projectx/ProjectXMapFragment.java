package charlyn23.c4q.nyc.projectx;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import charlyn23.c4q.nyc.projectx.shames.Shame;
import charlyn23.c4q.nyc.projectx.shames.ShameDetailActivity;
import charlyn23.c4q.nyc.projectx.shames.ShameDialogs;


public class ProjectXMapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "c4q.nyc.projectx";
    private static final String SHARED_PREFERENCE = "sharedPreference";
    private static final String SHAME_REPORT = "shameReport";
    private static final String LOGGED_IN = "isLoggedIn";
    private static final String LAT_LONG = "latLong";
    private static final LatLngBounds BOUNDS = new LatLngBounds(
            new LatLng(40.498425, -74.250219), new LatLng(40.792266, -73.776434));

    private PlaceAutocompleteAdapter mAdapter;
    private GoogleApiClient client;
    private boolean isDropped;
    private View view;
    private GoogleMap map;
    private Marker new_marker, woman, LGBTQ, minor, POC;
    private FloatingActionButton addShame;
    private AutoCompleteTextView search;
    private LatLng searchLocation;
    private LatLng search_location;
    private Button filter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.map_fragment, container, false);
        addShame = (FloatingActionButton) view.findViewById(R.id.add_shame);
        addShame.setOnClickListener(addShameListener);

        // Connect to Geolocation API to make current location request & load map
        buildGoogleApiClient(view.getContext());
        addMapFragment();

        // Autocomplete Places setup
        search = (AutoCompleteTextView) view.findViewById(R.id.search);
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

        filter = (Button) view.findViewById(R.id.filter);
        filter.setOnClickListener(filterClick);

        // brings up the dialog after the user logs in with the latlong coordinates
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            boolean createDialog = extras.getBoolean(SHAME_REPORT);
            LatLng latLng = extras.getParcelable(LAT_LONG);
            if (createDialog && latLng!=null) {
                ShameDialogs dialogs = new ShameDialogs();
                new_marker = map.addMarker(new MarkerOptions()
                        .title(latLng.latitude + " : " + latLng.longitude)
                        .position(latLng)
                        .draggable(true));
                addShame.setVisibility(View.VISIBLE);
                isDropped = true;
                dialogs.initialDialog(view.getContext(), latLng.latitude, latLng.longitude, new_marker, addShame);
            }
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

        //TODO populate map with parse data

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Shame");
        Calendar cal = Calendar.getInstance();
        //TODO month = 0-2?
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 2);
        String last_two_months = new SimpleDateFormat("yyyyMMdd_HHmmss").format(cal.getTime());
        query.whereGreaterThan("shameTime", last_two_months);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> shames, ParseException e) {
                if (e == null) {
                    for (ParseObject shame : shames) {
                        LatLng position = new LatLng(shame.getLong("latitude"), shame.getLong("longitude"));
                        String shame_type = shame.getString("Group");

                        switch (shame_type) {
                            case "woman":
                                woman = map.addMarker(new MarkerOptions().position(position));
                                break;
                            case "minor":
                                minor = map.addMarker(new MarkerOptions().position(position));
                                break;
                            case "POC":
                                POC = map.addMarker(new MarkerOptions().position(position));
                                break;
                            case "LGBTQ":
                                LGBTQ = map.addMarker(new MarkerOptions().position(position));
                                break;
                        }
                    }
                    Log.d("List of Shames", "Retrieved " + shames.size() + " Shames");
                } else {
                    Log.d("List of Shames", "Error: " + e.getMessage());
                }
            }
        });
    }

    //directs the user to SignUp Activity if not logged in yet or to Shame Activity if logged in when FAB is clicked
    private View.OnClickListener addShameListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences preferences = getActivity().getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
            boolean isLoggedIn = preferences.getBoolean(LOGGED_IN, false);

            if (isLoggedIn) {
                ShameDialogs dialogs = new ShameDialogs();
                //gets location coordinates of the last dropped pin
                Log.i(TAG, new_marker.getPosition().latitude + " " + new_marker.getPosition().longitude);
                dialogs.initialDialog(view.getContext(), new_marker.getPosition().latitude, new_marker.getPosition().longitude, new_marker, addShame);
            } else {
                Intent intent = new Intent(view.getContext(), SignUpActivity.class);
                intent.putExtra(LAT_LONG, new_marker.getPosition());
                startActivity(intent);
            }
        }
    };

    //drops a marker in any place on the map
    private GoogleMap.OnMapClickListener mapClickListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng point) {
            map.setOnMyLocationChangeListener(null);
            if (!isDropped) {
                new_marker = map.addMarker(new MarkerOptions()
                        .title(point.latitude + " : " + point.longitude)
                        .position(point)
                        .draggable(true));
                addShame.setVisibility(View.VISIBLE);
                isDropped = true;
            } else {
                new_marker.remove();
                new_marker = map.addMarker(new MarkerOptions()
                        .title(point.latitude + " : " + point.longitude)
                        .position(point)
                        .draggable(true));
            }
            if (map != null) {
                map.animateCamera(CameraUpdateFactory.newLatLng(point));
            }
            //dataPasser.onDataPass(point.latitude, point.longitude);

        }
    };

    private GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            //TODO differentiate shame markers
            if (marker.equals(new_marker)) {
                Snackbar.make(view, "Click the \"+\" to report new shame", Snackbar.LENGTH_LONG)
                        .setAction(R.string.snackbar_delete, snackBarDelete)
                        .show();
            } else {
                ParseQuery<Shame> query = ParseQuery.getQuery("Shame");
                query.whereEqualTo("latitude", marker.getPosition().latitude);
                query.whereEqualTo("longitude", marker.getPosition().longitude);
                query.getFirstInBackground(new GetCallback<Shame>() {
                    @Override
                    public void done(Shame shame, ParseException e) {
                        if (shame == null) {
                            Log.e("shame", "not found");
                        }
                        else {
                            Log.d("shame : " , String.valueOf(shame));

                        }
                    }
                });
                Snackbar.make(view, "SHAME + Date", Snackbar.LENGTH_LONG)
                        .setAction(R.string.snackbar_action, snackbarDetail)
                        .show();
                Log.i("current shame lat : ", String.valueOf(marker.getPosition().latitude));
                Log.i("current shame long : ", String.valueOf(marker.getPosition().longitude));

            }
            return true;
        }
    };

    private View.OnClickListener snackbarDetail = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO bring to shame detail
            Intent intent = new Intent(getActivity(), ShameDetailActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener snackBarDelete = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new_marker.remove();
            isDropped = false;
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
                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            // TODO filter markers
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
                    })
                    .show();
        }
    };

    // Listener that handles selections from suggestions from the AutoCompleteTextView
    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceAutocompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(TAG, "Autocomplete item selected: " + item.description);

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(client, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(TAG, "Called getPlaceById to get Place details for " + item.placeId);

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
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            places.release();
        }
    };

    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(client);
        if (location == null)
            LocationServices.FusedLocationApi.requestLocationUpdates(client, createLocationRequest(), new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    setViewToLocation(new LatLng(location.getLatitude(), location.getLongitude()));
                }
            });
        else
            setViewToLocation(new LatLng(location.getLatitude(), location.getLongitude()));
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
        if (map != null) {
            // Set initial view to current location
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
        }
    }

    // Called when the Activity could not connect to Google Play services and the auto manager
    // could resolve the error automatically.
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    public LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(getActivity());
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return p1;
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
}
