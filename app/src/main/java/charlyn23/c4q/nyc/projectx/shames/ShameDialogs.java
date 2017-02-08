package charlyn23.c4q.nyc.projectx.shames;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import charlyn23.c4q.nyc.projectx.Constants;
import charlyn23.c4q.nyc.projectx.R;
import io.realm.Realm;

public class ShameDialogs {
    private String shameType;
    private String verbalShame;
    private String physicalShame;
    private String otherShame;
    private String shameFeel;
    private String shameDoing;
    private String group;
    private String timestamp;
    private double latitude;
    private double longitude;
    private ShameObject newShame;
    private Marker new_marker;
    private FloatingActionButton addShame;
    private MarkerListener markerListener;
    private Toast toast;
    private Context context;

    public void initialDialog(final Context context, double latitude, double longitude, final Marker new_marker, final FloatingActionButton addShame) {
        this.context = context;
        this.latitude = latitude;
        this.longitude = longitude;
        this.new_marker = new_marker;
        this.addShame = addShame;

        new MaterialDialog.Builder(context)
                .title(R.string.new_shame_type)
                .items(R.array.shame_types)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (which < 0) {
                            YoYo.with(Techniques.Shake).playOn(dialog.getActionButton(DialogAction.POSITIVE));
                        } else {
                            shameType = text.toString();
                            shameTypeDialog(context);
                        }
                        return true;
                    }
                })
                .positiveText(R.string.next)
                .negativeText(R.string.cancel)
                .autoDismiss(false)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        if (dialog.getSelectedIndex() < 0)
                            YoYo.with(Techniques.Shake).playOn(dialog.getActionButton(DialogAction.POSITIVE));
                        else
                            dialog.cancel();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.cancel();
                        if (new_marker != null) {
                            new_marker.remove();
                            addShame.setVisibility(View.INVISIBLE);
                        }
                    }
                }).show();
    }


    public void shameTypeDialog(final Context context) {
        int content = R.string.other_shame, items = R.array.other_types;

        switch (shameType) {
            case Constants.VERBAL:
                content = R.string.verbal_shame;
                items = R.array.verbal_types;
                break;
            case Constants.PHYSICAL:
                content = R.string.physical_shame;
                items = R.array.physical_types;
                break;
            case Constants.OTHER:
                content = R.string.other_shame;
                items = R.array.other_types;
                break;
        }

        new MaterialDialog.Builder(context)
                .title(content)
                .items(items)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence type_choice) {
                        if (which < 0) {
                            YoYo.with(Techniques.Shake).playOn(dialog.getActionButton(DialogAction.POSITIVE));
                        } else {
                            switch (shameType) {
                                case Constants.VERBAL:
                                    verbalShame = type_choice.toString();
                                    break;
                                case Constants.PHYSICAL:
                                    physicalShame = type_choice.toString();
                                    break;
                                case Constants.OTHER:
                                    otherShame = type_choice.toString();
                                    break;
                            }
                            feelDialog(context);
                        }
                        return true;
                    }
                })
                .autoDismiss(false)
                .positiveText(R.string.next)
                .negativeText(R.string.back)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        if (dialog.getSelectedIndex() < 0)
                            YoYo.with(Techniques.Shake).playOn(dialog.getActionButton(DialogAction.POSITIVE));
                        else
                            dialog.cancel();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        initialDialog(context, latitude, longitude, new_marker, addShame);
                        dialog.cancel();
                    }
                })
                .show();
    }

    public void feelDialog(final Context context) {
        new MaterialDialog.Builder(context)
                .title(R.string.shame_feel)
                .items(R.array.feel_types)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence feel) {
                        if (which < 0) {
                            YoYo.with(Techniques.Shake).playOn(dialog.getActionButton(DialogAction.POSITIVE));
                        } else {
                            shameFeel = feel.toString();
                            doingDialog(context);
                        }
                        return true;
                    }
                })
                .autoDismiss(false)
                .positiveText(R.string.next)
                .negativeText(R.string.back)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        if (dialog.getSelectedIndex() < 0)
                            YoYo.with(Techniques.Shake).playOn(dialog.getActionButton(DialogAction.POSITIVE));
                        else
                            dialog.cancel();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        shameTypeDialog(context);
                        dialog.cancel();
                    }
                }).show();
    }

    public void doingDialog(final Context context) {
        new MaterialDialog.Builder(context)
                .title(R.string.shame_doing)
                .items(R.array.doing_types)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence doing) {
                        if (which < 0) {
                            YoYo.with(Techniques.Shake).playOn(dialog.getActionButton(DialogAction.POSITIVE));
                        } else {
                            shameDoing = doing.toString();
                            whenDialog(context);
                        }
                        return true;
                    }
                })
                .autoDismiss(false)
                .positiveText(R.string.next)
                .negativeText(R.string.back)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        if (dialog.getSelectedIndex() < 0)
                            YoYo.with(Techniques.Shake).playOn(dialog.getActionButton(DialogAction.POSITIVE));
                        else
                            dialog.cancel();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        feelDialog(context);
                        dialog.cancel();
                    }
                }).show();
    }

    public void whenDialog(final Context context) {
        new MaterialDialog.Builder(context)
                .title(R.string.shame_when)
                .customView(R.layout.time_picker, true)
                .autoDismiss(false)
                .positiveText(R.string.next)
                .negativeText(R.string.back)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        DatePicker date_picker = (DatePicker) dialog.findViewById(R.id.date);
                        TimePicker time_picker = (TimePicker) dialog.findViewById(R.id.time);
                        timestamp = getDateFromDatePicker(date_picker, time_picker);
                        Log.d("DATE__", timestamp);

                        whyDialog(context, timestamp);
                        dialog.cancel();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        doingDialog(context);
                        dialog.cancel();
                    }
                }).show();
    }

    public static String getDateFromDatePicker(DatePicker datePicker, TimePicker timePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        int hour = timePicker.getCurrentHour();
        int min = timePicker.getCurrentMinute();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, min);
        Date date = calendar.getTime();

        return new SimpleDateFormat("yyyyMMdd_HHmm").format(date);
    }

    public void whyDialog(final Context context, final String timestamp) {
        new MaterialDialog.Builder(context)
                .title(R.string.shame_why)
                .items(R.array.why_types)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence why) {
                        if (dialog.getSelectedIndex() < 0)
                            YoYo.with(Techniques.Shake).playOn(dialog.getActionButton(DialogAction.POSITIVE));
                        else {
                            if (which == 0)
                                group = Constants.WOMAN;
                            if (which == 1)
                                group = Constants.POC;
                            if (which == 2)
                                group = Constants.LGBTQ;
                            if (which == 3)
                                group = Constants.MINOR;
                            if (which == 4)
                                group = Constants.OTHER;

                            //check network connection
                            boolean isConnected = checkNetworkConnection();
                            if (!isConnected) {
                                Toast.makeText(context, R.string.check_network_connection, Toast.LENGTH_LONG).show();
                            } else {
//                                saveShame();
                                createShame();
                                //Show custom toast after submitting incident
                                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View layout = inflater.inflate(R.layout.custom_toast, null);
                                toast = new Toast(context);
                                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);

                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.setView(layout);
                                toast.show();
//                                checkIfGeofenceIsNeeded();

                                if (markerListener != null)
                                    markerListener.setMarker(latitude, longitude);
                                if (addShame != null) {
                                    addShame.setVisibility(View.INVISIBLE);
                                }
                            }
                            SharedPreferences preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE);
                            preferences.edit().putBoolean(Constants.IS_DROPPED, false).commit();
                        }
                        return true;
                    }
                })
                .autoDismiss(false)
                .positiveText(R.string.submit)
                .negativeText(R.string.back)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        if (dialog.getSelectedIndex() < 0)
                            YoYo.with(Techniques.Shake).playOn(dialog.getActionButton(DialogAction.POSITIVE));
                        else {
                            dialog.cancel();
                        }
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        whenDialog(context);
                        dialog.cancel();
                    }
                }).show();
    }
//TODO: Swap to RealmObject
//    public void checkIfGeofenceIsNeeded() {
//        ParseQuery<Shame> query = ParseQuery.getQuery(Constants.SHAME);
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 2);
//        String last_two_months = new SimpleDateFormat("yyyyMMdd_HHmmss").format(cal.getTime());
//        query.whereGreaterThanOrEqualTo(Constants.SHAME_TIME_COLUMN, last_two_months);
//        query.whereEqualTo(Constants.GROUP_COLUMN, group);
//        query.whereWithinMiles(Constants.LOCATION, new ParseGeoPoint(latitude, longitude), 0.5);
//        query.findInBackground(new FindCallback<Shame>() {
//            public void done(List<Shame> results, ParseException e) {
//                if (e == null) {
//                    float[] distance = new float[1];
//                    int count = 0;
//
//                    for (Shame shame : results) {
//                        // todo query only zipcode +- 2?
//                        Location.distanceBetween(latitude, longitude, shame.getDouble(Constants.SHAME_LATITUDE_COLUMN), shame.getDouble(Constants.SHAME_LONGITUDE_COLUMN), distance);
//                        if (distance[0] < Constants.GEOFENCE_RADIUS_IN_METER
//                                && shame.getString(Constants.GROUP_COLUMN).equals(group))
//                            count++;
//                    }
//
//                    // TODO && no geofence yet
//                    if (count > 5) {
//                        String time = new SimpleDateFormat("yyyyMMdd_HHmm").format(Calendar.getInstance().getTime());
//                        ShameGeofence newGeofence = new ShameGeofence();
//                        newGeofence.put(Constants.GROUP_COLUMN, group);
//                        newGeofence.put(Constants.LOCATION, new ParseGeoPoint(latitude, longitude));
//                        newGeofence.put(Constants.TIMESTAMP, time);
//                        newGeofence.saveInBackground();
//                    }
//                    Log.d("List of Shames", "Retrieved " + results.size() + " Shames");
//                } else {
//                    Log.d("List of Shames", "Error: " + e.getMessage());
//                }
//            }
//        });
//    }

    private String getZipcode(Context context, double latitude, double longitude) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        // 1 represents max location result to returned, by documents it recommended 1 to 5
        addresses = geocoder.getFromLocation(latitude, longitude, 1);
        if (addresses != null) {
            return addresses.get(0).getPostalCode();
        }
        else {
            return null;
        }
    }

    public void setListener(MarkerListener listener) {
        markerListener = listener;
    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
    }

    // Submit new shame
//    public void saveShame() {
//        newShame = new ShameObject();
//        try {
//            String zipcode = getZipcode(context, latitude, longitude);
//            if (zipcode != null)
//
//                newShame.put(Constants.SHAME_ZIPCODE_COLUMN, zipcode);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        switch (shameType) {
//            case Constants.VERBAL:
//                newShame.put(Constants.VERBAL_SHAME_COLUMN, verbalShame);
//                break;
//            case Constants.PHYSICAL:
//                newShame.put(Constants.PHYSICAL_SHAME_COLUMN, physicalShame);
//                break;
//            case Constants.OTHER:
//                newShame.put(Constants.OTHER_SHAME_COLUMN, otherShame);
//                break;
//        }
//
//        newShame.put(Constants.SHAME_TIME_COLUMN, timestamp);
//        newShame.put(Constants.SHAME_LATITUDE_COLUMN, latitude);
//        newShame.put(Constants.SHAME_LONGITUDE_COLUMN, longitude);
//        newShame.put(Constants.SHAME_TYPE_COLUMN, shameType);
//        newShame.put(Constants.SHAME_FEEL_COLUMN, shameFeel);
//        newShame.put(Constants.SHAME_DOING_COLUMN, shameDoing);
//        newShame.put(Constants.GROUP_COLUMN, group);
//        newShame.put(Constants.LOCATION, new ParseGeoPoint(latitude, longitude));
//        newShame.saveInBackground();
//
//    }
    //make realm shame object
    public void createShame() {
        Realm.init(context);
        Realm realm  = Realm.getDefaultInstance();
        try{
            String zipcode = getZipcode(context, latitude, longitude);
            if (zipcode != null) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        newShame = realm.createObject(ShameObject.class);
                        newShame.setLatitude(latitude);
                        newShame.setLongitude(longitude);
                        newShame.setShameType(shameType);
                        newShame.setShameFeel(shameFeel);
                        newShame.setShameDoing(shameDoing);
                        newShame.setGroup(group);

                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("created shame: ", newShame.getShameFeel());
        realm.close();
    }

}
