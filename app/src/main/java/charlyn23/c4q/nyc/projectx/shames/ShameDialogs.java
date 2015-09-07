package charlyn23.c4q.nyc.projectx.shames;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.maps.model.Marker;
import com.parse.ParseObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import charlyn23.c4q.nyc.projectx.Constants;
import charlyn23.c4q.nyc.projectx.R;


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
    private Marker newMarker;
    private FloatingActionButton addShame;
    private MarkerListener markerListener;

    public void initialDialog(final Context context, double latitude, double longitude, final Marker newMarker, final FloatingActionButton addShame) {
        ParseObject.registerSubclass(Shame.class);
        this.latitude = latitude;
        this.longitude = longitude;
        this.newMarker = newMarker;
        this.addShame = addShame;

        new MaterialDialog.Builder(context)
                .title(R.string.new_shame_type)
                .items(R.array.shame_types)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (which < 0) {
                            YoYo.with(Techniques.Shake).playOn(dialog.getActionButton(DialogAction.POSITIVE));
                        } else if (text.equals(Constants.VERBAL_CAPITAL)) {
                            shameTypeDialog(context, Constants.VERBAL);
                            shameType = Constants.VERBAL;
                        } else if (text.equals(Constants.PHYSICAL_CAPITAL)) {
                            shameTypeDialog(context, Constants.PHYSICAL);
                            shameType = Constants.PHYSICAL;
                        } else {
                            shameTypeDialog(context, Constants.OTHER);
                            shameType = Constants.OTHER;
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
                        if (newMarker != null) {
                            newMarker.remove();
                            addShame.setVisibility(View.INVISIBLE);
                        }
                    }
                })
                .show();
    }

    public void shameTypeDialog(final Context context, final String type) {
        int content, items;

        switch (type) {
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
            default:
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
                            feelDialog(context, type, type_choice.toString());

                            if (type.equals(Constants.VERBAL) && which == 0)
                                verbalShame = Constants.BODY_COMMENT;
                            else if (type.equals(Constants.VERBAL) && which == 1)
                                verbalShame = Constants.VULGAR;
                            else if (type.equals(Constants.VERBAL) && which == 2)
                                verbalShame = Constants.CREEPY;
                            else if (type.equals(Constants.VERBAL) && which == 3)
                                verbalShame = Constants.THREATENED;
                            else if (type.equals(Constants.PHYSICAL) && which == 0)
                                physicalShame = Constants.TOUCH;
                            else if (type.equals(Constants.PHYSICAL) && which == 1)
                                physicalShame = Constants.HIT;
                            else if (type.equals(Constants.PHYSICAL) && which == 2)
                                physicalShame = Constants.THROW_SOMETHING;
                            else if (type.equals(Constants.PHYSICAL) && which == 3)
                                physicalShame = Constants.SPIT;
                            else if (type.equals(Constants.PHYSICAL) && which == 4)
                                physicalShame = Constants.PULL_AT_CLOTHING;
                            else if (type.equals(Constants.PHYSICAL) && which == 5)
                                physicalShame = Constants.SEXUALLY_ASSAULTED;
                            else if (type.equals(Constants.OTHER) && which == 0)
                                otherShame = Constants.FOLLOW;
                            else if (type.equals(Constants.OTHER) && which == 1)
                                otherShame = Constants.EXPOSE_THEMSELVES;
                            else if (type.equals(Constants.OTHER) && which == 2)
                                otherShame = Constants.MASTURBATE;
                            else if (type.equals(Constants.OTHER) && which == 3)
                                otherShame = Constants.OTHER;
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
                        initialDialog(context, latitude, longitude, newMarker, addShame);
                        dialog.cancel();
                    }
                })
                .show();
    }

    public void feelDialog(final Context context, final String type, final String type_choice) {
        new MaterialDialog.Builder(context)
                .title(R.string.shame_feel)
                .items(R.array.feel_types)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence feel) {
                        if (which < 0) {
                            YoYo.with(Techniques.Shake).playOn(dialog.getActionButton(DialogAction.POSITIVE));
                        } else {
                            doingDialog(context, type, type_choice, feel.toString());

                            if (which == 0)
                                shameFeel = Constants.BARELY_NOTICED;
                            else if (which == 1)
                                shameFeel = Constants.ANGRY;
                            else if (which == 2)
                                shameFeel = Constants.ANNOYED;
                            else if (which == 3)
                                shameFeel = Constants.UNEASY;
                            else if (which == 4)
                                shameFeel = Constants.SCARED;
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
                        shameTypeDialog(context, type);
                        dialog.cancel();
                    }
                })
                .show();
    }

    public void doingDialog(final Context context, final String type, final String type_choice, final String feel) {
        new MaterialDialog.Builder(context)
                .title(R.string.shame_doing)
                .items(R.array.doing_types)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence doing) {
                        if (which < 0) {
                            YoYo.with(Techniques.Shake).playOn(dialog.getActionButton(DialogAction.POSITIVE));
                        } else {
                            whenDialog(context, type, type_choice, feel, doing.toString());

                            if (which == 0) {
                                shameDoing = Constants.WALKING;
                            }
                            if (which == 1) {
                                shameDoing = Constants.JOGGING;
                            }
                            if (which == 2) {
                                shameDoing = Constants.BIKING;
                            }
                            if (which == 3) {
                                shameDoing = Constants.WAITING;
                            }
                            if (which == 4) {
                                shameDoing = Constants.DRIVING;
                            }
                            if (which == 5) {
                                shameDoing = Constants.OTHER;
                            }
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
                        feelDialog(context, type, type_choice);
                        dialog.cancel();
                    }
                })
                .show();
    }

    public void whenDialog(final Context context, final String type, final String type_choice, final String feel, final String doing) {
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

                        whyDialog(context, type, type_choice, feel, doing, timestamp);
                        dialog.cancel();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        doingDialog(context, type, type_choice, feel);
                        dialog.cancel();
                    }
                })
                .show();
    }

    public static String getDateFromDatePicker(DatePicker datePicker, TimePicker timePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();
        int hour = timePicker.getCurrentHour();
        int min = timePicker.getCurrentMinute();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, min);
        Date date = calendar.getTime();

        return new SimpleDateFormat("yyyyMMdd_HHmm").format(date);
    }

    public void whyDialog(final Context context, final String type, final String type_choice, final String feel, final String doing, final String timestamp) {
        new MaterialDialog.Builder(context)
                .title(R.string.shame_why)
                .items(R.array.why_types)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {

                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence why) {
                        if (dialog.getSelectedIndex() < 0)
                            YoYo.with(Techniques.Shake).playOn(dialog.getActionButton(DialogAction.POSITIVE));
                        else {
                            if (which == 0) {
                                group = Constants.WOMAN;
                            }
                            if (which == 1) {
                                group = Constants.POC;
                            }
                            if (which == 2) {
                                group = Constants.LGBTQ;
                            }
                            if (which == 3) {
                                group = Constants.MINOR;
                            }

                            newShame = new Shame();
                            newShame.put(Constants.SHAME_TIME_COLUMN, timestamp);
                            newShame.put(Constants.SHAME_LATITUDE_COLUMN, latitude);
                            newShame.put(Constants.SHAME_LONGITUDE_COLUMN, longitude);
                            try {
                                Log.d("before", latitude + " lat in dialog");
                                Log.d("before", longitude + " long in dialog");
                                String zipcode = getZipcode(context, latitude, longitude);
                                if (zipcode != null) {
                                    newShame.put(Constants.SHAME_ZIPCODE_COLUMN, zipcode);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            switch (shameType) {
                                case Constants.VERBAL:
                                    newShame.put(Constants.SHAME_TYPE_COLUMN, Constants.VERBAL);
                                    newShame.saveInBackground();
                                    if (verbalShame.equals(Constants.BODY_COMMENT)) {
                                        newShame.put(Constants.VERBAL_SHAME_COLUMN, Constants.BODY_COMMENT);
                                        newShame.saveInBackground();
                                    } else if (verbalShame.equals(Constants.VULGAR)) {
                                        newShame.put(Constants.VERBAL_SHAME_COLUMN, Constants.VULGAR);
                                        newShame.saveInBackground();
                                    } else if (verbalShame.equals(Constants.CREEPY)) {
                                        newShame.put(Constants.VERBAL_SHAME_COLUMN, Constants.CREEPY);
                                        newShame.saveInBackground();
                                    } else {
                                        newShame.put(Constants.VERBAL_SHAME_COLUMN, Constants.THREATENED);
                                        newShame.saveInBackground();
                                    }
                                    break;
                                case Constants.PHYSICAL:
                                    newShame.put(Constants.SHAME_TYPE_COLUMN, Constants.PHYSICAL);
                                    newShame.saveInBackground();
                                    if (physicalShame.equals(Constants.TOUCH)) {
                                        newShame.put(Constants.PHYSICAL_SHAME_COLUMN, Constants.TOUCH);
                                    } else if (physicalShame.equals(Constants.HIT)) {
                                        newShame.put(Constants.PHYSICAL_SHAME_COLUMN, Constants.HIT);
                                        newShame.saveInBackground();
                                    } else if (physicalShame.equals(Constants.THROW_SOMETHING)) {
                                        newShame.put(Constants.PHYSICAL_SHAME_COLUMN, Constants.THROW_SOMETHING);
                                        newShame.saveInBackground();
                                    } else if (physicalShame.equals(Constants.SPIT)) {
                                        newShame.put(Constants.PHYSICAL_SHAME_COLUMN, Constants.SPIT);
                                        newShame.saveInBackground();
                                    } else if (physicalShame.equals(Constants.PULL_AT_CLOTHING)) {
                                        newShame.put(Constants.PHYSICAL_SHAME_COLUMN, Constants.PULL_AT_CLOTHING);
                                        newShame.saveInBackground();
                                    } else {
                                        newShame.put(Constants.PHYSICAL_SHAME_COLUMN, Constants.SEXUALLY_ASSAULTED);
                                        newShame.saveInBackground();
                                    }
                                    break;
                                case Constants.OTHER:
                                    newShame.put(Constants.SHAME_TYPE_COLUMN, Constants.OTHER);
                                    newShame.saveInBackground();
                                    if (otherShame.equals(Constants.FOLLOW)) {
                                        newShame.put(Constants.OTHER_SHAME_COLUMN, Constants.FOLLOW);
                                        newShame.saveInBackground();
                                    } else if (otherShame.equals(Constants.EXPOSE_THEMSELVES)) {
                                        newShame.put(Constants.OTHER_SHAME_COLUMN, Constants.EXPOSE_THEMSELVES);
                                        newShame.saveInBackground();
                                    } else if (otherShame.equals(Constants.MASTURBATE)) {
                                        newShame.put(Constants.OTHER_SHAME_COLUMN, Constants.MASTURBATE);
                                        newShame.saveInBackground();
                                    } else {
                                        newShame.put(Constants.OTHER_SHAME_COLUMN, Constants.OTHER);
                                        newShame.saveInBackground();
                                    }
                                    break;
                            }

                            switch (shameFeel) {
                                case Constants.BARELY_NOTICED:
                                    newShame.put(Constants.SHAME_FEEL_COLUMN, Constants.BARELY_NOTICED);
                                    newShame.saveInBackground();
                                    break;
                                case Constants.ANGRY:
                                    newShame.put(Constants.SHAME_FEEL_COLUMN, Constants.ANGRY);
                                    newShame.saveInBackground();
                                    break;
                                case Constants.ANNOYED:
                                    newShame.put(Constants.SHAME_FEEL_COLUMN, Constants.ANNOYED);
                                    newShame.saveInBackground();
                                    break;
                                case Constants.UNEASY:
                                    newShame.put(Constants.SHAME_FEEL_COLUMN, Constants.UNEASY);
                                    newShame.saveInBackground();
                                    break;
                                case Constants.SCARED:
                                    newShame.put(Constants.SHAME_FEEL_COLUMN, Constants.SCARED);
                                    newShame.saveInBackground();
                                    break;
                            }

                            switch (shameDoing) {
                                case Constants.WALKING:
                                    newShame.put(Constants.SHAME_DOING_COLUMN, Constants.WALKING);
                                    newShame.saveInBackground();
                                    break;
                                case Constants.JOGGING:
                                    newShame.put(Constants.SHAME_DOING_COLUMN, Constants.JOGGING);
                                    newShame.saveInBackground();
                                    break;
                                case Constants.BIKING:
                                    newShame.put(Constants.SHAME_DOING_COLUMN, Constants.BIKING);
                                    newShame.saveInBackground();
                                    break;
                                case Constants.WAITING:
                                    newShame.put(Constants.SHAME_DOING_COLUMN, Constants.WAITING);
                                    newShame.saveInBackground();
                                    break;
                                case Constants.DRIVING:
                                    newShame.put(Constants.SHAME_DOING_COLUMN, Constants.DRIVING);
                                    newShame.saveInBackground();
                                    break;
                                case Constants.OTHER:
                                    newShame.put(Constants.SHAME_DOING_COLUMN, Constants.OTHER);
                                    newShame.saveInBackground();
                                    break;
                            }

                            switch (group) {
                                case Constants.WOMAN:
                                    newShame.put(Constants.GROUP_COLUMN, Constants.WOMAN);
                                    newShame.saveInBackground();
                                    break;
                                case Constants.POC:
                                    newShame.put(Constants.GROUP_COLUMN, Constants.POC);
                                    newShame.saveInBackground();
                                    break;
                                case Constants.LGBTQ:
                                    newShame.put(Constants.GROUP_COLUMN, Constants.LGBTQ);
                                    newShame.saveInBackground();
                                    break;
                                case Constants.MINOR:
                                    newShame.put(Constants.GROUP_COLUMN, Constants.MINOR);
                                    newShame.saveInBackground();
                                    break;
                            }
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
                            Toast.makeText(context, context.getString(R.string.shame_submitted), Toast.LENGTH_LONG).show();
                            if (markerListener != null) {
                                markerListener.setMarker(latitude, longitude);
                            }
                        }
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        whenDialog(context, type, type_choice, feel, doing);
                        dialog.cancel();
                    }
                })
                .show();
    }

    private String getZipcode (Context context, double latitude, double longitude) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        // 1 represents max location result to returned, by documents it recommended 1 to 5
        addresses = geocoder.getFromLocation(latitude, longitude, 1);
        String postalCode = addresses.get(0).getPostalCode();
        return postalCode;
    }

    public void setListener(MarkerListener listener) {
        markerListener = listener;
    }
}
