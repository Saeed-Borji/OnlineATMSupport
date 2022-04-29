package com.epsengco.onlineatmsupport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

public class ChooseSituation extends AppCompatActivity {

    private Button Field;
    private Button Technical;
    private TextView Navigate;

    String Username = "";
    int Accounttype = -1;
    String Accounttypename = "";
    String Check = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_situation);

        // $.B \/

        Field  = (Button)findViewById(R.id.buttonfieldexpert);
        Technical = (Button)findViewById(R.id.buttontechnicalsupport);

        Bundle extras = getIntent().getExtras();//Recive Message
        if(extras !=null) {
            Username = extras.getString("Username");
            Accounttype = extras.getInt("Accounttype");
            Check = extras.getString("Check");
        }
        Navigate = (TextView)findViewById(R.id.textnavigate);
        Navigate.setText(Username + " > ");


        Field  = (Button)findViewById(R.id.buttonfieldexpert);
        Technical = (Button)findViewById(R.id.buttontechnicalsupport);

        if (Accounttype == -1){
            Field.setVisibility(View.INVISIBLE);
            Technical.setVisibility(View.INVISIBLE);
        }else if (Accounttype == 0){//Technical
            Field.setVisibility(View.INVISIBLE);
            Technical.setVisibility(View.VISIBLE);
        }else if (Accounttype == 1){//Field
            Field.setVisibility(View.VISIBLE);
            Technical.setVisibility(View.INVISIBLE);
        }else if (Accounttype == 2){//Both
            Field.setVisibility(View.VISIBLE);
            Technical.setVisibility(View.VISIBLE);
        }else{
            Field.setVisibility(View.INVISIBLE);
            Technical.setVisibility(View.INVISIBLE);
        }

        Field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Accounttypename = "Field";

                Intent intent = new Intent(ChooseSituation.this, ProductActivity.class);
                intent.putExtra("Username",Username.toString());//send data to next class
                intent.putExtra("Accounttype",Accounttype);
                intent.putExtra("Accounttypename",Accounttypename);
                intent.putExtra("Check",Check);//send data to next class
                startActivityForResult(intent, 2);

                //Toast.makeText(getApplicationContext(), Username + Accounttype + Check, Toast.LENGTH_SHORT).show();
            }
        });

        Technical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Accounttypename = "Technical";

                Intent intent = new Intent(ChooseSituation.this, ProductActivity.class);
                intent.putExtra("Username",Username.toString());//send data to next class
                intent.putExtra("Accounttype",Accounttype);
                intent.putExtra("Accounttypename",Accounttypename);
                intent.putExtra("Check",Check);//send data to next class
                startActivityForResult(intent, 2);

                //Toast.makeText(getApplicationContext(), Username + Accounttype + Check, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
