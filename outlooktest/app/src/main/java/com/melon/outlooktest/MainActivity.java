package com.melon.outlooktest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private LinearLayout panelSignin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        panelSignin = findViewById(R.id.panelSignin);

        (findViewById(R.id.signinBtn)).setOnClickListener((view)->{onSignin(); } );
        (findViewById(R.id.eventloadBtn)).setOnClickListener((view)->{ } );
        (findViewById(R.id.signoutBtn)).setOnClickListener((view)->{ } );


//        setPanelVisibility(true, false, false);
    }


    private void onSignin(){
        Toast.makeText(MainActivity.this, "hello<user>", Toast.LENGTH_LONG).show();

//        setPanelVisibility(false, true, false);
    }



//    private void setPanelVisibility(Boolean showSignIn, Boolean showLoadEvents, Boolean showList){
//        panelSi
//
//
//    }
}