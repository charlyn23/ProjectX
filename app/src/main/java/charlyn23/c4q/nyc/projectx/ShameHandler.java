package charlyn23.c4q.nyc.projectx;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import charlyn23.c4q.nyc.projectx.model.Shame;

/**
 * Created by charlynbuchanan on 8/11/15.
 */
public class ShameHandler extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

//        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Shame.class);
        Parse.initialize(this, "wXm5LSYRqb26gArXbWoZDkLCqzO4dD1pa3y5J34O", "kvV4Abba1l7DKhUBQxK3PWLvIsFjQwuPyrcuMhXq");

        Shame newShame = new Shame();
        newShame.reportShame(100, -100, 1, 2, 0, 0, 2, 3, 900, 1);

//        newShame.setLatitude(100);
//        newShame.setLongitude(-100);
//        newShame.setShameType(3);
//        newShame.setVerbalShame(2);
//        newShame.setShameFeel(1);
//        newShame.setShameReason(3);
        newShame.saveInBackground();
    }
}
