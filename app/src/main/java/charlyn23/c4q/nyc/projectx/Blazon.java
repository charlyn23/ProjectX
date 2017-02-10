package charlyn23.c4q.nyc.projectx;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by July on 8/9/15.
 */
public class Blazon extends Application {

//    private RefWatcher refWatcher;


    @Override
    public void onCreate() {
        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("shameObjectRealm.realm")
                .schemaVersion(2) // Must be bumped when the schema changes
                .migration(new Migration()) // Migration to run instead of throwing an exception
                .build();
        Realm.setDefaultConfiguration(config);

    }
}

