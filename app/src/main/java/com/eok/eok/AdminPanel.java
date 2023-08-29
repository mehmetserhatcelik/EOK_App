package com.eok.eok;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
}