package com.eok.eok.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eok.eok.Models.Event;
import com.eok.eok.Models.User;
import com.eok.eok.R;
import com.eok.eok.RecyclerViewInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> implements RecyclerViewInterface {

    private ArrayList<Event> events;
    private Context context;
    private boolean isTablet;
    private RecyclerViewInterface recyclerViewInterface;
    static int pos;


    public EventAdapter(ArrayList<Event> events, Context context, boolean isTablet, RecyclerViewInterface recyclerViewInterface )
    {
        this.events = events;
        this.context = context;
        this.isTablet = isTablet;
        this.recyclerViewInterface = recyclerViewInterface;
    }
    @NonNull
    @Override
    public EventAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View v = layoutInflater.inflate(R.layout.event_card,parent,false);
        return new EventAdapter.MyViewHolder(v, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.MyViewHolder holder, int position) {

        Event event = events.get(position);

        holder.title.setText(event.getTitle());
        holder.description.setText(event.getShortDescription());
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if(!isTablet)
        {
            holder.title.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.but_size_low_resolution));
            holder.description.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.card_size_low_resolution));
            layoutParams.width = layoutParams.width*3/4;
            layoutParams.height = layoutParams.height*3/4;
        }


        holder.itemView.setLayoutParams(layoutParams);

        Picasso.get().load(event.getEventImageURL()).into(holder.view);

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    @Override
    public void onItemClick(int pos) {

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        TextView description;
        ImageView view;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            title = itemView.findViewById(R.id.eventName);
            description = itemView.findViewById(R.id.description);
            view = itemView.findViewById(R.id.eventImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface != null)
                    {
                        pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION)
                        {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });

        }
    }

}
