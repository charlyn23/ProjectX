package charlyn23.c4q.nyc.projectx;

import com.facebook.login.LoginManager;

import io.realm.Realm;
import io.realm.SyncConfiguration;
import io.realm.SyncUser;


/**
 * Created by charlynbuchanan on 3/1/17.
 */

public class UserManager {
    // Supported authentication mode
    public enum AUTH_MODE {
        PASSWORD,
        FACEBOOK,
        GOOGLE
    }
    private static AUTH_MODE mode = AUTH_MODE.PASSWORD; // default

    public static void setAuthMode(AUTH_MODE m) {
        mode = m;
    }

    public static void logoutActiveUser() {
        switch (mode) {
            case PASSWORD: {
                // Do nothing, handled by the `User.currentUser().logout();`
                break;
            }
            case FACEBOOK: {
                LoginManager.getInstance().logOut();
                break;
            }
            case GOOGLE: {
                // the connection is handled by `enableAutoManage` mode
                break;
            }
        }

        SyncUser.currentUser().logout();
    }

    // Configure Realm for the current active user
    public static void setActiveUser(SyncUser user) {
        SyncConfiguration defaultConfig = new SyncConfiguration.Builder(user, Constants.AUTH_URL).build();
        Realm.setDefaultConfiguration(defaultConfig);
    }
}
