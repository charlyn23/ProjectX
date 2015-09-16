package charlyn23.c4q.nyc.projectx;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.parse.ParseUser;
import com.squareup.leakcanary.RefWatcher;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private View view;
    private CircleImageView profileImage;
    private SharedPreferences preferences;
    private GoogleApiClient googleLogInClient;
    private CheckBox allow_geofence;
    private TextView profile;
    private ToggleButton man, woman, lesbian, poc, gay, trans, bisexual, minor, queer, other;
    private EditText age;
    private Button logout;
    private boolean isLoggedIn_Google, geofenceEnabled, isConnected;


    public ProfileFragment(GoogleApiClient googleLogInClient) {
        this.googleLogInClient = googleLogInClient;
    }

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment, container, false);
        initializeViews();
        setCustomFont();
        logout.setOnClickListener(logoutClick);

        // get preference
        preferences = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE);
        isLoggedIn_Google = preferences.getBoolean(Constants.LOGGED_IN_GOOGLE, false);
        geofenceEnabled = preferences.getBoolean(Constants.ALLOW_GEOFENCE, false);
        isConnected = preferences.getBoolean(Constants.IS_CONNECTED, false);
        setUpToggleButtons();
        allow_geofence.setChecked(geofenceEnabled);
        allow_geofence.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                geofenceEnabled = isChecked;
                preferences.edit().putBoolean(Constants.ALLOW_GEOFENCE, isChecked).apply();
            }
        });

        // set profile image
        setProfileImage();
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeProfileImage();
            }
        });

        // set age
        int year = preferences.getInt(Constants.YEAR, 0);
        if (year != 0)
            age.setText(String.valueOf(year));
        age.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Calendar cal = Calendar.getInstance();
                    if (age.getText().toString().equals("")) ;
                    else if (Integer.valueOf(age.getText().toString()) < 1900 ||
                            Integer.valueOf(age.getText().toString()) > cal.get(Calendar.YEAR)) {
                        YoYo.with(Techniques.Shake).playOn(age);
                        age.setTextColor(Color.RED);
                        age.setError("Invalid Birth Year", getResources().getDrawable(R.drawable.what));
                    } else {
                        preferences.edit().putInt(Constants.YEAR, Integer.valueOf(age.getText().toString())).apply();
                        age.setTextColor(Color.BLACK);
                    }
                }
            }
        });

        return view;
    }

    // sets the user's image as a profile picture
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.PICK_IMAGE_REQUEST && resultCode == MainActivity.RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            profileImage.setImageURI(selectedImage);

            //starts an intentService to save the new picture in a file
            String imagePath = selectedImage.toString();
            Intent intent = new Intent(getActivity(), PictureService.class);
            intent.putExtra(Constants.PROFILE_IMAGE, imagePath);
            getActivity().startService(intent);
        }
    }

    // brings up the photo gallery and other resources to choose a picture
    private void changeProfileImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.PICK_IMAGE_REQUEST);
    }

    //sets and ImageView of the profile picture to the previously saved image
    private void setProfileImage() {
        Bitmap bm = PictureUtil.loadFromCacheFile();
        if (bm != null) {
            profileImage.setImageBitmap(bm);
        } else {
            profileImage.setImageResource(R.drawable.logo_large);
        }
    }

    //saves profile image in the background
    public static class PictureService extends IntentService {

        public PictureService() {
            super("pictureService");
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            String selectedImage = intent.getStringExtra(Constants.PROFILE_IMAGE);
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.parse(selectedImage)));
            } catch (FileNotFoundException e) {
                Log.d(Constants.TAG, "Image uri is not received or recognized");
            }
            PictureUtil.saveToCacheFile(bitmap);
        }
    }

    View.OnClickListener logoutClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ParseUser user = ParseUser.getCurrentUser();
            user.logOut();

            if (googleLogInClient.isConnected()) {
                Log.d("ProfileFragment", "Google Client log out");
                Plus.AccountApi.clearDefaultAccount(googleLogInClient);
                Plus.AccountApi.revokeAccessAndDisconnect(googleLogInClient);
                googleLogInClient.disconnect();
            }

            SharedPreferences preferences = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(Constants.LOGGED_IN, false).apply();
            editor.putBoolean(Constants.LOGGED_IN_GOOGLE, false).apply();
            editor.putBoolean(Constants.IS_DROPPED, false).apply();
            Toast.makeText(view.getContext(), getString(R.string.log_out_toast), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            startActivity(intent);
        }
    };

    public void initializeViews() {
        profile = (TextView) view.findViewById(R.id.profile_text);
        man = (ToggleButton) view.findViewById(R.id.man);
        woman = (ToggleButton) view.findViewById(R.id.woman);
        lesbian = (ToggleButton) view.findViewById(R.id.lesbian);
        poc = (ToggleButton) view.findViewById(R.id.poc);
        gay = (ToggleButton) view.findViewById(R.id.gay);
        trans = (ToggleButton) view.findViewById(R.id.trans);
        bisexual = (ToggleButton) view.findViewById(R.id.bisexual);
        minor = (ToggleButton) view.findViewById(R.id.minor);
        queer = (ToggleButton) view.findViewById(R.id.queer);
        other = (ToggleButton) view.findViewById(R.id.other);
        age = (EditText) view.findViewById(R.id.year);
        allow_geofence = (CheckBox) view.findViewById(R.id.enable_geofence);
        logout = (Button) view.findViewById(R.id.log_out);
        profileImage = (CircleImageView) view.findViewById(R.id.profile_image);
    }

    public void setCustomFont() {
        Typeface questrial = Typeface.createFromAsset(getActivity().getAssets(), "questrial.ttf");
        profile.setTypeface(questrial);
        age.setTypeface(questrial);
        man.setTypeface(questrial);
        woman.setTypeface(questrial);
        lesbian.setTypeface(questrial);
        poc.setTypeface(questrial);
        gay.setTypeface(questrial);
        trans.setTypeface(questrial);
        bisexual.setTypeface(questrial);
        minor.setTypeface(questrial);
        queer.setTypeface(questrial);
        other.setTypeface(questrial);
        logout.setTypeface(questrial);
        allow_geofence.setTypeface(questrial);
    }

    public void setUpToggleButtons() {
        man.setChecked(preferences.getBoolean(Constants.MAN, false));
        woman.setChecked(preferences.getBoolean(Constants.WOMAN, false));
        lesbian.setChecked(preferences.getBoolean(Constants.LESBIAN, false));
        poc.setChecked(preferences.getBoolean(Constants.POC, false));
        trans.setChecked(preferences.getBoolean(Constants.TRANS, false));
        gay.setChecked(preferences.getBoolean(Constants.GAY, false));
        bisexual.setChecked(preferences.getBoolean(Constants.BISEXUAL, false));
        minor.setChecked(preferences.getBoolean(Constants.MINOR, false));
        queer.setChecked(preferences.getBoolean(Constants.QUEER, false));
        other.setChecked(preferences.getBoolean(Constants.OTHER, false));
        man.setOnCheckedChangeListener(new toggleListener(Constants.MAN));
        woman.setOnCheckedChangeListener(new toggleListener(Constants.WOMAN));
        lesbian.setOnCheckedChangeListener(new toggleListener(Constants.LESBIAN));
        poc.setOnCheckedChangeListener(new toggleListener(Constants.POC));
        trans.setOnCheckedChangeListener(new toggleListener(Constants.TRANS));
        gay.setOnCheckedChangeListener(new toggleListener(Constants.GAY));
        bisexual.setOnCheckedChangeListener(new toggleListener(Constants.BISEXUAL));
        minor.setOnCheckedChangeListener(new toggleListener(Constants.MINOR));
        queer.setOnCheckedChangeListener(new toggleListener(Constants.QUEER));
        other.setOnCheckedChangeListener(new toggleListener(Constants.OTHER));
    }

    public class toggleListener implements CompoundButton.OnCheckedChangeListener {
        String button;

        public toggleListener(String button) {
            this.button = button;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            preferences.edit().putBoolean(button, isChecked).apply();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isLoggedIn_Google) {
            googleLogInClient.connect();
            Log.d("ProfileFragment", "Connected onStart");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleLogInClient.isConnected()) {
            googleLogInClient.disconnect();
            Log.d("ProfileFragment", "Disconnected onStop");
        }
    }
}
