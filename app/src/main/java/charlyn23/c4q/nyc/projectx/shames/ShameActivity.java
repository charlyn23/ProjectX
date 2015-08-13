package charlyn23.c4q.nyc.projectx.shames;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.TextView;

import charlyn23.c4q.nyc.projectx.R;

/**
 * Created by sufeizhao on 8/11/15.
 */
public class ShameActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.new_shame_layout);

        TextView shameType = (TextView)findViewById(R.id.shameType);
        RadioButton radioTwo = (RadioButton)findViewById(R.id.radioTwo);
        TextView verbalShame = (TextView)findViewById(R.id.verbalShame);
        TextView physicalShame = (TextView)findViewById(R.id.physicalShame);
        TextView otherShame = (TextView)findViewById(R.id.otherShame);
    }
}
