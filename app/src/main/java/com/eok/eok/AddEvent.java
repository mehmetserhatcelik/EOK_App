package com.eok.eok;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.eok.eok.Models.Event;
import com.eok.eok.Models.Photo;
import com.eok.eok.databinding.ActivityAddEventBinding;
import com.eok.eok.databinding.ActivityMainScreenBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class AddEvent extends AppCompatActivity {
    private ActivityAddEventBinding binding;

    private ActivityResultLauncher<String> permissionLauncher;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private FirebaseFirestore fstore;
    private FirebaseAuth auth;


    private Bitmap selectedImage;
    private Uri imageData;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        binding = ActivityAddEventBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        if (isTabletDevice()) {

            binding.button.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.but_size_high_resolution));
            binding.title.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_high_resolution));
            binding.title.setPadding((int)getResources().getDimension(R.dimen.paddingh),(int)getResources().getDimension(R.dimen.paddingh),(int)getResources().getDimension(R.dimen.paddingh),(int)getResources().getDimension(R.dimen.paddingh));
            binding.place.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_high_resolution));
            binding.place.setPadding((int)getResources().getDimension(R.dimen.paddingh),(int)getResources().getDimension(R.dimen.paddingh),(int)getResources().getDimension(R.dimen.paddingh),(int)getResources().getDimension(R.dimen.paddingh));
            binding.date.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_high_resolution));
            binding.date.setPadding((int)getResources().getDimension(R.dimen.paddingh),(int)getResources().getDimension(R.dimen.paddingh),(int)getResources().getDimension(R.dimen.paddingh),(int)getResources().getDimension(R.dimen.paddingh));
            binding.time.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_high_resolution));
            binding.time.setPadding((int)getResources().getDimension(R.dimen.paddingh),(int)getResources().getDimension(R.dimen.paddingh),(int)getResources().getDimension(R.dimen.paddingh),(int)getResources().getDimension(R.dimen.paddingh));
            binding.shortDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_high_resolution));
            binding.shortDescription.setPadding((int)getResources().getDimension(R.dimen.paddingh),(int)getResources().getDimension(R.dimen.paddingh),(int)getResources().getDimension(R.dimen.paddingh),(int)getResources().getDimension(R.dimen.paddingh));
            binding.longDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_high_resolution));
            binding.longDescription.setPadding((int)getResources().getDimension(R.dimen.paddingh),(int)getResources().getDimension(R.dimen.paddingh),(int)getResources().getDimension(R.dimen.paddingh),(int)getResources().getDimension(R.dimen.paddingh));



        } else {

            binding.button.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.but_size_low_resolution));
            binding.title.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.but_size_low_resolution));
            binding.title.setPadding((int)getResources().getDimension(R.dimen.padding),(int)getResources().getDimension(R.dimen.padding),(int)getResources().getDimension(R.dimen.padding),(int)getResources().getDimension(R.dimen.padding));
            binding.place.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.but_size_low_resolution));
            binding.place.setPadding((int)getResources().getDimension(R.dimen.padding),(int)getResources().getDimension(R.dimen.padding),(int)getResources().getDimension(R.dimen.padding),(int)getResources().getDimension(R.dimen.padding));
            binding.date.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.but_size_low_resolution));
            binding.date.setPadding((int)getResources().getDimension(R.dimen.padding),(int)getResources().getDimension(R.dimen.padding),(int)getResources().getDimension(R.dimen.padding),(int)getResources().getDimension(R.dimen.padding));
            binding.time.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.but_size_low_resolution));
            binding.time.setPadding((int)getResources().getDimension(R.dimen.padding),(int)getResources().getDimension(R.dimen.padding),(int)getResources().getDimension(R.dimen.padding),(int)getResources().getDimension(R.dimen.padding));
            binding.shortDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.but_size_low_resolution));
            binding.shortDescription.setPadding((int)getResources().getDimension(R.dimen.padding),(int)getResources().getDimension(R.dimen.padding),(int)getResources().getDimension(R.dimen.padding),(int)getResources().getDimension(R.dimen.padding));
            binding.longDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.but_size_low_resolution));
            binding.longDescription.setPadding((int)getResources().getDimension(R.dimen.padding),(int)getResources().getDimension(R.dimen.padding),(int)getResources().getDimension(R.dimen.padding),(int)getResources().getDimension(R.dimen.padding));


        }
        registerLauncher();
        fstore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        firebaseStorage= FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();

    }
    public void selectImage(View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give permission", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                        }
                    }).show();
                } else {
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            } else {
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intentToGallery);
            }
        }else{
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_MEDIA_IMAGES)) {
                    Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give permission", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
                        }
                    }).show();
                } else {
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
                }
            } else {
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intentToGallery);
            }
        }
    }
    public void addEvent(View view){

        String imageName = "EventImages/"+(UUID.randomUUID().toString()) + ".png";


        storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                StorageReference newReference = firebaseStorage.getReference(imageName);
                newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        String downloadUrl = uri.toString();

                        CollectionReference collectionReference = fstore.collection("Events");

                        collectionReference.add(new Event(binding.longDescription.getText().toString(),binding.shortDescription.getText().toString(),
                                binding.title.getText().toString(),binding.place.getText().toString(),binding.date.getText().toString(),
                                binding.time.getText().toString(),downloadUrl));

                        Toast.makeText(AddEvent.this, "Successfully Uploaded !", Toast.LENGTH_SHORT).show();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddEvent.this, "Failed to upload", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddEvent.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void registerLauncher()
    {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()==RESULT_OK){
                    Intent intentFromResult = result.getData();
                    if(intentFromResult!=null){
                        imageData = intentFromResult.getData();
                        try {
                            if(Build.VERSION.SDK_INT>=28) {
                                ImageDecoder.Source source = ImageDecoder.createSource(AddEvent.this.getContentResolver(), imageData);
                                selectedImage = ImageDecoder.decodeBitmap(source);
                                binding.iconImageView.setImageBitmap(selectedImage);
                            }
                            else{
                                selectedImage= MediaStore.Images.Media.getBitmap(AddEvent.this.getContentResolver(),imageData);
                                binding.iconImageView.setImageBitmap(selectedImage);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result){
                    Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentToGallery);
                }else{
                    Toast.makeText(AddEvent.this,"Permission needed!",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public boolean isTabletDevice() {
        int screenSize = Configuration.SCREENLAYOUT_SIZE_MASK &
                getResources().getConfiguration().screenLayout;

        return screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE ||
                screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }
}