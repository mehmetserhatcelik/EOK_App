package com.eok.eok;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.eok.eok.profilesettings.ProfileSettings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

public class MainScreen extends AppCompatActivity {

    private FirebaseFirestore fstore;
    private FirebaseAuth auth;
    private FirebaseUser user;

    private Intent HotPursuitIntent ;
    private Intent TimeRushIntent ;

    private Intent SettingsIntent ;




    private ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        img = findViewById(R.id.asas);

        TimeRushIntent =new Intent(MainScreen.this, TimeRush.class);
        SettingsIntent =new Intent(MainScreen.this, ProfileSetting.class);

        fstore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        HotPursuitIntent = new Intent(MainScreen.this, HotPursuit.class);



        fstore.collection("Users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {

                    DocumentSnapshot documentSnapshot =task.getResult();

                    if(documentSnapshot != null && documentSnapshot.exists())
                    {
                        HotPursuitIntent.putExtra("name",documentSnapshot.getString("name"));
                        HotPursuitIntent.putExtra("pp",documentSnapshot.getString("userPhotoUrl"));
                        HotPursuitIntent.putExtra("record",(long)documentSnapshot.get("hotPursuitRecord"));

                        TimeRushIntent.putExtra("name",documentSnapshot.getString("name"));
                        TimeRushIntent.putExtra("pp",documentSnapshot.getString("userPhotoUrl"));
                        TimeRushIntent.putExtra("record",(long)documentSnapshot.get("timeRushRecord"));


                        SettingsIntent.putExtra("name",documentSnapshot.getString("name"));
                        SettingsIntent.putExtra("pp",documentSnapshot.getString("userPhotoUrl"));



                        Picasso.get().load(documentSnapshot.getString("userPhotoUrl")).into(img);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
    public void HotPursuit(View view)
    {

        startActivity(HotPursuitIntent);
        finish();
    }

    public void timeRush(View view)
    {

        startActivity(TimeRushIntent);
        finish();
    }


    public void HotPursuitLeaderboard(View view)
    {
        Intent intent = new Intent(MainScreen.this, HotPursuitLeaderBoard.class);
        startActivity(intent);
        finish();
    }
    public void Settings(View view)
    {
        startActivity(SettingsIntent);
        finish();
    }
    public void TimeRushLeaderboard(View view)
    {
        Intent intent = new Intent(MainScreen.this, TimeRushLeaderboard.class);
        startActivity(intent);
        finish();
    }

}