package charlyn23.c4q.nyc.projectx.shames;

import android.content.Context;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import charlyn23.c4q.nyc.projectx.R;

/**
 * Created by sufeizhao on 8/13/15.
 */
public class MaterialDialogs {


    public static void initialDialog(final Context context) {
        new MaterialDialog.Builder(context)
                .title("Report New Shame")
                .content(R.string.new_shame_type)
                .items(R.array.shame_types)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (text.equals("Verbal"))
                            createDialog(context, "verbal");
                        else if (text.equals("Physical"))
                            createDialog(context, "physical");
                        else
                            createDialog(context, "other");
                        return true;
                    }
                })
                .negativeText(R.string.cancel)
                .show();
    }

    public static void createDialog(final Context context, String type) {
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
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                        //TODO save verbal?
                        return true;
                    }
                })
                .negativeText(R.string.back)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        initialDialog(context);
                    }
                })
                .show();
    }



}
