package charlyn23.c4q.nyc.projectx.shames;

import com.parse.ParseClassName;
import com.parse.ParseObject;

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
    private long shameTime;
    private String group;

    public Shame(float latitude, float longitude, int shameType, int verbalShame,
                 int physicalShame, int otherShame, int shameFeel, int shameDoing,
                 long shameTime, String group){
        this.latitude = latitude;
        this.longitude = longitude;
        this.shameType = shameType;
        this.verbalShame = verbalShame;
        this.physicalShame = physicalShame;
        this.otherShame = otherShame;
        this.shameFeel = shameFeel;
        this.shameDoing = shameDoing;
        this.shameTime = shameTime;
        this.group = group;
    }

    public Shame(){

    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getShameType() {
        return shameType;
    }

    public void setShameType(int shameType) {
        this.shameType = shameType;
    }

    public int getVerbalShame() {
        return verbalShame;
    }

    public void setVerbalShame(int verbalShame) {
        this.verbalShame = verbalShame;
    }

    public int getPhysicalShame() {
        return physicalShame;
    }

    public void setPhysicalShame(int physicalShame) {
        this.physicalShame = physicalShame;
    }

    public int getOtherShame() {
        return otherShame;
    }

    public void setOtherShame(int otherShame) {
        this.otherShame = otherShame;
    }

    public int getShameFeel() {
        return shameFeel;
    }

    public void setShameFeel(int shameFeel) {
        this.shameFeel = shameFeel;
    }

    public int getShameDoing() {
        return shameDoing;
    }

    public void setShameDoing(int shameDoing) {
        this.shameDoing = shameDoing;
    }

    public long getShameTime() {
        return shameTime;
    }

    public void setShameTime(long shameTime) {
        this.shameTime = shameTime;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
