package com.epsengco.onlineatmsupport;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public class Register extends AppCompatActivity {

    private LinearLayout AllLayout;
    private CheckBox TechnicalSupportchck;
    private CheckBox FieldExpertchck;

    private EditText UserNametxt;
    private EditText Passwordtxt;
    private EditText PasswordRepettxt;
    private EditText AccountNumbertxt;
    private EditText BankName;
    private EditText AccountNametxt;

    private LinearLayout layoutaccountnumber;
    private LinearLayout layoutbankname;
    private LinearLayout layoutaccountname;
    private LinearLayout Firstlayer;

    private ImageView ReciptPictureimg;

    private Button Reciptbtn;
    private Button Registerbtn;



    String Path1 = null;
    String AccountType = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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

        AllLayout = (LinearLayout)findViewById(R.id.allLayout);

        TechnicalSupportchck = (CheckBox)findViewById(R.id.checkboxtechnicalsupport);
        FieldExpertchck = (CheckBox)findViewById(R.id.checkboxfieldexpert);
        //FieldExpertchck.setChecked(true);//Defult

        UserNametxt = (EditText)findViewById(R.id.UsernameBox);
        Passwordtxt = (EditText)findViewById(R.id.PasswordBox1);
        PasswordRepettxt = (EditText)findViewById(R.id.PasswordBox2);
        AccountNumbertxt = (EditText)findViewById(R.id.accountnumber);
        BankName = (EditText)findViewById(R.id.bankname);
        AccountNametxt = (EditText)findViewById(R.id.accountname);

        layoutaccountname = (LinearLayout)findViewById(R.id.layoutaccountname);
        layoutbankname = (LinearLayout)findViewById(R.id.layoutbankname);
        layoutaccountnumber = (LinearLayout)findViewById(R.id.layoutaccountnumber);
        Firstlayer = (LinearLayout)findViewById(R.id.firstlayer);

        ReciptPictureimg = (ImageView)findViewById(R.id.reciptpicture);

        Reciptbtn = (Button)findViewById(R.id.reciptbutton);
        Registerbtn = (Button)findViewById(R.id.registerbutton);
        Reciptbtn.setEnabled(true);
        Registerbtn.setEnabled(true);


        TechnicalSupportchck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TechnicalSupportchck.isChecked()){
                    layoutaccountname.setVisibility(View.VISIBLE);
                    layoutbankname.setVisibility(View.VISIBLE);
                    layoutaccountnumber.setVisibility(View.VISIBLE);
                    FieldExpertchck.setChecked(false);
                    AccountType = "technicalsupport";
                }
                else if (TechnicalSupportchck.isChecked() == false && FieldExpertchck.isChecked() == false)
                {
                    Clearcls();
                    Enable();
                }
                /*
                else{
                    layoutaccountname.setVisibility(View.INVISIBLE);
                    layoutbankname.setVisibility(View.INVISIBLE);
                    layoutaccountnumber.setVisibility(View.INVISIBLE);
                    TechnicalSupportchck.setChecked(false);
                    FieldExpertchck.setChecked(true);
                    AccountType = "";
                }

                 */
                Reciptbtn.setEnabled(true);
                //Registerbtn.setEnabled(true);
            }
        });

        FieldExpertchck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FieldExpertchck.isChecked()){
                    layoutaccountname.setVisibility(View.INVISIBLE);
                    layoutbankname.setVisibility(View.INVISIBLE);
                    layoutaccountnumber.setVisibility(View.INVISIBLE);
                    TechnicalSupportchck.setChecked(false);
                    AccountType = "fieldexpert";
                }
                else if (TechnicalSupportchck.isChecked() == false && FieldExpertchck.isChecked() == false)
                {
                    Clearcls();
                    Enable();
                }
                /*
                else{
                    layoutaccountname.setVisibility(View.VISIBLE);
                    layoutbankname.setVisibility(View.VISIBLE);
                    layoutaccountnumber.setVisibility(View.VISIBLE);
                    FieldExpertchck.setChecked(false);
                    TechnicalSupportchck.setChecked(true);
                    AccountType = "";
                }

                 */
                Reciptbtn.setEnabled(true);
                //Registerbtn.setEnabled(true);
            }
        });

        Reciptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);//SD Have or Havent

                Registerbtn.setEnabled(true);

                if (isSDPresent == true){//Have SD Cars
                    File dir_image2 = new File(Environment.getExternalStorageDirectory()+
                            File.separator+"S_B");

                    Path1 = "1";

                    performFileSearch();
                }else if (isSDPresent == false){//Haven't CD Card
                }
            }
        });

        Registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (FieldExpertchck.isChecked()==true){
                    AccountNametxt.setText("---");
                    BankName.setText("---");
                    AccountNumbertxt.setText("---");
                }

                ReciptPictureimg.getDrawable();

                if (UserNametxt.getText().toString().equals("")||Passwordtxt.getText().toString().equals("")
                        || PasswordRepettxt.getText().toString().equals("") || AccountNametxt.getText().toString().equals("") || BankName.getText().equals("")
                        || AccountNumbertxt.getText().toString().equals("") || ReciptPictureimg.getDrawable()== null){

                    Toast.makeText(getApplicationContext(),"لطفا تمام فیلدها را تکمیل نمایید و تصویر رسید پرداخت شده را بارگذاری نمایید",Toast.LENGTH_LONG).show();

                }else{

                    if (Passwordtxt.getText().toString().equals(PasswordRepettxt.getText().toString())){
                        //Reciptbtn.setEnabled(false);
                        //Registerbtn.setEnabled(false);
                        Disable();

                        PostData();
                        Toast.makeText(getApplicationContext(),"Send Data",Toast.LENGTH_LONG).show();
                        Disable();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"کلمه عبور ها یکسان نمی باشد",Toast.LENGTH_LONG).show();
                        Passwordtxt.setText("");
                        PasswordRepettxt.setText("");

                        Enable();
                        //Reciptbtn.setEnabled(true);
                        //Registerbtn.setEnabled(true);
                    }


                }

            }
        });

    }

    public void performFileSearch() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {

        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    //ImageView photo1 = (ImageView) findViewById(R.id.Photo1);
                    ReciptPictureimg.setImageURI(selectedImage);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();

                    if (Path1 == "1"){
                        Path1 = selectedImage.toString();
                        if (Path1.contains("file:///") == true ){
                            Path1 = imageReturnedIntent.getData().toString();
                            Path1 = Uri.parse(Path1).getEncodedPath();
                        }else {
                            File file = new File(getPath(selectedImage));
                            Path1 = file.getAbsolutePath();
                        }
                    }

                }
                break;
        }

        setPic(0);

    }

    public String getPath(Uri uri){
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

    private void setPic(float angle1) {
        String fileUrl = null;//dir_image2+File.separator+"Image1.jpg";
        float angle = 0;
        if (Path1 == "selfie1" || Path1 == ""){
            //fileUrl = dir_image2+File.separator+"Image1.jpg";
            angle = -90;//90
        }else {
            fileUrl = Path1;
            angle = angle1;
        }
        File f = new File(fileUrl);

        if(f.exists()) {
            Bitmap pic0 = BitmapFactory.decodeFile(f.getAbsolutePath());

            if (pic0 == null){
                //txtresult = (TextView)findViewById(R.id.txtresult);
                //txtresult.setText("Access denied to memory");
                Path1="2";//2 = Access denied to Path
                //ImageView Rot1 = (ImageView) findViewById(R.id.RotatePhoto1);
                //Rot1.setVisibility(View.INVISIBLE);

            }else {
                if (pic0.getHeight() < 120 || pic0.getWidth() < 120) {
                    //txtresult.setText("The quality of the left side photo is low. It must be Height>120 _ Width>120");
                    Path1 = "";
                }else {
                    Matrix matrix = new Matrix();
                    matrix.preRotate(angle);
                    Bitmap pic1 = Bitmap.createBitmap(pic0,0,0, pic0.getWidth(),pic0.getHeight(),matrix, true);//Rotate 90
                    pic1 = Bitmap.createScaledBitmap(pic1,800,600,true);//W=800 H=600
                    pic0.recycle();

                    //ImageView btn1 = (ImageView) findViewById(R.id.Photo1);//$.B
                    ReciptPictureimg.setImageBitmap(pic1);//(circleBitmap);//$.B for circle
                    //btn1.setBackgroundResource(R.drawable.freebackground);
                    ReciptPictureimg.setEnabled(true);//$.B
                    ReciptPictureimg.setBackground(null);
                }
            }

        }else {
            //ImageView btn1 = (ImageView) findViewById(R.id.image2);//$.B
            //btn1.setImageResource(R.drawable.ic_face_btn);
            //btn1.setBackgroundResource(R.drawable.circlebackground);
            //btn1.setEnabled(true);//$.B
        }


    }

    private void PostData() {
        try {
            byte[] vArrReciptpic = GetByteArrayFromFile(Path1);

            // $.B = Resize \/
            Bitmap bm = BitmapFactory.decodeByteArray(vArrReciptpic, 0, vArrReciptpic.length);
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
                vArrReciptpic = stream.toByteArray();
                resizedBitmap.recycle();
            }
            // $.B = Resize /\

            Long vt = System.currentTimeMillis() / 1000;

            RequestParams params = new RequestParams();
            params.put("accounttype",AccountType.toString());
            params.put("username",UserNametxt.getText().toString());
            params.put("password",Passwordtxt.getText().toString());
            params.put("repetpassword",PasswordRepettxt.getText().toString());
            params.put("accountnumber",AccountNumbertxt.getText().toString());
            params.put("bankname",BankName.getText().toString());
            params.put("accountname",AccountNametxt.getText().toString());
            params.put("checked","-1");
            //params.put("sound", new ByteArrayInputStream(vArrVoice1), "sound.mp3");
            //params.put("reciptpic", new ByteArrayInputStream(vArrReciptpic), "reciptpic.jpg");
            params.put("photo1", new ByteArrayInputStream(vArrReciptpic), "photo1.jpg");
            //params.put("photo2", new ByteArrayInputStream(vArrPhoto2), "photo2.jpg");

            /// Check Internet Connection \/
            boolean connected = false;
            connected = CheckIntCon();
            /// Check Internet Connection /\

            if (connected == false) {
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.check_internet),Toast.LENGTH_LONG).show();
                Enable();

            } else if (connected == true) {

                PostURLUtils.post("/register", params, new JsonHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        //Disable();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject obj) {
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


                                Alert("پیام مرکز",resultMessage);
                                //Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();

                            } //else if (resultID == 1) {//in the first photo, the FACE was Not Found
                                //Toast.makeText(getApplicationContext(), "Change User Name"+resultMessage, Toast.LENGTH_SHORT).show();
                                //txtresult.setText("The face was not Found in the first photo");
                            //}else if (resultID == 2) {//in the first photo, the FACE was Not Found
                                //Toast.makeText(getApplicationContext(), "This User Name is not register"+resultMessage, Toast.LENGTH_SHORT).show();
                                //txtresult.setText("The face was not Found in the first photo");
                            //}//else if (resultID == 3) {//in the first photo, the FACE was Not Found
                            //Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();
                            //txtresult.setText("The face was not Found in the first photo");
                            //}else if (resultID == 4) {//in the first photo, the FACE was Not Found
                            // Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();
                            //txtresult.setText("The face was not Found in the first photo");
                            //}
                            // $.B /\

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, org.json.JSONObject errorResponse) {

                        Toast.makeText(getApplicationContext(),"onFailure",Toast.LENGTH_LONG).show();
                        String message = throwable.getMessage();
                        //TextView result = (TextView) findViewById(R.id.voiceresult);
                        //result.setText(message);
                        //TextView txtresult = (TextView) findViewById(R.id.txtresult);
                        if (message == null){
                            //txtresult.setText("Sorry :-( . It's not available in your country.");
                        }else {
                            //txtresult.setText("The image was not uploaded correctly. Check your connection please." );
                        }
                        Enable();
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

            Enable();

        }
    }

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

    public void Alert (String title, String message){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
//set icon
                .setIcon(R.drawable.alertgreen)
//set title
                .setTitle(title)
//set message
                .setMessage(message)
//set positive button
                .setPositiveButton("ادامه", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        //finish();
                        Clearcls();
                        Enable();
                    }
                })
//set negative button
                .setNegativeButton("تائید و خروج", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what should happen when negative button is clicked
                        //Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();


                        Intent intent = new Intent(Register.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent, 2);
                    }
                })
                .show();
    }

    private boolean CheckIntCon() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void Disable(){
        AllLayout.setEnabled(false);
        Reciptbtn.setEnabled(false);
        Registerbtn.setEnabled(false);
        Registerbtn.setVisibility(View.INVISIBLE);
    }

    private void Enable(){
        AllLayout.setEnabled(true);
        Reciptbtn.setEnabled(true);
        Registerbtn.setEnabled(true);
        Registerbtn.setVisibility(View.VISIBLE);
    }

    private void Clearcls(){
        TechnicalSupportchck.setChecked(false);
        FieldExpertchck.setChecked(false);//Defult

        UserNametxt.setText("");
        Passwordtxt.setText("");
        PasswordRepettxt.setText("");
        AccountNumbertxt.setText("");
        BankName.setText("");
        AccountNametxt.setText("");

        Reciptbtn.setEnabled(true);
        Registerbtn.setEnabled(false);

        Path1 = "";

        ReciptPictureimg.setImageBitmap(null);//(circleBitmap);//$.B for circle
        ReciptPictureimg.setEnabled(true);//$.B
        ReciptPictureimg.setBackground(null);
    }

}
