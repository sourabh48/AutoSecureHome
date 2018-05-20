package com.example.soura.autosecurehome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingsActivity extends AppCompatActivity
{
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;

    private FirebaseAuth mAuth;

    private CircleImageView mDisplayImage;
    private TextView mDisplayName;

    private StorageReference mImageStorage;
    private String switchstate;

    private String current_uid;
    Switch security_key;

    private ProgressDialog mProgressDialog;
    private static final int GALLERY_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        current_uid = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mUserDatabase.keepSynced(true);

        mImageStorage = FirebaseStorage.getInstance().getReference();

        mProgressDialog = new ProgressDialog(SettingsActivity.this);
        mProgressDialog.setTitle("Uploading");
        mProgressDialog.setMessage("Please Wait While We Upload and Process.");
        mProgressDialog.setCanceledOnTouchOutside(false);

        mDisplayImage = (CircleImageView) findViewById(R.id.profile_image);
        mDisplayName = (TextView) findViewById(R.id.setting_name);
        security_key = (Switch)findViewById(R.id.security_switch);

        security_key.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    mUserDatabase.child("security").setValue(true);
                }
                else
                {
                    mUserDatabase.child("security").setValue(false);
                }
            }
        });

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                final String image = dataSnapshot.child("image").getValue().toString();
                switchstate = dataSnapshot.child("security").getValue().toString();

                if(switchstate.equals("true"))
                {
                    security_key .setChecked(true);
                }
                else
                {
                    security_key .setChecked(false);
                }

                mDisplayName.setText("Hi! "+name);

                if (!image.equals("default")) {
                    Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.profile).into(mDisplayImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(image).placeholder(R.drawable.profile).into(mDisplayImage);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    public void ChangeName(View view) {
        String name_value = mDisplayName.getText().toString().substring(4);
        Intent name = new Intent(SettingsActivity.this, NameActivity.class);
        name.putExtra("name", name_value);
        startActivity(name);
    }

    public void Change(View view) {

        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(galleryIntent, GALLERY_PICK);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mProgressDialog.show();
                Uri resultUri = result.getUri();

                File thumb_filePath = new File(resultUri.getPath());

                String uid = mCurrentUser.getUid();


                Bitmap thumb_bitmap = null;

                try {
                    thumb_bitmap = new Compressor(this)
                            .setQuality(75)
                            .setMaxHeight(300)
                            .setMaxWidth(300)
                            .compressToBitmap(thumb_filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();

                StorageReference filepath = mImageStorage.child(uid).child("profile_images").child(uid + ".jpg");
                final StorageReference thumb_filepath = mImageStorage.child(uid).child("thumbs").child(uid + ".jpg");


                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String downloadUrl = task.getResult().getDownloadUrl().toString();

                            UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {

                                    String thumb_downloadUrl = thumb_task.getResult().getDownloadUrl().toString();

                                    if (thumb_task.isSuccessful()) {
                                        Map update_hashMap = new HashMap();
                                        update_hashMap.put("image", downloadUrl);
                                        update_hashMap.put("thumb_image", thumb_downloadUrl);

                                        mUserDatabase.updateChildren(update_hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    mProgressDialog.dismiss();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(SettingsActivity.this, "Failed to Upload thumbs", Toast.LENGTH_SHORT).show();
                                        mProgressDialog.dismiss();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(SettingsActivity.this, "Failed to Upload", Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    //For testing /database trial
    public void notify(View view)
    {
        DatabaseReference Devices = FirebaseDatabase.getInstance().getReference().child("Devices").child("12345");
        final DatabaseReference Notification = FirebaseDatabase.getInstance().getReference().child("Notification");

        final HashMap<String,String> notification =new HashMap<>();
        notification.put("from","12345");
        notification.put("type","burgler_alert");


        Devices.child("user_id").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                String user_id = dataSnapshot.getValue().toString();
                Toast.makeText(SettingsActivity.this,user_id,Toast.LENGTH_SHORT).show();
                Notification.child(user_id).push().setValue(notification);

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

    }

    public void dustbin(View view)
    {
        DatabaseReference Devices = FirebaseDatabase.getInstance().getReference().child("Devices").child("12345");
        final DatabaseReference Notification = FirebaseDatabase.getInstance().getReference().child("Notification");

        final HashMap<String,String> notification =new HashMap<>();
        notification.put("from","12345");
        notification.put("type","dustbin_alert");


        Devices.child("user_id").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                String user_id = dataSnapshot.getValue().toString();
                Toast.makeText(SettingsActivity.this,user_id, Toast.LENGTH_SHORT).show();
                Notification.child(user_id).push().setValue(notification);

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }
}