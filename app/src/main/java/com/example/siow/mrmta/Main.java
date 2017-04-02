package com.example.siow.mrmta;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main extends AppCompatActivity {

    private static final String TAG = "Malay Restaurant Menu Translator";
    String timeStamp;
    String imageFileName;
    public final static String bitmapFile = "com.example.siow.MRMTA.bitmap";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int SELECT_PHOTO = 2;
    private static final int MY_PERMISSIONS_REQUEST_READ = 4;
    private static final int PIC_CROP = 3;
    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/MRMTA/";
    public static final String lang = "eng";
    EditText searchET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchET = (EditText)findViewById(R.id.searchET);

        //workaround for "could not load library libjpgt.so for liblept.so on some API
        System.loadLibrary("jpgt");
        System.loadLibrary("pngt");
        System.loadLibrary("lept");
        System.loadLibrary("tess");

        //initialize opencv libraries
        if (!OpenCVLoader.initDebug()){
            Log.v(TAG, "ERROR: OpenCV could not initialize");
        }
        else{
            Log.v(TAG, "OpenCV initialized successfully");
        }

        //check if tessdata/traineddata exists on the device
        //copy the file from asset if not found
        //traineddata is compulsory for tess-two to work
        String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };

        for (String path : paths)
        {
            File dir = new File(path);
            if (!dir.exists())
            {
                if (!dir.mkdirs())
                {
                    Log.v(TAG, "ERROR: Creation of directory " + path + " on sdcard failed");
                    return;
                } else {
                    Log.v(TAG, "Created directory " + path + " on sdcard");
                }
            }
        }
        if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists())
        {
            try {
                AssetManager assetManager = getAssets();
                InputStream in = assetManager.open(lang + ".traineddata");
                OutputStream out = new FileOutputStream(DATA_PATH + "tessdata/" + lang + ".traineddata");
                byte[] buffer = new byte[1024];
                int read;
                while((read = in.read(buffer)) != -1){
                    out.write(buffer, 0, read);
                }
                in.close();
                out.close();
                Log.v(TAG, "Copied " + lang + " traineddata");
            } catch (IOException e) {
                Log.e(TAG, "Was unable to copy " + lang + " traineddata " + e.toString());
            }
        }
    }

    public void takePicture(View v)
    {
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_" + timeStamp + "_";
        dispatchTakePictureIntent();
    }

    public void Search(View V){
        String searhText = searchET.getText().toString();
        Intent toResult = new Intent(this, Result.class);
        toResult.putExtra("TO_RESULT",searhText);
        startActivity(toResult);
    }

   /* public void pickPhoto(View f)
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);

        }
        Intent photoPickIntent = new Intent(Intent.ACTION_PICK);
        photoPickIntent.setType("image/*");
        startActivityForResult(photoPickIntent, SELECT_PHOTO);
    }*/

    private void dispatchTakePictureIntent() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    0);
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = new File(Environment.getExternalStorageDirectory()+File.separator + imageFileName + ".jpg");
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File file = new File(Environment.getExternalStorageDirectory()+File.separator + imageFileName + ".jpg");

            //make the photo viewable at Gallery app
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(file);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);

//                Intent intent = new Intent(this, AreaOfInterest.class);
//
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
//                intent.putExtra(bitmapFile, ImagingTools.BitmapToString(bitmap));

            performCrop(contentUri);
        }
        else if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
            try {
                Uri selectedImage = data.getData();
                performCrop(selectedImage);

                //Bitmap img = decodeUri(selectedImage);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == PIC_CROP) {
            if (data != null) {
                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                Bitmap img = extras.getParcelable("data");
                Intent toResultIntent = new Intent(this, DetectWords.class);

                toResultIntent.putExtra(bitmapFile, ImagingTools.BitmapToString(img));
                startActivity(toResultIntent);
            }
        }

    }

    private void performCrop(Uri picUri) {
        try {

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", "true");
            //cropIntent.putExtra("aspectX", 1);
            //cropIntent.putExtra("aspectY", 1);
            //cropIntent.putExtra("outputX", 400);
            //cropIntent.putExtra("outputY", 400);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch (ActivityNotFoundException anfe) {
            String errorMessage = "The device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}


