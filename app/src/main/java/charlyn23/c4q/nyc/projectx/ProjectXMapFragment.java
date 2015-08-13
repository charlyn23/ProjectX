package charlyn23.c4q.nyc.projectx;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class ProjectXMapFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "c4q.nyc.projectx";
    private View view;
    private GoogleMap map;
    private MapFragment mapFragment;
    private Marker currentLocationMarker;
    private Marker marker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.map_fragment, container, false);

        Button submitBtn = (Button) view.findViewById(R.id.submit_button);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        // adds Google MapFragment to the existing xml
        mapFragment = (MapFragment) (getActivity()).getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        map = mapFragment.getMap();
        map.setOnMyLocationChangeListener(locationChangeListener);
        map.setOnMapClickListener(mapClickListener);
        map.setOnInfoWindowClickListener(deleteMarkerListener);
        return view;
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

    //sets a marker to the user's current location
    private GoogleMap.OnMyLocationChangeListener locationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            currentLocationMarker = map.addMarker(new MarkerOptions().position(loc));
            if(map != null){
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            }
        }
    };

    //drops a marker in any place on the map
    private GoogleMap.OnMapClickListener mapClickListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng point) {
            map.setOnMyLocationChangeListener(null);
            marker = map.addMarker(new MarkerOptions()
                    .title(point.latitude + " : " + point.longitude)
                    .position(point)
                    .draggable(true));
            map.setOnMapClickListener(null);
            if(map != null) {
                map.animateCamera(CameraUpdateFactory.newLatLng(point));
            }
        }
    };

    //removes a marker from the map if the user places it in the wrong location
    private GoogleMap.OnInfoWindowClickListener deleteMarkerListener = new GoogleMap.OnInfoWindowClickListener() {
        //TODO: add a trash bin icon on the info window. When the user taps it, marker gets deleted
        @Override
        public void onInfoWindowClick(Marker marker) {
            marker.remove();
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (map != null) {
            try{
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.map)).commitAllowingStateLoss();
            }catch(Exception e){
                Log.d(TAG, "MapFragment is destroyed." + e);
            }
        }
    }
}
