package com.epsengco.onlineatmsupport;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.File;

import cz.msebera.android.httpclient.Header;

public class ProductActivity extends AppCompatActivity {

    private ImageView ATM;
    private ImageView Online;
    private ImageView Kiosk;
    private ImageView VSAT;
    private ImageView VNB;
    private ImageView CRS;
    private ImageView VTM;
    private ImageView Earth;

    private TextView Navigate;

    private Button Inbox;

    String Username = "";
    int Accounttype = -1;
    String Accounttypename = "";
    String Check = "-1";
    String ProductName = "";
    int MessageCount = 0;

    int QuestionNotRead = 0;
    int AnswerNotRead = 0;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
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

        //$.B \/

        Bundle extras = getIntent().getExtras();//Recive Message
        if(extras !=null) {
            Username = extras.getString("Username");
            Accounttype = extras.getInt("Accounttype");
            Accounttypename = extras.getString("Accounttypename");
            Check = extras.getString("Check");
        }

        Navigate = (TextView)findViewById(R.id.textnavigate);
        Navigate.setText(Username + " > " + Accounttypename+ " > ");

        ATM = (ImageView)findViewById(R.id.atm_view);
        ATM.setBackgroundResource(R.drawable.atm);
        Online = (ImageView)findViewById(R.id.online_view);
        Kiosk = (ImageView)findViewById(R.id.kiosk_view);
        VSAT = (ImageView)findViewById(R.id.vsat_view);
        VNB = (ImageView)findViewById(R.id.vnb_view);
        CRS = (ImageView)findViewById(R.id.crs_view);
        VTM = (ImageView)findViewById(R.id.vtm_view);
        Earth = (ImageView)findViewById(R.id.earth_view);

        Inbox = (Button)findViewById(R.id.buttoninbox);

        //$.B 14/04/2022
        if (Accounttypename.equals("Technical"))
        {
            findViewById(R.id.atm_view).setEnabled(false);
            ATM.setBackgroundResource(R.drawable.atmdisable);
        }
        //$.B 14/04/2022

        //$.B _ Check Have Any Message for View in Inbox \/
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + Username +"_SB_Inbox");
        if (!folder.exists()){//$.B _ If Have not Question Message
            QuestionNotRead = 0;
        }else{
            Inbox.setBackgroundResource(R.drawable.buttonnotif);
            QuestionNotRead = 1;
        }

        Toast.makeText(getApplicationContext(), "سرویس های فعال = ATM", Toast.LENGTH_SHORT).show();

        PostInboxData();

        //$.B _ Check Have Any Message for View in Inbox /\




        Inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Check.equals("-1") || Check.equals("")){
                    Toast.makeText(getApplicationContext(), " دسترسی  > "+Username+" <  محدود می باشد ", Toast.LENGTH_SHORT).show();
                }else if(Check.equals("1")) {
                    Toast.makeText(getApplicationContext(), "Go to Inbox", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProductActivity.this, PreviewInboxActivity.class);
                    intent.putExtra("Username",Username.toString());//send Username to next class
                    intent.putExtra("Accounttype",Accounttype);
                    intent.putExtra("Accounttypename",Accounttypename);
                    intent.putExtra("Check",Check);//send data to next class
                    intent.putExtra("ProductName",ProductName.toString());//send ProductName to next class
                    intent.putExtra("QuestionNotRead",QuestionNotRead);
                    intent.putExtra("AnswerNotRead",AnswerNotRead);
                    intent.putExtra("MessageCount",MessageCount);//Tedad message ha dar markaz
                    startActivityForResult(intent, 2);
                }
            }
        });
        ATM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Check.equals("-1") || Check.equals("")){
                    Toast.makeText(getApplicationContext(), " دسترسی  > "+Username+" <  محدود می باشد ", Toast.LENGTH_SHORT).show();
                }else if(Check.equals("1")) {
                    ProductName = "ATM";
                    if (AnswerNotRead == 0 && QuestionNotRead == 0){
                        Intent intent = new Intent(ProductActivity.this, SoftHardActivity.class);
                        intent.putExtra("Username",Username.toString());//send Username to next class
                        intent.putExtra("Accounttype",Accounttype);
                        intent.putExtra("Accounttypename",Accounttypename);
                        intent.putExtra("Check",Check);//send data to next class
                        intent.putExtra("ProductName",ProductName.toString());//send ProductName to next class
                        startActivityForResult(intent, 2);

                    }else if (AnswerNotRead == 0 && QuestionNotRead != 0){
                        Toast.makeText(getApplicationContext(), "لطفا تا زمان دریافت راهنمایی کارشناسان پشتیبانی شکیبا باشید", Toast.LENGTH_SHORT).show();
                    }else if (AnswerNotRead != 0 && QuestionNotRead != 0){
                        Toast.makeText(getApplicationContext(), "لطفا جواب درست را مشخص نمایید", Toast.LENGTH_SHORT).show();
                    }else if (AnswerNotRead != 0 && QuestionNotRead == 0){
                        Toast.makeText(getApplicationContext(), "لطفا منتظر ثبت جواب خود توسط سیستم باشید", Toast.LENGTH_SHORT).show();
                    }else {
                        //AnswerNotRead=0;
                        //QuestionNotRead=0;
                    }
                }
            }
        });
    }

    private void PostInboxData() {
        try {

            Long vt = System.currentTimeMillis() / 1000;

            RequestParams params = new RequestParams();
            params.put("username",Username);
            params.put("accounttypename",Accounttypename);
            //params.put("password",password);
            //params.put("photo2", new ByteArrayInputStream(vArrPhoto2), "photo2.jpg");

            /// Check Internet Connection \/
            boolean connected = false;
            connected = CheckIntCon();
            /// Check Internet Connection /\

            if (connected == false) {
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.check_internet),Toast.LENGTH_LONG).show();

            } else if (connected == true) {

                PostURLUtils.post("/NotificationMessage", params, new JsonHttpResponseHandler() {
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
                            //String CheckID = obj.getString("CheckID");
                            int resultID = obj.getInt("resultID");
                            int messageCount = obj.getInt("messageCount");

                            if (resultID == 0){

                                Inbox.setBackgroundResource(R.drawable.buttonnotif2);
                                Toast.makeText(getApplicationContext(),"پیام های جدید خود را بررسی نمایید",Toast.LENGTH_LONG).show();
                                AnswerNotRead = 1;
                                MessageCount = messageCount;//tedad message ha dar markaz
                                //Intent intent = new Intent(MainActivity.this, ChooseSituation.class);
                                //intent.putExtra("Username",UserNametxt.getText().toString());//send data to next class
                                //intent.putExtra("Accounttype",resultID);
                                //intent.putExtra("Check",CheckID);//send data to next class
                                //startActivityForResult(intent, 2);

                                //Toast.makeText(getApplicationContext(), "Success Login _ "+resultID+resultMessage, Toast.LENGTH_SHORT).show();

                            } else if (resultID == 1) {//
                                Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();
                                AnswerNotRead = 0;
                                //txtresult.setText("The face was not Found in the first photo");
                            }
                            // $.B /\

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, org.json.JSONObject errorResponse) {

                        //Toast.makeText(getApplicationContext(),"onFailure_"+statusCode,Toast.LENGTH_LONG).show();
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
            //Toast.makeText(getApplicationContext(),"exeption"+e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    private boolean CheckIntCon() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
