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
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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


public class SignUpFragment extends Fragment implements View.OnClickListener {
    private SharedPreferences.Editor editor;
    public GoogleApiClient googleLogInClient;
    private View view;
    private SharedPreferences preferences = null;
    public LoginButton fbLoginButton;
    public CallbackManager callbackManager;




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
        ImageButton twitter = (ImageButton) view.findViewById(R.id.twitter_button);
        ImageButton google = (ImageButton) view.findViewById(R.id.googleplus_button);

        twitter.setOnClickListener(this);
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
    private void logInViaFacebook() {
        callbackManager = CallbackManager.Factory.create();

        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        System.out.println("Success");
                        editor = preferences.edit();
                        editor.putBoolean(Constants.LOGGED_IN, true).apply();
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

                                                String fbEmail = json.getString("email");
                                                String fbId = json.getString("id");
                                                String fbFirstName = json.getString("first_name");
                                                Log.i("FBLogin" , fbFirstName + ", " + fbEmail + ", fbID: " + fbId );

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                }).executeAsync();

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
        ParseTwitterUtils.logIn(view.getContext(), new LogInCallback() {
            @Override
            public void done(final ParseUser parseUser, ParseException e) {
                if (parseUser == null) {
                    Toast.makeText(view.getContext(), R.string.check_network_connection, Toast.LENGTH_LONG).show();
                    Log.i(Constants.TAG, "User cancelled the Twitter login.");
                    ParseUser.logOut();

                } else if (parseUser.isNew()) {
                    Log.i(Constants.TAG, "New user signed up and logged in through Twitter!");
                    if (!ParseTwitterUtils.isLinked(parseUser)) {
                        ParseTwitterUtils.link(parseUser, view.getContext(), new SaveCallback() {
                            @Override
                            public void done(ParseException ex) {
                                if (ParseTwitterUtils.isLinked(parseUser)) {
                                    Log.i(Constants.TAG, "New user logged in with Twitter and is linked!");
                                }
                            }
                        });
                    }

                    editor = preferences.edit();
                    editor.putBoolean(Constants.LOGGED_IN, true).apply();
                    reportShame();

                } else {
                    Log.i(Constants.TAG, "User logged in through Twitter!");
                    if (!ParseTwitterUtils.isLinked(parseUser)) {
                        ParseTwitterUtils.link(parseUser, view.getContext(), new SaveCallback() {
                            @Override
                            public void done(ParseException ex) {
                                if (ParseTwitterUtils.isLinked(parseUser)) {
                                    Log.i(Constants.TAG, "User logged in with Twitter and is linked!");
                                }
                            }
                        });
                    }
                    editor = preferences.edit();
                    editor.putBoolean(Constants.LOGGED_IN, true).apply();
                    reportShame();
                }
            }
        });
    }

    private void reportShame() {
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        intent.putExtra(Constants.SHOW_DIALOG, true);
        startActivity(intent);
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
