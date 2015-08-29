package charlyn23.c4q.nyc.projectx;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.FileNotFoundException;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private static final String TAG = "c4q.nyc.projectx";
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String PROFILE_IMAGE = "profileImage";
    private View view;
    private CircleImageView profileImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment, container, false);
        profileImage = (CircleImageView) view.findViewById(R.id.profile_image);
        setProfileImage();


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeProfileImage();
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
