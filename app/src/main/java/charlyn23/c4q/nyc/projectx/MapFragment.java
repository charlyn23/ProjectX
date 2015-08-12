package charlyn23.c4q.nyc.projectx;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
<<<<<<< HEAD
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
=======
import charlyn23.c4q.nyc.projectx.model.*;
>>>>>>> 89c073ced9a7d2328de094150011678bc0c8d514

/**
 * Created by sufeizhao on 8/9/15.
 */
public class MapFragment extends Fragment {
    private View view;
    
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
<<<<<<< HEAD

       // MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

        return view;
    }
=======
        reportNewShame();
        // MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);


        return view;
    }

    public void reportNewShame() {
        Shame newShame = new Shame();

        newShame.setLatitude(70);
        newShame.setLongitude(-100);
        newShame.setShameType(3);
        newShame.setVerbalShame(2);
        newShame.setShameFeel(1);
        newShame.setShameReason(3);
        newShame.saveInBackground();
//
//        newShame.reportShame(70, -100, 1, 2, 0, 0, 2, 3, 900, 1);


    }
>>>>>>> 89c073ced9a7d2328de094150011678bc0c8d514
}
