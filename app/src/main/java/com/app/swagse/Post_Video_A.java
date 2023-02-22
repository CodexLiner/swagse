package com.app.swagse;

import static com.app.swagse.helper.OwnerGlobal.toast;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.app.swagse.SimpleClasses.Functions;
import com.app.swagse.activity.MainActivity;
import com.app.swagse.activity.UploadSwagTubeVideoActivity;
import com.app.swagse.constants.Constants;
import com.app.swagse.constants.Variables;
import com.app.swagse.controller.App;
import com.app.swagse.helper.FileUploadNotification;
import com.app.swagse.helper.OwnerGlobal;
import com.app.swagse.helper.ProgressRequestBody;
import com.app.swagse.model.userDetails.UserDetailResponse;
import com.app.swagse.network.Api;
import com.app.swagse.network.RetrofitClient;
import com.app.swagse.sharedpreferences.PrefConnect;
import com.app.swagse.utils.WebUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Post_Video_A extends AppCompatActivity implements  /*ServiceCallback,*/View.OnClickListener , ProgressRequestBody.UploadCallbacks {

    private static final int PERMISSION_REQUEST = 1120;
    ImageView video_thumbnail;
    String video_path;
    private static final String VIDEO_DIRECTORY = "/swagtube_video";

    //    ServiceCallback serviceCallback;
    EditText swagger_title;
    String draft_file;
    TextView privcy_type_txt;
    Switch comment_switch;
    private Context context = this;
    Bitmap bitmap;
    FileUploadNotification fileUploadNotification;
    private Api apiInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_video);
        apiInterface = RetrofitClient.getInstance().getApi();
        Intent intent = getIntent();
        checkpermission();
        if (intent != null) {
            draft_file = intent.getStringExtra("draft_file");
        }

        Objects.requireNonNull(getSupportActionBar()).setTitle("Upload Swagger Video");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        video_path = Variables.output_filter_file;
        video_thumbnail = findViewById(R.id.video_thumbnail);
        swagger_title = findViewById(R.id.swagger_title);

        if (video_path != null) {
            Glide
                    .with(context)
                    .asBitmap()
                    .load(Uri.fromFile(new File(video_path)))
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(video_thumbnail);
            bitmap = ThumbnailUtils.createVideoThumbnail(new File(video_path).getAbsolutePath(), MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
            saveVideoToInternalStorage(video_path);


        }

        privcy_type_txt = findViewById(R.id.privcy_type_txt);
        comment_switch = findViewById(R.id.comment_switch);

        comment_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        findViewById(R.id.Goback).setOnClickListener(this);

        findViewById(R.id.privacy_type_layout).setOnClickListener(this);
        findViewById(R.id.post_btn).setOnClickListener(v -> {
            if (PrefConnect.readBoolean(Post_Video_A.this, Constants.GUEST_USER, false)) {
                startActivity(new Intent(Post_Video_A.this, LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                OwnerGlobal.toast(Post_Video_A.this, "Video is Uploading in Background");
                UpdateProfileService(uploadSwagTubePost(), getImg());
            }
        });
        findViewById(R.id.save_draft_btn).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.Goback:
                onBackPressed();
                break;

            case R.id.privacy_type_layout:
//                privacy_dialog();
                break;

            case R.id.save_draft_btn:
                save_file_in_draft();
                break;

            case R.id.post_btn:
//                start_Service();
                break;
        }
    }



/*    private void privacy_dialog() {
        final CharSequence[] options = new CharSequence[]{"Public","Friend","Private"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogCustom);

        builder.setTitle(null);

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {
                privcy_type_txt.setText(options[item]);

            }

        });

        builder.show();

    }*/

/*
    // this will start the service for uploading the video into database
    public void start_Service(){

        serviceCallback=this;

        Upload_Service mService = new Upload_Service(serviceCallback);
        if (!Functions.isMyServiceRunning(this,mService.getClass())) {
            Intent mServiceIntent = new Intent(this.getApplicationContext(), mService.getClass());
            mServiceIntent.setAction("startservice");
            mServiceIntent.putExtra("draft_file",draft_file);
            mServiceIntent.putExtra("uri",""+ video_path);
            mServiceIntent.putExtra("desc",""+description_edit.getText().toString());
            mServiceIntent.putExtra("privacy_type",privcy_type_txt.getText().toString());

            if(comment_switch.isChecked())
              mServiceIntent.putExtra("allow_comment","true");
             else
                mServiceIntent.putExtra("allow_comment","false");

            startService(mServiceIntent);


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendBroadcast(new Intent("uploadVideo"));
                    startActivity(new Intent(Post_Video_A.this, MainMenuActivity.class));
                }
            },1000);


        }
        else {
            Toast.makeText(this, "Please wait video already in uploading progress", Toast.LENGTH_LONG).show();
        }


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
//        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }


/*
    // when the video is uploading successfully it will restart the appliaction
    @Override
    public void showResponce(final String responce) {

        if(mConnection!=null)
        unbindService(mConnection);
        Toast.makeText(this, responce, Toast.LENGTH_SHORT).show();

    }


    // this is importance for binding the service to the activity
    Upload_Service mService;
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

           Upload_Service.LocalBinder binder = (Upload_Service.LocalBinder) service;
            mService = binder.getService();

            mService.setCallbacks(Post_Video_A.this);



        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

        }
    };
*/


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void save_file_in_draft() {
        File source = new File(video_path);
        File destination = new File(Variables.draft_app_folder + Functions.getRandomString() + ".mp4");
        try {
            if (source.exists()) {

                InputStream in = new FileInputStream(source);
                OutputStream out = new FileOutputStream(destination);

                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                in.close();
                out.close();

                Toast.makeText(Post_Video_A.this, "File saved in Draft", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Post_Video_A.this, MainActivity.class));

            } else {
                Toast.makeText(Post_Video_A.this, "File failed to saved in Draft", Toast.LENGTH_SHORT).show();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // upload video starts here
    public Map<String, RequestBody> uploadSwagTubePost() {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("userid", WebUtils.createRequest(PrefConnect.readString(Post_Video_A.this, Constants.USERID, "")));
        map.put("post_title", WebUtils.createRequest(swagger_title.getText().toString()));
        map.put("post_description", WebUtils.createRequest(swagger_title.getText().toString()));

        if (bitmap != null) {
            map.put("thumbnail_video", WebUtils.createRequest(getBase64String(bitmap)));
        }
        return map;
    }

    private String getBase64String(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG, 60, baos);

        byte[] imageBytes = baos.toByteArray();

        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
    }

    public long durationTime() throws IOException {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//       use one of overloaded setDataSource() functions to set your data source
        retriever.setDataSource(Post_Video_A.this, Uri.fromFile(new File(video_path)));
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInMillisec = Long.parseLong(time);
        Log.d("TAG", "timeInMillisecond: " + timeInMillisec);
        retriever.release();
        return timeInMillisec;
    }

    public MultipartBody.Part getImg() {
        return WebUtils.getImagePart("file-input", new File(video_path));
    }

    private void saveVideoToInternalStorage(String filePath) {
        File saved_file;
        try {
            File currentFile = new File(filePath);
            File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + VIDEO_DIRECTORY);
            saved_file = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".mp4");
            if (!wallpaperDirectory.exists()) {
                wallpaperDirectory.mkdirs();
            }
            if (currentFile.exists()) {
                InputStream in = new FileInputStream(currentFile);
                OutputStream out = new FileOutputStream(saved_file);
                // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                Log.v("vii", "Video file saved successfully.");
            } else {
                Log.v("vii", "Video saving failed. Source file missing.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateProfileService(Map<String, RequestBody> stringRequestBodyMap, MultipartBody.Part img) {
        if (App.isOnline()) {
            // if (isValid()) {
//            progressDialog.show();
            fileUploadNotification = new FileUploadNotification(Post_Video_A.this);
            Call<UserDetailResponse> call = apiInterface.uploadSwaggerVideo(stringRequestBodyMap, img);
            call.enqueue(new Callback<UserDetailResponse>() {
                @Override
                public void onResponse(Call<UserDetailResponse> call, @NonNull Response<UserDetailResponse> response) {
//                    progressDialog.dismiss();
                    Log.d("TAG", "postvideoswagger errorbody : " + response.body());
                    if (response.code() == Constants.SUCCESS) {
                        Toast.makeText(Post_Video_A.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        File file = new File(video_path);
                        file.delete();
                        finish();
                    } else if (response.code() == Constants.FAILED) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            toast(Post_Video_A.this, jObjError.getString("response_msg"));
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    } else if (response.code() == Constants.UNAUTHORIZED) {
                        OwnerGlobal.LoginRedirect(Post_Video_A.this);
                    }
                }

                @Override
                public void onFailure(Call<UserDetailResponse> call, Throwable t) {
                    Log.d("TAG", "postvideoswagger failed: " + t);
                }
            });
            //}
        } else {
            OwnerGlobal.networkToast(Post_Video_A.this);
        }
    }


    public boolean checkpermission() {
        ArrayList<String> permissionList = new ArrayList<>();
        if (checkSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED) {
            permissionList.add(Manifest.permission.INTERNET);
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            permissionList.add(Manifest.permission.CAMERA);
        }

        if (permissionList.size() > 0) {
            String[] permissionArray = new String[permissionList.size()];
            permissionList.toArray(permissionArray);
            requestPermissions(permissionArray, PERMISSION_REQUEST);
            return false;
        }
        return true;
    }

    @Override
    public void onProgressUpdate(int percentage) {
        FileUploadNotification.updateNotification(String.valueOf(percentage), video_path, "Uploading");
    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {
        FileUploadNotification.updateNotification("100", video_path, "Uploaded");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Variables.CreateButton = true;
    }
}
