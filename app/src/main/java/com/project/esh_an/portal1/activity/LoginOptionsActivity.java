package com.project.esh_an.portal1.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.project.esh_an.portal1.R;
import com.project.esh_an.portal1.Signin;
import com.project.esh_an.portal1.utility.Constants;
import com.project.esh_an.portal1.utility.SharedPreferenceUtils;

public class LoginOptionsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_options);

        Button BE_button = findViewById(R.id.be_button);
        Button MBA_button = findViewById(R.id.mba_button);
        Button PUC_button = findViewById(R.id.puc_button);

        BE_button.setOnClickListener(this);
        MBA_button.setOnClickListener(this);
        PUC_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.be_button:
                SharedPreferenceUtils.set(LoginOptionsActivity.this, Constants.COLLEGE_TYPE, Constants.BE);
                startActivity( new Intent(LoginOptionsActivity.this, Signin.class));
                finish();
                break;
            case R.id.mba_button:
                SharedPreferenceUtils.set(LoginOptionsActivity.this, Constants.COLLEGE_TYPE, Constants.MBA);
                startActivity(new Intent(LoginOptionsActivity.this, Signin.class));
                finish();
                break;
            case R.id.puc_button:
                SharedPreferenceUtils.set(LoginOptionsActivity.this, Constants.COLLEGE_TYPE, Constants.PUC);
                startActivity(new Intent(LoginOptionsActivity.this, Signin.class));
                finish();
                break;
        }
    }
}
