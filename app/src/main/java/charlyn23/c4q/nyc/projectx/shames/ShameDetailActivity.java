package charlyn23.c4q.nyc.projectx.shames;

import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import charlyn23.c4q.nyc.projectx.Constants;
import charlyn23.c4q.nyc.projectx.R;

public class ShameDetailActivity extends AppCompatActivity {

    private TextView details, group, where, when, shameDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        initializeView();
        setCustomFont();

        //Populates textfields
        group.setText(getIntent().getStringExtra(Constants.WHO));
        when.setText(getDate());
        where.setText(getAddress());
        shameDetail.setText(getIntent().getStringExtra(Constants.SHAME_TYPE_COLUMN));
        Log.i("date and time ", getDate());

    }

    //Converts latlng to address
    public String getAddress() {
        double lat = getIntent().getDoubleExtra(Constants.SHAME_LATITUDE_COLUMN, 0.0);
        double longitude = getIntent().getDoubleExtra(Constants.SHAME_LONGITUDE_COLUMN, 0.0);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, longitude, 1);

            String streetAddress = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getSubLocality();
            String state = addresses.get(0).getAdminArea();
            String zip = addresses.get(0).getPostalCode();
            String country = addresses.get(0).getCountryName();
            if (zip == null)
                zip = "";
            if (city == null)
                city = "";
            if (streetAddress == null)
                streetAddress = "";
            if (state == null)
                state = "";

            return streetAddress + " " + city + " " + state + " " + zip + " " + country;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    //converts timestamp to familiar date/time format
    private String getDate() {
        String date = getIntent().getStringExtra(Constants.WHEN);
        String year = date.substring(0, 4);
        String month = date.substring(5, 6);
        String day = date.substring(7, 8);
        String hour = date.substring(9, 11);
        String minute = date.substring(11, 13);

        return month + "/" + day + "/" + year + "  " + hour + ":" + minute;
    }

    public void initializeView() {
        details = (TextView) findViewById(R.id.details_text);
        group = (TextView) findViewById(R.id.who);
        where = (TextView) findViewById(R.id.where);
        when = (TextView) findViewById(R.id.when);
        shameDetail = (TextView) findViewById(R.id.what);
    }

    public void setCustomFont() {
        Typeface questrial = Typeface.createFromAsset(this.getAssets(), "questrial.ttf");
        details.setTypeface(questrial);
        group.setTypeface(questrial);
        where.setTypeface(questrial);
        when.setTypeface(questrial);
        shameDetail.setTypeface(questrial);
    }
}
