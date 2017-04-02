package com.example.siow.mrmta;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SelectedPart extends AppCompatActivity{
    FocusBoxView focusBox;
    Bitmap bitmap;
    ImageView imgView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageprocess);
        imgView = (ImageView)findViewById(R.id.imageView);
        focusBox = (FocusBoxView)findViewById(R.id.focus_box);
        Intent intent = getIntent();
        bitmap = ImagingTools.StringToBitmap(intent.getStringExtra(Main.bitmapFile));

        imgView.setImageBitmap(bitmap);
    }

    public void toDetectWords(View v) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        stream.close();

        Intent detect = new Intent(this, DetectWords.class);
        detect.putExtra(Main.bitmapFile, ImagingTools.BitmapToString(bitmap));
        startActivity(detect);
    }
}
