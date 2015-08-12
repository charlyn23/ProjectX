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
public class ShameFeelFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        TextView shameFeelInquiry = (TextView)getView().findViewById(R.id.shameFeelInquiry);

        TextView shameFeelOne = (TextView)getView().findViewById(R.id.shameFeelOne);
        TextView shameFeelTwo = (TextView)getView().findViewById(R.id.shameFeelTwo);
        TextView shameFeelThree = (TextView)getView().findViewById(R.id.shameFeelThree);
        TextView shameFeelFour = (TextView)getView().findViewById(R.id.shameFeelFour);
        TextView shameFeelFive = (TextView)getView().findViewById(R.id.shameFeelFive);

        RadioButton shameFeelRadioOne = (RadioButton)getView().findViewById(R.id.shameFeelRadioOne);
        RadioButton shameFeelRadioTwo = (RadioButton)getView().findViewById(R.id.shameFeelRadioTwo);
        RadioButton shameFeelRadioThree = (RadioButton)getView().findViewById(R.id.shameFeelRadioThree);
        RadioButton shameFeelRadioFour = (RadioButton)getView().findViewById(R.id.shameFeelRadioFour);
        RadioButton shameFeelRadioFive = (RadioButton)getView().findViewById(R.id.shameFeelRadioFive);

        return inflater.inflate(R.layout.shame_feel_fragment, container, false);
    }
}
