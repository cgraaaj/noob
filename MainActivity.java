package com.example.game.sample;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Matrix;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    int serverResponseCode = 0,a;
    String mCurrentPhotoPath,serverFileName,flag;
    static final int REQUEST_TAKE_PHOTO = 1;
    Button p, up;
    ImageView mImageView;
    TextView t;
    Toast toast;
    private final String SERVERURL = "http://192.168.43.138/CHSC/up.php";
    final int CROP_PIC_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        p = (Button) findViewById(R.id.pi);
        up = (Button) findViewById(R.id.upd);
        mImageView = (ImageView) findViewById(R.id.imageView);
        t=(TextView) findViewById(R.id.tex);
        flag = getIntent().getStringExtra("hdata");
        t.setText("Take a Snapshot of The Code");
        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
               // doCrop();

            }
        });
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(a==1) {

                    upf(mCurrentPhotoPath);
                    toast = Toast.makeText(getApplicationContext(), "Image Uploaded Sucessfully", Toast.LENGTH_SHORT);
                    toast.show();
                    t.setText("Click on the image to process");
                    a=0;
                }
                else {
                    toast = Toast.makeText(getApplicationContext(), "Get a Image First...!", Toast.LENGTH_SHORT);
                    toast.show();
                }


            }
        });
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentPhotoPath==null)
                {
                    toast = Toast.makeText(getApplicationContext(), "No image to be processed..Plz pic one", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if (a!=0)
                {
                    toast = Toast.makeText(getApplicationContext(), "Upload image first", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else
                {

                   Intent process=new Intent(MainActivity.this,process.class);
                     process.putExtra("name",serverFileName);
                    startActivity(process);


                }
            }
        });
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "CHSC_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

            }
        }
    }


    private void doCrop(String pathda) {
        try {

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            File file=new File(pathda);
            cropIntent.setDataAndType(Uri.fromFile(file), "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, CROP_PIC_REQUEST_CODE);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
       // super.onActivityResult(requestCode, resultCode, data);
        // Get the dimensions of the View

        if(requestCode==REQUEST_TAKE_PHOTO){ int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
        t.setText("Go on Upload to the SERVER");
        a = 1;
            Log.d("PATH",mCurrentPhotoPath+"");
           // doCrop(mCurrentPhotoPath);
    }
        else  if (requestCode == CROP_PIC_REQUEST_CODE) {
            if (mCurrentPhotoPath!=null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap)extras.get("data");
                mImageView.setImageBitmap(imageBitmap);
            }
        }

    }
    public void upf(String sourceFileUri) {



        class ServerTask extends AsyncTask<String, Integer, Void> {




            @Override
            protected Void doInBackground(String... params) {
                String fileName = params[0];

                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                serverFileName = "CHSC"+flag + (int) Math.round(Math.random() * 1000) + ".jpg";
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                File sourceFile = new File(fileName);
                try {

                    // open a URL connection to the Servlet
                    FileInputStream fileInputStream = new FileInputStream(sourceFile);
                    URL url = new URL(SERVERURL);

                    // Open a HTTP  connection to  the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("uploaded_file", fileName);



                    dos = new DataOutputStream(conn.getOutputStream());

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + serverFileName + "\"" + lineEnd);

                    dos.writeBytes(lineEnd);

                    // create a buffer of  maximum size
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    }

                    // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


                    // Responses from the server (code and message)
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
                    fileInputStream.close();
                    dos.flush();
                    dos.close();

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
        serverTask.execute(sourceFileUri);
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
