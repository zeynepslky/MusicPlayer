package com.example.musicplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MusicList extends RecyclerView.Adapter<MusicList.ViewHolder>{

    ArrayList<Audio> songsList;
    Context context;

    public MusicList(ArrayList<Audio> songsList,Context context)
    {
        this.songsList = songsList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item,parent,false);
        return new MusicList.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MusicList.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Audio songData = songsList.get(position);
        holder.titleText.setText(songData.getTitle());

        if(MyMediaPlayer.currentIndex == position)
        {
            holder.titleText.setTextColor(Color.parseColor("#FF0000"));
        }
        else
        {
            holder.titleText.setTextColor(Color.parseColor("#000000"));

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyMediaPlayer.getInstance().reset();
                MyMediaPlayer.currentIndex = position;
                Intent intent = new Intent(context,MusicPlayerActivity.class);
                intent.putExtra("LIST",songsList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return songsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView titleText;
        ImageView iconImage;

        public ViewHolder(View itemView) {
            super(itemView);

            titleText = (TextView) itemView.findViewById(R.id.mucic_title);
            iconImage = (ImageView) itemView.findViewById(R.id.icon);
        }
    }
}
