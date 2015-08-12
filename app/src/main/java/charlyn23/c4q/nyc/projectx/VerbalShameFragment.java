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
public class VerbalShameFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        TextView verbalInquiry = (TextView)getView().findViewById(R.id.verbalInquiry);

        TextView verbalShameOne = (TextView)getView().findViewById(R.id.verbalShameOne);
        TextView verbalShameTwo = (TextView)getView().findViewById(R.id.verbalShameTwo);
        TextView verbalShameThree = (TextView)getView().findViewById(R.id.verbalShameThree);
        TextView verbalShameFour = (TextView)getView().findViewById(R.id.verbalShameFour);

        RadioButton verbalRadioOne = (RadioButton)getView().findViewById(R.id.verbalRadioOne);
        RadioButton verbalRadioTwo = (RadioButton)getView().findViewById(R.id.verbalRadioTwo);
        RadioButton verbalRadioThree = (RadioButton)getView().findViewById(R.id.verbalRadioThree);
        RadioButton verbalRadioFour = (RadioButton)getView().findViewById(R.id.verbalRadioFour);


        return inflater.inflate(R.layout.verbal_shame_fragment, container, false);
    }
}
