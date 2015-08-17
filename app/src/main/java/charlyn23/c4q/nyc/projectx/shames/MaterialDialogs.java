package charlyn23.c4q.nyc.projectx.shames;

import android.content.Context;
import android.view.View;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.parse.ParseObject;
import charlyn23.c4q.nyc.projectx.R;


public class  MaterialDialogs {

    private int shameType;
    private int verbalShame;
    private int physicalShame;
    private int otherShame;
    private int shameFeel;
    private int shameDoing;
    private int shameReason;
    private double latitude;
    private double longitude;
    private Shame newShame;

    public void initialDialog(final Context context, double latitude, double longitude) {
        ParseObject.registerSubclass(Shame.class);
        this.latitude = latitude;
        this.longitude = longitude;

        new MaterialDialog.Builder(context)
                .title("Report New Shame")
                .content(R.string.new_shame_type)
                .items(R.array.shame_types)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {

                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (text.equals("Verbal")) {
                            shameTypeDialog(context, "verbal");
                            shameType = 1;
                        }
                        else if (text.equals("Physical")) {
                            shameTypeDialog(context, "physical");
                            shameType = 2;

                        }
                        else {
                            shameTypeDialog(context, "other");
                            shameType = 3;

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
                        dialog.cancel();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    public void shameTypeDialog(final Context context, final String type) {
        int content, items;

        switch (type) {
            case "verbal":
                content = R.string.verbal_shame;
                items = R.array.verbal_types;
                break;
            case "physical":
                content = R.string.physical_shame;
                items = R.array.physical_types;
                break;
            case "other":
                content = R.string.other_shame;
                items = R.array.other_types;
                break;
            default:
                content = R.string.other_shame;
                items = R.array.other_types;
                break;
        }

        new MaterialDialog.Builder(context)
                .title("Report New Shame")
                .content(content)
                .items(items)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence type_choice) {
                        feelDialog(context, type, type_choice.toString());
                        if (type.equals("verbal") && which == 0){
                            verbalShame = 1;
                        }
                        else if (type.equals("verbal") && which == 1) {
                            verbalShame = 2;
                        }
                        else if (type.equals("verbal") && which == 2) {
                            verbalShame = 3;
                        }
                        else if (type.equals("verbal") && which == 3) {
                            verbalShame = 4;
                        }
                        else if (type.equals("physical") && which == 0) {
                            physicalShame = 1;
                        }
                        else if (type.equals("physical") && which == 1) {
                            physicalShame = 2;
                        }
                        else if (type.equals("physical") && which == 2) {
                            physicalShame = 3;
                        }
                        else if (type.equals("physical") && which == 3) {
                            physicalShame = 4;
                        }
                        else if (type.equals("physical") && which == 4) {
                            physicalShame = 5;
                        }
                        else if (type.equals("physical") && which == 5) {
                            physicalShame = 6;
                        }
                        else if (type.equals("other") && which == 0){
                            otherShame = 1;
                        }
                        else if (type.equals("other") && which == 1){
                            otherShame = 2;
                        }
                        else if (type.equals("other") && which == 2){
                            otherShame = 3;
                        }
                        else if (type.equals("other") && which == 3){
                            otherShame = 4;
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
                        dialog.cancel();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        initialDialog(context, latitude, longitude);
                        dialog.cancel();
                    }
                })
                .show();
    }

    public void feelDialog(final Context context, final String type, final String type_choice) {
        new MaterialDialog.Builder(context)
                .title("Report New Shame")
                .content(R.string.shame_feel)
                .items(R.array.feel_types)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence feel) {
                        doingDialog(context, type, type_choice, feel.toString());
                        if (which == 0) {
                            shameFeel = 1;
                        } else if (which == 1){
                            shameFeel = 2;
                        } else if (which == 2) {
                            shameFeel = 3;
                        }else if (which == 3) {
                            shameFeel = 4;
                        }else if (which == 4) {
                            shameFeel = 5;
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
                .title("Report New Shame")
                .content(R.string.shame_doing)
                .items(R.array.doing_types)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence doing) {
                        whenDialog(context, type, type_choice, feel, doing.toString());
                        if (which == 0) {
                            shameDoing = 1;
                        }if (which == 1) {
                            shameDoing = 2;
                        }if (which == 2) {
                            shameDoing = 3;
                        }if (which == 3) {
                            shameDoing = 4;
                        }if (which == 4) {
                            shameDoing = 5;
                        }if (which == 5) {
                            shameDoing = 6;
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
                .title("Report New Shame")
                .customView(R.layout.time_picker, true)
                .autoDismiss(false)
                .positiveText(R.string.next)
                .negativeText(R.string.back)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        //TODO save time?
                        whyDialog(context, type, type_choice, feel, doing);
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


    public void whyDialog(final Context context, final String type, final String type_choice, final String feel, final String doing) {
        new MaterialDialog.Builder(context)
                .title("Report New Shame")
                .content(R.string.shame_why)
                .items(R.array.why_types)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {

                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence why) {
                        //TODO submit shame to parse
                        if (which == 0) {
                            shameReason = 1;
                        }
                        if (which == 1) {
                            shameReason = 2;
                        }
                        if (which == 2) {
                            shameReason = 3;
                        }
                        if (which == 3) {
                            shameReason = 4;
                        }

                        newShame = new Shame();
                        newShame.put("latitude", latitude);
                        newShame.put("longitude", longitude);
                        switch(shameType) {
                            case 1:
                                newShame.put("shameType", 1);
                                newShame.saveInBackground();
                                if (verbalShame == 1) {
                                    newShame.put("verbalShame", 1);
                                    newShame.saveInBackground();
                                } else if (verbalShame == 2) {
                                    newShame.put("verbalShame", 2);
                                    newShame.saveInBackground();
                                } else if (verbalShame == 3) {
                                    newShame.put("verbalShame", 3);
                                    newShame.saveInBackground();
                                } else {
                                    newShame.put("verbalShame", 4);
                                    newShame.saveInBackground();
                                }
                                break;
                            case 2:
                                newShame.put("shameType", 2);
                                newShame.saveInBackground();
                                if (physicalShame == 1) {
                                    newShame.put("physicalShame", 1);
                                } else if (physicalShame == 2) {
                                    newShame.put("physicalShame", 2);
                                    newShame.saveInBackground();
                                } else if (physicalShame == 3) {
                                    newShame.put("physicalShame", 3);
                                    newShame.saveInBackground();
                                } else if (physicalShame == 4) {
                                    newShame.put("physicalShame", 4);
                                    newShame.saveInBackground();
                                } else if (physicalShame == 5) {
                                    newShame.put("physicalShame", 5);
                                    newShame.saveInBackground();
                                } else {
                                    newShame.put("physicalShame", 6);
                                    newShame.saveInBackground();
                                }
                                break;
                            case 3:
                                newShame.put("shameType", 3);
                                newShame.saveInBackground();
                                if (otherShame == 1) {
                                    newShame.put("otherShame", 1);
                                    newShame.saveInBackground();
                                } else if (otherShame == 2) {
                                    newShame.put("otherShame", 2);
                                    newShame.saveInBackground();
                                } else if (otherShame == 3) {
                                    newShame.put("otherShame", 3);
                                    newShame.saveInBackground();
                                } else {
                                    newShame.put("otherShame", 4);
                                    newShame.saveInBackground();
                                }
                                break;
                            }
                        switch (shameFeel) {
                            case 1:
                                newShame.put("shameFeel", 1);
                                newShame.saveInBackground();
                                break;
                            case 2:
                                newShame.put("shameFeel", 2);
                                newShame.saveInBackground();
                                break;
                            case 3:
                                newShame.put("shameFeel", 3);
                                newShame.saveInBackground();
                                break;
                            case 4:
                                newShame.put("shameFeel", 4);
                                newShame.saveInBackground();
                                break;
                            case 5:
                                newShame.put("shameFeel", 5);
                                newShame.saveInBackground();
                                break;
                        }

                        switch (shameDoing) {
                            case 1:
                                newShame.put("shameDoing", 1);
                                newShame.saveInBackground();
                                break;
                            case 2:
                                newShame.put("shameDoing", 2);
                                newShame.saveInBackground();
                                break;
                            case 3:
                                newShame.put("shameDoing", 3);
                                newShame.saveInBackground();
                                break;
                            case 4:
                                newShame.put("shameDoing", 4);
                                newShame.saveInBackground();
                                break;
                            case 5:
                                newShame.put("shameDoing", 5);
                                newShame.saveInBackground();
                                break;
                            case 6:
                                newShame.put("shameDoing", 1);
                                newShame.saveInBackground();
                                break;
                        }

                        switch (shameReason) {
                            case 1:
                                newShame.put("shameReason", 1);
                                newShame.saveInBackground();
                                break;
                            case 2:
                                newShame.put("shameReason", 2);
                                newShame.saveInBackground();
                                break;
                            case 3:
                                newShame.put("shameReason", 3);
                                newShame.saveInBackground();
                                break;
                            case 4:
                                newShame.put("shameReason", 4);
                                newShame.saveInBackground();
                                break;
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
                        dialog.cancel();
                        Toast.makeText(context, "Shame successfully submitted!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        whenDialog(context, type, type_choice, feel, doing);
                        dialog.cancel();
                    }
                })
                .show();
    }
}
