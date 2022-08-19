package com.epsengco.onlineatmsupport;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class SoftHardActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ImageView Hard;
    private ImageView Soft;
    private Spinner SelectModel;

    private TextView Navigate;

    String Test = "";
    String Username = "";
    int Accounttype = -1;
    String Accounttypename = "";
    String Check = "";
    String ProductName = "";
    String HardSoft = "";
    String ProductModel = "";
    String SelectModelstr = "";

    String[] ATMModel={"مدل مورد نظر را انتخاب کنید","Wincore Nixdorf","Dibold Nixdorf", "NCR","GRG","BanQuet","Hyosung","EastCom","سایر..."};
    String[] KioskModel={"مدل مورد نظر را انتخاب کنید","Wincore Nixdorf","Dibold Nixdorf", "NCR","GRG","BanQuet","Hyosung","EastCom","Faradis","Green","سایر..."};
    String[] OnlineModel={"مدل مورد نظر را انتخاب کنید","CISCO","HP","سایر..."};
    String[] VNBModel={"مدل مورد نظر را انتخاب کنید","8000","4000","سایر..."};
    String[] VSATModel={"مدل مورد نظر را انتخاب کنید","50inch","80inch","سایر..."};
    String[] CRSModel={"مدل مورد نظر را انتخاب کنید","Dibold Nixdorf", "NCR","GRG","BanQuet","Hyosung","EastCom","سایر..."};
    String[] VTMModel={"مدل مورد نظر را انتخاب کنید","Wincore Nixdorf", "NCR","GRG","BanQuet","Hyosung","EastCom","ADONIS","سایر..."};
    String[] EarthModel={"مدل مورد نظر را انتخاب کنید","سایر..."};
    String[] NotModel = {"محصول انتخاب شده مدل ندارد"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_hard);
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
            //ProductModel = extras.getString("ProductModel");
        }
        Navigate = (TextView)findViewById(R.id.textnavigate);
        Navigate.setText(Username + " > " + Accounttypename+ " > " + ProductName + " > ");

        //$.B _ Spineer for select model \/
        SelectModel = (Spinner)findViewById(R.id.spinnerModel);
        SelectModel.setOnItemSelectedListener(this);

        if (ProductName.equals("ATM")){
            ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,ATMModel);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            SelectModel.setAdapter(aa);
        }else if (ProductName.equals("Online")){
            ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,OnlineModel);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            SelectModel.setAdapter(aa);
        }else if (ProductName.equals("Kiosk")){
            ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,KioskModel);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            SelectModel.setAdapter(aa);
        }else if (ProductName.equals("VSAT")){
            ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,VSATModel);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            SelectModel.setAdapter(aa);
        }else if (ProductName.equals("VNB")){
            ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,VNBModel);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            SelectModel.setAdapter(aa);
        }else if (ProductName.equals("CRS")){
            ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,CRSModel);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            SelectModel.setAdapter(aa);
        }else if (ProductName.equals("VTM")){
            ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,VTMModel);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            SelectModel.setAdapter(aa);
        }else if (ProductName.equals("Earth")){
            ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,EarthModel);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            SelectModel.setAdapter(aa);
        }else{
            ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,NotModel);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            SelectModel.setAdapter(aa);
        }

        //$.B _ Spineer for select model /\



        Hard = (ImageView)findViewById(R.id.hard_view);
        Soft = (ImageView)findViewById(R.id.soft_view);

        Hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HardSoft = "Hard";

                if (SelectModelstr.equals("") || SelectModelstr.equals("مدل مورد نظر را انتخاب کنید")){
                    Toast.makeText(getApplicationContext(), "لطفا مدل مورد نظر را انتخاب کنید", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(SoftHardActivity.this, ModuleActivity.class);
                    intent.putExtra("Username", Username.toString());//send Username to next class
                    intent.putExtra("Accounttype", Accounttype);
                    intent.putExtra("Accounttypename", Accounttypename);
                    intent.putExtra("Check", Check);
                    intent.putExtra("ProductName", ProductName.toString());//send ProductName to next class
                    intent.putExtra("HardSoft", HardSoft.toString());//send Hardware to next class
                    intent.putExtra("ProductModel", SelectModelstr.toString());//send Hardware to next class
                    startActivityForResult(intent, 2);
                }
            }
        });

        Soft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HardSoft = "Soft";
                if (SelectModelstr.equals("") || SelectModelstr.equals("مدل مورد نظر را انتخاب کنید")){
                    Toast.makeText(getApplicationContext(), "لطفا مدل مورد نظر را انتخاب کنید", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(SoftHardActivity.this, ModuleActivity.class);
                    intent.putExtra("Username",Username.toString());//send Username to next class
                    intent.putExtra("Accounttype",Accounttype);
                    intent.putExtra("Check",Check);
                    intent.putExtra("Accounttypename",Accounttypename);
                    intent.putExtra("ProductName",ProductName.toString());//send ProductName to next class
                    intent.putExtra("HardSoft",HardSoft.toString());//send Hardware to next class
                    intent.putExtra("ProductModel",SelectModelstr.toString());//send Hardware to next class
                    startActivityForResult(intent, 2);
                }

            }
        });
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
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        SelectModelstr = SelectModel.getSelectedItem().toString();
        //Toast.makeText(getApplicationContext(), SelectModelstr, Toast.LENGTH_SHORT).show();

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
}
