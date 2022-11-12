package com.epsengco.onlineatmsupport;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Layout;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SendQuestionActivity extends AppCompatActivity {

    private int progressStatus = 0;
    private Handler handler = new Handler();

    private EditText Question;
    private ImageButton PlayVoice;
    private ProgressBar progressBar;
    private ImageView ErrorPic;
    private ImageButton PicViewer;
    String Path1 = null;

    private ImageView ErrorPicViewer;
    int BigPreview = 1;

    private TextView Navigate;
    //private Layout ErrorTextViewerLayout;


    Bitmap pic1 = null;

    String Username = "";
    int Accounttype = -1;
    String Accounttypename = "";
    String Check = "";
    String ProductName = "";
    String HardSoft = "";
    String ProductModel = "";
    String SelectModelstr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_question);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //$.B

        //$.B \/

        Bundle extras = getIntent().getExtras();//Recive Message
        if(extras !=null) {
            Username = extras.getString("Username");
            Accounttype = extras.getInt("Accounttype");
            Accounttypename = extras.getString("Accounttypename");
            Check = extras.getString("Check");
            ProductName = extras.getString("ProductName");
            //ProductModel = extras.getString("ProductModel");
        }
        Navigate = (TextView)findViewById(R.id.textnavigate);
        //Navigate.setText(Username + " > " + Accounttypename+ " > " + ProductName + " > ");
        Navigate.setText(Username + " > " + Accounttypename+ " > Local Inbox");


        Question = (EditText)findViewById(R.id.questionbox);
        PlayVoice = (ImageButton)findViewById(R.id.Voiceplayerbtn);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        ErrorPic = (ImageView)findViewById(R.id.Erro);
        PicViewer = (ImageButton)findViewById(R.id.Errorpicbtn);

        ErrorPicViewer = (ImageView)findViewById(R.id.picpreview);
        @SuppressLint("WrongViewCast") final View ErrorTextViewerLayout = (View) findViewById(R.id.errortextviewerlayout);

        //$.B _ Read Message.txt \/
        final File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + Username +"_SB_Inbox");
        boolean success = true;
        if (!folder.exists()) {
            //success = folder.mkdirs();
            Toast.makeText(getApplicationContext(), "Don't have any question files in your application", Toast.LENGTH_SHORT).show();
        }
        if (success) {
            // Do something on success
            ReadtxtMessage(folder.toString());
            SetPic(folder.toString());

        } else {
            // Do something else on failure
        }
        //$.B _ Read Message.txt /\

        PlayVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressStatus = 0;

                new Thread(new Runnable() {
                    public void run() {

                        while (progressStatus < 100) {
                            progressStatus += 10;
                            // Update the progress bar and display the
                            //current value in the text view
                            handler.post(new Runnable() {
                                public void run() {
                                    progressBar.setProgress(progressStatus);
                                    if (progressStatus >99){//Enable VoiceButton
                                        Enable();
                                    }
                                }
                            });
                            try {
                                // Sleep for 200 milliseconds.
                                Thread.sleep(1500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();

                Disable();//Disable All Button or ...

                audioPlayer(folder.toString(),"voice.mp3");
            }
        });

        PicViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BigPreview == 1){
                    ErrorPicViewer.setVisibility(View.VISIBLE);
                    //Question.setText("");
                    ErrorTextViewerLayout.setVisibility(View.INVISIBLE);
                    BigPreview = 0;
                }else if (BigPreview == 0){
                    ErrorPicViewer.setVisibility(View.INVISIBLE);
                    //ReadtxtMessage(folder.toString());
                    ErrorTextViewerLayout.setVisibility(View.VISIBLE);
                    BigPreview = 1;
                }

            }
        });

    }

    private void ReadtxtMessage(String Path){
        //Find the directory for the SD Card using the API
//*Don't* hardcode "/sdcard"
        File sdcard = Environment.getExternalStorageDirectory();

//Get the text file
        //File file = new File(sdcard,"file.txt");
        File file1 = new File(Path,"Message.txt");

//Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file1));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }

//Find the view by its id
        //TextView tv = (TextView)findViewById(R.id.text_view);

//Set the text
        //tv.setText(text.toString());

        Question.setText(text.toString());
    }

    public void audioPlayer(String path, String fileName){
        //set up MediaPlayer
        MediaPlayer mp = new MediaPlayer();

        try {
            mp.setDataSource(path + File.separator + fileName);
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Disable(){//Disable All Button or ...
        PlayVoice.setEnabled(false);
    }

    public void Enable(){//Enable All Button or ...
        PlayVoice.setEnabled(true);
    }


    public void SetPic(String Path){

        File sdcard = Environment.getExternalStorageDirectory();
        File f = new File(Path + "//pic.jpg");

        if(f.exists()) {
            Bitmap pic0 = BitmapFactory.decodeFile(f.getAbsolutePath());

            if (pic0 == null){
                //txtresult = (TextView)findViewById(R.id.txtresult);
                //txtresult.setText("Access denied to memory");
                Path="2";//2 = Access denied to Path
                //ImageView Rot1 = (ImageView) findViewById(R.id.RotatePhoto1);
                //Rot1.setVisibility(View.INVISIBLE);
            }else {
                if (pic0.getHeight() < 120 || pic0.getWidth() < 120) {
                    //txtresult.setText("The quality of the left side photo is low. It must be Height>120 _ Width>120");
                    Path = "";

                }else {
                    Matrix matrix = new Matrix();
                    matrix.preRotate(0);
                    pic1 = Bitmap.createBitmap(pic0,0,0, pic0.getWidth(),pic0.getHeight(),matrix, true);//Rotate 90
                    pic1 = Bitmap.createScaledBitmap(pic1,800,300,true);//W=800 H=600
                    pic0.recycle();
                    pic0 = Bitmap.createBitmap(pic1,0,0, pic1.getWidth(),pic1.getHeight(),matrix, true);//Rotate 90
                    pic0 = Bitmap.createScaledBitmap(pic0,150,80,true);//W=800 H=600

                    ErrorPicViewer.setImageBitmap(pic1);//$.B _ Big Preview

                    ErrorPic.setImageBitmap(pic0);//(circleBitmap);//$.B for circle
                    ErrorPic.setEnabled(true);//$.B
                    ErrorPic.setBackground(null);
                }
            }

        }else {
            //ImageView btn1 = (ImageView) findViewById(R.id.image2);//$.B
            //btn1.setImageResource(R.drawable.ic_face_btn);
            //btn1.setBackgroundResource(R.drawable.circlebackground);
            //btn1.setEnabled(true);//$.B
        }

    }


}
