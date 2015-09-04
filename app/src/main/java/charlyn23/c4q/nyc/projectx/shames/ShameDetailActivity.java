package charlyn23.c4q.nyc.projectx.shames;

import android.app.Activity;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import charlyn23.c4q.nyc.projectx.R;

/**
 * Created by sufeizhao on 8/15/15.
 */
public class ShameDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shame_layout);

        // set custom font
        Typeface questrial = Typeface.createFromAsset(this.getAssets(), "questrial.ttf");
        TextView details = (TextView) findViewById(R.id.details_text);
        TextView group = (TextView) findViewById(R.id.who);
        TextView where = (TextView) findViewById(R.id.where);
        TextView when = (TextView) findViewById(R.id.when);
        TextView shameDetail = (TextView) findViewById(R.id.what);
        details.setTypeface(questrial);
        group.setTypeface(questrial);
        where.setTypeface(questrial);
        when.setTypeface(questrial);
        shameDetail.setTypeface(questrial);

        //Populate textfields
        group.setText(getIntent().getStringExtra("who"));
        when.setText(getDate());
        where.setText(getAddress());
        shameDetail.setText(getIntent().getStringExtra("type"));
        Log.i("date and time " , getDate());

    }

        //Convert latlng to address
        public String getAddress(){
            double lat = getIntent().getDoubleExtra("latitude", 0.0);
            double longitude = getIntent().getDoubleExtra("longitude", 0.0);
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(lat, longitude, 1);

                String streetAddress = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getSubLocality();
                String state = addresses.get(0).getAdminArea();
                String zip = addresses.get(0).getPostalCode();
                String country = addresses.get(0).getCountryName();
                if (zip == null) {
                    zip = "";
                }
                if (city == null) {
                    city = "";
                }
                if (streetAddress == null) {
                    streetAddress = "";
                }
                if (state == null) {
                    state = "";
                }
                String address = streetAddress + " " + city + " " + state + " " + zip + " " + country;
                return address;
            } catch (IOException e) {
                e.printStackTrace();
            }
        return "";
    }

    //convert timestamp to familiar date/time format
    private String getDate(){
        String date = getIntent().getStringExtra("when");
        String year = date.substring(0, 4);
        String month = date.substring(5, 6);
        String day = date.substring(7, 8);
        String hour = date.substring(9, 11);
        String minute = date.substring(11, 13);

        return month + "/" + day + "/" + year + "  " + hour + ":" + minute;
    }
}
