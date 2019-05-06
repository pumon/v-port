package com.project.esh_an.portal1;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.project.esh_an.portal1.activity.InitialScreenActivity;
import com.project.esh_an.portal1.add_on.netConnection;
import com.project.esh_an.portal1.utility.Constants;
import com.project.esh_an.portal1.utility.SharedPreferenceUtils;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class rentdetails extends AppCompatActivity {

    Button ok;
    private EditText roomnum, usn, amt;
    private Spinner semester;
    ArrayAdapter<CharSequence> adapter;
    String gender = "", feeType = "", sem = "";
    private  String collegeType = "";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rentdetails);


        //net connection
        netConnection checkCon = new netConnection(this);
        if (checkCon.isConnected() == false) {
            new AlertDialog.Builder(this)
                    .setTitle("WARNING")
                    .setMessage("No Internet Connection!!\n\nEnable it..!!!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .show();
        }

        roomnum = (EditText) findViewById(R.id.roomnum);
        usn = (EditText) findViewById(R.id.usn);
        amt = (EditText) findViewById(R.id.amt);

        ok = (Button) findViewById(R.id.button2);

        semester = (Spinner) findViewById(R.id.spinner);

        Intent in = getIntent();
        if (in != null) {
            gender = in.getExtras().getString("gender");
            feeType = in.getExtras().getString("feeType");
        }



        String type = (String) SharedPreferenceUtils.get(rentdetails.this, Constants.COLLEGE_TYPE, "aa");
        switch (type) {
            case Constants.BE:
                collegeType = Constants.BE_STUDENTS_INFO;
                adapter = ArrayAdapter.createFromResource(this, R.array.semester, android.R.layout.simple_spinner_item);
                usn.setHint("USN");
                break;
            case Constants.MBA:
                collegeType = Constants.MBA_STUDENTS_INFO;
                adapter = ArrayAdapter.createFromResource(this, R.array.semester4, android.R.layout.simple_spinner_item);
                usn.setHint("Roll No.");
                break;
            case Constants.PUC:
                collegeType = Constants.PUC_STUDENTS_INFO;
                adapter = ArrayAdapter.createFromResource(this, R.array.semester4, android.R.layout.simple_spinner_item);
                usn.setHint("Roll No.");
                break;
        }
        semester.setAdapter(adapter);


        semester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i > 0) {
                    sem = (String) adapterView.getItemAtPosition(i);
                    Toast.makeText(getBaseContext(), adapterView.getItemAtPosition(i) + " selected", Toast.LENGTH_LONG).show();
                } else {
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(rentdetails.this, checksum.class);
                i.putExtra("amount", amt.getText().toString());
                i.putExtra("feeType", feeType);
                i.putExtra("gender", gender);
                i.putExtra("roomNo", roomnum.getText().toString());
                i.putExtra("usn", usn.getText().toString());
                i.putExtra("sem", sem);
                i.putExtra("custid",usn.getText().toString());
                i.putExtra("orderid",new randnum().getNum());
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        netConnection checkCon = new netConnection(this);
        if (checkCon.isConnected() == false) {
            new AlertDialog.Builder(this)
                    .setTitle("WARNING")
                    .setMessage("No Internet Connection!!\n\nEnable it..!!!")
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

