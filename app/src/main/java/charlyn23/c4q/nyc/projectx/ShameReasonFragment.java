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
public class ShameReasonFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        TextView shameReasonInquiry = (TextView)getView().findViewById(R.id.shameReasonInquiry);

        TextView shameReasonOne = (TextView)getView().findViewById(R.id.shameReasonOne);
        TextView shameReasonTwo = (TextView)getView().findViewById(R.id.shameReasonTwo);
        TextView shameReasonThree = (TextView)getView().findViewById(R.id.shameReasonThree);
        TextView shameReasonFour = (TextView)getView().findViewById(R.id.shameReasonFour);

        RadioButton shameReasonRadioOne = (RadioButton)getView().findViewById(R.id.shameReasonRadioOne);
        RadioButton shameReasonRadioTwo = (RadioButton)getView().findViewById(R.id.shameReasonRadioTwo);
        RadioButton shameReasonRadioThree = (RadioButton)getView().findViewById(R.id.shameReasonRadioThree);
        RadioButton shameReasonRadioFour = (RadioButton)getView().findViewById(R.id.shameReasonRadioFour);

        
        
        return inflater.inflate(R.layout.shame_reason_fragment, container, false);
    }
}
