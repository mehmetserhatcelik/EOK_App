package com.eok.eok;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.eok.eok.Adapters.EventAdapter;
import com.eok.eok.Adapters.LeaderBoardAdapter;
import com.eok.eok.Models.Event;
import com.eok.eok.Models.User;
import com.eok.eok.databinding.ActivityMainScreenBinding;
import com.eok.eok.profilesettings.ProfileSettings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainScreen extends AppCompatActivity implements RecyclerViewInterface{

    private FirebaseFirestore fstore;
    private FirebaseAuth auth;
    private FirebaseUser user;

    private Intent HotPursuitIntent ;
    private Intent TimeRushIntent ;

    private Intent SettingsIntent ;
    private ActivityMainScreenBinding binding;
    private RecyclerView recyclerView;
    private ArrayList<Event> list;
    private EventAdapter myAdapter;
    private CollectionReference database;



    private ProgressDialog progressDialog;
    private ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);
        binding = ActivityMainScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data..."  );
        progressDialog.show();

        if (isTabletDevice()) {

            binding.textView4.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.hello_size_high_resolution));
            binding.textView5.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.hello_size_high_resolution));
            binding.textView7.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.back_size_high_resolution));
            binding.textView8.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.hello_size_high_resolution));
            binding.te.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.hello_size_high_resolution));
            binding.but2.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.but_size_high_resolution));
            binding.but3.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.but_size_high_resolution));
            binding.but1.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.but_size_high_resolution));
            binding.but4.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.but_size_high_resolution));
            binding.textView56.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.hello_size_high_resolution));
            binding.textView83.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.hello_size_high_resolution));
            binding.button578.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.but_size_high_resolution));
            binding.button87.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.but_size_high_resolution));


        } else {

            binding.textView4.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.hello_size_low_resolution));
            binding.textView5.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.hello_size_low_resolution));
            binding.textView7.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.back_size_low_resolution));
            binding.textView8.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.hello_size_low_resolution));
            binding.te.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.hello_size_low_resolution));
            binding.but2.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.but_size_low_resolution));
            binding.but3.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.but_size_low_resolution));
            binding.but1.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.but_size_low_resolution));
            binding.but4.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.but_size_low_resolution));
            binding.textView56.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.hello_size_low_resolution));
            binding.textView83.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.hello_size_low_resolution));
            binding.button578.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.but_size_low_resolution));
            binding.button87.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.but_size_low_resolution));


        }
        img = findViewById(R.id.asas);

        TimeRushIntent =new Intent(MainScreen.this, TimeRush.class);
        SettingsIntent =new Intent(MainScreen.this, ProfileSetting.class);

        fstore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        HotPursuitIntent = new Intent(MainScreen.this, HotPursuit.class);

        recyclerView = binding.recview;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        auth = FirebaseAuth.getInstance();
        list = new ArrayList<>();
        myAdapter = new EventAdapter( list,MainScreen.this,isTabletDevice(),this);

        recyclerView.setAdapter(myAdapter);

        EventChangeListener();


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

    public void EventChangeListener()
    {


        database = fstore.collection("Events");
        database.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot d: queryDocumentSnapshots) {
                    Event event = new Event(""+d.get("eventDescription"),""+d.get("shortDescription"),""+d.get("title"),""+d.get("place"),""+d.get("date"),""+d.get("time"),""+d.get("eventImageURL"));
                    list.add(event);
                    myAdapter.notifyDataSetChanged();
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainScreen.this, "Failed to sing in", Toast.LENGTH_SHORT).show();

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
    public boolean isTabletDevice() {
        int screenSize = Configuration.SCREENLAYOUT_SIZE_MASK &
                getResources().getConfiguration().screenLayout;

        return screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE ||
                screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    @Override
    public void onItemClick(int pos) {
        Intent intent = new Intent(MainScreen.this, EventInfo.class);
        intent.putExtra("photo",list.get(pos).getEventImageURL());
        intent.putExtra("long",list.get(pos).getEventDescription());
        intent.putExtra("date",list.get(pos).getDate());
        intent.putExtra("time",list.get(pos).getTime());
        intent.putExtra("title",list.get(pos).getTitle());
        intent.putExtra("place",list.get(pos).getPlace());
        startActivity(intent);
        finish();
    }
}