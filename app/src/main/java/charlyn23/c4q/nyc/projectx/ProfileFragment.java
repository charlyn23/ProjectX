package charlyn23.c4q.nyc.projectx;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.parse.ParseUser;
import java.io.FileNotFoundException;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private View view;
    private CircleImageView profileImage;
    private GoogleApiClient googleLogInClient;
    private CheckBox allow_geofence;
    private boolean isLoggedIn_Google, geofenceEnabled;
    private int year;

    public ProfileFragment(GoogleApiClient googleLogInClient) {
        this.googleLogInClient = googleLogInClient;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment, container, false);

        final SharedPreferences preferences = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE);
        isLoggedIn_Google = preferences.getBoolean(Constants.LOGGED_IN_GOOGLE, false);
        geofenceEnabled = preferences.getBoolean(Constants.ALLOW_GEOFENCE, false);

        profileImage = (CircleImageView) view.findViewById(R.id.profile_image);
        setProfileImage();
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeProfileImage();
            }
        });

        ListView shame_list = (ListView) view.findViewById(R.id.shame_list);
        // IF list == 0, print "You haven't submitted any shames yet!"

        Button logout = (Button) view.findViewById(R.id.log_out);
        logout.setOnClickListener(logoutClick);

        // set custom font
        Typeface questrial = Typeface.createFromAsset(getActivity().getAssets(), "questrial.ttf");
        TextView profile = (TextView) view.findViewById(R.id.profile_text);
        ToggleButton man = (ToggleButton) view.findViewById(R.id.man);
        ToggleButton woman = (ToggleButton) view.findViewById(R.id.woman);
        ToggleButton lesbian = (ToggleButton) view.findViewById(R.id.lesbian);
        ToggleButton poc = (ToggleButton) view.findViewById(R.id.poc);
        ToggleButton gay = (ToggleButton) view.findViewById(R.id.gay);
        ToggleButton trans = (ToggleButton) view.findViewById(R.id.trans);
        ToggleButton bisexual = (ToggleButton) view.findViewById(R.id.bisexual);
        ToggleButton minor = (ToggleButton) view.findViewById(R.id.minor);
        ToggleButton queer = (ToggleButton) view.findViewById(R.id.queer);
        EditText age = (EditText) view.findViewById(R.id.year);
        allow_geofence = (CheckBox) view.findViewById(R.id.enable_geofence);
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
        logout.setTypeface(questrial);
        allow_geofence.setTypeface(questrial);

        allow_geofence.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    geofenceEnabled = true;
                    preferences.edit().putBoolean(Constants.ALLOW_GEOFENCE, true).apply();
                } else {
                    geofenceEnabled = true;
                    preferences.edit().putBoolean(Constants.ALLOW_GEOFENCE, true).apply();
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
            //TODO: put default profile image
            profileImage.setImageResource(R.drawable.logo);
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
            Toast.makeText(view.getContext(), getString(R.string.log_out_toast), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            startActivity(intent);
        }
    };


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
