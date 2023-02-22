package com.app.swagse.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.swagse.CreateVideoSwagger;
import com.app.swagse.LoginActivity;
import com.app.swagse.R;
import com.app.swagse.SimpleClasses.Functions;
import com.app.swagse.SubscriberUserProfileActivity;
import com.app.swagse.activity.MakeVideoActivity;
import com.app.swagse.activity.SwagTubeDetailsActivity;
import com.app.swagse.activity.SwaggerCommentActivity;
import com.app.swagse.constants.Constants;
import com.app.swagse.controller.App;
import com.app.swagse.helper.OwnerGlobal;
import com.app.swagse.model.swagTube.SwagTubeResponse;
import com.app.swagse.model.swagTube.SwagtubedataItem;
import com.app.swagse.model.swaggerData.SwaggerdataItem;
import com.app.swagse.network.Api;
import com.app.swagse.network.Notify;
import com.app.swagse.network.RetrofitClient;
import com.app.swagse.sharedpreferences.PrefConnect;

import com.banuba.sdk.cameraui.data.PipConfig;
import com.banuba.sdk.ve.flow.VideoCreationActivity;
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
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import codex.CodexPerms;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.PUT;

import static com.app.swagse.helper.OwnerGlobal.toast;

public class VideoAdapterNew extends RecyclerView.Adapter<VideoAdapterNew.VideoViewHolder> {

    private static final String TAG = VideoAdapterNew.class.getSimpleName();
    private List<SwaggerdataItem> mVideoItems;
    Context context;
    boolean flag = false;
    boolean volumeFlag = false;
    Api apiInterface;
    private MediaPlayer mediaPlayer;
    private ProgressDialog progressDialog;

    public VideoAdapterNew(Context context, List<SwaggerdataItem> mVideoItems) {
        this.mVideoItems = mVideoItems;
        this.context = context;
    }


    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoAdapterNew.VideoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_videos_container, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.setVideoData(mVideoItems.get(position));
        if (PrefConnect.readBoolean(context, Constants.GUEST_USER, false)) {
            holder.album_view.setVisibility(View.GONE);
        } else {
            holder.album_view.setVisibility(View.VISIBLE);
        }
        if (mVideoItems.get(position).getUserfollowstatus() == 1){
            holder.image_view_follow_option.setImageDrawable(AppCompatResources.getDrawable(context , R.drawable.unfollow_layer));
            holder.follow_btn.setText("Unfollow");
        }

        Glide.with(context).load(PrefConnect.readString(context, Constants.USERPIC, "")).placeholder(R.drawable.ic_user).into(holder.userImage);
        Glide.with(context).load(mVideoItems.get(position).getThmubnal()).placeholder(R.drawable.ic_user).into(holder.image_view_profile_pic);

        holder.image_view_option_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                holder.image_view_option_like.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.like_selector));
//                holder.image_view_option_like_title.setText();
                if (PrefConnect.readBoolean(context, Constants.GUEST_USER, false)) {
                    context.startActivity(new Intent(context, LoginActivity.class));
                } else {
                    if (mVideoItems.get(position).getUserlikestatus() == 1) {
                        mVideoItems.get(position).setUserlikestatus(0);
                        mVideoItems.get(position).setLikecount(mVideoItems.get(position).getLikecount() - 1);
                        holder.image_view_option_like_title.setText("" + mVideoItems.get(position).getLikecount());
                        holder.image_view_option_like.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_like_unfilled));
                        swaggerLike(mVideoItems.get(position).getId());
                    } else if (mVideoItems.get(position).getUserlikestatus() == 0) {
                        mVideoItems.get(position).setUserlikestatus(1);
                        mVideoItems.get(position).setLikecount(mVideoItems.get(position).getLikecount() + 1);
                        holder.image_view_option_like.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.like_selector));
                        holder.image_view_option_like_title.setText("" + mVideoItems.get(position).getLikecount());
                        swaggerLike(mVideoItems.get(position).getId());
                    }
                }
            }
        });

        holder.image_view_option_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PrefConnect.readBoolean(context, Constants.GUEST_USER, false)) {
                    context.startActivity(new Intent(context, LoginActivity.class));
                } else {
                    context.startActivity(new Intent(context, SwaggerCommentActivity.class).putExtra(VideoAdapterNew.class.getSimpleName(), (Serializable) mVideoItems.get(position).getCommentdata()).putExtra("id", mVideoItems.get(position).getId()));
                }
            }
        });

        holder.image_view_option_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Hii");
                    String shareMessage = "\nHi! I found this Post on SwagSe App - check it out now \n\n" + Constants.BASE_URL + "watch/" + OwnerGlobal.getMd5(mVideoItems.get(position).getId());
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    context.startActivity(Intent.createChooser(shareIntent, "Send To"));
                } catch (Exception ignored) {}
            }
        });

        holder.album_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = LayoutInflater.from(context).inflate(R.layout.swagger_menu_layout, null);
                AppCompatTextView reportVideo = view1.findViewById(R.id.reportVideo);
                AppCompatTextView copy_video_url = view1.findViewById(R.id.copy_video_url);
                AppCompatTextView Download = view1.findViewById(R.id.downloadV);
                final Dialog mBottomSheetDialog = new Dialog(context, R.style.MaterialDialogSheet);
                mBottomSheetDialog.setContentView(view1);
                mBottomSheetDialog.setCancelable(true);
                mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
                mBottomSheetDialog.getWindow().getAttributes().horizontalMargin = 10;
                mBottomSheetDialog.show();


                Download.setOnClickListener(v->{
                    downloadVideo(mVideoItems.get(position).getVideourl());
                    mBottomSheetDialog.dismiss();
                });

                reportVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBottomSheetDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Choose a reason for reporting this post:");
                        // add a list
                        String[] animals = {"Not Relevant to SwagSe", "Suspicious or Spam", "Abusive or Obscene", "Posted Multiple Times", "Fake news", "Not Interested"};
                        builder.setItems(animals, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0: // horse
                                    {
                                        removePost(mVideoItems.get(position).getId(), "Not Relevant to SwagSe", holder.getAdapterPosition());
                                        break;
                                    }
                                    case 1: // cow
                                    {
                                        removePost(mVideoItems.get(position).getId(), "Suspicious or Spam", holder.getAdapterPosition());
                                        break;
                                    }
                                    case 2: // camel
                                    {
                                        removePost(mVideoItems.get(position).getId(), "Abusive or Obscene", holder.getAdapterPosition());
                                        break;
                                    }
                                    case 3: // sheep
                                    {
                                        removePost(mVideoItems.get(position).getId(), "Posted Multiple Times", holder.getAdapterPosition());
                                        break;
                                    }
                                    case 4: // sheep
                                    {
                                        removePost(mVideoItems.get(position).getId(), "Fake news", holder.getAdapterPosition());
                                        break;
                                    }
                                }
                            }
                        });

                        // create and show the alert dialog
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });

                copy_video_url.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBottomSheetDialog.dismiss();
                        int sdk = android.os.Build.VERSION.SDK_INT;
                        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            clipboard.setText("text to clip");
                        } else {
                            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            android.content.ClipData clip = android.content.ClipData.newPlainText("copy", mVideoItems.get(position).getVideourl());
                            clipboard.setPrimaryClip(clip);
                        }
                    }
                });

            }
        });

        holder.image_view_profile_pic.setOnClickListener(v -> {
            this.context.startActivity(new Intent(context, SubscriberUserProfileActivity.class).putExtra(PlayerViewHolder.class.getSimpleName(),  mVideoItems.get(position).getUserid()));
        });
        holder.image_view_follow_option.setOnClickListener(v->{
            if (PrefConnect.readBoolean(context, Constants.GUEST_USER, false)) {
                context.startActivity(new Intent(context, LoginActivity.class));
            } else {
                if (mVideoItems.get(position).getUserfollowstatus() == 1) {
                    mVideoItems.get(position).setUserfollowstatus(0);
                    holder.image_view_follow_option.setImageDrawable(AppCompatResources.getDrawable(context , R.drawable.follow_layer));
                } else if (mVideoItems.get(position).getUserfollowstatus() == 0) {
                    mVideoItems.get(position).setUserfollowstatus(1);
                    holder.image_view_follow_option.setImageDrawable(AppCompatResources.getDrawable(context , R.drawable.unfollow_layer));
                }
                swagTubeFollow(mVideoItems.get(0).getUserid());
            }
        });





        holder.follow_btn.setOnClickListener(v -> {
            if (PrefConnect.readBoolean(context, Constants.GUEST_USER, false)) {
                context.startActivity(new Intent(context, LoginActivity.class));
            } else {
                if (mVideoItems.get(position).getUserfollowstatus() == 1) {
                    mVideoItems.get(position).setUserfollowstatus(0);
                    holder.follow_btn.setText("Follow");
                } else if (mVideoItems.get(position).getUserfollowstatus() == 0) {
                    mVideoItems.get(position).setUserfollowstatus(1);
                    holder.follow_btn.setText("Unfollow");
                }
                swagTubeFollow(mVideoItems.get(0).getUserid());
            }
        });

    }
    private void downloadVideo(String urlString) {
       Notify n = Notify.build(context)

                .setTitle("Downloading Video")

                .setSmallIcon(R.drawable.ic_download)
                .setColor(R.color.colorBlack);
        n.show();
        n.setProgress(1);
        n.enableVibration(false);
        if (new CodexPerms((Activity) context).hasPermision(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE})) {
            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/SwagSe");
//            Functions.show_determinent_loader(this, false, false);
            PRDownloader.initialize(context);


            DownloadRequest prDownloader = PRDownloader.download(urlString, file.getPath(), mVideoItems.get(0).getTitle() + "_no_watermark" + ".mp4")
                    .build()
                    .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                        @Override
                        public void onStartOrResume() {

                        }
                    })
                    .setOnPauseListener(new OnPauseListener() {
                        @Override
                        public void onPause() {

                        }
                    })
                    .setOnCancelListener(new OnCancelListener() {
                        @Override
                        public void onCancel() {

                        }
                    })
                    .setOnProgressListener(new OnProgressListener() {
                        @Override
                        public void onProgress(Progress progress) {
                            int prog = (int) ((progress.currentBytes * 100) / progress.totalBytes);
//                            Functions.show_loading_progress(prog);
                            n.setProgress(prog);

                        }
                    });

            prDownloader.start(new OnDownloadListener() {
                @Override
                public void onDownloadComplete() {

                    Functions.cancel_determinent_loader();
                    Toast.makeText(context, "Video downloaded", Toast.LENGTH_SHORT).show();
                    Notify.cancelAll(context);
                    n.setTitle("Download Complete");
                    n.show();

                }

                @Override
                public void onError(Error error) {
                    Notify.cancelAll(context);
                    n.setTitle("Download Failed");
                    n.show();
                    Toast.makeText(context, "Downloading Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            new CodexPerms((Activity) context).requestPerms(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
        }

    }

    private void swagTubeFollow(String postId) {
        if (App.isOnline()) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            apiInterface = RetrofitClient.getInstance().getApi();
            Call<SwagTubeResponse> userResponseCall = apiInterface.swagTubeFollow(postId, PrefConnect.readString(context, Constants.USERID, ""), "");
            userResponseCall.enqueue(new Callback<SwagTubeResponse>() {
                @Override
                public void onResponse(Call<SwagTubeResponse> call, Response<SwagTubeResponse> response) {
                    progressDialog.dismiss();
                    if (response.code() == Constants.SUCCESS) {
                        if (response.body().getStatus().equals("1")) {
                            OwnerGlobal.toast((Activity) context, response.body().getMessage());
                        }
                    } else if (response.code() == Constants.FAILED) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            toast((Activity) context, jObjError.getString("message"));
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }

                    } else if (response.code() == Constants.UNAUTHORIZED) {
                        OwnerGlobal.LoginRedirect((Activity) context);
                    }
                }

                @Override
                public void onFailure(Call<SwagTubeResponse> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });
        }
    }

    private void removePost(String videoId, String fake_news, int adapterPosition) {
        if (App.isOnline()) {
            apiInterface = RetrofitClient.getInstance().getApi();
            Call<SwagTubeResponse> userResponseCall = apiInterface.swaggerReport(videoId, PrefConnect.readString(context, Constants.USERID, ""), fake_news);
            userResponseCall.enqueue(new Callback<SwagTubeResponse>() {
                @Override
                public void onResponse(Call<SwagTubeResponse> call, Response<SwagTubeResponse> response) {
                    if (response.code() == Constants.SUCCESS) {
                        if (response.body().getStatus().equals("1")) {
                            mVideoItems.remove(adapterPosition);
                            notifyItemRemoved(adapterPosition);
                            notifyItemRangeChanged(adapterPosition, mVideoItems.size());
                        }
                    } else if (response.code() == Constants.FAILED) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
//                            toast(context, jObjError.getString("response_msg"));
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }

                    } else if (response.code() == Constants.UNAUTHORIZED) {
//                        OwnerGlobal.LoginRedirect(getActivity());
                    }
                }

                @Override
                public void onFailure(Call<SwagTubeResponse> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t);
                }
            });
        }
    }


    private void swaggerLike(String postId) {
        if (App.isOnline()) {
            apiInterface = RetrofitClient.getInstance().getApi();
            Call<SwagTubeResponse> userResponseCall = apiInterface.swaggerLike(postId, PrefConnect.readString(context, Constants.USERID, ""));
            userResponseCall.enqueue(new Callback<SwagTubeResponse>() {
                @Override
                public void onResponse(Call<SwagTubeResponse> call, Response<SwagTubeResponse> response) {
                    if (response.code() == Constants.SUCCESS) {
                        if (response.body().getStatus().equals("1")) {

                        }
                    } else if (response.code() == Constants.FAILED) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
//                            toast(context, jObjError.getString("response_msg"));
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }

                    } else if (response.code() == Constants.UNAUTHORIZED) {
//                        OwnerGlobal.LoginRedirect(getActivity());
                    }
                }

                @Override
                public void onFailure(Call<SwagTubeResponse> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mVideoItems == null ? 0 : mVideoItems.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        VideoView mVideoView;
        TextView txtTitle, txtDesc, createVideo, follow_btn;
        ProgressBar mProgressBar;
        AppCompatImageView image_view_option_like, image_view_option_share, image_view_option_comment, image_view_follow_option;
        AppCompatTextView image_view_option_like_title, image_view_option_share_title, image_view_option_comment_title;
        ConstraintLayout container;
        AppCompatImageView album_view;
        ImageView ivVolumeControl, volumeControl;
        CircleImageView userImage;
        ConstraintLayout container_profile;
        ShapeableImageView image_view_profile_pic;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            mVideoView = itemView.findViewById(R.id.videoView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDesc = itemView.findViewById(R.id.txtDesc);
            userImage = itemView.findViewById(R.id.userImage);
            mProgressBar = itemView.findViewById(R.id.progressBar);
            container = itemView.findViewById(R.id.container);
            ivVolumeControl = itemView.findViewById(R.id.ivVolumeControl);
            volumeControl = itemView.findViewById(R.id.muteButton);
            createVideo = itemView.findViewById(R.id.createVideo);
            image_view_option_share = itemView.findViewById(R.id.image_view_option_share);
            image_view_option_comment = itemView.findViewById(R.id.image_view_option_comment);
            image_view_option_like_title = itemView.findViewById(R.id.image_view_option_like_title);
            image_view_option_like = itemView.findViewById(R.id.image_view_option_like);
            image_view_option_comment_title = itemView.findViewById(R.id.image_view_option_comment_title);
            image_view_option_share_title = itemView.findViewById(R.id.image_view_option_share_title);
            album_view = itemView.findViewById(R.id.album_view);
            container_profile = itemView.findViewById(R.id.container_profile);
            image_view_follow_option = itemView.findViewById(R.id.image_view_follow_option);
            follow_btn = itemView.findViewById(R.id.follow_btn);
            image_view_profile_pic = itemView.findViewById(R.id.image_view_profile_pic);
        }

        void setVideoData(SwaggerdataItem videoItem) {
            flag = false;
            txtTitle.setText(videoItem.getTitle());
            txtDesc.setText(videoItem.getId());
            mVideoView.setVideoPath(videoItem.getVideourl());

            if (videoItem.getUserlikestatus() == 1) {
                image_view_option_like.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.like_selector));
            } else {
                image_view_option_like.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_like_unfilled));
            }

            image_view_option_like_title.setText("" + videoItem.getLikecount());
            image_view_option_comment_title.setText("" + videoItem.getCommentcount());
            image_view_option_share_title.setText("0");

            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer = mp;
                    mProgressBar.setVisibility(View.GONE);
                    mp.start();

                    float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
                    float screenRatio = mVideoView.getWidth() / (float) mVideoView.getHeight();
                    float scale = videoRatio / screenRatio;
                    if (scale >= 1f) {
                        mVideoView.setScaleX(scale);
                    } else {
                        mVideoView.setScaleY(1f / scale);
                    }
                }
            });
            mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.start();
                }
            });


            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (flag) {
                        flag = false;
                        ivVolumeControl.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_pause_circle_filled_24));
                        mVideoView.start();
                        ivVolumeControl.animate().cancel();
                        ivVolumeControl.setAlpha(1f);
                        ivVolumeControl.animate()
                                .alpha(0f)
                                .setDuration(1000).setStartDelay(2000);
                    } else {
                        flag = true;
                        ivVolumeControl.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_play_circle_filled_24));
                        mVideoView.pause();
                        ivVolumeControl.animate().cancel();
                        ivVolumeControl.setAlpha(1f);
                        ivVolumeControl.animate()
                                .alpha(0f)
                                .setDuration(1000).setStartDelay(2000);
                    }

                }
            });

            volumeControl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!volumeFlag) {
                        mute();
                        volumeFlag = true;
                        volumeControl.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_volume_off));
                        volumeControl.animate().cancel();
                        volumeControl.setAlpha(1f);
                        volumeControl.animate()
                                .alpha(0f)
                                .setDuration(1000).setStartDelay(2000);
                    } else {
                        unmute();
                        volumeFlag = false;
                        volumeControl.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_volume_on));
                        volumeControl.animate().cancel();
                        volumeControl.setAlpha(1f);
                        volumeControl.animate()
                                .alpha(0f)
                                .setDuration(1000).setStartDelay(2000);
                    }
                }
            });

            createVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Activity activity = (Activity) context;
                    activity.startActivity(new Intent(context , CreateVideoSwagger.class));
//                    if (new CodexPerms(activity).hasPermision(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE})) {
//                        Uri[] list = new Uri[0];
//                        Intent videoCreationIntent = new Intent(
//                                VideoCreationActivity.startFromCamera(context, new PipConfig(Uri.EMPTY, false, 0.9F), null, null));
//                        context.startActivity(videoCreationIntent);
//                    } else {
//                        new CodexPerms(activity).requestPerms(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
//                    }
                }
            });

        }
    }

    public void mute() {
        this.setVolume(0);
    }

    public void unmute() {
        this.setVolume(100);
    }

    private void setVolume(int amount) {
        final int max = 100;
        final double numerator = max - amount > 0 ? Math.log(max - amount) : 0;
        final float volume = (float) (1 - (numerator / Math.log(max)));
        try {
            this.mediaPlayer.setVolume(volume, volume);
        } catch (Exception ignored) {
        }
    }

}
