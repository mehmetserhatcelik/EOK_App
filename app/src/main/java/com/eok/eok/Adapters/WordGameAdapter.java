package com.eok.eok.Adapters;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eok.eok.R;

import java.util.ArrayList;

public class WordGameAdapter extends RecyclerView.Adapter<WordGameAdapter.MyViewHolder> {


        private ArrayList<Character> letters;
        private Context context;
        private boolean isTablet;
        static int pos;
        private boolean adapted;
        private boolean isKnowed;
        private boolean isResult;



    public WordGameAdapter(ArrayList<Character> letter, Context context, boolean isTablet , boolean isResult)
        {
            this.letters = letter;
            this.context = context;
            this.isTablet = isTablet;
            this.isResult = isResult;

        }
        @NonNull
        @Override
        public WordGameAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View v = layoutInflater.inflate(R.layout.word_game_card_view,parent,false);
        return new WordGameAdapter.MyViewHolder(v);
    }

    public boolean isAdapted() {
        return adapted;
    }

    public boolean isKnowed() {
        return isKnowed;
    }

    public void setKnowed(boolean knowed) {
        isKnowed = knowed;
    }

    public void setAdapted(boolean adapted) {
        this.adapted = adapted;
    }

    @Override
        public void onBindViewHolder(@NonNull WordGameAdapter.MyViewHolder holder, int position) {

        char letter = letters.get(position);

            holder.textView.setText(letter+"");
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if(!isTablet &&!adapted)
            {
                holder.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_size_medium_resolution));

                layoutParams.width = layoutParams.width*3/4;
                layoutParams.height = layoutParams.height*3/4;

                holder.itemView.setLayoutParams(layoutParams);
            }



            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int screenHeight = displayMetrics.widthPixels;

            if(layoutParams.width*getItemCount()>=screenHeight)
            {
                int temp = screenHeight/(getItemCount()+2);
                layoutParams.width = temp;
                holder.itemView.setLayoutParams(layoutParams);
            }

            if(isResult && isKnowed)
            {
                holder.itemView.setBackgroundColor(context.getColor(R.color.easy));
            }
        if(isResult && !isKnowed)
        {
            holder.itemView.setBackgroundColor(context.getColor(R.color.hard));
        }


    }





    @Override
        public int getItemCount() {
        return letters.size();
    }


        public static class MyViewHolder extends RecyclerView.ViewHolder
        {
            TextView textView;


            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                textView = itemView.findViewById(R.id.letter);




            }
        }


}
