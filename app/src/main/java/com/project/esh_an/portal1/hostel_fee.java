package com.project.esh_an.portal1;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.esh_an.portal1.activity.InitialScreenActivity;
import com.project.esh_an.portal1.add_on.netConnection;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class hostel_fee extends AppCompatActivity implements View.OnClickListener{

    TextView girls_rent,boys_rent;
    ImageView girl,boy;

protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hostel_fee);

        girls_rent = findViewById(R.id.girls_rent);
        girls_rent.setOnClickListener(this);

        boys_rent = findViewById(R.id.boys_rent);
        boys_rent.setOnClickListener(this);

        girl = (ImageView)findViewById(R.id.imagegirl);
        girl.setOnClickListener(this);

        boy = (ImageView) findViewById(R.id.imageboy);
        boy.setOnClickListener(this);


    //net connection
    netConnection checkCon=new netConnection(this);
    if(checkCon.isConnected()==false){
        new AlertDialog.Builder(this)
                .setMessage("Internet Connection not Available..!!!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        startActivity(intent);
                    }
                })
                .show();
    }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.girls_rent:
                Intent i = new Intent(hostel_fee.this, rentdetails.class);
                i.putExtra("gender", "GIRL");
                i.putExtra("feeType","Hostel Fee");
                startActivity(i);
                finish();
                break;

            case R.id.boys_rent:
                Intent i2 = new Intent(hostel_fee.this, rentdetails.class);
                i2.putExtra("gender", "BOY");
                i2.putExtra("feeType","Hostel Fee");
                startActivity(i2);
                finish();
                break;

            case R.id.imagegirl:
                Intent i3 = new Intent(hostel_fee.this, rentdetails.class);
                i3.putExtra("gender", "GIRL");
                i3.putExtra("feeType","Hostel Fee");
                startActivity(i3);
                finish();
                break;

            case R.id.imageboy:
                Intent i4 = new Intent(hostel_fee.this, rentdetails.class);
                i4.putExtra("gender", "BOY");
                i4.putExtra("feeType","Hostel Fee");
                startActivity(i4);
                finish();
                break;
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        netConnection checkCon=new netConnection(this);
        if(checkCon.isConnected()==false){
            new AlertDialog.Builder(this)
                    .setMessage("Internet Connection not Available..!!!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .show();
        }

    }
}
