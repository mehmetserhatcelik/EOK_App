package com.eok.eok;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import com.eok.eok.databinding.ActivityHotPursuitEndBinding;
import com.eok.eok.databinding.ActivityTimeRushEndBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class TimeRushEnd extends AppCompatActivity {

    private ActivityTimeRushEndBinding binding;
    private String photUrl;
    private String name;
    private long score;
    private long record;
    private FirebaseAuth auth;
    private FirebaseFirestore fstore;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimeRushEndBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (isTabletDevice()) {

            binding.title.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.high));
            binding.ta.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.high));
            binding.tas.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.high));
            binding.score.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.high));
            binding.record.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.high));




        } else {
            binding.title.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.low));
            binding.ta.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.low));
            binding.tas.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.low));
            binding.score.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.low));
            binding.record.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.low));

        }


        photUrl = getIntent().getStringExtra("pp");
        name = getIntent().getStringExtra("name");
        score = getIntent().getIntExtra("totalpoints",0);
        record = getIntent().getIntExtra("record",0);

        if(photUrl.equals("default"))
            Picasso.get().load(R.drawable.wavy).into(binding.pp);
        else
            Picasso.get().load(photUrl).into(binding.pp);

        binding.name.setText(name);
        binding.score.setText(""+score);
        fstore =  FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        if (score>record)
        {
            user = auth.getCurrentUser();
            DocumentReference documentReference = fstore.collection("Users").document(user.getUid());
            Map<String,Object> userInfo = new HashMap<>();
            userInfo.put("timeRushRecord",score);
            documentReference.update(userInfo);

            binding.record.setText(score+"");
            record = score;

        }else {
            binding.score.setText(record+"");
        }

    }
    public void main(View v)
    {
        Intent intent = new Intent(TimeRushEnd.this, MainScreen.class);
        startActivity(intent);
        finish();
    }
    public void again(View v)
    {
        Intent intent = new Intent(TimeRushEnd.this, TimeRush.class);
        intent.putExtra("name",name);
        intent.putExtra("pp",photUrl);
        intent.putExtra("record",record);
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