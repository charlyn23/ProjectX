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
import android.widget.TextView;

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

import java.util.List;

import charlyn23.c4q.nyc.projectx.shames.MaterialDialogs;
import charlyn23.c4q.nyc.projectx.shames.ShameDetailActivity;


public class ProjectXMapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "c4q.nyc.projectx";
    private static final String SHARED_PREFERENCE = "sharedPreference";
    private static final String LOGGED_IN = "isLoggedIn";
    private static final String LAT_LONG = "latLong";
    private static final LatLngBounds BOUNDS = new LatLngBounds(
            new LatLng(40.498425, -74.250219), new LatLng(40.792266, -73.776434));

    private PlaceAutocompleteAdapter mAdapter;
    private GoogleApiClient client;
    private boolean isDropped;
    private View view;
    private GoogleMap map;
    private Marker marker;
    private FloatingActionButton addShame;
    private Location location;
    private AutoCompleteTextView search;
    private LatLng searchLocation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.map_fragment, container, false);
        addShame = (FloatingActionButton) view.findViewById(R.id.add_shame);
        addShame.setOnClickListener(addShameListener);

        // Connect to Geolocation API to make current location request & load map
        buildGoogleApiClient(view.getContext());
        addMapFragment();

        search = (AutoCompleteTextView) view.findViewById(R.id.search);
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

        search.setOnItemClickListener(mAutocompleteClickListener);
        mAdapter = new PlaceAutocompleteAdapter(view.getContext(), android.R.layout.simple_list_item_1,
                client, BOUNDS, null);
        search.setAdapter(mAdapter);

        //TODO populate map with parse data
//        ParseQuery<Shame> query = ParseQuery.getQuery(Shame.class); 

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
        map.setOnInfoWindowClickListener(deleteMarkerListener);

        // fake data TODO DELETE
        map.addMarker(new MarkerOptions()
                .position(new LatLng(40.7449386285115534, -73.9359836652875)));

        map.addMarker(new MarkerOptions()
                .position(new LatLng(40.741885070719945, -73.933373875916))
                .title("Hello world"));
        map.addMarker(new MarkerOptions()
                .position(new LatLng(40.73994785206857, -73.93543615937233))
                .title("Hello world"));
        map.addMarker(new MarkerOptions()
                .position(new LatLng(40.74341224816964, -73.93776163458824))
                .title("Hello world"));
    }

    //directs the user to SignUp Activity if not logged in yet or to Shame Activity if logged in when FAB is clicked
    private View.OnClickListener addShameListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences preferences = getActivity().getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
            boolean isLoggedIn = preferences.getBoolean(LOGGED_IN, false);

            if (isLoggedIn) {
                MaterialDialogs dialogs = new MaterialDialogs();
                //gets location coordinates of the last dropped pin
                Log.i(TAG, marker.getPosition().latitude + " " + marker.getPosition().longitude);
                dialogs.initialDialog(view.getContext(), marker.getPosition().latitude, marker.getPosition().longitude);
            } else {
                Intent intent = new Intent(view.getContext(), SignUpActivity.class);
                intent.putExtra(LAT_LONG, marker.getPosition());
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
                marker = map.addMarker(new MarkerOptions()
                        .title(point.latitude + " : " + point.longitude)
                        .position(point)
                        .draggable(true));
                addShame.setVisibility(View.VISIBLE);
                isDropped = true;
            } else {
                marker.remove();
                marker = map.addMarker(new MarkerOptions()
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
            //TODO differentiate shame markers with location markers
            Snackbar.make(view, "SHAME + Date", 7000)
                    .setAction(R.string.snackbar_action, snackbarClick)
                    .show();
            return true;
        }
    };

    private View.OnClickListener snackbarClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO bring to shame detail
            Intent intent = new Intent(getActivity(), ShameDetailActivity.class);
            startActivity(intent);
        }
    };

    //removes a marker from the map if the user places it in the wrong location
    private GoogleMap.OnInfoWindowClickListener deleteMarkerListener = new GoogleMap.OnInfoWindowClickListener() {
        //TODO: add a trash bin icon on the info window. When the user taps it, marker gets deleted
        @Override
        public void onInfoWindowClick(Marker marker) {
            marker.remove();
            addShame.setVisibility(View.INVISIBLE);
            map.setOnMapClickListener(mapClickListener);
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
        location = LocationServices.FusedLocationApi.getLastLocation(client);
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
