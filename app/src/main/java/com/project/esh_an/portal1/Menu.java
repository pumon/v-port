package com.project.esh_an.portal1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Menu extends AppCompatActivity implements View.OnClickListener {
    Button my_profile, pay_fee, check_fee, change_pswd,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu2);

        my_profile= findViewById(R.id.my_profile);
        my_profile.setOnClickListener(this);

        pay_fee= findViewById(R.id.pay_fee);
        pay_fee.setOnClickListener(this);

        check_fee= findViewById(R.id.check_fee);
        check_fee.setOnClickListener(this);

        change_pswd= findViewById(R.id.change_pswd);
        change_pswd.setOnClickListener(this);

        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Menu.this,Signin.class));
                finish();
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_profile:
                startActivity(new Intent(this,myprofile.class));
                break;
            case R.id.pay_fee:
               // startActivity(new Intent(this,payfee.class));
                break;
            case R.id.check_fee:
                finish();
               // startActivity(getIntent());
                break;
            case R.id.change_pswd:
                startActivity(new Intent(this,changepswd.class));
                break;
        }
    }
}
