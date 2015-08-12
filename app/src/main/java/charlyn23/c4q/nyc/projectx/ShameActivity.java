package charlyn23.c4q.nyc.projectx;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by sufeizhao on 8/11/15.
 */
public class ShameActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.new_shame_layout);

        ShameHandler shameHandler = new ShameHandler();


        TextView shameType = (TextView)findViewById(R.id.shameType);
        RadioButton radioOne = (RadioButton)findViewById(R.id.radioOne);
        RadioButton radioTwo = (RadioButton)findViewById(R.id.radioTwo);
        RadioButton radioThree = (RadioButton)findViewById(R.id.radioThree);
        TextView verbalShame = (TextView)findViewById(R.id.verbalShame);
        TextView physicalShame = (TextView)findViewById(R.id.physicalShame);
        TextView otherShame = (TextView)findViewById(R.id.otherShame);
    }
}
