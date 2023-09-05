package com.eok.eok;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.eok.eok.Adapters.WordGameAdapter;
import com.eok.eok.databinding.ActivityMainScreenBinding;
import com.eok.eok.databinding.ActivityWordGameResultBinding;

import java.util.ArrayList;
import java.util.Locale;

public class WordGameResult extends AppCompatActivity {
    private ActivityWordGameResultBinding binding;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 3000;
    private RecyclerView recyclerView;
    private ArrayList<Character> letter;
    private WordGameAdapter myAdapter;
    private boolean isKnowed;
    String word;
    long timer;
    int points;
    int currenQuestionNumber;
    private long record;
    private String name;
    private String pp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWordGameResultBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        startCountdown();

        if (isTabletDevice()) {

            binding.roundpoint.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.hello_size_medium_resolution));
            binding.takenletterno.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.hello_size_medium_resolution));
            binding.gainedpoint.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.hello_size_medium_resolution));
            binding.givenAnswer.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.hello_size_medium_resolution));
            binding.timer.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.hello_size_medium_resolution));
            binding.totalPoints.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.hello_size_medium_resolution));



        } else {
            binding.roundpoint.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_medium_resolution));
            binding.takenletterno.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_medium_resolution));
            binding.gainedpoint.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_medium_resolution));
            binding.givenAnswer.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_medium_resolution));
            binding.timer.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_medium_resolution));
            binding.totalPoints.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_medium_resolution));

        }
        record = getIntent().getLongExtra("record",0);
        pp = getIntent().getStringExtra("pp");
        System.out.println(pp);
        name = getIntent().getStringExtra("name");

        String answer = getIntent().getStringExtra("answer");
         currenQuestionNumber= getIntent().getIntExtra("q",1);
        currenQuestionNumber= currenQuestionNumber+1;
         timer = getIntent().getLongExtra("time",24000);
        points = getIntent().getIntExtra("points",0);
        word = getIntent().getStringExtra("word");
        int takenletter = getIntent().getIntExtra("letterno",0);

        binding.roundpoint.setText("Sorunun Puan Değeri: "+(word.length()*100));
        binding.takenletterno.setText("Alınan Harf Sayısı: "+takenletter);
        int gainedpoints = 0;
        if(answer.equalsIgnoreCase(word))
            gainedpoints= ((word.length()-takenletter)*100);
        points += gainedpoints;
        binding.gainedpoint.setText("Bu Turda Kazanılan Puan: "+gainedpoints);
        binding.totalPoints.setText("Total Puan: "+points);
        binding.givenAnswer.setText("Given Answer: " + answer);

        recyclerView = binding.recyclerView2;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        letter = new ArrayList<>();
        myAdapter = new WordGameAdapter( letter,WordGameResult.this,isTabletDevice(),true);
        myAdapter.setKnowed(answer.replace(" ","").equalsIgnoreCase(word));
        recyclerView.setAdapter(myAdapter);


        EventChangeListener();

    }
    public void EventChangeListener()
    {
        myAdapter.setAdapted(false);
        letter.clear();
        for (int i = 0; i < word.length(); i++) {

                letter.add(word.charAt(i));

        }
        myAdapter.notifyDataSetChanged();
    }
    private void updateCountdownText() {
        int seconds = (int) (timeLeftInMillis / 1000);
        binding.timer.setText("Next Round Starts in "+String.valueOf(seconds)+"s");
    }public void startCountdown() {

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                if(currenQuestionNumber<15) {
                    Intent intent = new Intent(WordGameResult.this, WordGame.class);
                    Log.d("Resultistan",pp);
                    intent.putExtra("time", timer);
                    intent.putExtra("points", points);
                    intent.putExtra("q", currenQuestionNumber);
                    intent.putExtra("record", record);
                    intent.putExtra("pp", pp);
                    intent.putExtra("name", name);

                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(WordGameResult.this, WordGameEnd.class);
                    intent.putExtra("total",points);
                    intent.putExtra("record", record);
                    intent.putExtra("pp", pp);
                    intent.putExtra("name", name);
                    startActivity(intent);
                    finish();
                }
                updateCountdownText();
            }
        }.start();
    }

    public boolean isTabletDevice() {
        int screenSize = Configuration.SCREENLAYOUT_SIZE_MASK &
                getResources().getConfiguration().screenLayout;

        return screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE ||
                screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }
}