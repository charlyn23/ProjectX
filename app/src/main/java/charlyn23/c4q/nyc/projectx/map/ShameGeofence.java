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
    private String group;

    public ShameGeofence(float latitude, float longitude, String group){
        this.latitude = latitude;
        this.longitude = longitude;
        this.group = group;
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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
