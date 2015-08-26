package charlyn23.c4q.nyc.projectx;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.plus.Plus;
import com.parse.*;
import java.util.Arrays;
import java.util.List;


public class SignUpFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private static final String TAG = "c4q.nyc.projectx";
    private static final String SHAME_REPORT = "shameReport";
    private static final int RC_SIGN_IN = 0;
    private static final String SHARED_PREFERENCE = "sharedPreference";
    private static final String LOGGED_IN = "isLoggedIn";
    private static final String LAT_LONG = "latLong";
    private SharedPreferences.Editor editor;
    public static GoogleApiClient googleApiClient;
    private boolean mIsResolving = false;
    private boolean mShouldResolve = false;
    private View view;
    private SharedPreferences preferences = null;
    private LatLng latLng;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.signup_fragment, container, false);

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null)
            latLng = extras.getParcelable(LAT_LONG);

        //initializes views
        Button fb = (Button) view.findViewById(R.id.facebook_button);
        Button twitter = (Button) view.findViewById(R.id.twitter_button);
        Button logOut = (Button) view.findViewById(R.id.log_out);
        SignInButton google = (SignInButton) view.findViewById(R.id.sign_in_button);

        ParseFacebookUtils.initialize(view.getContext());
        // builds GoogleApiClient with access to basic profile
        googleApiClient = new GoogleApiClient.Builder(view.getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.EMAIL))
                .build();

        preferences = getActivity().getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);

        fb.setOnClickListener(this);
        twitter.setOnClickListener(this);
        google.setOnClickListener(this);
        logOut.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    private void logInViaFB(final List<String> permissions) {
        ParseFacebookUtils.logInWithReadPermissionsInBackground(SignUpFragment.this, permissions, new LogInCallback() {
            @Override
            public void done(final ParseUser user, ParseException err) {
                if (user == null) {
                    Log.i(TAG, "Log in failed");
                    Toast.makeText(view.getContext(), "Try again, please!", Toast.LENGTH_SHORT).show();
                    ParseUser.logOut();
                } else if (user.isNew()) {
                    Log.i(TAG, "User signed up and logged in through Facebook!");
                    if (!ParseFacebookUtils.isLinked(user)) {
                        ParseFacebookUtils.linkWithReadPermissionsInBackground(user, SignUpFragment.this, permissions, new SaveCallback() {
                            @Override
                            public void done(ParseException ex) {
                                if (ParseFacebookUtils.isLinked(user)) {
                                    Log.i(TAG, "New user logged in with Facebook and is linked!");
                                }
                            }
                        });
                    }
                    editor = preferences.edit();
                    editor.putBoolean(LOGGED_IN, true).apply();
                    reportShame();

                } else {
                    Log.i(TAG, "User logged in through Facebook!");
                    if (!ParseFacebookUtils.isLinked(user)) {
                        ParseFacebookUtils.linkWithReadPermissionsInBackground(user, SignUpFragment.this, permissions, new SaveCallback() {
                            @Override
                            public void done(ParseException ex) {
                                if (ParseFacebookUtils.isLinked(user)) {
                                    Log.i(TAG, "User logged in with Facebook and is linked!");
                                }
                            }
                        });
                    }
                    editor = preferences.edit();
                    editor.putBoolean(LOGGED_IN, true).apply();
                    reportShame();
                }
            }
        });
    }

    private void logInViaTwitter() {
        ParseTwitterUtils.logIn(view.getContext(), new LogInCallback() {
            @Override
            public void done(final ParseUser parseUser, ParseException e) {
                if (parseUser == null) {
                    Log.i(TAG, "User cancelled the Twitter login.");
                    ParseUser.logOut();

                } else if (parseUser.isNew()) {
                    Log.i(TAG, "New user signed up and logged in through Twitter!");
                    if (!ParseTwitterUtils.isLinked(parseUser)) {
                        ParseTwitterUtils.link(parseUser, view.getContext(), new SaveCallback() {
                            @Override
                            public void done(ParseException ex) {
                                if (ParseTwitterUtils.isLinked(parseUser)) {
                                    Log.i(TAG, "New user logged in with Twitter and is linked!");
                                }
                            }
                        });
                    }

                    editor = preferences.edit();
                    editor.putBoolean(LOGGED_IN, true).apply();
                    reportShame();

                } else {
                    Log.i(TAG, "User logged in through Twitter!");
                    if (!ParseTwitterUtils.isLinked(parseUser)) {
                        ParseTwitterUtils.link(parseUser, view.getContext(), new SaveCallback() {
                            @Override
                            public void done(ParseException ex) {
                                if (ParseTwitterUtils.isLinked(parseUser)) {
                                    Log.i(TAG, "User logged in with Twitter and is linked!");
                                }
                            }
                        });
                    }
                    editor = preferences.edit();
                    editor.putBoolean(LOGGED_IN, true).apply();
                    reportShame();
                }
            }
        });
    }

    private void reportShame() {
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        intent.putExtra(SHAME_REPORT, true);
        intent.putExtra(LAT_LONG, latLng);
        startActivity(intent);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected: " + bundle);
        mShouldResolve = false;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onClick(View v) {
        List<String> permissions = Arrays.asList("public_profile", "email");
        switch (v.getId()) {
            case R.id.facebook_button:
                logInViaFB(permissions);
                break;
            case R.id.sign_in_button:
                onSignInClicked();
                break;
            case R.id.twitter_button:
                logInViaTwitter();
                break;
            case R.id.log_out:
                logOut();
                break;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: " + connectionResult);

        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(getActivity(), RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    googleApiClient.connect();
                }
            } else {
                Toast.makeText(view.getContext(), getString(R.string.network_connection_problem), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void onSignInClicked() {
        mShouldResolve = true;
        googleApiClient.connect();
        reportShame();
        editor = preferences.edit();
        editor.putBoolean(LOGGED_IN, true).apply();
        Toast.makeText(view.getContext(), "Signing in", Toast.LENGTH_LONG).show();
    }

    private void logOut() {
        ParseUser user = ParseUser.getCurrentUser();
        user.logOut();

        if (googleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(googleApiClient);
            googleApiClient.disconnect();
        }


        editor = preferences.edit();
        editor.putBoolean(LOGGED_IN, false).apply();
        Toast.makeText(view.getContext(), getString(R.string.log_out_toast), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        startActivity(intent);
    }
}
