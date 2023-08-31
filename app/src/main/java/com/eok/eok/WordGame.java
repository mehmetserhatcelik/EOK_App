package com.eok.eok;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eok.eok.Adapters.EventAdapter;
import com.eok.eok.Adapters.WordGameAdapter;
import com.eok.eok.Models.Event;
import com.eok.eok.Models.WordGameSoru;
import com.eok.eok.databinding.ActivityMainScreenBinding;
import com.eok.eok.databinding.ActivityWordGameBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class WordGame extends AppCompatActivity {
    private ActivityWordGameBinding binding;
    private RecyclerView recyclerView;
    private ArrayList<Character> letter;
    private WordGameAdapter myAdapter;
    private boolean isTimeLocked;
    private boolean isTimeFinished;
    private int currentQuestionNumber;
    private ArrayList<WordGameSoru> questions4;
    private ArrayList<WordGameSoru> questions5;
    private ArrayList<WordGameSoru> questions6;
    private ArrayList<WordGameSoru> questions7;
    private ArrayList<WordGameSoru> questions8;
    private ArrayList<WordGameSoru> questions9;
    private ArrayList<WordGameSoru> questions10;




    private long counter;
    private GameBeginDialog calendarDialog;
    private int totalPoints;
    private TextView countdownTextView;
    private CountDownTimer countDownTimer;
    private CountDownTimer generalCountDownTimer;
    private long timeLeftInMillis = 10000;
    private WordGameSoru question;
    private boolean[] known;
    private boolean adapted;
    private int takenletter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWordGameBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        takenletter = 0;

        if (isTabletDevice()) {

            binding.counter.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.hello_size_high_resolution));
            binding.smallTimer.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.hello_size_high_resolution));
            binding.points.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_high_resolution));
            binding.thisRoundPoint.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_high_resolution));
            binding.textView12.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_high_resolution));
            binding.editTextTextPersonName.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_high_resolution));
            binding.button5.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.but_size_high_resolution));
            binding.button6.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.but_size_high_resolution));



        } else {

            binding.counter.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.hello_size_low_resolution));
            binding.smallTimer.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.hello_size_low_resolution));
            binding.points.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_medium_resolution));
            binding.thisRoundPoint.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_medium_resolution));
            binding.textView12.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_medium_resolution));
            binding.editTextTextPersonName.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_medium_resolution));
            binding.button5.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.but_size_low_resolution));
            binding.button6.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.but_size_low_resolution));


        }

        currentQuestionNumber = getIntent().getIntExtra("q",1);
        counter = getIntent().getLongExtra("time",240000);
        isTimeFinished = counter == 0;
        countdownTextView = binding.smallTimer;
        countdownTextView.setVisibility(View.INVISIBLE);
        totalPoints = getIntent().getIntExtra("points",0);
        questions4 = new ArrayList<>();
        questions5 = new ArrayList<>();
        questions6 = new ArrayList<>();
        questions7 = new ArrayList<>();
        questions8 = new ArrayList<>();
        questions9 = new ArrayList<>();
        questions10 = new ArrayList<>();

        binding.points.setText( "Total Puan: "+totalPoints);
        loadJson();
        binding.editTextTextPersonName.setEnabled(false);
        binding.textView12.setText(question.getDefinition());
        binding.thisRoundPoint.setText((question.length()*100)+" puan değerinde soru");
        known = new boolean[question.getWord().length()];
        adapted = false;
        if(currentQuestionNumber == 1)
            openDialog();
        else
            pb();

        Thread t = new Thread()
        {
            @Override
            public void run()
            {
                while (isTimeLocked ==false && isTimeFinished==false && timeLeftInMillis!=0)
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
                if(isTimeFinished==true || timeLeftInMillis==0) {

                    if(isTimeFinished==true)
                    {

                        Intent intent = new Intent(WordGame.this,WordGameEnd.class);
                        intent.putExtra("lost",true);
                        startActivity(intent);
                        finish();
                    }
                    if(currentQuestionNumber==15)
                    {
                        Intent intent = new Intent(WordGame.this,WordGameEnd.class);
                        intent.putExtra("lost",false);
                        intent.putExtra("total",totalPoints);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Intent intent = new Intent(WordGame.this, WordGameResult.class);
                        intent.putExtra("answer",binding.editTextTextPersonName.getText().toString());
                        intent.putExtra("q",currentQuestionNumber);
                        intent.putExtra("time",counter);
                        intent.putExtra("points",totalPoints);
                        intent.putExtra("word",question.getWord());
                        intent.putExtra("letterno",takenletter);
                        startActivity(intent);
                        finish();}

                }
            }
        };
        t.start();

        recyclerView = binding.recyclerView2;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        letter = new ArrayList<>();
        myAdapter = new WordGameAdapter( letter,WordGame.this,isTabletDevice(),false);

        recyclerView.setAdapter(myAdapter);


        EventChangeListener();


    }
    public void EventChangeListener()
    {
        myAdapter.setAdapted(adapted);
        letter.clear();
        for (int i = 0; i < question.getWord().length(); i++) {
            if(!known[i])
            {
                letter.add(' ');
            }
            else{
                letter.add(question.getWord().charAt(i));
            }
        }
        myAdapter.notifyDataSetChanged();
    }
    public void openDialog()
    {
        calendarDialog = new GameBeginDialog(this);
        calendarDialog.show(getSupportFragmentManager(),"Calendar Dialog");
    }
    public void pb()
    {

        generalCountDownTimer = new CountDownTimer(counter, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                counter = millisUntilFinished;
                updateGeneralCountdownText();
            }

            @Override
            public void onFinish() {
                counter = 0;
                isTimeFinished = true;
                updateGeneralCountdownText();
            }
        }.start();

    }
    private void updateGeneralCountdownText() {
        int seconds = (int) (counter / 1000);
        String temp = seconds/60 +":"+seconds%60;
        binding.counter.setText(temp);
    }
    public boolean isTabletDevice() {
        int screenSize = Configuration.SCREENLAYOUT_SIZE_MASK &
                getResources().getConfiguration().screenLayout;

        return screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE ||
                screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }
    public void startCountdown() {

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountdownText();
            }
        }.start();
    }
    public void getLetter(View v)
    {
        if(takenletter<=question.length()){
        takenletter++;
        adapted = true;
        Random rand = new Random();
        int i;
        do{
        i= rand.nextInt(question.getWord().length());
        }while(known[i]);
        known[i] = true;
        EventChangeListener();}
    }
    private void loadJson()
    {

        try {

            InputStream inputStream =getAssets().open("data.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            String json ;
            int max;

            json = new String(buffer, StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(json);
            max =jsonArray.length();

            String definition, word, length;


            for (int i = 0; i < max; i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                definition = jsonObject.getString("Sorular");
                word = jsonObject.getString("Cevap");
                length = jsonObject.getString("Harf Sayısı");


                if (length.equals("4"))
                    questions4.add(new WordGameSoru(definition.trim(), word.trim().toUpperCase()));
                if (length.equals("5"))
                    questions5.add(new WordGameSoru(definition.trim(), word.trim().toUpperCase()));
                if (length.equals("6"))
                    questions6.add(new WordGameSoru(definition.trim(), word.trim().toUpperCase()));
                if (length.equals("7"))
                    questions7.add(new WordGameSoru(definition.trim(), word.trim().toUpperCase()));
                if (length.equals("8"))
                    questions8.add(new WordGameSoru(definition.trim(), word.trim().toUpperCase()));
                if (length.equals("9"))
                    questions9.add(new WordGameSoru(definition.trim(), word.trim().toUpperCase()));
                if (length.equals("10"))
                    questions10.add(new WordGameSoru(definition.trim(), word.trim().toUpperCase()));


            }

            getWord();


        }
        catch (Exception e)
        {
            Log.e("Tag","loadJson: error "+e);
        }
    }
    public void getWord()
    {
        Random rand = new Random();

        if(currentQuestionNumber ==1 || currentQuestionNumber == 2)
        {
            int i = rand.nextInt(questions4.size());
            question = questions4.get(i);
        }
        if(currentQuestionNumber ==3 || currentQuestionNumber == 4)
        {

            int i = rand.nextInt(questions5.size());
            question = questions5.get(i);
            System.out.println(question.getWord());
        }
        if(currentQuestionNumber ==5 || currentQuestionNumber == 6)
        {
            int i = rand.nextInt(questions6.size());
            question = questions6.get(i);
        }
        if(currentQuestionNumber ==7 || currentQuestionNumber == 8)
        {
            int i = rand.nextInt(questions7.size());
            question = questions7.get(i);
        }
        if(currentQuestionNumber ==9 || currentQuestionNumber == 10)
        {
            int i = rand.nextInt(questions8.size());
            question = questions8.get(i);
        }
        if(currentQuestionNumber ==11 || currentQuestionNumber == 12)
        {
            int i = rand.nextInt(questions9.size());
            question = questions9.get(i);
        }
        if(currentQuestionNumber ==13 || currentQuestionNumber == 14)
        {
            int i = rand.nextInt(questions10.size());
            question = questions10.get(i);
        }

    }

    private void updateCountdownText() {
        int seconds = (int) (timeLeftInMillis / 1000);
        countdownTextView.setText(String.valueOf(seconds));
    }
    public void lockTime(View V)
    {
        binding.button5.setEnabled(false);
        binding.editTextTextPersonName.setEnabled(true);
        countdownTextView.setVisibility(View.VISIBLE);
        generalCountDownTimer.cancel();
        startCountdown();
    }
}