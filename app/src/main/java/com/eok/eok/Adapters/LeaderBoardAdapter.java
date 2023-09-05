package com.eok.eok.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eok.eok.Models.User;
import com.eok.eok.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.MyViewHolder>{

    private ArrayList<User> users;
    private Context context;

    public boolean isTablet() {
        return isTablet;
    }

    public void setTablet(boolean tablet) {
        isTablet = tablet;
    }

    private String gameMode;
    private String[] colors = {"#E0B528","#C0C0C0","#CD7F32","#2E4E9B"};
    boolean isTablet ;

    public LeaderBoardAdapter(ArrayList<User> users, Context context, String gameMode)
    {
        this.users = users;
        this.context = context;
        this.gameMode=gameMode;
    }
    @NonNull
    @Override
    public LeaderBoardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View v = layoutInflater.inflate(R.layout.leader_board_card,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderBoardAdapter.MyViewHolder holder, int position) {

        User user = users.get(position);

        holder.userName.setText(user.getName());
        System.out.println(gameMode);
        if(gameMode.equals("HotPursuit")) {
            holder.score.setText("" + user.getHotPursuitRecord());
        }
        else if(gameMode.equals("TimeRush")){
            holder.score.setText(""+user.getTimeRushRecord());}
        else if(gameMode.equals("WordGame")){
            holder.score.setText(""+user.getWordGameRecord());}

        if(user.getUserPhotoUrl().equals("default"))
            Picasso.get().load(R.drawable.wavy).into(holder.view);
        else
            Picasso.get().load(user.getUserPhotoUrl()).into(holder.view);
        if(position == 0)
            holder.itemView.setBackgroundColor(Color.parseColor(colors[0]));
        else if(position == 1)
            holder.itemView.setBackgroundColor(Color.parseColor(colors[1]));
        else if(position == 2)
            holder.itemView.setBackgroundColor(Color.parseColor(colors[2]));
        else {
            holder.itemView.setBackgroundColor(Color.parseColor(colors[3]));
        }
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if(isTablet )
        {

            System.out.println(layoutParams.width+" "+layoutParams.height);
            layoutParams.width = 1480;
            layoutParams.height = 300;
            holder.userName.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.welcometext_size_high_resolution));
            holder.score.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.welcometext_size_high_resolution));

            holder.itemView.setLayoutParams(layoutParams);
        }


        int viewWidth = holder.userName.getWidth();
        if (viewWidth <= 0) return; // View not measured yet

        CharSequence text = user.getName();
        if (text == null || text.length() == 0) return; // No text to display

        Paint textPaint = holder.userName.getPaint();
        float textSize = holder.userName.getTextSize();
        float textWidth = textPaint.measureText(text.toString());

        // Decrease text size if text width exceeds view width
        while (textWidth > viewWidth) {
            textSize -= 1;
            textPaint.setTextSize(textSize);
            textWidth = textPaint.measureText(text.toString());
        }

        // Apply the adjusted text size
        holder.userName.setTextSize(textSize);

    }


    @Override
    public int getItemCount() {
        return users.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView userName;
        TextView score;
        CircleImageView view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.userName);
            score = itemView.findViewById(R.id.score);
            view = itemView.findViewById(R.id.pp);
        }
    }

}
