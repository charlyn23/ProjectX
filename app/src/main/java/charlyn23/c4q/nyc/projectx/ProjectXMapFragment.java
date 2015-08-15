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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import charlyn23.c4q.nyc.projectx.shames.ShameDetailActivity;

public class ProjectXMapFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "c4q.nyc.projectx";
    private static final String SHARED_PREFERENCE = "sharedPreference";
    private static final String LOGGED_IN = "isLoggedIn";

    private boolean isDropped;
    private View view;
    private GoogleMap map;
    private Marker currentLocationMarker;
    private Marker marker;
    private FloatingActionButton addShame;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.map_fragment, container, false);
        addShame = (FloatingActionButton) view.findViewById(R.id.add_shame);
        addShame.setOnClickListener(addShameListener);

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
    }

    //directs the user to SignUp Activity if not logged in yet or to Shame Activity if logged in when FAB is clicked
    private View.OnClickListener addShameListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences preferences = getActivity().getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
            boolean isLoggedIn = preferences.getBoolean(LOGGED_IN, false);

            if (isLoggedIn) {
                //SHOW DIALOG
            } else {
                Intent intent = new Intent(view.getContext(), SignUpActivity.class);
                startActivity(intent);
            }
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
            //TODO differenciate shame markers with location markers
            Snackbar.make(view, "SHAME + Date", 8000)
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

}
