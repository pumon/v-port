package com.project.esh_an.portal1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class changepswd extends AppCompatActivity implements View.OnClickListener{

    Button btn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_changepswd);

        btn= findViewById(R.id.dont_change_pswd);
        btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.dont_change_pswd:
                //startActivity(new Intent(this,homepage.class));
        }
    }
}

