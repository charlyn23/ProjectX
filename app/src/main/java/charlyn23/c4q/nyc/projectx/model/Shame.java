package charlyn23.c4q.nyc.projectx.model;

import android.text.format.DateFormat;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by charlynbuchanan on 8/11/15.
 */

@ParseClassName("Shame")
public class Shame extends ParseObject {

    private float latitude;
    private float longitude;
    private int shameType;
    private int verbalShame;
    private int physicalShame;
    private int otherShame;

    private int shameFeel;
    private int shameDoing;
    private String shameTime;
    private int shameReason;

    public Shame(){
        super();
    }

    public Shame(float latitude, float longitude, int shameType, int verbalShame,
                 int physicalShame, int otherShame, int shameFeel, int shameDoing,
                 int shameTime, int shameReason) {
        super();
        setLatitude(latitude);
        setLongitude(longitude);
        setShameType(shameType);
        setVerbalShame(verbalShame);
        setPhysicalShame(physicalShame);
        setOtherShame(otherShame);
        setShameFeel(shameFeel);
        setShameDoing(shameDoing);
        setShameTime();
        setShameReason(shameReason);
    }

    public void reportShame(float latitude, float longitude, int shameType, int verbalShame,
                         int physicalShame, int otherShame, int shameFeel, int shameDoing,
                         String shameTime, int shameReason){
        this.latitude = latitude;           //shame latitude
        this.longitude = longitude;         //shame longitude
        this.otherShame = otherShame;       //shameType = other(three options)
        this.physicalShame = physicalShame; //shameType = physical (six options)
        this.shameDoing = shameDoing;       //what user was doing at time of shame (six options)
        this.shameFeel = shameFeel;         //how they felt (five options)
        this.shameReason = shameReason;     // why they were targeted (four options)
        this.shameTime = shameTime;         // time of incident
        this.shameType = shameType;         //what type? (three options: verbal, physical other)
        this.verbalShame = verbalShame;     // verbal description (four options)
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public int getOtherShame() {
        return otherShame;
    }

    public int getPhysicalShame() {
        return physicalShame;
    }

    public int getShameDoing() {
        return shameDoing;
    }

    public int getShameFeel() {
        return shameFeel;
    }

    public int getShameReason() {
        return shameReason;
    }

    public String getShameTime() {
        shameTime = (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());

        return shameTime;
    }

    public int getShameType() {

        return shameType;
    }

    public int getVerbalShame() {
        return verbalShame;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setOtherShame(int otherShame) {
        this.otherShame = otherShame;
    }

    public void setPhysicalShame(int physicalShame) {
        this.physicalShame = physicalShame;
    }

    public void setShameDoing(int shameDoing) {
        this.shameDoing = shameDoing;
    }

    public void setShameFeel(int shameFeel) {
        this.shameFeel = shameFeel;
    }

    public void setShameReason(int shameReason) {
        this.shameReason = shameReason;
    }

    public void setShameTime() {
        shameTime = (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());

        this.shameTime = shameTime;
    }

    public void setShameType(int shameType) {
        this.shameType = shameType;
    }

    public void setVerbalShame(int verbalShame) {
        this.verbalShame = verbalShame;
    }

}
