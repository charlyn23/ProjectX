package charlyn23.c4q.nyc.projectx;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.parse.*;
import java.util.Arrays;
import java.util.List;
import charlyn23.c4q.nyc.projectx.shames.ShameActivity;


public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "c4q.nyc.projectx";
    private static final String SHARED_PREFERENCE = "sharedPreference";
    private static final String LOGGED_IN = "isLoggedIn";
    private SharedPreferences.Editor editor;
    private List<String> permissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        editor = preferences.edit();

        permissions = Arrays.asList("public_profile", "email");

        Button fb = (Button) findViewById(R.id.facebook_button);
        Button twitter = (Button) findViewById(R.id.twitter_button);
        Button logOut = (Button) findViewById(R.id.log_out);

        fb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                logInViaFB(permissions);
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInViaTwitter();
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    private void logInViaFB(final List<String> permissions) {
        ParseFacebookUtils.logInWithReadPermissionsInBackground(SignUpActivity.this, permissions, new LogInCallback() {
            @Override
            public void done(final com.parse.ParseUser user, ParseException err) {
                if (user == null) {
                    Log.i(TAG, "The user cancelled the Facebook login");
                    Toast.makeText(getApplicationContext(), "Log out from Facebook and try again please!", Toast.LENGTH_SHORT).show();
                    com.parse.ParseUser.logOut();
                } else if (user.isNew()) {
                    Log.i(TAG, "User signed up and logged in through Facebook!");
                    if (!ParseFacebookUtils.isLinked(user)) {
                        ParseFacebookUtils.linkWithReadPermissionsInBackground(user, SignUpActivity.this, permissions, new SaveCallback() {
                            @Override
                            public void done(ParseException ex) {
                                if (ParseFacebookUtils.isLinked(user)) {
                                    Log.i(TAG, "User logged in with Facebook!");
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "You can change your personal data in Settings tab!", Toast.LENGTH_SHORT).show();
                    }
                    editor.putBoolean(LOGGED_IN, true).apply();
                    sendIntentToMainActivity();
                } else {
                    Log.i(TAG, "User logged in through Facebook!");
                    if (!ParseFacebookUtils.isLinked(user)) {
                        ParseFacebookUtils.linkWithReadPermissionsInBackground(user, SignUpActivity.this, permissions, new SaveCallback() {
                            @Override
                            public void done(ParseException ex) {
                                if (ParseFacebookUtils.isLinked(user)) {
                                    Log.i(TAG, "User logged in with Facebook!");
                                }
                            }
                        });
                    }
                    editor.putBoolean(LOGGED_IN, true).apply();
                    sendIntentToMainActivity();
                }
            }
        });
    }

    private void logInViaTwitter() {
        ParseTwitterUtils.logIn(SignUpActivity.this, new LogInCallback() {
            @Override
            public void done(final com.parse.ParseUser parseUser, ParseException e) {
                if (parseUser == null) {
                    Log.i(TAG, "Uh oh. The user cancelled the Twitter login.");
                    com.parse.ParseUser.logOut();

                } else if (parseUser.isNew()) {
                    Log.i(TAG, "User signed up and logged in through Twitter!");

                    if (!ParseTwitterUtils.isLinked(parseUser)) {
                        ParseTwitterUtils.link(parseUser, SignUpActivity.this, new SaveCallback() {
                            @Override
                            public void done(ParseException ex) {
                                if (ParseTwitterUtils.isLinked(parseUser)) {
                                    Log.i(TAG, "Woohoo, user logged in with Twitter!");
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "You can change your personal data in Settings tab!", Toast.LENGTH_SHORT).show();
                    }
                    sendIntentToMainActivity();

                } else {
                    Log.i(TAG, "User logged in through Twitter!");
                    Log.i(TAG, "user info after logging in with Twitter is " + com.parse.ParseUser.getCurrentUser());

                    if (!ParseTwitterUtils.isLinked(parseUser)) {
                        ParseTwitterUtils.link(parseUser, SignUpActivity.this, new SaveCallback() {
                            @Override
                            public void done(ParseException ex) {
                                if (ParseTwitterUtils.isLinked(parseUser)) {
                                    Log.i(TAG, "Woohoo, user logged in with Twitter!");
                                }
                            }
                        });
                    }
                    sendIntentToMainActivity();
                }
            }
        });
    }

    private void logOut() {
        com.parse.ParseUser.logOut();
        Log.i(TAG, "user id when logged out is " + com.parse.ParseUser.getCurrentUser());
        editor.putBoolean(LOGGED_IN, false).apply();
    }

    private void sendIntentToMainActivity() {
        Intent intent = new Intent(SignUpActivity.this, ShameActivity.class);
        startActivity(intent);
    }
}
