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
public class PhysicalShameFragment extends Fragment {
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        TextView physicalInquiry = (TextView)getView().findViewById(R.id.physicalInquiry);

        TextView physicalShameOne = (TextView)getView().findViewById(R.id.physicalShameOne);
        TextView physicalShameTwo = (TextView)getView().findViewById(R.id.physicalShameTwo);
        TextView physicalShameThree = (TextView)getView().findViewById(R.id.physicalShameThree);
        TextView physicalShameFour = (TextView)getView().findViewById(R.id.physicalShameFour);

        RadioButton physicalRadioOne = (RadioButton)getView().findViewById(R.id.physicalRadioOne);
        RadioButton physicalRadioTwo = (RadioButton)getView().findViewById(R.id.physicalRadioTwo);
        RadioButton physicalRadioThree = (RadioButton)getView().findViewById(R.id.physicalRadioThree);
        RadioButton physicalRadioFour = (RadioButton)getView().findViewById(R.id.physicalRadioFour);




        return inflater.inflate(R.layout.physical_shame_fragment, container, false);

    }
}
