package com.epsengco.onlineatmsupport;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MultiPreviewInboxActivity extends AppCompatActivity {

    private Button SelectTrueAnser;
    private Button CreateAnser;
    private Button MSGbtn;

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

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_preview_inbox);
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
        if (extras != null) {
            Username = extras.getString("Username");
            Accounttype = extras.getInt("Accounttype");
            Accounttypename = extras.getString("Accounttypename");
            Check = extras.getString("Check");
            ProductName = extras.getString("ProductName");
            QuestionNotRead = extras.getInt("QuestionNotRead");
            AnswerNotRead = extras.getInt("AnswerNotRead");
            MessageCount = extras.getInt("MessageCount");//Tedad message haye markaz
        }

        Navigate = (TextView) findViewById(R.id.textnavigate);
        Navigate.setText(Username + " > " + Accounttypename + " > Server Inbox = " + MessageCount);

        SelectTrueAnser = (Button) findViewById(R.id.buttonSelectTrueAnser);//Field
        CreateAnser = (Button) findViewById(R.id.buttonCreateAnser);//Technical

        //$.B \/ Set Visibility
        if (Accounttypename.equals("Field")) {
            SelectTrueAnser.setVisibility(View.INVISIBLE);//VISIBLE
            CreateAnser.setVisibility(View.INVISIBLE);
            Textbtn = "پاسخ کارشناس لایه دو ";
        } else if (Accounttypename.equals("Technical")) {
            SelectTrueAnser.setVisibility(View.INVISIBLE);
            CreateAnser.setVisibility(View.INVISIBLE);//VISIBLE
            Textbtn = "سوال کارشناس لایه یک ";
        }
        //$.B /\ Set Visibility

        LinearLayout MSGlayout= (LinearLayout) findViewById(R.id.msglayout);
        for (int i = 0; i < MessageCount; i++) {

            MSGbtn = new Button (MultiPreviewInboxActivity.this);
            MSGbtn.setWidth(40);
            MSGbtn.setHeight(20);
            MSGbtn.setId(i);
            MSGbtn.setText(Textbtn + (i+1));
            MSGlayout.addView(MSGbtn);

            MSGbtn =(Button)findViewById(i);
            int btnid = i;
            MSGbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goQoustionClass(btnid);
                }
            });

        }

    }


    public void goQoustionClass(int number){
        Intent intent = new Intent(MultiPreviewInboxActivity.this, GetQuestionSetAnswer.class);
        intent.putExtra("Username",Username);//send data to next class
        intent.putExtra("Accounttype",Accounttype);
        intent.putExtra("Accounttypename",Accounttypename);
        intent.putExtra("Check",Check);//send data to next class
        intent.putExtra("MessageCount",MessageCount);
        intent.putExtra("QuestionNumber",number);//send data to next class
        startActivityForResult(intent, 2);

        Toast.makeText(MultiPreviewInboxActivity.this, "x "+number , Toast.LENGTH_SHORT).show();
    }

    public void Enable(){

    }

    public void Desable(){

    }

}
