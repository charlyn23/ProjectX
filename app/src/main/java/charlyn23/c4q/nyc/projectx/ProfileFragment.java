package charlyn23.c4q.nyc.projectx;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.w3c.dom.Text;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private static final String TAG = "c4q.nyc.projectx";
    private static final String SHARED_PREFERENCE = "s";
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String URI_PATH = "image";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String imagePath;
    private View view;
    private CircleImageView profileImage;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        preferences = getActivity().getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        editor = preferences.edit();

        view = inflater.inflate(R.layout.profile_fragment, container, false);
        profileImage = (CircleImageView) view.findViewById(R.id.profile_image);

        String path = preferences.getString(URI_PATH, "");
        Log.d("yuliya", "on CreateView " + path);
        if (path != null && !path.equals("")) {
            Uri uri = Uri.parse(path);
            profileImage.setImageURI(null);
            profileImage.setImageURI(uri);
        } else {
            profileImage.setImageResource(R.drawable.map);
        }
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeProfileImage();
            }
        });

        RadioGroup sex = (RadioGroup) view.findViewById(R.id.sex);
        sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.male) {
                    //TODO save sex
                } else {
                    //TODO save sex
                }
            }

        });


        return view;
    }

    // sets the user's image as a profile picture
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == MainActivity.RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            imagePath = selectedImage.toString();
            Log.d("yuliya", "get Path " + imagePath);

            profileImage.setImageURI(selectedImage);

            editor.putString(URI_PATH, imagePath).commit();
        }
    }

    // brings up the photo gallery to choose a picture
    private void changeProfileImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

//    private void setImageProfile() throws IOException {
//        imagePath = preferences.getString(URI_PATH, "");
//        Log.d("yuliya", "set Image" + imagePath);
//        profileImage.setImageURI(null);
//        profileImage.setImageURI(Uri.parse(imagePath));
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        profileImage.invalidate();
//    }
//
//    //    private class ImageLoadAsync extends AsyncTask<Void, Void, String> {
////
////        @Override
////        protected String doInBackground(Void... params) {
////            uriPath = preferences.getString(URI_PATH, "");
////            return uriPath;
////        }
////
////        @Override
////        protected void onPostExecute(String path) {
////            profileImage.setImageURI(null);
////            profileImage.setImageURI(Uri.parse(path));
////        }
////    }
}
