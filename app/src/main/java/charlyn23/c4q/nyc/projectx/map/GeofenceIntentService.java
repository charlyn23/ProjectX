package charlyn23.c4q.nyc.projectx.map;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by sufeizhao on 9/2/15.
 */
public class GeofenceIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GeofenceIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
