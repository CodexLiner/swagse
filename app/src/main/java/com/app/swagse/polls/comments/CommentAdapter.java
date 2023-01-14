package com.app.swagse.polls.comments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.swagse.R;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.holder> {
    List<comments> list;

    public CommentAdapter(List<comments> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.swagtube_comment_design, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        if (list != null) {
            holder.comment_user_message.setText(list.get(position).getComment());
            if (list.get(position).getUserdata() != null) {
                holder.comment_user_name.setText(list.get(position).getUserdata().getUserName());
                holder.comment_user_time.setVisibility(View.GONE);
                Glide.with(holder.comment_user_pic.getContext()).load(list.get(position).getUserdata().getImg()).into(holder.comment_user_pic);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class holder extends RecyclerView.ViewHolder {
        AppCompatTextView comment_user_message, comment_user_name, comment_user_time;
        CircleImageView comment_user_pic;

        public holder(@NonNull View itemView) {
            super(itemView);
            comment_user_message = itemView.findViewById(R.id.comment_user_message);
            comment_user_name = itemView.findViewById(R.id.comment_user_name);
            comment_user_time = itemView.findViewById(R.id.comment_user_time);
            comment_user_pic = itemView.findViewById(R.id.comment_user_pic);
        }
    }
}
