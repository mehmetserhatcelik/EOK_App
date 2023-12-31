package com.eok.eok;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.sql.Time;

public class GameBeginDialog extends AppCompatDialogFragment {

    boolean isReady;
    HotPursuit context;
    TimeRush context2;
    WordGame context3;
    int type;

    public GameBeginDialog(HotPursuit singlePlayer)
    {
        this.context=singlePlayer;
        type = 0;
    }
    public GameBeginDialog(WordGame wordGame)
    {
        this.context3=wordGame;
        type = 2;
    }
    public GameBeginDialog(TimeRush singlePlayer)
    {
        this.context2=singlePlayer;
        type =1;
    }

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog, null);

        if(type == 0)
            view.findViewById(R.id.as).setOnClickListener(this::getReady);
        else if(type == 1) {
            view.findViewById(R.id.as).setOnClickListener(this::getReady2);}
        else {
            view.findViewById(R.id.as).setOnClickListener(this::getReady3);
        }

        isReady = false;
        builder.setView(view);
        return builder.create();
    }
    public void getReady(View view)
    {
        dismiss();
        isReady = true;
        context.pb();

    }
    public void getReady2(View view)
    {
        dismiss();
        isReady = true;
        context2.pb();

    }
    public void getReady3(View view)
    {
        dismiss();
        isReady = true;
        context3.pb();

    }
    public boolean isReady()
    {
        return isReady;
    }
}
