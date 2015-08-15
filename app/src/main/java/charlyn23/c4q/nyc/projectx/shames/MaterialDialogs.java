package charlyn23.c4q.nyc.projectx.shames;

import android.content.Context;
import android.view.View;
import android.widget.DatePicker;

import com.afollestad.materialdialogs.MaterialDialog;

import charlyn23.c4q.nyc.projectx.R;

/**
 * Created by sufeizhao on 8/13/15.
 */
public class MaterialDialogs {


    public static void initialDialog(final Context context) {
        final CharSequence[] type = new CharSequence[1];
        new MaterialDialog.Builder(context)
                .title("Report New Shame")
                .content(R.string.new_shame_type)
                .items(R.array.shame_types)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        type[0] = text;
                        return true;
                    }
                })
                .positiveText(R.string.next)
                .negativeText(R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        if (type[0].equals("Verbal"))
                            shameTypeDialog(context, "verbal");
                        else if (type[0].equals("Physical"))
                            shameTypeDialog(context, "physical");
                        else
                            shameTypeDialog(context, "other");
                    }
                })
                .show();
    }

    public static void shameTypeDialog(final Context context, final String type) {
        int content, items;
        final String[] type_choice = new String[1];
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
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        type_choice[0] = text.toString();
                        return true;
                    }
                })
                .positiveText(R.string.next)
                .negativeText(R.string.back)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        feelDialog(context, type, type_choice[0]);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        initialDialog(context);
                    }
                })
                .show();
    }

    public static void feelDialog(final Context context, final String type, final String type_choice) {
        final String[] feel = new String[1];
        new MaterialDialog.Builder(context)
                .title("Report New Shame")
                .content(R.string.shame_feel)
                .items(R.array.feel_types)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        feel[0] = text.toString();
                        return true;
                    }
                })
                .positiveText(R.string.next)
                .negativeText(R.string.back)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        doingDialog(context, type, type_choice, feel[0]);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        shameTypeDialog(context, type);
                    }
                })
                .show();
    }

    public static void doingDialog(final Context context, final String type, final String type_choice, final String feel) {
        final String[] doing = new String[1];
        new MaterialDialog.Builder(context)
                .title("Report New Shame")
                .content(R.string.shame_doing)
                .items(R.array.doing_types)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int when, CharSequence text) {
                        doing[0] = text.toString();
                        return true;
                    }
                })
                .positiveText(R.string.next)
                .negativeText(R.string.back)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        whenDialog(context, type, type_choice, feel, doing[0]);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        feelDialog(context, type, type_choice);
                    }
                })
                .show();
    }

    public static void whenDialog(final Context context, final String type, final String type_choice, final String feel, final String doing) {
        new MaterialDialog.Builder(context)
                .title("Report New Shame")
                .customView(R.layout.time_picker, true)
                .positiveText(R.string.next)
                .negativeText(R.string.back)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        //TODO save time?
                        whyDialog(context, type, type_choice, feel, doing);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        doingDialog(context, type, type_choice, feel);
                    }
                })
                .show();
    }


    public static void whyDialog(final Context context, final String type, final String type_choice, final String feel, final String doing) {
        final String[] why = new String[1];
        new MaterialDialog.Builder(context)
                .title("Report New Shame")
                .content(R.string.shame_why)
                .items(R.array.why_types)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        why[0] = text.toString();
                        return true;
                    }
                })
                .positiveText(R.string.submit)
                .negativeText(R.string.back)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        //TODO submit shame
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        whenDialog(context, type, type_choice, feel, doing);
                    }
                })
                .show();
    }



}
