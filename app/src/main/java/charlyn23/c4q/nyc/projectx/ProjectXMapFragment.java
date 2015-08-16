package charlyn23.c4q.nyc.projectx;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
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
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import charlyn23.c4q.nyc.projectx.shames.MaterialDialogs;
import charlyn23.c4q.nyc.projectx.shames.ShameDetailActivity;
import charlyn23.c4q.nyc.projectx.shames.Shame;


public class ProjectXMapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "c4q.nyc.projectx";
    private static final String SHARED_PREFERENCE = "sharedPreference";
    private static final String LOGGED_IN = "isLoggedIn";
    private static final LatLngBounds BOUNDS = new LatLngBounds(
            new LatLng(40.498425, -74.250219), new LatLng(40.792266, -73.776434));

    private PlaceAutocompleteAdapter mAdapter;
    private GoogleApiClient client;
    private boolean isDropped;
    private View view;
    private GoogleMap map;
    private Marker currentLocationMarker;
    private Marker marker;
    private FloatingActionButton addShame;
    private LatLng point;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.map_fragment, container, false);
        addShame = (FloatingActionButton) view.findViewById(R.id.add_shame);
        addShame.setOnClickListener(addShameListener);

        client = new GoogleApiClient.Builder(view.getContext())
                .addApi(Places.GEO_DATA_API)
                .build();

        AutoCompleteTextView search = (AutoCompleteTextView) view.findViewById(R.id.search);
        mAdapter = new PlaceAutocompleteAdapter(view.getContext(), android.R.layout.simple_list_item_1,
                client, BOUNDS, null);
        search.setAdapter(mAdapter);

        //TODO populate map with parse data
//        ParseQuery<Shame> query = ParseQuery.getQuery(Shame.class);

        addMapFragment();

        return view;
    }

    // adds Google MapFragment to the existing xml and set listeners
    public void addMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        map = mapFragment.getMap();
        map.setOnMyLocationChangeListener(locationChangeListener);
        map.setOnMapClickListener(mapClickListener);
        map.setOnMarkerClickListener(markerClickListener);
        map.setOnInfoWindowClickListener(deleteMarkerListener);
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

//            if (isLoggedIn) {
                MaterialDialogs.initialDialog(view.getContext());
                //Grabs lat and long of marker when FAB button is pressed
                marker.getPosition();
                Log.i("position", String.valueOf(marker.getPosition()));

                ParseQuery<Shame> query = ParseQuery.getQuery("Shame");
                query.whereExists("latitude");
                query.findInBackground(new FindCallback<Shame>() {
                    @Override
                    public void done(List<Shame> list, ParseException e) {
                        Log.i("list = ", list.toString());
                    }
                });
//            } else {
//                Intent intent = new Intent(view.getContext(), SignUpActivity.class);
//                startActivity(intent);
//            }
        }
    };

    //sets a marker to the user's current location
    private GoogleMap.OnMyLocationChangeListener locationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            currentLocationMarker = map.addMarker(new MarkerOptions().position(loc));
            if (map != null) {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
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


    // Listener that handles selections from suggestions from the AutoCompleteTextView that
    // displays Place suggestions. Gets the place id of the selected item and issues a request to
    // the Places Geo Data API to retrieve more details about the place.
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            final PlaceAutocompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(TAG, "Autocomplete item selected: " + item.description);


            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(client, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(TAG, "Called getPlaceById to get Place details for " + item.placeId);
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

    // Called when the Activity could not connect to Google Play services and the auto manager
    // could resolve the error automatically.
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
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
