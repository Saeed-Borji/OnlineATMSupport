package com.epsengco.onlineatmsupport;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.epsengco.onlineatmsupport.ui.AudioRecorder;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.epsengco.onlineatmsupport.databinding.ActivityGetQuestionSetAnswerBinding;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public class GetQuestionSetAnswer extends AppCompatActivity {

    private int progressStatus = 0;
    private Handler handler = new Handler();

    public EditText QuestionBox;

    public EditText AnswerBox;
    String Answerstr = "";
    private Button SendAnswer;

    private ImageButton PlayVoice;
    private ProgressBar progressBar;
    public LinearLayout VoicePlayer;
    byte[] questionVoiceByte;
    String VoicePath;

    public ImageView Errorpic;
    private ImageButton PicViewer;
    byte[] ErrPicByte;
    private ImageView ErrorPicViewer;
    int BigPreview = 1;

    private TextView Navigate;


    String Username = "";
    int Accounttype = -1;
    String Accounttypename = "";
    String Check = "";
    String ProductName = "";

    int QuestionNotRead = 0;
    int AnswerNotRead = 0;
    int MessageCount = 0;
    int QuestionNumber = -1;

    private AppBarConfiguration appBarConfiguration;
    private ActivityGetQuestionSetAnswerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGetQuestionSetAnswerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_get_question_set_answer);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //$.B \/

        Bundle extras = getIntent().getExtras();//Recive Message
        if (extras != null) {
            Username = extras.getString("Username");
            Accounttype = extras.getInt("Accounttype");
            Accounttypename = extras.getString("Accounttypename");
            Check = extras.getString("Check");
            ProductName = extras.getString("ProductName");
            QuestionNotRead = extras.getInt("QuestionNotRead");
            AnswerNotRead = extras.getInt("AnswerNotRead");
            MessageCount = extras.getInt("MessageCount");//Tedad message haye markaz
            QuestionNumber = extras.getInt("QuestionNumber");//Index of Question
        }

        Navigate = (TextView) findViewById(R.id.textnavigate);
        Navigate.setText(Username + " > " + Accounttypename + " > Server Inbox = " + QuestionNumber + "_from_" + MessageCount);

        QuestionBox = (EditText)findViewById(R.id.questionbox);
        VoicePlayer = (LinearLayout)findViewById(R.id.voiceplayerlayout);
        PlayVoice = (ImageButton)findViewById(R.id.Voiceplayerbtn);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        PicViewer = (ImageButton)findViewById(R.id.Errorpicbtn);

        Errorpic = (ImageView)findViewById(R.id.errorpic);
        PicViewer = (ImageButton)findViewById(R.id.Errorpicbtn);

        ErrorPicViewer = (ImageView)findViewById(R.id.picpreview);
        @SuppressLint("WrongViewCast") final View ErrorTextViewerLayout = (View) findViewById(R.id.errortextviewerlayout);
        PostInboxData();

        SendAnswer = (Button)findViewById(R.id.buttonsendanswer);
        AnswerBox = (EditText)findViewById(R.id.Answerbox);

        //$.B /\


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

                audioPlayer(VoicePath,"");
            }
        });

        PicViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BigPreview == 1){
                    ErrorPicViewer.setVisibility(View.VISIBLE);//Big Pic
                    //Question.setText("");
                    VoicePlayer.setVisibility(View.INVISIBLE);
                    ErrorTextViewerLayout.setVisibility(View.INVISIBLE);
                    BigPreview = 0;
                }else if (BigPreview == 0){
                    ErrorPicViewer.setVisibility(View.INVISIBLE);//Big Pic
                    //Question.setText(questionText);
                    VoicePlayer.setVisibility(View.VISIBLE);
                    ErrorTextViewerLayout.setVisibility(View.VISIBLE);
                    BigPreview = 1;
                }

            }
        });

        SendAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Check.equals("-1")){
                    Toast.makeText(getApplicationContext(), "دسترسی این کاربر > "+Username+"< تایید نشده است", Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(getApplicationContext(), "Go to Send data to server", Toast.LENGTH_SHORT).show();
                    GetَAnswerData();
                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_get_question_set_answer);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private String sanitizePath(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (!path.contains(".")) {
            //path += ".3gp";
            path += ".mp3";
        }
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + path;
    }


    public void VoiceRecord(String filename){
        final AudioRecorder Record = new AudioRecorder(filename);

        try {
            Record.start();
            //$.B _ Timeout \/
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                private String path;
                @Override
                public void run() {
                    try {
                        Record.stop();
                        // $B - VoiceRecButton.setEnabled(true);
                        //AuthenticationButton.setClickable(true);
                        Toast.makeText(getApplicationContext(), "صدای شما با موفقیت ذخیره شد.", Toast.LENGTH_SHORT).show();

                        Enable();//Enable All Button or ...

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            },15000L);//5"
            //$.B _ Timeout /\

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void audioPlayer(String path, String fileName){
        //set up MediaPlayer
        MediaPlayer mp = new MediaPlayer();

        try {
            if (fileName.equals(""))
            {
                mp.setDataSource(path);
            }
            else
            {
                mp.setDataSource(path + File.separator + fileName);
            }
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Disable(){//Disable All Button or ...
        PlayVoice.setEnabled(false);
        PlayVoice.setImageResource(R.drawable.playepauseblack);
    }

    public void Enable(){//Enable All Button or ...
        PlayVoice.setEnabled(true);
        PlayVoice.setImageResource(R.drawable.playeblack);
    }

    ////// GET QUESTION FROM SERVER by SendQuestion api \/
    private void PostInboxData() {
        try {

            Long vt = System.currentTimeMillis() / 1000;

            RequestParams params = new RequestParams();
            //params.put("username",Username);
            //params.put("accounttypename",Accounttypename);
            //params.put("questionnumber",QuestionNumber);
            //params.put("password",password);
            //params.put("photo2", new ByteArrayInputStream(vArrPhoto2), "photo2.jpg");

            boolean NET = false;
            try {
                Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
                int returnVal = p1.waitFor();
                boolean reachable = (returnVal == 0);
                NET = reachable;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                NET = false;
            }

            if (NET == false) {
                Toast.makeText(getApplicationContext(),"لطفا اتصال به اینترنت را بررسی نمایید - Plaese check your internet conection",Toast.LENGTH_LONG).show();

            } else if (NET == true) {
                PostURLUtils.Get("/SendQuestion/" + QuestionNumber, params, new JsonHttpResponseHandler(){
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject obj) {
                        //Toast.makeText(getApplicationContext(),"onSuccess"+statusCode,Toast.LENGTH_LONG).show();
                        try {
                            //$.B \/
                            String resultMessage = obj.getString("statusMessage");
                            int resultID = obj.getInt("resultID");

                            String questionText = obj.getString("questionText");
                            QuestionBox.setText( questionText +"\n" + resultMessage + " <--> " + resultID);
                            //Toast.makeText(getApplicationContext(),resultID + "متن="+questionText,Toast.LENGTH_LONG).show();

                            String ErrPicString = obj.getString("questionPic");
                            ErrPicByte = Base64.decode(ErrPicString, Base64.DEFAULT);
                            Bitmap ErrPicBitmap = BitmapFactory.decodeByteArray(ErrPicByte, 0, ErrPicByte.length);
                            Errorpic.setImageBitmap(ErrPicBitmap);
                            ErrorPicViewer.setImageBitmap(ErrPicBitmap);

                            String questionVoice = obj.getString("questionVoice");
                            questionVoiceByte = Base64.decode(questionVoice, Base64.DEFAULT);
                            File tempMp3 = File.createTempFile("test", ".mp3", getCacheDir());
                            //tempMp3.deleteOnExit();
                            VoicePath = tempMp3.toString();
                            FileOutputStream fos = new FileOutputStream(tempMp3);
                            fos.write(questionVoiceByte);
                            fos.close();

                            Toast.makeText(getApplicationContext(),VoicePath.toString(),Toast.LENGTH_LONG).show();



                            //Toast.makeText(getApplicationContext(),tempMp3.toString(),Toast.LENGTH_LONG).show();


                            /*
                            if (resultID == 0){

                                Toast.makeText(getApplicationContext(),"0MSG="+questionText,Toast.LENGTH_LONG).show();
                                AnswerNotRead = 1;
                                QuestionBox.setText(questionText);

                            } else if (resultID == 1) {//
                                Toast.makeText(getApplicationContext(), "1MSG="+questionText, Toast.LENGTH_SHORT).show();
                                AnswerNotRead = 0;
                                QuestionBox.setText(questionText);
                            }
                            */
                            // $.B /\

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, org.json.JSONObject errorResponse) {

                        Toast.makeText(getApplicationContext(),"onFailure_"+statusCode,Toast.LENGTH_LONG).show();
                        String message = throwable.getMessage();
                        AnswerNotRead = 0;
                        //TextView result = (TextView) findViewById(R.id.voiceresult);
                        //result.setText(message);
                        //TextView txtresult = (TextView) findViewById(R.id.txtresult);
                        if (message == null){
                            //txtresult.setText("Sorry :-( . It's not available in your country.");
                        }else {
                            //txtresult.setText("The image was not uploaded correctly. Check your connection please." );
                        }
                    }

                    @Override
                    public void onFinish() {
                        //Register.setBackgroundResource(R.drawable.buttonbackground3);
                        //TextView result = (TextView) findViewById(R.id.voiceresult);
                        //result.setText("خطا در ارسال فایل ها، لطفا مجدد تلاش نمایید");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"exeption"+e.toString(),Toast.LENGTH_LONG).show();
        }
    }
    ////// GET QUESTION FROM SERVER by SendQuestion api /\

    ////// SEND ANSWER TO SERVER \/
    private String path;
    private void PostAnswerData() {

        try {

            if (!Username.equals("") && !Accounttypename.equals("")){

                Disable();//Disable All Button or ...
/*
                if (!Path1.equals("")){
                    GetPicByte(Path1);
                }else{
                    ArrPic = null;
                    ArrPic = new byte[1];
                    ArrPic[0] = 0;
                }



                //Get Voice Arrey
                String Path = "sound";
                this.path = sanitizePath(Path);
                ArrVoice = GetByteArrayFromFile(this.path);
                File voice = new File(this.path);
                if (voice.exists()==true) {
                    ArrVoice = GetByteArrayFromFile(this.path);
                }else{
                    ArrVoice = null;
                    ArrVoice = new byte[1];
                    ArrVoice[0] = 0;
                }
        */

                //Alert("نتیجه","سوال شما ارسال شد. تا دقایقی دیگر جواب کارشناسان برای شما ارسال می شود.");
                Long vt = System.currentTimeMillis() / 1000;

                RequestParams params = new RequestParams();

                params.put("username",Username);
                params.put("accountname",Accounttypename);
                params.put("message", Answerstr);
                params.put("questionnumber",QuestionNumber);
                //params.put("ErrorPic", new ByteArrayInputStream(ArrPic), "ErrorPic.jpg");
                //params.put("ErrorVoice", new ByteArrayInputStream(ArrVoice), "ErrorVoice.mp3");

                //$.B _ Save Message in the cellphone \/
                String message =
                        " ___________________  : متن پاسخ " + "\n"+
                        Answerstr;
                SaveQuestionMessage(message);// , ArrPic,ArrVoice);
                //$.B _ Save Message in the cellphone /\

                boolean NET = false;
                try {
                    Process p1 = Runtime.getRuntime().exec("ping -c 1 www.google.com");
                    int returnVal = p1.waitFor();
                    boolean reachable = (returnVal == 0);
                    NET = reachable;
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    NET = false;
                }


                if (NET == false) {
                    Toast.makeText(getApplicationContext(),"Plaese check your internet conection",Toast.LENGTH_LONG).show();

                    Enable();

                } else if (NET == true) {

                    PostURLUtils.post("/GetAnswer", params, new JsonHttpResponseHandler() {
                        @Override
                        public void onStart() {
                            super.onStart();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                            //Toast.makeText(getApplicationContext(),"onSuccess"+statusCode,Toast.LENGTH_LONG).show();
                            try {
                                //$.B \/
                                String resultMessage = obj.getString("statusMessage");//
                                int resultID = obj.getInt("resultID");

                                //TextView result = (TextView) findViewById(R.id.voiceresult);
                                //result.setText(resultX);

                                if (resultID == 0) { //in the second photo, the FACE was Not Found
                                    //txtresult.setText("The face was not found in the second photo");

                                    //Intent intent = new Intent(MainActivity.this, ChooseSituation.class);
                                    //intent.putExtra("KEY",UserNametxt.getText().toString());//send data to next class
                                    //startActivityForResult(intent, 2);


                                    Alert("Server Message",resultMessage);
                                    //Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();

                                } else if (resultID == 1) {//in the first photo, the FACE was Not Found
                                    Toast.makeText(getApplicationContext(), "Change User Name"+resultMessage, Toast.LENGTH_SHORT).show();
                                    //txtresult.setText("The face was not Found in the first photo");
                                }else if (resultID == 2) {//in the first photo, the FACE was Not Found
                                    Toast.makeText(getApplicationContext(), "This User Name is not register"+resultMessage, Toast.LENGTH_SHORT).show();
                                    //txtresult.setText("The face was not Found in the first photo");
                                }//else if (resultID == 3) {//in the first photo, the FACE was Not Found
                                //Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();
                                //txtresult.setText("The face was not Found in the first photo");
                                //}else if (resultID == 4) {//in the first photo, the FACE was Not Found
                                // Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();
                                //txtresult.setText("The face was not Found in the first photo");
                                //}
                                // $.B /\


                                Enable();//Enable All Button or ...

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Enable();//Enable All Button or ...
                            }

                            //Reciptbtn.setEnabled(true);
                            //Registerbtn.setEnabled(false);
                            //Path1 = "";
                            //ReciptPictureimg.setImageBitmap(null);//(circleBitmap);//$.B for circle
                            //ReciptPictureimg.setEnabled(true);//$.B
                            //ReciptPictureimg.setBackground(null);

                            //finish();

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              Throwable throwable, JSONObject errorResponse) {

                            String message = throwable.getMessage();
                            Toast.makeText(getApplicationContext(),"onFailure_"+message,Toast.LENGTH_LONG).show();
                            //TextView result = (TextView) findViewById(R.id.voiceresult);
                            //result.setText(message);
                            //TextView txtresult = (TextView) findViewById(R.id.txtresult);
                            if (message == null){
                                //txtresult.setText("Sorry :-( . It's not available in your country.");
                            }else {
                                //txtresult.setText("The image was not uploaded correctly. Check your connection please." );
                            }

                            Enable();//Enable All Button or ...
                            //Reciptbtn.setEnabled(true);
                            //Registerbtn.setEnabled(false);
                        }

                        @Override
                        public void onFinish() {
                            //Register.setBackgroundResource(R.drawable.buttonbackground3);
                            //TextView result = (TextView) findViewById(R.id.voiceresult);
                            //result.setText("خطا در ارسال فایل ها، لطفا مجدد تلاش نمایید");
                        }
                    });
                }


            }
            else {
                Toast.makeText(getApplicationContext(),"We have a problem aboute your back select steps ",Toast.LENGTH_LONG).show();
                Enable();//Enable All Button or ...
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"exeption"+e.toString(),Toast.LENGTH_LONG).show();
            Enable();//Enable All Button or ...
        }
    }
    ////// SEND ANSWER TO SERVER /\

    public void Alert (String title, String message){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
//set icon
                .setIcon(android.R.drawable.ic_dialog_alert)
//set title
                .setTitle(title)
//set message
                .setMessage(message)
//set positive button
                // // $.B 14/04/2022 "ادامه"
                .setPositiveButton("", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        //finish();
                        Clearcls();
                        Enable();
                    }
                })
//set negative button
                .setNegativeButton("بازگشت", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Clearcls();
                        //set what should happen when negative button is clicked
                        //Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(GetQuestionSetAnswer.this, MultiPreviewInboxActivity.class);
                        intent.putExtra("Username",Username.toString());//send data to next class
                        intent.putExtra("Accounttype",Accounttype);
                        intent.putExtra("Accounttypename",Accounttypename);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent, 2);
                    }
                })
                .show();
    }


    public void GetَAnswerData(){

            Answerstr = AnswerBox.getText().toString();//Text of Question Box
            if (!Answerstr.equals("")){

                //Go To Send Param Request to server
                Disable();
                PostAnswerData();

            }else {
                Toast.makeText(getApplicationContext(), "لطفا متن پاسخ خود به مشکل را وارد نمایید", Toast.LENGTH_SHORT).show();
            }



    }
    /*
    private void GetPicByte (String Path){
        //File dir_voice1 = new File("/storage/emulated/0");//$.b
        //dir_voice1.mkdirs();
        //File dir_Reciptpic = new File(Path1);//$.b
        //dir_Reciptpic.mkdirs();

        //byte[] vArrVoice1 = GetByteArrayFromFile( dir_voice1+"/sound.mp3");
        ArrPic = GetByteArrayFromFile(Path);


        // $.B = Resize \/
        Bitmap bm = BitmapFactory.decodeByteArray(ArrPic, 0, ArrPic.length);
        int maxWidth = 800;
        int maxHeight = 600;
        int sourceWidth1 = bm.getWidth();
        int sourceHeight1 = bm.getHeight();
        float nPercentW1 = 0, nPercentH1 = 0;

        if ((sourceWidth1 <= maxWidth && sourceHeight1 <= maxHeight) || (sourceWidth1 <= maxHeight && sourceHeight1 <= maxWidth)) {
            nPercentW1 = 1;
            nPercentH1 = 1;
        } else {
            if (sourceWidth1 < sourceHeight1) {
                int buff = maxWidth;
                maxWidth = maxHeight;
                maxHeight = buff;
            }
            nPercentW1 = ((float) maxWidth) / (float) sourceWidth1;
            nPercentH1 = ((float) maxHeight) / (float) sourceHeight1;

            Matrix matrix = new Matrix();
            matrix.postScale(nPercentW1, nPercentH1);

            Bitmap resizedBitmap = Bitmap.createBitmap(
                    bm, 0, 0, sourceWidth1, sourceHeight1, matrix, false);
            bm.recycle();
            bm = resizedBitmap;

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            ArrPic = stream.toByteArray();
            resizedBitmap.recycle();
        }
        // $.B = Resize /\
    }

     */
    private byte[] GetByteArrayFromFile (String aFileName){
        File vFile = new File(aFileName);
        int size = (int) vFile.length();
        byte[] bytes = new byte[size];

        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(vFile));
            buf.read(bytes, 0, bytes.length);
            buf.close();
            return  bytes;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return  null;
    }

    private void SaveQuestionMessage(String Message) throws IOException {//(String Message,byte[] Pic, byte[] Voice)

        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + Username +"_SB_Inbox");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            // Do something on success
            File dir = new File(folder.toString());
            if (dir.isDirectory())
            {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++)
                {
                    new File(dir, children[i]).delete();
                }
            }
            success = folder.delete();
            success = folder.mkdirs();
        } else {
            // Do something else on failure
        }

        try {

            File gpxfile = new File(folder, "Message.txt");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(Message.toString());
            writer.flush();
            writer.close();

            //FileOutputStream pic = new FileOutputStream(folder+"//pic.jpg");
            //pic.write(Pic);
            //pic.close();

            //FileOutputStream voice = new FileOutputStream(folder+"//voice.mp3");
            //voice.write(Voice);
            //voice.close();

        } catch (Exception e) {
            //Log.e(TAG, e.getMessage());
        }

    }

    private void Clearcls(){
        //SelectBank.setAdapter(bb);
        //SelectModule.setAdapter(aa);
        //SelectModulestr = "";
        //SelectBanknamestr = "";
        //oldModuleindx = -1;
        //oldBanknameindx = -1;

        Answerstr = "";
        AnswerBox.setText("");

        //Path1 = "";
        //ErrorPic.setImageBitmap(null);
        //ErrorPic.setBackground(null);
        //pic1 = null;

        progressStatus = 0;
        progressBar.setProgress(progressStatus);
        //final String path;
        String Path = "sound";
        this.path = sanitizePath(Path);
        new File(this.path).delete();

    }

}