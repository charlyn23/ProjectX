package charlyn23.c4q.nyc.projectx.shames;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import charlyn23.c4q.nyc.projectx.R;

/**
 * Created by charlynbuchanan on 8/12/15.
 */
public class ShameTimeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        TextView shameTimeInquiry = (TextView)getView().findViewById(R.id.shameTimeInquiry);
        TimePicker shameTimePicker = (TimePicker)getView().findViewById(R.id.shameTimePicker);


        return inflater.inflate(R.layout.shame_time_fragment, container, false);
    }
}
