package charlyn23.c4q.nyc.projectx;

import charlyn23.c4q.nyc.projectx.shames.ShameObject;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by charlynbuchanan on 2/18/17.
 */

public class User extends RealmObject {

    @PrimaryKey
    private String id;
    private String name;
    private RealmList<ShameObject> userShames;

    public User(){}

    public User(String id, String name){
        this.id = id;
        this.name = name;
        this.userShames = new RealmList<>();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<ShameObject> getUserShames() {
        return userShames;
    }

    public void setUserShames(RealmList<ShameObject> userShames) {
        this.userShames = userShames;
    }

    public void addShame(ShameObject shameObject) {
        this.userShames.add(shameObject);
    }

}
