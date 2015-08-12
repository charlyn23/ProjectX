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
//        newShame.reportShame(100, -100, 1, 2, 0, 0, 2, 3, 900, 1);


        newShame.put("latitude", 100);
        newShame.put("longitude", -100);
        newShame.put("shameType", 3);
        newShame.put("verbalShame", 2);
        newShame.put("shameFeel", 2);
        newShame.put("shameDoing", 3);
        newShame.put("shameTime", 900);
        newShame.put("shameReason", 3);
        newShame.saveInBackground();



    }
}
