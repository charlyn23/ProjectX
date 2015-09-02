package charlyn23.c4q.nyc.projectx;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.parse.*;

import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class SignUpFragment extends Fragment implements View.OnClickListener {
    private static final String SHAME_REPORT = "shameReport";
    private SharedPreferences.Editor editor;
    public GoogleApiClient googleLogInClient;
    private View view;
    private SharedPreferences preferences = null;
    private LatLng latLng;

    public SignUpFragment(GoogleApiClient googleLogInClient) {
        this.googleLogInClient = googleLogInClient;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.signup_fragment, container, false);
        preferences = getActivity().getSharedPreferences(MainActivity.SHARED_PREFERENCE, Context.MODE_PRIVATE);

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null)
            latLng = extras.getParcelable(MainActivity.LAT_LONG);

        //initializes views
        CircleImageView fb = (CircleImageView) view.findViewById(R.id.facebook_button);
        CircleImageView twitter = (CircleImageView) view.findViewById(R.id.twitter_button);
        CircleImageView google = (CircleImageView) view.findViewById(R.id.googleplus_button);

        ParseFacebookUtils.initialize(view.getContext());

        fb.setOnClickListener(this);
        twitter.setOnClickListener(this);
        google.setOnClickListener(this);

        return view;
    }

    private void logInViaFB(final List<String> permissions) {
        ParseFacebookUtils.logInWithReadPermissionsInBackground(SignUpFragment.this, permissions, new LogInCallback() {
            @Override
            public void done(final ParseUser user, ParseException err) {
                if (user == null) {
                    Log.i(MainActivity.TAG, "Log in failed");
                    Toast.makeText(view.getContext(), "Try again, please!", Toast.LENGTH_SHORT).show();
                    ParseUser.logOut();
                } else if (user.isNew()) {
                    Log.i(MainActivity.TAG, "User signed up and logged in through Facebook!");
                    if (!ParseFacebookUtils.isLinked(user)) {
                        ParseFacebookUtils.linkWithReadPermissionsInBackground(user, SignUpFragment.this, permissions, new SaveCallback() {
                            @Override
                            public void done(ParseException ex) {
                                if (ParseFacebookUtils.isLinked(user)) {
                                    Log.i(MainActivity.TAG, "New user logged in with Facebook and is linked!");
                                }
                            }
                        });
                    }
                    editor = preferences.edit();
                    editor.putBoolean(MainActivity.LOGGED_IN, true).apply();
                    reportShame();

                } else {
                    Log.i(MainActivity.TAG, "User logged in through Facebook!");
                    if (!ParseFacebookUtils.isLinked(user)) {
                        ParseFacebookUtils.linkWithReadPermissionsInBackground(user, SignUpFragment.this, permissions, new SaveCallback() {
                            @Override
                            public void done(ParseException ex) {
                                if (ParseFacebookUtils.isLinked(user)) {
                                    Log.i(MainActivity.TAG, "User logged in with Facebook and is linked!");
                                }
                            }
                        });
                    }
                    editor = preferences.edit();
                    editor.putBoolean(MainActivity.LOGGED_IN, true).apply();
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
                    Log.i(MainActivity.TAG, "User cancelled the Twitter login.");
                    ParseUser.logOut();

                } else if (parseUser.isNew()) {
                    Log.i(MainActivity.TAG, "New user signed up and logged in through Twitter!");
                    if (!ParseTwitterUtils.isLinked(parseUser)) {
                        ParseTwitterUtils.link(parseUser, view.getContext(), new SaveCallback() {
                            @Override
                            public void done(ParseException ex) {
                                if (ParseTwitterUtils.isLinked(parseUser)) {
                                    Log.i(MainActivity.TAG, "New user logged in with Twitter and is linked!");
                                }
                            }
                        });
                    }

                    editor = preferences.edit();
                    editor.putBoolean(MainActivity.LOGGED_IN, true).apply();
                    reportShame();

                } else {
                    Log.i(MainActivity.TAG, "User logged in through Twitter!");
                    if (!ParseTwitterUtils.isLinked(parseUser)) {
                        ParseTwitterUtils.link(parseUser, view.getContext(), new SaveCallback() {
                            @Override
                            public void done(ParseException ex) {
                                if (ParseTwitterUtils.isLinked(parseUser)) {
                                    Log.i(MainActivity.TAG, "User logged in with Twitter and is linked!");
                                }
                            }
                        });
                    }
                    editor = preferences.edit();
                    editor.putBoolean(MainActivity.LOGGED_IN, true).apply();
                    reportShame();
                }
            }
        });
    }

    private void reportShame() {
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        intent.putExtra(SHAME_REPORT, true);
        intent.putExtra(MainActivity.LAT_LONG, latLng);
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
                onSignInClicked();
                break;
            case R.id.twitter_button:
                logInViaTwitter();
                break;
        }
    }

    private void onSignInClicked() {
        googleLogInClient.connect();
        Log.d("SignUpFragment", "Google+ login successful");
    }

    @Override
    public void onPause() {
        super.onPause();
        googleLogInClient.disconnect();
    }
}
