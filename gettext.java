package com.example.game.sample;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class gettext extends AppCompatActivity {
    String SERVERURL = "http://192.168.43.138/CHSC/", name, SERVER,etext,serverFileName,ftext,summah,ffname,compile,run,p;
    Button up,sh;
    EditText e;
    TextView t;
    Toast toast;
    int a, serverResponseCode = 0,comp=0,doq=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gettext);
        up = (Button) findViewById(R.id.up0);
        sh = (Button) findViewById(R.id.bui);
        e = (EditText) findViewById(R.id.editT);
        t = (TextView) findViewById(R.id.te);
        name = getIntent().getStringExtra("qwe");
        a=name.length()-4;
        p=name.substring(0, 5);
        compile=SERVERURL+"Output/C/error/err.txt";
        run=SERVERURL+"Output/C/finaloutput/output.txt";
        ffname=name.substring(0, a);
        ffname=ffname+".c";
        name=name.substring(0, a)+".txt";

        if(p.contains("CHSCH")) {
            SERVER = SERVERURL + "Output/" + "CHSC3.txt";
        }
        else
        {
            SERVER = SERVERURL + "Output/" + name;
        }
        //Toast.makeText(getApplicationContext(),p,Toast.LENGTH_SHORT).show();
        toast = Toast.makeText(getApplicationContext(), "" + SERVER, Toast.LENGTH_SHORT);
        toast.show();
        sh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doq();
                t.setText("The Extracted text");
                e.setText(etext);


            }

        });
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SERVER=SERVERURL+"down.php";
                toast = Toast.makeText(getApplicationContext(), "" + SERVER, Toast.LENGTH_SHORT);
                toast.show();
                ftext=e.getText().toString();
                doup();
                t.setText("Tap me to compile,double tap to run");

            }
        });
        t.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if(comp==0) {
                    toast = Toast.makeText(getApplicationContext(), "single", Toast.LENGTH_SHORT);
                    toast.show();
                    e.setText(null);
                    compile();
                    comp++;
                    t.setText("Not compiled tap again..!");
                }
                if(comp==1) {

                    t.setText("Compiled,double tap to compile n run");
                    if (etext == null)
                        e.setText("There is no Compilation error");
                    else
                        e.setText(etext);
                }
            }

            @Override
            public void onDoubleClick(View v) {
                toast = Toast.makeText(getApplicationContext(), "double", Toast.LENGTH_SHORT);
                toast.show();
                e.setText(null);
                run();

                t.setText("Output");
                e.setText(etext);

            }
        });

    }
    public abstract class DoubleClickListener implements View.OnClickListener {

        private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds

        long lastClickTime = 0;

        @Override
        public void onClick(View v) {
            long clickTime = System.currentTimeMillis();
            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
                onDoubleClick(v);
            } else {
                onSingleClick(v);
            }
            lastClickTime = clickTime;
        }

        public abstract void onSingleClick(View v);
        public abstract void onDoubleClick(View v);
    }



    public void doq()

    {
        class ServerTask extends AsyncTask<String, Integer, Void> {
            @Override
            protected Void doInBackground(String... params) {

                String asd = params[0];
                HttpURLConnection conn = null;
                try {


                    URL url = new URL(asd);

                    // Open a HTTP  connection to  the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    etext = sb.toString();

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

        ServerTask serverTask = new ServerTask();
        serverTask.execute(SERVER);
    }

    public void doup()
    {
        class ServerTask extends AsyncTask<String, Integer, Void> {




            @Override
            protected Void doInBackground(String... params) {
                String data = params[0];

                HttpURLConnection conn = null;
                try {

                    // open a URL connection to the Servlet
                    URL url = new URL(SERVER);

                    // Open a HTTP  connection to  the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    Uri.Builder builder = new Uri.Builder();
                    builder.appendQueryParameter("sendMessage", data);
                    builder.appendQueryParameter("fname", ffname);
                    String query = builder.build().getEncodedQuery();
                    conn.setFixedLengthStreamingMode(query.getBytes().length);
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    os.close();


                    conn.connect();
                    serverResponseCode = conn.getResponseCode();
                    String serverResponseMessage = conn.getResponseMessage();

                    Log.d("uploadFile", "HTTP Response is : "
                            + serverResponseMessage + ": " + serverResponseCode);
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    Log.d("RESPONSE", sb.toString());
                    //close the streams //


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
        serverTask.execute(ftext);

    }
    public void compile()
    {
        class ServerTask extends AsyncTask<String, Integer, Void> {
            @Override
            protected Void doInBackground(String... params) {

                String asd = params[0];
                HttpURLConnection conn = null;
                try {


                    URL url = new URL(asd);

                    // Open a HTTP  connection to  the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    etext = sb.toString();

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
        ServerTask serverTask = new ServerTask();
        serverTask.execute(compile);
    }

    public void run()
    {
        class ServerTask extends AsyncTask<String, Integer, Void> {
            @Override
            protected Void doInBackground(String... params) {

                String asd = params[0];
                HttpURLConnection conn = null;
                try {


                    URL url = new URL(asd);

                    // Open a HTTP  connection to  the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    etext = sb.toString();

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
        ServerTask serverTask = new ServerTask();
        serverTask.execute(run);
    }
}