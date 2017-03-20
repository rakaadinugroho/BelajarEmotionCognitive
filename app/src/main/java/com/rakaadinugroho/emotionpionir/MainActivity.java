package com.rakaadinugroho.emotionpionir;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.microsoft.projectoxford.emotion.EmotionServiceClient;
import com.microsoft.projectoxford.emotion.EmotionServiceRestClient;
import com.microsoft.projectoxford.emotion.contract.RecognizeResult;
import com.microsoft.projectoxford.emotion.contract.Scores;
import com.microsoft.projectoxford.emotion.rest.EmotionServiceException;
import com.rakaadinugroho.emotionpionir.helper.ImageHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CAMERA = 100;
    private static final String TAG = MainActivity.class.getSimpleName();
    private Button buttonTakePhoto;
    private Button buttonRecognize;
    private ImageView imageTake;

    private Bitmap bitmap;
    private Uri file;

    private EmotionServiceClient emotionServiceClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            buttonTakePhoto.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        buttonRecognize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Convert Image to Stream */
                ByteArrayOutputStream outputStream  = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                final ByteArrayInputStream inputStream    = new ByteArrayInputStream(outputStream.toByteArray());

                AsyncTask<InputStream, String, List<RecognizeResult>> emotionTask  = new AsyncTask<InputStream, String, List<RecognizeResult>>(){

                    ProgressDialog mProgressDialog  = new ProgressDialog(MainActivity.this);
                    @Override
                    protected List<RecognizeResult> doInBackground(InputStream... inputStreams) {
                        try {
                            publishProgress("Processing . . .");
                            List<RecognizeResult> results   = emotionServiceClient.recognizeImage(inputStreams[0]);
                            return results;
                        } catch (EmotionServiceException e) {
                            e.printStackTrace();
                            return null;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }

                    @Override
                    protected void onPreExecute() {
                        mProgressDialog.show();
                    }

                    @Override
                    protected void onPostExecute(List<RecognizeResult> recognizeResults) {
                        mProgressDialog.dismiss();
                        for (RecognizeResult recognizeResult: recognizeResults){
                            String status   = getDominantEmotion(recognizeResult);
                            imageTake.setImageBitmap(ImageHelper.drawRectOnBitmap(bitmap, recognizeResult.faceRectangle, status));
                        }
                    }

                    @Override
                    protected void onProgressUpdate(String... values) {
                        mProgressDialog.setMessage(values[0]);
                    }
                };

                emotionTask.execute(inputStream);
            }
        });
    }

    private String getDominantEmotion(RecognizeResult recognizeResult) {
        List<Double> listResponseEmotion    = new ArrayList<>();
        Scores scores   = recognizeResult.scores;

        Log.d(TAG, "emotion Marah: "+ scores.anger);
        Log.d(TAG, "emotion Leceh: "+ scores.contempt);
        Log.d(TAG, "emotion Jijik: "+ scores.disgust);
        Log.d(TAG, "emotion Takut: "+ scores.fear);
        Log.d(TAG, "emotion Bahagia: "+ scores.happiness);
        Log.d(TAG, "emotion Netral: "+ scores.neutral);
        Log.d(TAG, "emotion Takut: "+ scores.sadness);
        Log.d(TAG, "emotion Kejut: "+ scores.surprise);
        listResponseEmotion.add(scores.anger);
        listResponseEmotion.add(scores.contempt);
        listResponseEmotion.add(scores.disgust);
        listResponseEmotion.add(scores.fear);
        listResponseEmotion.add(scores.happiness);
        listResponseEmotion.add(scores.neutral);
        listResponseEmotion.add(scores.sadness);
        listResponseEmotion.add(scores.surprise);

        /* Sorting to Get Dominant Emotion Response */
        Collections.sort(listResponseEmotion);

        /* Clasification for WaterMark */
        double getlastdata  = listResponseEmotion.get(listResponseEmotion.size() - 1);
        if (getlastdata == scores.anger)
            return "Marah";
        else if (getlastdata == scores.contempt)
            return "Meremehkan";
        else if (getlastdata == scores.disgust)
            return "Jijik";
        else if (getlastdata == scores.fear)
            return "Takut";
        else if (getlastdata == scores.happiness)
            return "Bahagia";
        else if (getlastdata == scores.neutral)
            return "Netral";
        else if (getlastdata == scores.sadness)
            return "Sedih";
        else
            return "Terkejut";
    }

    private void initViews() {
        emotionServiceClient    = new EmotionServiceRestClient("f260ef6056cf429fbb27c7df23d5d11c");
        buttonRecognize = (Button) findViewById(R.id.buttonRecognize);
        buttonTakePhoto = (Button) findViewById(R.id.buttonTakePhoto);
        imageTake   = (ImageView) findViewById(R.id.imageTake);
    }

    public void openCamera(View view){
        /*
        Intent intent   = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file    = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        startActivityForResult(intent, 100);
        */
        Intent intent   = new Intent(MainActivity.this, ChartAcitivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                buttonTakePhoto.setEnabled(true);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100){
            if (resultCode == RESULT_OK){
                imageTake.setImageURI(file);
                try {
                    bitmap  = MediaStore.Images.Media.getBitmap(this.getContentResolver(), file);
                    Log.d(TAG, "onActivityResult: "+ bitmap.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "onActivityResult: "+ e.toString());
                }
            }
        }
    }

    private static File getOutputMediaFile() {
        File mediaStorageDir    = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Camera Track");
        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }
        String timestamp    = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath()+ File.separator+"IMG_"+timestamp+".jpg");
    }
}
