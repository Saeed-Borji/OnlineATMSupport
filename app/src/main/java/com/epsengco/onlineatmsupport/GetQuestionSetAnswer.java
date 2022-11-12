package com.epsengco.onlineatmsupport;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import cz.msebera.android.httpclient.Header;

public class GetQuestionSetAnswer extends AppCompatActivity {


    private EditText Question;

    public EditText QuestionBox;
    public LinearLayout VoicePlayer;
    public ImageView Errorpic;
    private ImageButton PicViewer;
    private ImageView ErrorPicViewer;
    int BigPreview = 1;

    private TextView Navigate;

    String Textbtn;
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

        Errorpic = (ImageView)findViewById(R.id.errorpic);
        PicViewer = (ImageButton)findViewById(R.id.Errorpicbtn);

        ErrorPicViewer = (ImageView)findViewById(R.id.picpreview);
        @SuppressLint("WrongViewCast") final View ErrorTextViewerLayout = (View) findViewById(R.id.errortextviewerlayout);
        PostInboxData();

        //$.B /\


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

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_get_question_set_answer);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }



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
                PostURLUtils.Get("/GetQuestionSetAnswer/" + QuestionNumber, params, new JsonHttpResponseHandler(){
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
                            byte[] ErrPicByte = Base64.decode(ErrPicString, Base64.DEFAULT);
                            Bitmap ErrPicBitmap = BitmapFactory.decodeByteArray(ErrPicByte, 0, ErrPicByte.length);
                            Errorpic.setImageBitmap(ErrPicBitmap);
                            ErrorPicViewer.setImageBitmap(ErrPicBitmap);

                            String questionVoice = obj.getString("questionVoice");
                            Toast.makeText(getApplicationContext(),questionVoice,Toast.LENGTH_LONG).show();


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

                        } catch (JSONException e) {
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


}