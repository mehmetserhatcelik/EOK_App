package com.eok.eok;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eok.eok.databinding.ActivityProfileSettingBinding;
import com.eok.eok.databinding.ActivityWordGameBinding;
import com.eok.eok.profilesettings.ProfileSettings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class ProfileSetting extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    String userName;
    String playerName;
    String playerpp;
    ImageView profileIcon;
    TextView usernameText;
    TextView feedback;
    TextView send_your_message;
    ImageView share,instagram;
    private ActivityProfileSettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileSettingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        if (isTabletDevice()) {

            binding.changeName.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.th));
            binding.textViewChangeEmail.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.th));
            binding.textViewChangeIcon.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.th));
            binding.textViewChangePassword.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.th));



        } else {

            binding.changeName.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.tl));
            binding.textViewChangeEmail.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.tl));
            binding.textViewChangeIcon.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.tl));
            binding.textViewChangePassword.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.tl));




        }
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        profileIcon = findViewById(R.id.imageProfile);
        usernameText = findViewById(R.id.userName);


        feedback = findViewById(R.id.feedback);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                String uriText = "mailto:" + Uri.encode("furkan.basibuyuk18@gmail.com") + "?subject=" +
                        Uri.encode("your email id ") + "&body=" + Uri.encode("");

                Uri uri = Uri.parse(uriText);
                intent.setData(uri);
                startActivity(Intent.createChooser(intent, "Send Email"));

            }
        });

        send_your_message = findViewById(R.id.feedback1);
        send_your_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileSetting.this, QueryPage.class);
                startActivity(intent);
                finish();
            }
        });
        instagram = findViewById(R.id.instagram);
        instagram.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                gotoUrl("https://www.instagram.com/eokbilkent/");


            }


            private void gotoUrl(String s) {
                Uri uri = Uri.parse(s);
                startActivity(new Intent(Intent.ACTION_VIEW,uri));
            }
        });



        share = findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                String sharebody = "look all Programmings";
                String subject = "https://play.google.com/store/apps/details?id=in.seekmyvision.seekmyvision";
                i.putExtra(Intent.EXTRA_SUBJECT,sharebody);
                i.putExtra(Intent.EXTRA_TEXT,subject);
                startActivity(Intent.createChooser(i,"EOK"));

            }
        });

        FirebaseFirestore fstore = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        fstore.collection("Users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {

                    DocumentSnapshot documentSnapshot =task.getResult();

                    if(documentSnapshot != null && documentSnapshot.exists())
                    {


                        playerpp=documentSnapshot.getString("userPhotoUrl");

                        userName = documentSnapshot.getString("name");
                        if(playerpp.equals("default"))
                            Picasso.get().load(R.drawable.wavy).into(profileIcon);
                        else
                            Picasso.get().load(playerpp).into(profileIcon);

                        usernameText.setText(userName);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });







        findViewById(R.id.logOutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });

        findViewById(R.id.textViewChangePassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });

        findViewById(R.id.textViewChangeIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeIcon();
            }
        });

        findViewById(R.id.textViewChangeEmail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeEmail();
            }
        });
        findViewById(R.id.changeName).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeName();
            }
        });
    }

    public void changeName() {
        Intent intent = new Intent(this, ProfileSettings.class);
        intent.putExtra("setting", "name");
        startActivity(intent);
    }
    public void home(View v) {
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
        finish();
    }

    public void logOut() {
        firebaseAuth.signOut();
        SharedPreferences preferences = getSharedPreferences("switch",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("remember",false);
        editor.apply();
        Intent intent = new Intent(this, LogInPage.class);
        startActivity(intent);
        finish();
    }

    public void changePassword() {
        Intent intent = new Intent(this, ProfileSettings.class);
        intent.putExtra("setting", "password");
        startActivity(intent);
    }

    public void changeIcon() {
        Intent intent = new Intent(this, ProfileSettings.class);
        intent.putExtra("setting", "icon");
        startActivity(intent);
    }

    private void changeEmail() {
        Intent intent = new Intent(this, ProfileSettings.class);
        intent.putExtra("setting", "email");
        startActivity(intent);
    }
    public boolean isTabletDevice() {
        int screenSize = Configuration.SCREENLAYOUT_SIZE_MASK &
                getResources().getConfiguration().screenLayout;

        return screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE ||
                screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

}
