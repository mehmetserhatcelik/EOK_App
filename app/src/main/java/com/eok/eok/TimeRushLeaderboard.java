package com.eok.eok;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.eok.eok.Adapters.LeaderBoardAdapter;
import com.eok.eok.Models.User;
import com.eok.eok.databinding.ActivityHotPursuitLeaderBoardBinding;
import com.eok.eok.databinding.ActivityTimeRushBinding;
import com.eok.eok.databinding.ActivityTimeRushLeaderboardBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TimeRushLeaderboard extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FirebaseFirestore fstore;
    private CollectionReference database;
    private LeaderBoardAdapter myAdapter;
    private FirebaseAuth auth;
    private ArrayList<User> list;
    private ProgressDialog progressDialog;

    private ActivityTimeRushLeaderboardBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimeRushLeaderboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        if (isTabletDevice()) {

            binding.l.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.hello_size_high_resolution));





        } else {
            binding.l.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.hello_size_low_resolution));



        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data..."  );
        progressDialog.show();

        recyclerView = binding.recyclerView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        auth = FirebaseAuth.getInstance();
        list = new ArrayList<>();
        myAdapter = new LeaderBoardAdapter ( list,TimeRushLeaderboard.this,"TimeRush" );
        myAdapter.setTablet(isTabletDevice());
        recyclerView.setAdapter(myAdapter);

        EventChangeListener();

    }
    public void EventChangeListener()
    {
        fstore = FirebaseFirestore.getInstance();

        database = fstore.collection("Users");
        database.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot d: queryDocumentSnapshots) {
                    if((long)d.get("isAdmin")!=1)
                    {
                    User user = new User(""+d.get("name"),""+d.get("userPhotoUrl"),(long)d.get("timeRushRecord"));

                    list.add(user);
                    for (int i = 0; i < list.size(); i++) {
                        for (int j = 0; j < list.size() - i - 1; j++) {
                            if (list.get(j).getTimeRushRecord() < list.get(j + 1).getTimeRushRecord()) {
                                // Swap scores[j] and scores[j + 1]
                                User temp = list.get(j);
                                list.set(j, list.get(j + 1));
                                list.set(j + 1, temp);
                            }
                        }
                    }}
                    myAdapter.notifyDataSetChanged();
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TimeRushLeaderboard.this, "Failed to sing in", Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void back(View v)
    {
        Intent intent = new Intent(TimeRushLeaderboard.this, MainScreen.class);
        startActivity(intent);
        finish();
    }
    public boolean isTabletDevice() {
        int screenSize = Configuration.SCREENLAYOUT_SIZE_MASK &
                getResources().getConfiguration().screenLayout;

        return screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE ||
                screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }
}