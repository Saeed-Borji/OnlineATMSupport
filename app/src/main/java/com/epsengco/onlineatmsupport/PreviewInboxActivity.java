package com.epsengco.onlineatmsupport;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

public class PreviewInboxActivity extends AppCompatActivity {

    String Username = "";
    int Accounttype = -1;
    String Accounttypename = "";
    String Check = "";
    String ProductName = "";
    int MessageCount = 0;

    int QuestionNotRead = 0;
    int AnswerNotRead = 0;

    private Button SendInbox;
    private Button ReciveInbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_inbox);
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
            MessageCount = extras.getInt("MessageCount");
        }

        SendInbox = (Button)findViewById(R.id.sendinboxButton);
        ReciveInbox = (Button)findViewById(R.id.reciveinboxButton);

        if (Accounttypename.equals("Technical")) {
            SendInbox.setVisibility(View.INVISIBLE);
            ReciveInbox.setText("سوال های ارسال شده");
        }


        if (QuestionNotRead == 1 && AnswerNotRead == 1){
            SendInbox.setBackgroundResource(R.drawable.buttonnotif);
            ReciveInbox.setBackgroundResource(R.drawable.buttonnotif);
        }else if (QuestionNotRead == 1 && AnswerNotRead == 0){
            SendInbox.setBackgroundResource(R.drawable.buttonnotif);
            ReciveInbox.setBackgroundResource(R.drawable.buttonw);
        }else if (QuestionNotRead == 0 && AnswerNotRead == 1){
            SendInbox.setBackgroundResource(R.drawable.buttonw);
            ReciveInbox.setBackgroundResource(R.drawable.buttonnotif);
        }else if (QuestionNotRead == 0 && AnswerNotRead == 0){
            SendInbox.setBackgroundResource(R.drawable.buttonw);
            ReciveInbox.setBackgroundResource(R.drawable.buttonw);
        }


        SendInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreviewInboxActivity.this, SendQuestionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, 2);
            }
        });

        ReciveInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreviewInboxActivity.this, MultiPreviewInboxActivity.class);
                intent.putExtra("Username",Username.toString());//send Username to next class
                intent.putExtra("Accounttype",Accounttype);
                intent.putExtra("Accounttypename",Accounttypename);
                intent.putExtra("Check",Check);//send data to next class
                intent.putExtra("ProductName",ProductName.toString());//send ProductName to next class
                intent.putExtra("QuestionNotRead",QuestionNotRead);
                intent.putExtra("AnswerNotRead",AnswerNotRead);
                intent.putExtra("MessageCount",MessageCount);//Tedad message ha dar markaz
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, 2);
            }
        });


    }

}
