package charlyn23.c4q.nyc.projectx;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.parse.*;
import java.util.Arrays;
import java.util.List;


public class SignUpActivity extends AppCompatActivity {
    List<String> permissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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
                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");

                    Toast.makeText(getApplicationContext(), "Log-out from Facebook and try again please!", Toast.LENGTH_SHORT).show();

                    com.parse.ParseUser.logOut();
                } else if (user.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Facebook!");

                    if (!ParseFacebookUtils.isLinked(user)) {
                        ParseFacebookUtils.linkWithReadPermissionsInBackground(user, SignUpActivity.this, permissions, new SaveCallback() {
                            @Override
                            public void done(ParseException ex) {
                                if (ParseFacebookUtils.isLinked(user)) {
                                    Log.d("MyApp", "Woohoo, user logged in with Facebook!");

                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "You can change your personal data in Settings tab!", Toast.LENGTH_SHORT).show();
                    }
                    sendIntentToMainActivity();
                } else {
                    Log.d("MyApp", "User logged in through Facebook!");

                    if (!ParseFacebookUtils.isLinked(user)) {
                        ParseFacebookUtils.linkWithReadPermissionsInBackground(user, SignUpActivity.this, permissions, new SaveCallback() {
                            @Override
                            public void done(ParseException ex) {
                                if (ParseFacebookUtils.isLinked(user)) {
                                    Log.d("MyApp", "Woohoo, user logged in with Facebook!");
                                }
                            }
                        });
                    }
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
                    Log.d("MyApp", "Uh oh. The user cancelled the Twitter login.");
                    com.parse.ParseUser.logOut();

                } else if (parseUser.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Twitter!");

                    if (!ParseTwitterUtils.isLinked(parseUser)) {
                        ParseTwitterUtils.link(parseUser, SignUpActivity.this, new SaveCallback() {
                            @Override
                            public void done(ParseException ex) {
                                if (ParseTwitterUtils.isLinked(parseUser)) {
                                    Log.d("MyApp", "Woohoo, user logged in with Twitter!");
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "You can change your personal data in Settings tab!", Toast.LENGTH_SHORT).show();
                    }
                    sendIntentToMainActivity();

                } else {
                    Log.d("MyApp", "User logged in through Twitter!");
                    Log.d("MyApp", "after log in with twitter user info = " + com.parse.ParseUser.getCurrentUser());

                    if (!ParseTwitterUtils.isLinked(parseUser)) {
                        ParseTwitterUtils.link(parseUser, SignUpActivity.this, new SaveCallback() {
                            @Override
                            public void done(ParseException ex) {
                                if (ParseTwitterUtils.isLinked(parseUser)) {
                                    Log.d("MyApp", "Woohoo, user logged in with Twitter!");
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
        Log.d("MyApp", "log out user info = " + com.parse.ParseUser.getCurrentUser());
    }

    private void sendIntentToMainActivity() {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
