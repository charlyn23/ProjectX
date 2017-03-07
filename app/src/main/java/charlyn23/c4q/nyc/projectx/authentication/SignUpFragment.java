package charlyn23.c4q.nyc.projectx.authentication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import charlyn23.c4q.nyc.projectx.Constants;
import charlyn23.c4q.nyc.projectx.MainActivity;
import charlyn23.c4q.nyc.projectx.R;
import charlyn23.c4q.nyc.projectx.User;
import io.realm.Realm;
import io.realm.SyncCredentials;
import io.realm.SyncUser;
import twitter4j.Twitter;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;


public class SignUpFragment extends Fragment implements View.OnClickListener {
    private SharedPreferences.Editor editor;
    public GoogleApiClient googleLogInClient;
    private View view;
    public SharedPreferences preferences;
    public LoginButton fbLoginButton;
    public CallbackManager callbackManager;

    private static SharedPreferences mSharedPreferences;
    private static Twitter twitter;
    private static RequestToken requestToken;
    private AccessToken accessToken;
    public SignUpFragment.UserOnDataPass userDataPasser;


    public User newUser;
    public static String fbId;
    public static String fbFirstName;

    /*
     * Each shameObject must have a userID associated with it. This interface will allow user data to be
    * passed from the SignupFragment to the MainActivity. This data will be used in the dialog box sequence
    * to ultimately create the shameObject.
    */
    public interface UserOnDataPass {
        void passUserData();
        User getUserData();

    }
    public SignUpFragment() {
        this.googleLogInClient = new GoogleApiClient() {
            @Override
            public boolean hasConnectedApi(@NonNull Api<?> api) {
                return false;
            }

            @NonNull
            @Override
            public ConnectionResult getConnectionResult(@NonNull Api<?> api) {
                return null;
            }

            @Override
            public void connect() {

            }

            @Override
            public ConnectionResult blockingConnect() {
                return null;
            }

            @Override
            public ConnectionResult blockingConnect(long l, @NonNull TimeUnit timeUnit) {
                return null;
            }

            @Override
            public void disconnect() {

            }

            @Override
            public void reconnect() {

            }

            @Override
            public PendingResult<Status> clearDefaultAccountAndReconnect() {
                return null;
            }

            @Override
            public void stopAutoManage(@NonNull FragmentActivity fragmentActivity) {

            }

            @Override
            public boolean isConnected() {
                return false;
            }

            @Override
            public boolean isConnecting() {
                return false;
            }

            @Override
            public void registerConnectionCallbacks(@NonNull ConnectionCallbacks connectionCallbacks) {

            }

            @Override
            public boolean isConnectionCallbacksRegistered(@NonNull ConnectionCallbacks connectionCallbacks) {
                return false;
            }

            @Override
            public void unregisterConnectionCallbacks(@NonNull ConnectionCallbacks connectionCallbacks) {

            }

            @Override
            public void registerConnectionFailedListener(@NonNull OnConnectionFailedListener onConnectionFailedListener) {

            }

            @Override
            public boolean isConnectionFailedListenerRegistered(@NonNull OnConnectionFailedListener onConnectionFailedListener) {
                return false;
            }

            @Override
            public void unregisterConnectionFailedListener(@NonNull OnConnectionFailedListener onConnectionFailedListener) {

            }

            @Override
            public void dump(String s, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strings) {

            }
        };
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.signup_fragment, container, false);
        preferences = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE);

        //initializes views and OnClickListeners
        fbLoginButton = (LoginButton)view.findViewById(R.id.login_button);
        fbLoginButton.setFragment(this);
        fbLoginButton.setReadPermissions("email");
        fbLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInViaFacebook();
            }
        });
        ImageButton twitterButton = (ImageButton) view.findViewById(R.id.twitter_button);
        ImageButton google = (ImageButton) view.findViewById(R.id.googleplus_button);

        twitterButton.setOnClickListener(this);
        google.setOnClickListener(this);

        FacebookSdk.sdkInitialize(view.getContext());
        AppEventsLogger.activateApp(view.getContext());

        //sets custom font
        Typeface questrial = Typeface.createFromAsset(getActivity().getAssets(), "questrial.ttf");
        TextView blazon = (TextView) view.findViewById(R.id.blazon);
        blazon.setTypeface(questrial);

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            userDataPasser = (SignUpFragment.UserOnDataPass) context;

        }
        catch (ClassCastException e) {
            throw new ClassCastException(context.toString()  + " must implement UserOnDataPass");
        }

    }

    private void logInViaFacebook() {
        callbackManager = CallbackManager.Factory.create();

        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        System.out.println("Success");

                        GraphRequest.newMeRequest(
                                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject json, GraphResponse response) {
                                        if (response.getError() != null) {
                                            // handle error
                                            System.out.println("ERROR");
                                        } else {
                                            System.out.println("Success");
                                            try {

                                                String jsonresult = String.valueOf(json);
                                                Log.i("JSON Result", jsonresult);

                                                fbId = json.getString("id");
                                                fbFirstName = json.getString("first_name");
                                                Log.i("FBLogin" , fbFirstName + " fbID: " + fbId );

                                                //Create new User via FB authentication, and sync it
                                                String token = fbId;
                                                SyncCredentials myCredentials = SyncCredentials.facebook(token);
                                                Realm realm = Realm.getDefaultInstance();
                                                realm.beginTransaction();
                                                newUser = realm.createObject(User.class);
                                                newUser.setId(fbId);
                                                newUser.setName(fbFirstName);

                                                SyncUser user = SyncUser.login(myCredentials, Constants.AUTH_URL);
                                                user.isValid();

                                                realm.commitTransaction();
                                                Log.i("User", fbFirstName + " " + fbId);


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                }).executeAsync();
                        //Save POJO user data in in SharedPreferences
                        editor = preferences.edit();
                        editor.putString(Constants.USER_ID, fbId);
                        editor.putString(Constants.USER_NAME, fbFirstName);
                        editor.putBoolean(Constants.LOGGED_IN, true);
                        editor.commit();

                        //Add user data to intent, then send data back to MainActivity to begin dialog box sequence.
                        Intent intent = new Intent(view.getContext(), MainActivity.class);
                        intent.putExtra(Constants.SHOW_DIALOG, true);
                        intent.putExtra(Constants.USER_ID, fbId);
                        intent.putExtra(Constants.USER_NAME, fbFirstName);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancel() {
                        Log.d("Cancel", "On cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("Error", error.toString());
                    }
                });
    }

    private void logInViaTwitter(){
//        if (!isTwitterLoggedInAlready()) {
//            Uri.Builder builder = new Uri.Builder();
//            Uri uri = builder.build();
//
//            if (uri != null && uri.toString().startsWith(TwitterConstants.CALLBACKURL)) {
//                // oAuth verifier
//                final String verifier = uri
//                        .getQueryParameter(TwitterConstants.URL_TWITTER_OAUTH_VERIFIER);
//
//                try {
//
//                    Thread thread = new Thread(new Runnable(){
//                        @Override
//                        public void run() {
//                            try {
//
//                                // Get the access token
//                                accessToken = twitter.getOAuthAccessToken(
//                                        requestToken, verifier);
//                                Log.v("accessToken", accessToken.getToken());
//                                // Shared Preferences
//                                mSharedPreferences = getApplicationContext().getSharedPreferences(
//                                        "twitter4j-sample", 0);
//                                SharedPreferences.Editor e = mSharedPreferences.edit();
//
//
//                                // After getting access token, access token secret
//                                // store them in application preferences
//                                e.putString(TwitterConstants.PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
//                                e.putString(TwitterConstants.PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
//                                // Store login status - true
//                                e.putBoolean(TwitterConstants.PREF_KEY_TWITTER_LOGIN, true);
//                                e.commit(); // save changes
//
//                                Log.e("Twitter OAuth Token", "> " + accessToken.getToken());
//                                Intent myIntent = new Intent(getContext(), MainActivity.class);
//                                startActivity(myIntent);
//
//                                // Hide login button
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                    thread.start();
//
//
//
//                } catch (Exception e) {
//                    // Check log for login errors
//                    Log.e("Twitter Login Error", "> " + e.getMessage());
//                    e.printStackTrace();
//                }
//            }
//        } else {
//            Intent myIntent = new Intent(getContext(), MainActivity.class);
//            startActivity(myIntent);
//
//        }
    }


    @Override
    public void onClick(View v) {
        List<String> permissions = Arrays.asList("public_profile", "email");
        switch (v.getId()) {
            case R.id.googleplus_button:
                logInViaGooglePlus();
                break;
            case R.id.twitter_button:
                logInViaTwitter();
                break;
        }
    }

    private void logInViaGooglePlus() {
        boolean isConnected = checkNetworkConnection();
        if (isConnected) {
            googleLogInClient.connect();
            Log.d("SignUpFragment", "Google+ login successful");
        }
        else {
            Toast.makeText(view.getContext(), R.string.check_network_connection, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        googleLogInClient.disconnect();
    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

}
