package charlyn23.c4q.nyc.projectx.map;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by sufeizhao on 9/3/15.
 */
@ParseClassName("Geofence")
public class ShameGeofence extends ParseObject {
    private float latitude;
    private float longitude;
    private String stringID;

    public ShameGeofence(float latitude, float longitude, String stringID){
        this.latitude = latitude;
        this.longitude = longitude;
        this.stringID = stringID;
    }

    public ShameGeofence(){
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getStringID() {
        return stringID;
    }

    public void setStringID(String stringID) {
        this.stringID = stringID;
    }
}
