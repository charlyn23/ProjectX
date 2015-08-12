//package charlyn23.c4q.nyc.projectx;
//
//import com.parse.ParseClassName;
//import com.parse.ParseObject;
//
///**
// * Created by charlynbuchanan on 8/9/15.
// */
//
//@ParseClassName("Shame")
//public class Shame extends ParseObject{
//    //user, lat, long, shameType3, shameFeel5, shameDoing6, shameTime, shameReason4
//
//    private float latitude;
//    private float longitude;
//    private int shameType;
//    private int verbalShame;
//    private int physicalShame;
//    private int otherShame;
//
//    private int shameFeel;
//    private int shameDoing;
//    private long shameTime;
//    private int shameReason;
//
//    public Shame(){
//        super();
//    }
//
//    public long getShameTime() {
//        shameTime = System.currentTimeMillis()/1000;
//        return shameTime;
//    }
//
//    public void Shame(float latitude, float longitude, int shameType, int verbalShame,
//                      int physicalShame, int otherShame, int shameFeel, int shameDoing,
//                      long shameTime, int shameReason) {
//        this.latitude = latitude;           //shame latitude
//        this.longitude = longitude;         //shame longitude
//        this.otherShame = otherShame;       //shameType = other(three options)
//        this.physicalShame = physicalShame; //shameType = physical (six options)
//        this.shameDoing = shameDoing;       //what user was doing at time of shame (six options)
//        this.shameFeel = shameFeel;         //how they felt (five options)
//        this.shameReason = shameReason;     // why they were targeted (four options)
//        this.shameTime = shameTime;         // time of incident
//        this.shameType = shameType;         //what type? (three options: verbal, physical other)
//        this.verbalShame = verbalShame;     // verbal description (four options)
//    }
////
////
////    private Shame verbalIncident(float latitude, float longitude, int verbalShame, int shameFeel, int shameDoing, long shameTime, int shameReason){
////
////        return new Shame(latitude, longitude, shameType, verbalShame, shameFeel, shameDoing, shameTime, shameReason);
////    }
////
////    private Shame physicalIncident(float latitude, float longitude, int physicalShame, int shameFeel, int shameDoing, long shameTime, int shameReason){
////
////        return new Shame(latitude, longitude, shameType, verbalShame, shameFeel, shameDoing, shameTime, shameReason);
////    }
////
////    private Shame otherIncident(float latitude, float longitude, int otherShame, int shameFeel, int shameDoing, long shameTime, int shameReason){
////
////        return new Shame(latitude, longitude, shameType, otherShame, shameFeel, shameDoing, shameTime, shameReason);
////    }
//
//
//
//
//
//
//
//    public void setLatitude(float latitude) {
//        this.latitude = latitude;
//    }
//
//    public void setLongitude(float longitude) {
//        this.longitude = longitude;
//    }
//
//    public void setOtherShame(int otherShame) {
//        this.otherShame = otherShame;
//    }
//
//    public void setPhysicalShame(int physicalShame) {
//        this.physicalShame = physicalShame;
//    }
//
//    public void setShameDoing(int shameDoing) {
//        this.shameDoing = shameDoing;
//    }
//
//    public void setShameFeel(int shameFeel) {
//        this.shameFeel = shameFeel;
//    }
//
//    public void setShameReason(int shameReason) {
//        this.shameReason = shameReason;
//    }
//
//    public void setShameTime(long shameTime) {
//        this.shameTime = shameTime;
//    }
//
//    public void setShameType(int shameType) {
//        this.shameType = shameType;
//    }
//
//    public void setVerbalShame(int verbalShame) {
//        this.verbalShame = verbalShame;
//    }
//
//
//
//
//
//}
