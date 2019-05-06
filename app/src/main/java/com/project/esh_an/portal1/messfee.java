package com.project.esh_an.portal1;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.esh_an.portal1.activity.InitialScreenActivity;
import com.project.esh_an.portal1.add_on.messUser;
import com.project.esh_an.portal1.add_on.netConnection;
import com.project.esh_an.portal1.utility.Constants;
import com.project.esh_an.portal1.utility.SharedPreferenceUtils;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class messfee extends AppCompatActivity implements View.OnClickListener {

    Button proceed;
    EditText usn2, rmn, am, mon;
    private String collegeType = "";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messfee);

        usn2 = (EditText) findViewById(R.id.usn2);
        rmn = (EditText) findViewById(R.id.rmn);
        am = (EditText) findViewById(R.id.am);
        mon = (EditText) findViewById(R.id.mon);

        proceed = findViewById(R.id.proceed);
        proceed.setOnClickListener(this);

        String type = (String) SharedPreferenceUtils.get(messfee.this, Constants.COLLEGE_TYPE, "aa");
        switch (type) {
            case Constants.BE:
                collegeType = Constants.BE_STUDENTS_INFO;
                usn2.setHint("USN");
                break;
            case Constants.MBA:
                collegeType = Constants.MBA_STUDENTS_INFO;
                usn2.setHint("Roll No.");
                break;
            case Constants.PUC:
                collegeType = Constants.PUC_STUDENTS_INFO;
                usn2.setHint("Roll No.");
                break;
        }
        //net connection
        netConnection checkCon = new netConnection(this);
        if (checkCon.isConnected() == false) {
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
//    DatabaseReference databaseMess = FirebaseDatabase.getInstance().getReference("MessFee");

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.proceed:
                if (validate()) {
//                        addMessDetails();
                    Intent i = new Intent(this, checksum.class);
                    i.putExtra("amount", am.getText().toString());
                    i.putExtra("feeType", "Mess Fee");
                    i.putExtra("usn", usn2.getText().toString());
                    i.putExtra("roomNo", rmn.getText().toString());
                    i.putExtra("month", mon.getText().toString());
                    i.putExtra("custid",usn2.getText().toString());
                    i.putExtra("orderid",new randnum().getNum());
                    startActivity(i);
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        netConnection checkCon = new netConnection(this);
        if (checkCon.isConnected() == false) {
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

   /* private void addMessDetails() {

        String droom = rmn.getText().toString();
        String dmonth = mon.getText().toString();
        String dusn = usn2.getText().toString();
        String dam = am.getText().toString();

        if (!TextUtils.isEmpty(dusn)) {
            String id = databaseMess.push().getKey();
            messUser mess = new messUser(droom,dusn,dmonth,dam);
            databaseMess.child(id).setValue(mess);
            startActivity(getIntent());
            finish();
        }
    }*/

    private Boolean validate() {
        Boolean result = false;

        String vroom = rmn.getText().toString();
        String vmonth = mon.getText().toString();
        String vusn = usn2.getText().toString();
        if (vroom.isEmpty() || vmonth.isEmpty() || vusn.isEmpty()) {
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        } else {
            result = true;
        }

        return result;
    }
}

