package com.eok.eok;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import com.eok.eok.databinding.ActivityEventInfoBinding;
import com.eok.eok.databinding.ActivityMainScreenBinding;
import com.squareup.picasso.Picasso;

public class EventInfo extends AppCompatActivity {
    private ActivityEventInfoBinding binding;
    private String imageURL;
    private String description;
    private String date;
    private String place;
    private String time;
    private String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventInfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if (isTabletDevice()) {

            binding.title.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.hello_size_high_resolution));

            binding.place.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.hello_size_medium_resolution));

            binding.date.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.hello_size_medium_resolution));

            binding.time.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.hello_size_medium_resolution));

            binding.description.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.hello_size_medium_resolution));




        } else {

            binding.title.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.hello_size_low_resolution));

            binding.place.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_medium_resolution));

            binding.date.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_medium_resolution));

            binding.time.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_medium_resolution));

            binding.description.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_medium_resolution));


        }

        imageURL= getIntent().getStringExtra("photo");
        description = getIntent().getStringExtra("long");
        date = getIntent().getStringExtra("date");
        place = getIntent().getStringExtra("place");
        title = getIntent().getStringExtra("title");
        time = getIntent().getStringExtra("time");

        binding.date.setText("Date: "+date);
        Picasso.get().load(imageURL).into(binding.img);
        binding.title.setText(title);
        binding.time.setText("Time: "+time);
        binding.description.setText(description);
        binding.place.setText("Place: "+place);


    }
    public void back(View v)
    {
        Intent intent = new Intent(EventInfo.this,MainScreen.class);
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