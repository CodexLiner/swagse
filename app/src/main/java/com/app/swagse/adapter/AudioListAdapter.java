package com.app.swagse.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.swagse.R;
import com.app.swagse.SimpleClasses.Functions;
import com.app.swagse.TEST.AwesomeAudioContentActivity;
import com.app.swagse.activity.SwagTubeDetailsActivity;
import com.app.swagse.model.SongsData;
import com.bumptech.glide.Glide;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.request.DownloadRequest;

import java.io.File;
import java.util.ArrayList;

import codex.CodexPerms;

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.AudioListHolder>{
    Context context;
    private ArrayList<SongsData> list;
    private boolean clicked = false;

    public AudioListAdapter(Context context, ArrayList<SongsData> list) {
        this.context = context;
        this.list = list;
        Log.d("TAG", "AudioListAdapter: "+list.size());
    }

    public AudioListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public AudioListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AudioListHolder(LayoutInflater.from(context).inflate(R.layout.audio_item_layout, parent, false));
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull AudioListHolder holder, int position) {
        if (list.get(position).getImage()!=null){
            Glide.with(holder.itemView).load(list.get(position).getImage()).into(holder.thumb);
        }
        if (list.get(position).getTitle()!=null){
            holder.title.setText(list.get(position).getTitle());
        }
        holder.main_layout.setOnClickListener(v->{
            setTrack(list.get(position).getSong() , list.get(position).getTitle() );
        });
        holder.play.setOnClickListener(v->{
//            ((AwesomeAudioContentActivity)context).playAudio(list.get(0).getSong());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    // to call back for music
    public void setTrack(String url , String title){
        if (clicked){
            return;
        }
        clicked= true;
        Toast.makeText(context, "Please wait while adding music", Toast.LENGTH_SHORT).show();
        ((AwesomeAudioContentActivity) context).TrackDataSetter(url , title);
    }
    static class AudioListHolder extends RecyclerView.ViewHolder {
        ImageView thumb , play;
        TextView title ;
        LinearLayout main_layout;
        public AudioListHolder(@NonNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            play = itemView.findViewById(R.id.play);
            main_layout = itemView.findViewById(R.id.main_layout);
        }
    }
}
