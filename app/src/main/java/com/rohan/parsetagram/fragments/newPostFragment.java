package com.rohan.parsetagram.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.rohan.parsetagram.MainActivity;
import com.rohan.parsetagram.Post;
import com.rohan.parsetagram.R;

import java.io.File;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link newPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class newPostFragment extends Fragment {


    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 328;
    private File photoFile;
    private String photoFileName = "photo.jpg";
    private Button btPost;
    private Button btTakePicture;
    private EditText etDescription;
    private ImageView ivPostImage;
    private ActionBar actionBar;
    private ProgressBar pbProgress;
    private BottomNavigationView bottomNavigationView;

    public newPostFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btPost = view.findViewById(R.id.btPost);
        btTakePicture = view.findViewById(R.id.btTakePicture);
        etDescription = view.findViewById(R.id.etDescription);
        pbProgress = view.findViewById(R.id.pbProgress);
        ivPostImage = view.findViewById(R.id.ivPostImage);



        btPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();
                if (description.isEmpty()) {
                    Toast.makeText(getContext(), "Description cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (photoFile == null || ivPostImage.getDrawable() == null) {
                    Toast.makeText(getContext(), "There is no image!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(description, currentUser, photoFile);

            }
        });

        btTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });
    }

    private void launchCamera() {

        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.rohan.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivPostImage.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "newPost");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d("newPost", "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    private void savePost(String description, ParseUser currentUser, File photoFile) {
        pbProgress.setVisibility(ProgressBar.VISIBLE);
        Post post = new Post();
        post.setDescription(description);
        post.setUser(currentUser);
        post.setImage(new ParseFile(photoFile));
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("newPost", "Error while saving.....", e);
                    Toast.makeText(getContext(), "Error while posting", Toast.LENGTH_SHORT).show();
                }
                pbProgress.setVisibility(ProgressBar.INVISIBLE);
                ((MainActivity) getActivity()).setHome();
            }
        });
    }
}