package com.epsengco.onlineatmsupport;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.File;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private EditText UserNametxt;
    private EditText Password;
    private Button Login;
    String IMEI = null;
    String username = null;
    String password = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //$.B \/

        Login = (Button) findViewById(R.id.LoginButton);


        Login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LoginMethod(v);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = null;
        switch(menuItem.getItemId()) {
            case R.id.nav_home:
//                fragmentClass = FirstFragment.class;
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivityForResult(intent, 2);
                //Toast.makeText(getApplicationContext(),"About us" , Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_gallery:
//                fragmentClass = SecondFragment.class;
                Intent email = new Intent(Intent.ACTION_SEND);
                String to = "mysupport@4Hiway.com";
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
                //email.putExtra(Intent.EXTRA_CC, new String[]{ to});
                //email.putExtra(Intent.EXTRA_BCC, new String[]{to});
                email.putExtra(Intent.EXTRA_SUBJECT, "Hiway app User's Feedback");
                email.putExtra(Intent.EXTRA_TEXT, "");

                //need this to prompts email client only
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Choose an Email client :"));
                //Toast.makeText(getApplicationContext(),"Send Feedback" , Toast.LENGTH_LONG).show();
                break;
            default:
//                fragmentClass = FirstFragment.class;
        }
    }



    public void Alert (String title, String message){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
//set icon
                .setIcon(android.R.drawable.ic_dialog_alert)
//set title
                .setTitle(title)
//set message
                .setMessage(message)
//set positive button
                .setPositiveButton("خروج از برنامه", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        finish();
                    }
                })
//set negative button
                .setNegativeButton("بازگشت", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what should happen when negative button is clicked
                        //Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }

    public void LoginMethod(View view) {


        //TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
        //    return;
       // }
        //IMEI = telephonyManager.getDeviceId();

        UserNametxt = (EditText)findViewById(R.id.UsernameBox);
        username = UserNametxt.getText().toString();

        Password = (EditText)findViewById(R.id.PasswordBox);
        password = Password.getText().toString();

        if (username.equals("") || password.equals("") )
        {
            Alert("","لطفا نام کاربری و کلمه غبور خود را وارد نماییدو یا ثبت نام نمایید");

            //Toast.makeText(getApplicationContext(),"لطفا نام، نام خانوادگی و شماره مبایل خود را وارد نمایید" , Toast.LENGTH_LONG).show();
        }else {
            UserNametxt.setEnabled(false);
            Password.setEnabled(false);
            Login.setEnabled(false);

            PostFaceVideoData();
        }
    }

    private void PostFaceVideoData() {
        try {
            //File dir_voice1 = new File("/storage/emulated/0");//$.b
            //dir_voice1.mkdirs();
            //File dir_voice2 = new File("/storage/emulated/0");//$.b
            //dir_voice2.mkdirs();

            //byte[] vArrVoice1 = GetByteArrayFromFile( dir_voice1+"/sound1.mp3");
            //byte[] vArrVoice2 = GetByteArrayFromFile( dir_voice2+"/sound2.mp3");

            Long vt = System.currentTimeMillis() / 1000;

            RequestParams params = new RequestParams();
            params.put("username",username);
            params.put("password",password);
            //params.put("photo2", new ByteArrayInputStream(vArrPhoto2), "photo2.jpg");

            boolean NET = true;
//            try {
//                Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
//                int returnVal = p1.waitFor();
//                boolean reachable = (returnVal == 0);
//                NET = reachable;
//            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//                NET = false;
//            }

            if (NET == false) {
                Toast.makeText(getApplicationContext(),"Plaese check your internet conection",Toast.LENGTH_LONG).show();

                UserNametxt.setEnabled(true);
                Password.setEnabled(true);
                Login.setEnabled(true);

            } else if (NET == true) {

                PostURLUtils.post("/login", params, new JsonHttpResponseHandler() {
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
                            String CheckID = obj.getString("CheckID");
                            int resultID = obj.getInt("resultID");

                            if (resultID == 0 || resultID == 1 || resultID == 2) { //0 = "technicalsupport" _ 1 = "fieldexpert" _ 2 = "Both"

                                Intent intent = new Intent(MainActivity.this, ChooseSituation.class);
                                intent.putExtra("Username",UserNametxt.getText().toString());//send data to next class
                                intent.putExtra("Accounttype",resultID);
                                intent.putExtra("Check",CheckID);//send data to next class
                                startActivityForResult(intent, 2);

                                //Toast.makeText(getApplicationContext(), "Success Login _ "+resultID+resultMessage, Toast.LENGTH_SHORT).show();

                            } else if (resultID == 3) {//in the first photo, the FACE was Not Found
                                Toast.makeText(getApplicationContext(), "Username or Password is incorrect", Toast.LENGTH_SHORT).show();
                                //txtresult.setText("The face was not Found in the first photo");
                            }else if (resultID == 4) {//in the first photo, the FACE was Not Found
                                Toast.makeText(getApplicationContext(), "This Username is not register", Toast.LENGTH_SHORT).show();
                                //txtresult.setText("The face was not Found in the first photo");
                            }//else if (resultID == 3) {//in the first photo, the FACE was Not Found

                            // $.B /\

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        UserNametxt.setEnabled(true);
                        Password.setEnabled(true);
                        Login.setEnabled(true);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, org.json.JSONObject errorResponse) {

                        Toast.makeText(getApplicationContext(),"onFailure_"+statusCode,Toast.LENGTH_LONG).show();
                        String message = throwable.getMessage();
                        //TextView result = (TextView) findViewById(R.id.voiceresult);
                        //result.setText(message);
                        //TextView txtresult = (TextView) findViewById(R.id.txtresult);
                        if (message == null){
                            //txtresult.setText("Sorry :-( . It's not available in your country.");
                        }else {
                            //txtresult.setText("The image was not uploaded correctly. Check your connection please." );
                        }

                        UserNametxt.setEnabled(true);
                        Password.setEnabled(true);
                        Login.setEnabled(true);
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

            UserNametxt.setEnabled(true);
            Password.setEnabled(true);
            Login.setEnabled(true);

        }
    }
}
