package charlyn23.c4q.nyc.projectx;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Arrays;
import java.util.List;


public class SignUpFragment extends Fragment implements View.OnClickListener {
    private SharedPreferences.Editor editor;
    public GoogleApiClient googleLogInClient;
    private View view;
    private SharedPreferences preferences = null;

    public SignUpFragment(GoogleApiClient googleLogInClient) {
        this.googleLogInClient = googleLogInClient;
    }

    public SignUpFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.signup_fragment, container, false);
        preferences = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE);

        //initializes views
        ImageButton fb = (ImageButton) view.findViewById(R.id.facebook_button);
        ImageButton twitter = (ImageButton) view.findViewById(R.id.twitter_button);
        ImageButton google = (ImageButton) view.findViewById(R.id.googleplus_button);

        ParseFacebookUtils.initialize(view.getContext());

        fb.setOnClickListener(this);
        twitter.setOnClickListener(this);
        google.setOnClickListener(this);

        //sets custom font
        Typeface questrial = Typeface.createFromAsset(getActivity().getAssets(), "questrial.ttf");
        TextView blazon = (TextView) view.findViewById(R.id.blazon);
        blazon.setTypeface(questrial);

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    private void logInViaFB(final List<String> permissions) {
        ParseFacebookUtils.logInWithReadPermissionsInBackground(SignUpFragment.this, permissions, new LogInCallback() {
            @Override
            public void done(final ParseUser user, ParseException err) {
                if (user == null) {
                    Log.i(Constants.TAG, "Log in failed");
                    Toast.makeText(view.getContext(), R.string.check_network_connection, Toast.LENGTH_SHORT).show();
                    ParseUser.logOut();
                } else if (user.isNew()) {
                    Log.i(Constants.TAG, "User signed up and logged in through Facebook!");
                    if (!ParseFacebookUtils.isLinked(user)) {
                        ParseFacebookUtils.linkWithReadPermissionsInBackground(user, SignUpFragment.this, permissions, new SaveCallback() {
                            @Override
                            public void done(ParseException ex) {
                                if (ParseFacebookUtils.isLinked(user)) {
                                    Log.i(Constants.TAG, "New user logged in with Facebook and is linked!");
                                }
                            }
                        });
                    }
                    editor = preferences.edit();
                    editor.putBoolean(Constants.LOGGED_IN, true).apply();
                    reportShame();

                } else {
                    Log.i(Constants.TAG, "User logged in through Facebook!");
                    if (!ParseFacebookUtils.isLinked(user)) {
                        ParseFacebookUtils.linkWithReadPermissionsInBackground(user, SignUpFragment.this, permissions, new SaveCallback() {
                            @Override
                            public void done(ParseException ex) {
                                if (ParseFacebookUtils.isLinked(user)) {
                                    Log.i(Constants.TAG, "User logged in with Facebook and is linked!");
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

    private void logInViaTwitter() {
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
            case R.id.facebook_button:
                logInViaFB(permissions);
                break;
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
