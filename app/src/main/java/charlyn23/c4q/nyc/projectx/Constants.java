package charlyn23.c4q.nyc.projectx;


public class Constants {
    public static final String YOUR_APPLICATION_ID = "wXm5LSYRqb26gArXbWoZDkLCqzO4dD1pa3y5J34O";
    public static final String YOUR_CLIENT_KEY = "kvV4Abba1l7DKhUBQxK3PWLvIsFjQwuPyrcuMhXq";
    public static final String TWITTER_ID = "ouo6bc7Pw4aScPNRnsKJlvm2K";
    public static final String TWITTER_KEY = "qRY7V1WbBMXogze0wC0m05cIgMpzl3bn8Kt9NpOpHkW7S5kKLk";

    public static final int LOG_IN_VIEW = 2;
    public static final int RC_SIGN_IN = 0;
    public static final int PICK_IMAGE_REQUEST = 1;
    public static final int PIE_CHART = 0;
    public static final int BAR_CHART = 1;

    public static final String TAG = "c4q.nyc.projectx";
    public static final String LOGGED_IN = "isLoggedIn";
    public static final String LOGGED_IN_GOOGLE = "logIn_Google";
    public static final String SHARED_PREFERENCE = "sharedPreference";
    public static final String PROFILE_IMAGE = "profileImage";
    public static final String MARKER_DROPPED = "markerDropped";
    public static final String YEAR = "Year";
    public static final String IS_CONNECTED = "isConnected";

    // Geofence constants
    public static final String GEOFENCE_NAME = "Geofence";
    public static final String LAST_GEOFENCE_FETCH = "lastGeofenceFetch";
    public static final String LOCATION = "location";
    public static final String TIMESTAMP = "timestamp";
    public static final long MILLI_24HOURS = 86400000;
    public static final long MILLI_48HOURS = 172800000;
    public static final float FIFTY_METERS = 80467.2f;
    public static final float GEOFENCE_RADIUS_IN_METER = 804.672f; // 1/2 mile
    public static final long GEOFENCE_EXPIRATION_IN_HOURS = 24;
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;
    public static final String LATITUDE_PREFERENCE = "latitudePreference";
    public static final String LONGITUDE_PREFERENCE = "longitudePreference";
    public static final String IS_DROPPED = "isDropped";
    public static final String ALLOW_GEOFENCE = "allowGeofence";

    // Shame constants
    public static final String SHOW_DIALOG = "showDialog";
    public static final String SHAME = "Shame";
    public static final String LAST_UPDATE = "lastUpdate";
    public static final String GROUP_COLUMN = "Group";
    public static final String SHAME_TYPE_COLUMN = "shameType";
    public static final String SHAME_FEEL_COLUMN = "shameFeel";
    public static final String SHAME_DOING_COLUMN = "shameDoing";
    public static final String VERBAL_SHAME_COLUMN = "verbalShame";
    public static final String PHYSICAL_SHAME_COLUMN = "physicalShame";
    public static final String OTHER_SHAME_COLUMN = "otherShame";
    public static final String SHAME_TIME_COLUMN = "shameTime";
    public static final String SHAME_LATITUDE_COLUMN = "latitude";
    public static final String SHAME_LONGITUDE_COLUMN = "longitude";
    public static final String SHAME_ZIPCODE_COLUMN = "zipCode";
    public static final String VERBAL = "Catcall";
    public static final String PHYSICAL = "Physical";
    public static final String OTHER = "Other";
    public static final String WOMAN = "Woman";
    public static final String WOMEN = "Women";
    public static final String POC = "POC";
    public static final String LGBTQ = "LGBTQ";
    public static final String MINOR = "Minor";
    public static final String PERSON = "person";
    public static final String MAN = "Man";
    public static final String LESBIAN = "Lesbian";
    public static final String TRANS = "Trans";
    public static final String GAY = "Gay";
    public static final String BISEXUAL = "Bisexual";
    public static final String QUEER = "Queer";
    public static  final String WHEN = "when";
    public static  final String WHO = "who";
    public static final String PIE_CHART_NAME = "pieChart";

    //local DB constants
    public static final String DB = "ShameDB";
    public static final int VERSION = 1;

}
