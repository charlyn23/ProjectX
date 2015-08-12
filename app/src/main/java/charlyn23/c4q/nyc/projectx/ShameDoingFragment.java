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
public class ShameDoingFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        TextView doingInquiry = (TextView)getView().findViewById(R.id.doingInquiry);

        TextView doingOne = (TextView)getView().findViewById(R.id.doingShameOne);
        TextView doingTwo = (TextView)getView().findViewById(R.id.doingShameTwo);
        TextView doingThree = (TextView)getView().findViewById(R.id.doingShameThree);
        TextView doingFour = (TextView)getView().findViewById(R.id.doingShameFour);
        TextView doingFive = (TextView)getView().findViewById(R.id.doingShameFive);
        TextView doingSix = (TextView)getView().findViewById(R.id.doingShameSix);


        RadioButton doingRadioOne = (RadioButton)getView().findViewById(R.id.doingRadioOne);
        RadioButton doingRadioTwo = (RadioButton)getView().findViewById(R.id.doingRadioTwo);
        RadioButton doingRadioThree = (RadioButton)getView().findViewById(R.id.doingRadioThree);
        RadioButton doingRadioFour = (RadioButton)getView().findViewById(R.id.doingRadioFour);
        RadioButton doingRadioFive = (RadioButton)getView().findViewById(R.id.doingRadioFive);
        RadioButton doingRadioSix = (RadioButton)getView().findViewById(R.id.doingRadioSix);

        return inflater.inflate(R.layout.shame_doing_fragment, container, false);
    }
}
