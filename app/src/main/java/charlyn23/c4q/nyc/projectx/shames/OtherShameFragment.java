package charlyn23.c4q.nyc.projectx.shames;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import charlyn23.c4q.nyc.projectx.R;

/**
 * Created by charlynbuchanan on 8/12/15.
 */
public class OtherShameFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        TextView otherInquiry = (TextView)getView().findViewById(R.id.otherInquiry);

        TextView otherShameOne = (TextView)getView().findViewById(R.id.otherShameOne);
        TextView otherShameTwo = (TextView)getView().findViewById(R.id.otherShameTwo);
        TextView otherShameThree = (TextView)getView().findViewById(R.id.otherShameThree);
        TextView otherShameFour = (TextView)getView().findViewById(R.id.otherShameFour);

        RadioButton otherRadioOne = (RadioButton)getView().findViewById(R.id.otherRadioOne);
        RadioButton otherRadioTwo = (RadioButton)getView().findViewById(R.id.otherRadioTwo);
        RadioButton otherRadioThree = (RadioButton)getView().findViewById(R.id.otherRadioThree);
        RadioButton otherRadioFour = (RadioButton)getView().findViewById(R.id.otherRadioFour);
        
        return inflater.inflate(R.layout.other_shame_fragement, container, false);
    }
}
