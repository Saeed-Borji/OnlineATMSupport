package com.epsengco.onlineatmsupport;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MultiPreviewInboxActivity extends AppCompatActivity {

    private LinearLayout MSG1layou;
    private LinearLayout MSG1layoutview;
    private Button MSG1;
    int VisibilityButton1 = 0;

    private LinearLayout MSG2layou;
    private LinearLayout MSG2layoutview;
    private Button MSG2;
    int VisibilityButton2 = 0;

    private LinearLayout MSG3layou;
    private LinearLayout MSG3layoutview;
    private Button MSG3;
    int VisibilityButton3 = 0;


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
        if(extras !=null) {
            Username = extras.getString("Username");
            Accounttype = extras.getInt("Accounttype");
            Accounttypename = extras.getString("Accounttypename");
            Check = extras.getString("Check");
            ProductName = extras.getString("ProductName");
            QuestionNotRead = extras.getInt("QuestionNotRead");
            AnswerNotRead = extras.getInt("AnswerNotRead");
            MessageCount = extras.getInt("MessageCount");//Tedad message haye markaz
        }



        MSG1layou = (LinearLayout)findViewById(R.id.msg1layout);
        MSG1layoutview = (LinearLayout)findViewById(R.id.msg1layoutview);
        MSG1 = (Button)findViewById(R.id.msg1btn);

        MSG2layou = (LinearLayout)findViewById(R.id.msg2layout);
        MSG2layoutview = (LinearLayout)findViewById(R.id.msg2layoutview);
        MSG2 = (Button)findViewById(R.id.msg2btn);

        MSG3layou = (LinearLayout)findViewById(R.id.msg3layout);
        MSG3layoutview = (LinearLayout)findViewById(R.id.msg3layoutview);
        MSG3 = (Button)findViewById(R.id.msg3btn);
        //...



        //$.B \/ Set Visibility
        if (MessageCount == 1){
            MSG1layou.setVisibility(View.VISIBLE);
            MSG1.setVisibility(View.VISIBLE);
        }else if (MessageCount == 2){
            MSG1layou.setVisibility(View.VISIBLE);
            //MSG1layoutview.setVisibility(View.VISIBLE);
            MSG2layou.setVisibility(View.VISIBLE);
            //MSG2layoutview.setVisibility(View.VISIBLE);
        }else if (MessageCount == 3){
            MSG1layou.setVisibility(View.VISIBLE);
            //MSG1layoutview.setVisibility(View.VISIBLE);
            MSG2layou.setVisibility(View.VISIBLE);
            //MSG2layoutview.setVisibility(View.VISIBLE);
            MSG3layou.setVisibility(View.VISIBLE);
            //MSG3layoutview.setVisibility(View.VISIBLE);
        } //...



        //$.B OnClick \/
        MSG1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VisibilityButton1 == 0){
                    //Desable();
                    MSG1layoutview.setVisibility(View.VISIBLE);
                    VisibilityButton1 = 1;
                }else if (VisibilityButton1 == 1){
                    //Desable();
                    MSG1layoutview.setVisibility(View.GONE);
                    VisibilityButton1 = 0;
                }
            }
        });

        MSG2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VisibilityButton2 == 0){
                    //Desable();
                    MSG2layoutview.setVisibility(View.VISIBLE);
                    VisibilityButton2 = 1;
                }else if (VisibilityButton2 == 1){
                    //Desable();
                    MSG2layoutview.setVisibility(View.GONE);
                    VisibilityButton2 = 0;
                }
            }
        });

        MSG3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VisibilityButton3 == 0){
                    //Desable();
                    MSG3layoutview.setVisibility(View.VISIBLE);
                    VisibilityButton3 = 1;
                }else if (VisibilityButton3 == 1){
                    //Desable();
                    MSG3layoutview.setVisibility(View.GONE);
                    VisibilityButton3 = 0;
                }
            }
        });


    }

    public void Enable(){
        //MSG1layoutview.setVisibility(View.VISIBLE);
        //MSG2layoutview.setVisibility(View.VISIBLE);
        //MSG3layoutview.setVisibility(View.VISIBLE);
    }

    public void Desable(){
        MSG1layoutview.setVisibility(View.GONE);
        VisibilityButton1=0;
        MSG2layoutview.setVisibility(View.GONE);
        VisibilityButton2=0;
        MSG3layoutview.setVisibility(View.GONE);
        VisibilityButton3=0;
        //...
    }

}
