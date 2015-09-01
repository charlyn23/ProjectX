package charlyn23.c4q.nyc.projectx;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.parse.ParseUser;

import java.io.FileNotFoundException;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "c4q.nyc.projectx";
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int RC_SIGN_IN = 0;
    private static final String PROFILE_IMAGE = "profileImage";
    private static final String LOGGED_IN_GOOGLE = "isLoggedInGoogle";
    private static final String SHARED_PREFERENCE = "sharedPreference";
    private static final String LOGGED_IN = "isLoggedIn";
    private GoogleApiClient googleApiClient;
    private boolean isResolving = false;
    private boolean shouldResolve = false;
    private View view;
    private CircleImageView profileImage;
    private GoogleApiClient client;
    private boolean isLoggedIn_Google;
    private SharedPreferences preferences;

    public  ProfileFragment() {
    }

    public ProfileFragment(GoogleApiClient client) {
        this.client = client;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment, container, false);
        preferences = getActivity().getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        isLoggedIn_Google = preferences.getBoolean(LOGGED_IN_GOOGLE, false);
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


        googleApiClient = new GoogleApiClient.Builder(view.getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.EMAIL))
                .build();

        return view;
    }

    // sets the user's image as a profile picture
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == MainActivity.RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            profileImage.setImageURI(selectedImage);

            //starts an intentService to save the new picture in a file
            String imagePath = selectedImage.toString();
            Intent intent = new Intent(getActivity(), PictureService.class);
            intent.putExtra(PROFILE_IMAGE, imagePath);
            getActivity().startService(intent);
        }
    }

    // brings up the photo gallery and other resources to choose a picture
    private void changeProfileImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //sets and ImageView of the profile picture to the previously saved image
    private void setProfileImage() {
        Bitmap bm = PictureUtil.loadFromCacheFile();
        if (bm != null) {
            profileImage.setImageBitmap(bm);
        } else {
            //TODO: put default profile image
            profileImage.setImageResource(R.drawable.map);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected: " + bundle);
        shouldResolve = false;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: " + connectionResult);

        if (!isResolving && shouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(getActivity(), RC_SIGN_IN);
                    isResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    isResolving = false;
                    googleApiClient.connect();
                }
            } else {
                Toast.makeText(view.getContext(), getString(R.string.network_connection_problem), Toast.LENGTH_LONG).show();
            }
        }

    }

    View.OnClickListener logoutClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ParseUser user = ParseUser.getCurrentUser();
            user.logOut();
//
//            if (client.isConnected()) {
//                    client.connect();
//                    Plus.AccountApi.clearDefaultAccount(client);
//                    preferences.edit().putBoolean(LOGGED_IN_GOOGLE, false).apply();
//                    client.disconnect();
//
//            }

            if (googleApiClient.isConnected()) {
                Plus.AccountApi.clearDefaultAccount(googleApiClient);
                googleApiClient.disconnect();
            }

            SharedPreferences preferences = getActivity().getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(LOGGED_IN, false).apply();
            Toast.makeText(view.getContext(), getString(R.string.log_out_toast), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            startActivity(intent);
        }
    };

    //saves profile image in the background
    public static class PictureService extends IntentService{

        public PictureService() {
            super("pictureService");
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            String selectedImage = intent.getStringExtra(PROFILE_IMAGE);
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.parse(selectedImage)));
            } catch (FileNotFoundException e) {
                Log.d(TAG, "Image uri is not received or recognized");
            }
            PictureUtil.saveToCacheFile(bitmap);
        }
    }
}
