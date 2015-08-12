package charlyn23.c4q.nyc.projectx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.TextView;

import charlyn23.c4q.nyc.projectx.model.Shame;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ShameHandler shameHandler = new ShameHandler();


        TextView shameType = (TextView)findViewById(R.id.shameType);
        RadioButton radioOne = (RadioButton)findViewById(R.id.radioOne);
        RadioButton radioTwo = (RadioButton)findViewById(R.id.radioTwo);
        RadioButton radioThree = (RadioButton)findViewById(R.id.radioThree);
        TextView verbalShame = (TextView)findViewById(R.id.verbalShame);
        TextView physicalShame = (TextView)findViewById(R.id.physicalShame);
        TextView otherShame = (TextView)findViewById(R.id.otherShame);



    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
