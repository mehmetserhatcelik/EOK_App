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
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eok.eok.Models.Photo;
import com.eok.eok.databinding.ActivityHotpursuitBinding;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;


import com.eok.eok.databinding.ActivityHotpursuitBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class HotPursuit extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {


    private GoogleMap mMap;



    private FirebaseFirestore fstore;
    private FirebaseAuth auth;


    Timer timer;

    private int currentQuestionNumber;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private int totalPoints;

    List<Photo> photoList;

    private Photo currentPhoto;

    private double longitude;
    private double latitude;
    private ActivityHotpursuitBinding binding;

    private ImageView imageView;
    private boolean isTimeFinished;
    private boolean isAnswerLocked;
    private ProgressBar pb;
    private int counter;

    private String ppUrl;
    private String name;
    private int prevp;
    private long record;
    private Dialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHotpursuitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog = new Dialog(this);
        if (isTabletDevice()) {

            binding.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.gamehigh));
            binding.textView5.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.gamehigh));
            binding.textView2.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.gamehigh));

            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            int screenWidth = displayMetrics.widthPixels;
            int screenHeight = displayMetrics.heightPixels;

            ViewGroup.LayoutParams layoutParams = binding.imageView.getLayoutParams();

                layoutParams.width = screenWidth*8/10;
                layoutParams.height = screenHeight*3/10;

            binding.imageView.setLayoutParams(layoutParams);


            ViewGroup.LayoutParams layoutParams2 = findViewById(R.id.map2).getLayoutParams();


            layoutParams2.width = screenWidth*8/10;
            layoutParams2.height = screenHeight*3/10;

            findViewById(R.id.map2).setLayoutParams(layoutParams2);



        } else {

            binding.button2.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.but_size_low_resolution));
            binding.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.gamelow));
            binding.textView5.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.gamelow));
            binding.textView2.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.gamelow));



        }

        hideSystemUI();
        ppUrl = getIntent().getStringExtra("pp");
        System.out.println(ppUrl);
        Picasso.get().load(ppUrl).into(binding.pp);
        name = getIntent().getStringExtra("name");
        System.out.println(name);
        firebaseStorage = FirebaseStorage.getInstance();
        fstore = FirebaseFirestore.getInstance();

        currentQuestionNumber=getIntent().getIntExtra("q",1);

        binding.textView.setText(currentQuestionNumber+"/10");
        totalPoints = getIntent().getIntExtra("point",0);
        binding.textView2.setText(totalPoints+"");

        if(currentQuestionNumber == 1)
            openDialog();
        else
            pb();
        isTimeFinished=false;
        isAnswerLocked =false;


        pb=binding.pb;
        imageView = binding.imageView;
        photoList = new ArrayList<>();

        counter = 100;

        getImage();
        prevp =getIntent().getIntExtra("pgained",0);
        record = getIntent().getLongExtra("record",0);

        Thread t = new Thread()
        {
            @Override
            public void run()
            {
                while (isAnswerLocked ==false && isTimeFinished==false)
                {
                    try {

                        Thread.sleep(10);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });


                    }catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                if(isAnswerLocked == true || isTimeFinished==true) {
                    if(currentQuestionNumber==10)
                    {
                        Intent intent = new Intent(HotPursuit.this,HotPursuitEnd.class);
                        intent.putExtra("total",totalPoints);
                        intent.putExtra("record",record);
                        intent.putExtra("pp",ppUrl);
                        intent.putExtra("name",name);
                        startActivity(intent);
                        finish();
                    }
                    else{
                    Intent intent = new Intent(HotPursuit.this, HotPursuitResult.class);
                    int temp = calculateDistance();
                    intent.putExtra("q",currentQuestionNumber);
                    intent.putExtra("d",temp);
                    intent.putExtra("totalpoints",(totalPoints+givePoint(temp)));
                    intent.putExtra("latitude",latitude);
                    intent.putExtra("longitude",longitude);
                    intent.putExtra("photolat",currentPhoto.getLatitude());
                    intent.putExtra("photolong",currentPhoto.getLongitude());
                    intent.putExtra("pp",ppUrl);
                    intent.putExtra("name",name);
                    intent.putExtra("prevp",prevp);
                    startActivity(intent);
                    finish();}
                }
            }
        };
        t.start();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);



        //Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/bilguessr.appspot.com/o/images%2F01c9e59c-924a-4f38-8e7d-c1fec505fd48.png?alt=media&token=e9af9f53-3027-4b6f-aaa4-9b5a3389725f").into(binding.imageView);


    }
    public void back(View view)
    {
        timer.cancel();
        Dialog pausedialog = new Dialog(this);
        pausedialog.setContentView(R.layout.pause_dialog);
        pausedialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        pausedialog.findViewById(R.id.replay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotPursuit.this, HotPursuit.class);
                intent.putExtra("name",name);
                intent.putExtra("pp",ppUrl);
                intent.putExtra("record",record);
                startActivity(intent);
                pausedialog.dismiss();
                finish();

            }
        });
        pausedialog.findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotPursuit.this, MainScreen.class);
                startActivity(intent);
                pausedialog.dismiss();
                finish();

            }
        });
        pausedialog.findViewById(R.id.resume).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausedialog.dismiss();
                pb();
            }
        });
        pausedialog.show();

    }

    public void openDialog()
    {
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView t = dialog.findViewById(R.id.gamedescription);
        t.setText("Try to get maximum points among 10 questions that ask the location of the photos from the campus.");
        dialog.findViewById(R.id.as).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                pb();
            }
        });
        dialog.show();
    }
    private void getImage(){

        fstore.collection("Photos").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot d: queryDocumentSnapshots) {
                    Photo photo = d.toObject(Photo.class);

                    photoList.add(photo);

                }
                Random random = new Random();
                int i = random.nextInt(photoList.size());
                Photo photo = photoList.get(i);
                Picasso.get().load(photo.getDownloadUrl()).into(binding.imageView);
                currentPhoto = photo;

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HotPursuit.this, "Failed to fetch data.", Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
    }
    public int calculateDistance() {
        final double EARTH_RADIUS_KM = 6371.0;
        double dLat = Math.toRadians(currentPhoto.getLatitude() - latitude);
        double dLon = Math.toRadians(currentPhoto.getLongitude() - longitude);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(currentPhoto.getLatitude())) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Distance in kilometers
        double distance = EARTH_RADIUS_KM * c;
        System.out.println(distance);
        return (int)((distance)*1000);
    }
    public int givePoint(int distance)
    {
        if(distance>1000)
        {
            return 0;
        }
        return 1000-distance;
    }

    public void pb()
    {
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                counter--;
                pb.setProgress(counter);
                if(counter==0) {
                    timer.cancel();
                    isTimeFinished=true;
                }
            }
        };
        timer.schedule(timerTask,0,150);

    }

    public void lock(View view)
    {
        isAnswerLocked=true;
        timer.cancel();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);

        LatLng bilkent = new LatLng(39.87462344844274, 32.747621680995884);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bilkent,15));
    }
    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        mMap.clear();
        latitude = latLng.latitude;
        longitude = latLng.longitude;

        mMap.addMarker(new MarkerOptions().position(latLng));

    }
    public boolean isTabletDevice() {
        int screenSize = Configuration.SCREENLAYOUT_SIZE_MASK &
                getResources().getConfiguration().screenLayout;

        return screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE ||
                screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }
}