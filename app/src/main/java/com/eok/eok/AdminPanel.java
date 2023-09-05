package com.eok.eok;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class AdminPanel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
    }
    public void addevent(View v)
    {
        Intent intent = new Intent(AdminPanel.this,AddEvent.class);
        startActivity(intent);
        finish();

    }
    public void addphoto(View v)
    {
        Intent intent = new Intent(AdminPanel.this,PhotoAddScreen.class);
        startActivity(intent);
        finish();

    }
    public void logout(View v) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        SharedPreferences preferences = getSharedPreferences("switch",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("remember",false);
        editor.apply();
        Intent intent = new Intent(this, LogInPage.class);
        startActivity(intent);
        finish();
    }

}