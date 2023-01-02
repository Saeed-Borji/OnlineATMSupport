package com.epsengco.onlineatmsupport;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PathDashPathEffect;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.epsengco.onlineatmsupport.ui.AudioRecorder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public class ModuleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner SelectModule;
    private Spinner SelectBankname;
    private ImageButton VoiceRecButton;
    private String VoiceFileName = "";
    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    String Path1 = null;

    private ImageView ErrorPic;
    private ImageButton ErrorBtn;

    private EditText QuestionBox;

    private Button SendQuestion;
    private Button Clear;

    private TextView Navigate;

    String Username = "";
    int Accounttype = -1;
    String Accounttypename = "";
    String Check = "";
    String ProductName = "";
    String HardSoft = "";
    String ProductModel = "";
    String Questionstr = "";
    Bitmap pic1 = null;
    String SelectModulestr = "";
    String SelectBanknamestr = "";
    int oldModuleindx = -1;
    int oldBanknameindx = -1;

    private byte[] ArrPic = null;
    private byte[] ArrVoice = null;


    String[] ModuleNameHW={"ماژول مورد نظر را انتخاب کنید","برق و ارت", "بخش گاوصندوق ","بخش کابینت ","کارتخوان ","پرینتر مشتری","پرینتر ژورنال ","آنتی فراد","آنتی اسکیم ","کامپیوتر","اسپشیال ","دیسپنسر","بدنه و پایه ","شبکه","سایر..."};
    String[] ModuleNameSF={"ماژول مورد نظر را انتخاب کنید","ویندوز","دوربین","شبکه و امنیت","ابزار های تست","نرم افزار لایه بالا","نرم افزار لایه پایین","آپدیت ها","درایور ها","سایر..."};
    String[] BankName={"بانک مورد نظر را انتخاب کنید","ملی","صادرات","تجارت","پاسارگاد","ملت","سامان","دی","اقتصادنوین","پارسیان","آینده","سپه","حکمت ایرانیان","سایر..."};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);
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
        VoiceRecButton = (ImageButton)findViewById(R.id.Voicerecorderbtn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        ErrorBtn = (ImageButton)findViewById(R.id.Errorpicbtn);
        ErrorPic = (ImageView)findViewById(R.id.Errorpicimg);

        SendQuestion = (Button)findViewById(R.id.buttonsendquestion);
        Clear = (Button)findViewById(R.id.buttonclear);

        QuestionBox = (EditText) findViewById(R.id.questionbox);

        Bundle extras = getIntent().getExtras();//Recive Message
        if(extras !=null) {
            Username = extras.getString("Username");
            Accounttype = extras.getInt("Accounttype");
            Accounttypename = extras.getString("Accounttypename");
            Check = extras.getString("Check");
            ProductName = extras.getString("ProductName");
            HardSoft = extras.getString("HardSoft");
            ProductModel = extras.getString("ProductModel");
        }
        Navigate = (TextView)findViewById(R.id.textnavigate);
        Navigate.setText(Username + " > " + Accounttypename+ " > " + ProductName + " > " + ProductModel + " > " +HardSoft);


        //$.B _ Spineer for select module \/
        SelectModule =(Spinner)findViewById(R.id.spinnerModule);
        SelectModule.setOnItemSelectedListener(this);
        if (HardSoft.equals("Hard")){
            ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,ModuleNameHW);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            SelectModule.setAdapter(aa);
        }else if (HardSoft.equals("Soft")){
            ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,ModuleNameSF);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            SelectModule.setAdapter(aa);
        }
/*
        SelectModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectModulestr = "OK";
                SelectBanknamestr = "";
            }
        });

 */
        //$.B _ Spineer for select module /\

        //$.B _ Spineer for select Bank \/
        SelectBankname =(Spinner)findViewById(R.id.spinnerBank);
        SelectBankname.setOnItemSelectedListener(this);
        final ArrayAdapter bb = new ArrayAdapter(this,android.R.layout.simple_spinner_item,BankName);
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SelectBankname.setAdapter(bb);
/*
        SelectBankname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectModulestr = "";
                SelectBanknamestr = "OK";
            }
        });


 */
        //$.B _ Spineer for select Bank /\
        VoiceRecButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressStatus = 0;

                new Thread(new Runnable() {
                    public void run() {

                        while (progressStatus < 100) {
                            progressStatus += 12;
                            // Update the progress bar and display the
                            //current value in the text view
                            handler.post(new Runnable() {
                                public void run() {
                                        progressBar.setProgress(progressStatus);
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

                VoiceFileName = "sound";
                VoiceRecord(VoiceFileName);

            }
        });

        ErrorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);//SD Have or Havent

                pic1 = null;
                ErrorPic.setImageBitmap(pic1);

                if (isSDPresent == true){//Have SD Cars
                    File dir_image2 = new File(Environment.getExternalStorageDirectory()+
                            File.separator+"S_B");

                    Path1 = "1";

                    performFileSearch();
                }else if (isSDPresent == false){//Haven't CD Card
                }
            }
        });

        SendQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Check.equals("-1")){
                    Toast.makeText(getApplicationContext(), "دسترسی این کاربر > "+Username+"< تایید نشده است", Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(getApplicationContext(), "Go to Send data to server", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(), "SendQuestion - 247", Toast.LENGTH_SHORT).show();
                    GetQuestionData();
                }
            }
        });

        Clear.setOnClickListener(new View.OnClickListener() {
            private String path;

            @Override
            public void onClick(View v) {
                Clearcls();
            }
        });

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
                        VoiceRecButton.setEnabled(true);
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


    /**
     * <p>Callback method to be invoked when an item in this view has been
     * selected. This callback is invoked only when the newly selected
     * position is different from the previously selected position or if
     * there was no selected item.</p>
     * <p>
     * Implementers can call getItemAtPosition(position) if they need to access the
     * data associated with the selected item.
     *
     * @param parent   The AdapterView where the selection happened
     * @param view     The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id       The row id of the item that is selected
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //String wichspinner = SelectModule.getItemAtPosition(0).toString();
        //wichspinner = SelectBankname.getItemAtPosition(0).toString();
        Integer indexValueModule = SelectModule.getSelectedItemPosition();
        Integer indexValueBankname = SelectBankname.getSelectedItemPosition();

        if (SelectModulestr.equals("ماژول مورد نظر را انتخاب کنید") && SelectBanknamestr.equals("ماژول مورد نظر را انتخاب کنید")){
            SelectModulestr = "";
            SelectBanknamestr ="";
        }
        if ((indexValueModule != 0 && indexValueBankname == 0)){
            SelectModulestr = SelectModule.getSelectedItem().toString();
            oldModuleindx = indexValueModule;
        }else if (indexValueModule == 0 && indexValueBankname != 0){
            SelectBanknamestr = SelectBankname.getSelectedItem().toString();
            oldBanknameindx = indexValueBankname;
        }else if (indexValueModule != oldModuleindx && indexValueBankname == oldBanknameindx){
            SelectModulestr = SelectModule.getSelectedItem().toString();
            oldModuleindx = indexValueModule;
        }else if (indexValueModule == oldModuleindx && indexValueBankname != oldBanknameindx){
            SelectBanknamestr = SelectBankname.getSelectedItem().toString();
            oldBanknameindx = indexValueBankname;
        }

    }

    /**
     * Callback method to be invoked when the selection disappears from this
     * view. The selection can disappear for instance when touch is activated
     * or when the adapter becomes empty.
     *
     * @param parent The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void performFileSearch() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        /*
        LinearLayout LinerDate1 =(LinearLayout)findViewById(R.id.date1);
        LinerDate1.setVisibility(View.VISIBLE);
        LinearLayout LinerDate2 =(LinearLayout)findViewById(R.id.date2);
        LinerDate2.setVisibility(View.VISIBLE);
        LinearLayout LinerDate3 =(LinearLayout)findViewById(R.id.date3);
        LinerDate3.setVisibility(View.VISIBLE);

         */

        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    //ImageView photo1 = (ImageView) findViewById(R.id.Photo1);
                    ErrorPic.setImageURI(selectedImage);
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

    public String getPath(Uri uri)//$.B
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }
    private void setPic(float angle1) {//Set Piic1 and Pic2 _ rotate 90 _ W=300 H=300 _ Make Circle

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
                Path1="2";//2 = Access denied to Pat
                Toast.makeText(getApplicationContext(),"Access denied to memory" , Toast.LENGTH_LONG).show();
                //ImageView Rot1 = (ImageView) findViewById(R.id.RotatePhoto1);
                //Rot1.setVisibility(View.INVISIBLE);

            }else {
                if (pic0.getHeight() < 120 || pic0.getWidth() < 120) {
                    //txtresult.setText("The quality of the left side photo is low. It must be Height>120 _ Width>120");
                    Path1 = "";
                }else {
                    Matrix matrix = new Matrix();
                    matrix.preRotate(angle);
                    pic1 = Bitmap.createBitmap(pic0,0,0, pic0.getWidth(),pic0.getHeight(),matrix, true);//Rotate 90
                    pic1 = Bitmap.createScaledBitmap(pic1,800,600,true);//W=800 H=600
                    pic0.recycle();

                    /*
                    Bitmap circleBitmap = Bitmap.createBitmap(pic1.getWidth(), pic1.getHeight(), Bitmap.Config.ARGB_8888);//Make Circle \/
                    BitmapShader shader = new BitmapShader(pic1,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                    Paint paint = new Paint();
                    paint.setShader(shader);
                    paint.setAntiAlias(true);
                    Canvas c = new Canvas(circleBitmap);
                    c.drawCircle(pic1.getWidth()/2, pic1.getHeight()/2, pic1.getWidth()/2, paint);//Make Circle /\

                     */

                    //ImageView btn1 = (ImageView) findViewById(R.id.Photo1);//$.B
                    ErrorPic.setImageBitmap(pic1);//(circleBitmap);//$.B for circle
                    //btn1.setBackgroundResource(R.drawable.freebackground);
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

    ////// SEND QUESTION TO SERVER \/
    private String path;
    private void PostData() {

        try {

                if (!Username.equals("") && !Accounttypename.equals("") && !ProductName.equals("") && !HardSoft.equals("")){

                    Disable();//Disable All Button or ...

                    //if(pic1 != null)
                    //{
                        Toast.makeText(getApplicationContext(),"498",Toast.LENGTH_LONG).show();

                        if (!Path1.equals("")){
                            GetPicByte(Path1);
                        }else{
                            ArrPic = null;
                            ArrPic = new byte[1];
                            ArrPic[0] = 0;
                        }
                    //}else{
                        //ArrPic = null;
                        //ArrPic = new byte[1];
                        //ArrPic[0] = 0;
                    //}

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

                    Long vt = System.currentTimeMillis() / 1000;

                    RequestParams params = new RequestParams();

                    params.put("username",Username);
                    params.put("accountname",Accounttypename);
                    params.put("productname",ProductName);
                    params.put("hardsoft",HardSoft);
                    params.put("productmodel",ProductModel);
                    params.put("modulename",SelectModulestr.toString());
                    params.put("bankname",SelectBanknamestr.toString());
                    params.put("message",Questionstr);
                    params.put("ErrorPic", new ByteArrayInputStream(ArrPic), "ErrorPic.jpg");
                    params.put("ErrorVoice", new ByteArrayInputStream(ArrVoice), "ErrorVoice.mp3");


                    //$.B _ Save Message in the cellphone \/
                    String message ="نام محصول :" + ProductName + HardSoft +"\n"+
                            "مدل محصول :" + ProductModel + "\n"+
                             "نام ماژول :" + SelectModulestr + "\n"+
                            "نام بانک :" + SelectBanknamestr +"\n"+
                            " ___________________  : متن سوال " + "\n"+
                            Questionstr;

                    SaveQuestionMessage(message , ArrPic,ArrVoice);
                    //$.B _ Save Message in the cellphone /\

                    /// Check Internet Connection \/
                    boolean connected = false;
                    connected = CheckIntCon();
                    /// Check Internet Connection /\
                    if (connected == false) {
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.check_internet),Toast.LENGTH_LONG).show();

                        Enable();

                    } else if (connected == true) {

                        PostURLUtils.post("/question", params, new JsonHttpResponseHandler() {
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
    ////// SEND QUESTION TO SERVER /\

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
                        Intent intent = new Intent(ModuleActivity.this, ChooseSituation.class);
                        intent.putExtra("Username",Username.toString());//send data to next class
                        intent.putExtra("Accounttype",Accounttype);
                        intent.putExtra("Accounttypename",Accounttypename);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent, 2);
                    }
                })
                .show();
    }


    public void GetQuestionData(){

        if (!SelectModulestr.equals("") && !SelectBanknamestr.equals("")){

            Questionstr = QuestionBox.getText().toString();//Text of Question Box
            if (!Questionstr.equals("")){

                //Go To Send Param Request to server
                //Disable();
                PostData();

            }else {
                Toast.makeText(getApplicationContext(), "لطفا متن توضیح مشکل را وارد نمایید", Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(getApplicationContext(), "لطفا نوع ماژول و نام بانک مورد نظر خود را مشخص نمایید", Toast.LENGTH_SHORT).show();
        }

    }

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

    private void SaveQuestionMessage(String Message,byte[] Pic, byte[] Voice) throws IOException {

        Toast.makeText(getApplicationContext(),"805",Toast.LENGTH_LONG).show();
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

            //if (!Pic.equals(null) || Pic[0] != 0)
            //{
                FileOutputStream pic = new FileOutputStream(folder+"//pic.jpg");
                pic.write(Pic);
                pic.close();
            //}

            //if (!Voice.equals(null) || Voice[0] != 0)
            //{
                FileOutputStream voice = new FileOutputStream(folder+"//voice.mp3");
                voice.write(Voice);
                voice.close();
            //}

        } catch (Exception e) {
            //Log.e(TAG, e.getMessage());
        }

    }

    private void Disable(){
        SelectModule.setEnabled(false);
        SelectBankname.setEnabled(false);
        QuestionBox.setEnabled(false);
        VoiceRecButton.setEnabled(false);
        ErrorBtn.setEnabled(false);
        ErrorPic.setEnabled(false);
        SendQuestion.setEnabled(false);
        Clear.setEnabled(false);
        //pic1=null;
    }

    private void Enable(){
        SelectModule.setEnabled(true);
        SelectBankname.setEnabled(true);
        QuestionBox.setEnabled(true);
        VoiceRecButton.setEnabled(true);
        ErrorBtn.setEnabled(true);
        ErrorPic.setEnabled(true);
        SendQuestion.setEnabled(true);
        Clear.setEnabled(true);
    }

    private void Clearcls(){
        //SelectBank.setAdapter(bb);
        //SelectModule.setAdapter(aa);
        //SelectModulestr = "";
        //SelectBanknamestr = "";
        //oldModuleindx = -1;
        //oldBanknameindx = -1;

        Questionstr = "";
        QuestionBox.setText("");

        Path1 = "";
        ErrorPic.setImageBitmap(null);
        ErrorPic.setBackground(null);
        pic1 = null;

        progressStatus = 0;
        progressBar.setProgress(progressStatus);
        //final String path;
        String Path = "sound";
        this.path = sanitizePath(Path);
        new File(this.path).delete();

    }

    private boolean CheckIntCon() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
