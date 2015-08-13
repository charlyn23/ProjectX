package charlyn23.c4q.nyc.projectx.shames;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by charlynbuchanan on 8/11/15.
 */

@ParseClassName("Shame")
public class Shame extends ParseObject {

    public Shame(){
        super();
    }

    public Shame(float latitude, float longitude, int shameType, int verbalShame,
                 int physicalShame, int otherShame, int shameFeel, int shameDoing,
                 long shameTime, int shameReason) {
        super();
        setLatitude(latitude);
        setLongitude(longitude);
        setShameType(shameType);
        setVerbalShame(verbalShame);
        setPhysicalShame(physicalShame);
        setOtherShame(otherShame);
        setShameFeel(shameFeel);
        setShameDoing(shameDoing);
        setShameTime(shameTime);
        setShameReason(shameReason);
    }

    public void reportShame(float latitude, float longitude, int shameType, int verbalShame,
                         int physicalShame, int otherShame, int shameFeel, int shameDoing,
                         long shameTime, int shameReason){
        setLatitude(latitude);
        setLongitude(longitude);
        setOtherShame(otherShame);       //shameType = other(three options)
        setShameType(shameType); //shameType = physical (six options)
        setShameDoing(shameDoing);       //what user was doing at time of shame (six options)
        setShameFeel(shameFeel);         //how they felt (five options)
        setShameReason(shameReason);     // why they were targeted (four options)
        setShameTime(shameTime);         // time of incident
        setPhysicalShame(physicalShame);     // verbal description (four options)
    }

    public float getLatitude() {
        return getLatitude();
    }

    public float getLongitude() {
        return getLongitude();
    }

    public int getOtherShame() {
        return getOtherShame();
    }

    public int getPhysicalShame() {
        return getPhysicalShame();
    }

    public int getShameDoing() {
        return getShameDoing();
    }

    public int getShameFeel() {
        return getShameFeel();
    }

    public int getShameReason() {
        return getShameReason();
    }

    public long getShameTime() {
        return getShameTime();
    }

    public int getShameType() {
        return getShameType();
    }

    public int getVerbalShame() {
        return getVerbalShame();
    }

    public void setLatitude(float latitude) {
        put("latitude", latitude);
    }

    public void setLongitude(float longitude) {
        put("longitude", longitude);
    }

    public void setOtherShame(int otherShame) {
        put("otherShame", otherShame);
    }

    public void setPhysicalShame(int physicalShame) {
        put("physicalShame", physicalShame);
    }

    public void setShameDoing(int shameDoing) {
        put("shameDoing", shameDoing);
    }

    public void setShameFeel(int shameFeel) {
        put("shameFeel", shameFeel);
    }

    public void setShameReason(int shameReason) {
        put("shameReason", shameReason);
    }

    public void setShameTime(long shameTime) {
        put("shameTime", shameTime);
    }

    public void setShameType(int shameType) {
        put("shameType", shameType);
    }

    public void setVerbalShame(int verbalShame) {
        put("verbalShame", verbalShame);
    }
}
