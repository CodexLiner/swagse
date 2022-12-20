package com.app.swagse.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.swagse.R;
import com.app.swagse.activity.SwagTubeDetailsActivity;
import com.app.swagse.model.swagTube.SwagtubedataItem;
import com.bumptech.glide.Glide;

import java.util.List;

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.SearchViewHolder> {

    Context context;
    List<SwagtubedataItem> swagTubeDataList;

    public SearchRecyclerViewAdapter(Context context, List<SwagtubedataItem> swagTubeDataList) {
        this.context = context;
        this.swagTubeDataList = swagTubeDataList;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchViewHolder(LayoutInflater.from(context).inflate(R.layout.searc_item_list, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        SwagtubedataItem swagtubedataItem = swagTubeDataList.get(position);
        holder.searchTitle.setText(swagtubedataItem.getTitle());
        holder.searchTimeago.setText(swagtubedataItem.getTimeago());
        holder.searchChanelName.setText(swagtubedataItem.getName());
        holder.searchTitle.setText(swagtubedataItem.getTitle());
//        holder.searchViews.setText(swagtubedataItem.getViewscount());
        if (swagtubedataItem.getViewscount() > 0){
            holder.searchViews.setText(String.valueOf(swagtubedataItem.getViewscount()) +" Views");
        }
        Glide.with(holder.itemView).load(swagtubedataItem.getThmubnal()).into(holder.search_img);
        holder.search_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, SwagTubeDetailsActivity.class).putExtra(SearchRecyclerViewAdapter.class.getSimpleName(), swagTubeDataList.get(position).getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return swagTubeDataList == null ? 0 : swagTubeDataList.size();
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView searchTitle ,searchChanelName , searchViews , searchTimeago;
        ImageView search_img , swagTubePic ;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            searchTitle = itemView.findViewById(R.id.searchTitle);
            search_img = itemView.findViewById(R.id.search_img);
            searchChanelName = itemView.findViewById(R.id.searchChanelName);
            searchViews = itemView.findViewById(R.id.searchViews);
            searchTimeago = itemView.findViewById(R.id.searchTimeago);
            swagTubePic = itemView.findViewById(R.id.swagTubePic);
        }
    }
}
