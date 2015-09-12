package charlyn23.c4q.nyc.projectx.shames;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
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
import charlyn23.c4q.nyc.projectx.map.ShameGeofence;

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
    private Shame newShame;
    private Marker new_marker;
    private FloatingActionButton addShame;
    private MarkerListener markerListener;
    public List<Shame> active_shames;
    private Toast toast;

    public void initialDialog(final Context context, double latitude, double longitude, final Marker new_marker, final FloatingActionButton addShame, List<Shame> active_shames) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.new_marker = new_marker;
        this.addShame = addShame;
        this.active_shames = active_shames;

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
                        initialDialog(context, latitude, longitude, new_marker, addShame, active_shames);
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

                            // Submit new shame
                            newShame = new Shame();
                            try {
                                String zipcode = getZipcode(context, latitude, longitude);
                                if (zipcode != null)
                                    newShame.put(Constants.SHAME_ZIPCODE_COLUMN, zipcode);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            switch (shameType) {
                                case Constants.VERBAL:
                                    newShame.put(Constants.VERBAL_SHAME_COLUMN, verbalShame);
                                    break;
                                case Constants.PHYSICAL:
                                    newShame.put(Constants.PHYSICAL_SHAME_COLUMN, physicalShame);
                                    break;
                                case Constants.OTHER:
                                    newShame.put(Constants.OTHER_SHAME_COLUMN, otherShame);
                                    break;
                            }

                            newShame.put(Constants.SHAME_TIME_COLUMN, timestamp);
                            newShame.put(Constants.SHAME_LATITUDE_COLUMN, latitude);
                            newShame.put(Constants.SHAME_LONGITUDE_COLUMN, longitude);
                            newShame.put(Constants.SHAME_TYPE_COLUMN, shameType);
                            newShame.put(Constants.SHAME_FEEL_COLUMN, shameFeel);
                            newShame.put(Constants.SHAME_DOING_COLUMN, shameDoing);
                            newShame.put(Constants.GROUP_COLUMN, group);
                            newShame.saveInBackground();
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
//                            Toast.makeText(context, context.getString(R.string.shame_submitted), Toast.LENGTH_LONG).show();
                            if (markerListener != null)
                                markerListener.setMarker(latitude, longitude);

                            SharedPreferences preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE);
                            preferences.edit().putBoolean(Constants.IS_DROPPED, false).apply();
                            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View layout = inflater.inflate(R.layout.custom_toast, null);

                            toast = new Toast(context);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();

                            if (addShame != null) {
                                addShame.setVisibility(View.INVISIBLE);
                            } else {
                                Log.e("error", "foo");
                            }

                            // only check geofence checkbox has been checked
//                            if (enableGeofence)
//                                checkIfGeofenceIsNeeded();
                        }
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        whenDialog(context);
                        dialog.cancel();
                    }
                }).show();
    }

    public void checkIfGeofenceIsNeeded() {
        float[] distance = new float[1];
        int count = 0;

        // todo query only zipcode +- 2?
//        for (Shame shame : active_shames) {
//            Location.distanceBetween(latitude, longitude, shame.getLatitude(), shame.getLongitude(), distance);
//            if (distance[0] < Constants.GEOFENCE_RADIUS)
//                count++;
//        }

        // TODO && no geofence yet
        if (count > 10) {
            ShameGeofence newGeofence = new ShameGeofence();
            newGeofence.put(Constants.GROUP_COLUMN, group);
            newGeofence.put(Constants.SHAME_LATITUDE_COLUMN, latitude);
            newGeofence.put(Constants.SHAME_LONGITUDE_COLUMN, longitude);
            newGeofence.saveInBackground();
        }
    }

    private String getZipcode(Context context, double latitude, double longitude) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        // 1 represents max location result to returned, by documents it recommended 1 to 5
        addresses = geocoder.getFromLocation(latitude, longitude, 1);
        return addresses.get(0).getPostalCode();
    }

    public void setListener(MarkerListener listener) {
        markerListener = listener;
    }
}
