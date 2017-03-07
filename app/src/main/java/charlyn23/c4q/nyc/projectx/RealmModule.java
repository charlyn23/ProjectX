package charlyn23.c4q.nyc.projectx;

import charlyn23.c4q.nyc.projectx.shames.ShameObject;

/**
 * Created by charlynbuchanan on 3/2/17.
 *
 * User Objects and ShameObjects are both Realm Objects, so we point the realm module to those classes,
 * then, initialize in the Application class (Blazon.java)
 */

@io.realm.annotations.RealmModule(classes = {ShameObject.class, User.class})
public class RealmModule {
}
