package charlyn23.c4q.nyc.projectx;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by charlynbuchanan on 8/12/15.
 */
public class NewShameLayout extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        TextView shameType = (TextView)getView().findViewById(R.id.shameType);
        TextView verbalShame = (TextView)getView().findViewById(R.id.verbalShame);
        TextView otherShame = (TextView)getView().findViewById(R.id.otherShame);
        RadioButton verbalRadio = (RadioButton)getView().findViewById(R.id.verbalRadio);
        RadioButton otherRadio = (RadioButton)getView().findViewById(R.id.otherRadio);
        RadioButton physicalRadio = (RadioButton)getView().findViewById(R.id.physicalRadio);



        return inflater.inflate(R.layout.new_shame_layout, container, false);

    }
}
