package com.example.game.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class home extends AppCompatActivity {
    private final String SERVERURL = "http://192.168.43.190/CHSC/up.php";
    Button prin,hand;
    String flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        prin = (Button) findViewById(R.id.button2);
        hand=(Button)findViewById(R.id.button3);
        prin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag="P";
                Intent print=new Intent(home.this,MainActivity.class);
                print.putExtra("hdata",flag);
                startActivity(print);
            }
        });
        hand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag="H";
                Intent handt=new Intent(home.this,MainActivity.class);
                handt.putExtra("hdata", flag);
                startActivity(handt);
            }
        });

    }


}
