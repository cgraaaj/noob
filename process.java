package com.example.game.sample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class process extends AppCompatActivity {
    String SERVERURL = "http://192.168.43.138/CHSC/Mat_file/",server="http://192.168.1.11/CHSC/Output/";
    TextView t;
    EditText e;
    String text,etext,file;
    Button but,ge;
    Bitmap bitmap = null,bit;
    ImageView im;
    Toast toast;
    int a;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        t=(TextView)findViewById(R.id.tes);
        but=(Button)findViewById(R.id.button);
        ge=(Button)findViewById(R.id.gt);
        im=(ImageView)findViewById(R.id.img);
        file=getIntent().getStringExtra("name");

        SERVERURL=SERVERURL+file;//a=file.length()-4;
        //file=file.substring(0,a)+".txt";
        toast = Toast.makeText(getApplicationContext(), "" + SERVERURL, Toast.LENGTH_SHORT);
        toast.show();

        t.setText("This is the processed image");
        // new process();
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bit=download();
                im.setImageBitmap(bit);

            }
        });
        im.setImageBitmap(bitmap);
ge.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        get_text();
    }
});
    }
      public void get_text()
    {


        Intent gettext=new Intent(process.this,gettext.class);
        gettext.putExtra("qwe",file);
        startActivity(gettext);



    }

  public Bitmap download() {

        class ServerTask extends AsyncTask<String, Integer, Void> {
            @Override
            protected Void doInBackground(String... params) {

                String url = params[0];


    try {

        InputStream input = new java.net.URL(url).openStream();
        // Decode Bitmap
        bitmap = BitmapFactory.decodeStream(input);

    } catch (MalformedURLException ex) {
        ex.printStackTrace();
        Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
    } catch (Exception e) {
        e.printStackTrace();
        Log.e("Upload file to server Exception", "Exception : " + e.getMessage(), e);
    }
                return null;
            }
        }
        ServerTask serverTask=new ServerTask();
        serverTask.execute(SERVERURL);
        return bitmap;


    }


}
