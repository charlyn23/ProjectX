package charlyn23.c4q.nyc.projectx.shames;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import charlyn23.c4q.nyc.projectx.Constants;

@ParseClassName(Constants.SHAME)
public class Shame extends ParseObject {
    private double latitude;
    private double longitude;
    private String shameType;
    private String verbalShame;
    private String physicalShame;
    private String otherShame;
    private String shameFeel;
    private String shameDoing;
    private String shameTime;
    private String group;
    private String zipcode;

    public Shame(double latitude, double longitude, String shameType, String verbalShame,
                 String physicalShame, String otherShame, String shameFeel, String shameDoing,
                 String shameTime, String group, String zipcode){
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
        this.zipcode = zipcode;
    }

    public Shame(){

    }

    public String getGroup() {
        return group;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getOtherShame() {
        return otherShame;
    }

    public void setOtherShame(String otherShame) {
        this.otherShame = otherShame;
    }

    public String getPhysicalShame() {
        return physicalShame;
    }

    public void setPhysicalShame(String physicalShame) {
        this.physicalShame = physicalShame;
    }

    public String getShameDoing() {
        return shameDoing;
    }

    public void setShameDoing(String shameDoing) {
        this.shameDoing = shameDoing;
    }

    public String getShameFeel() {
        return shameFeel;
    }

    public void setShameFeel(String shameFeel) {
        this.shameFeel = shameFeel;
    }

    public String getShameTime() {
        return shameTime;
    }

    public void setShameTime(String shameTime) {
        this.shameTime = shameTime;
    }

    public String getShameType() {
        return shameType;
    }

    public void setShameType(String shameType) {
        this.shameType = shameType;
    }

    public String getVerbalShame() {
        return verbalShame;
    }

    public void setVerbalShame(String verbalShame) {
        this.verbalShame = verbalShame;
    }
}
