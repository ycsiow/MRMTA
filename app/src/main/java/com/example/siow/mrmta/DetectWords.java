package com.example.siow.mrmta;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class DetectWords extends AppCompatActivity{
    EditText mEdit;
    ImageView imgView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_result);

        Intent intent = getIntent();
        Bitmap bitmap = ImagingTools.StringToBitmap(intent.getStringExtra(Main.bitmapFile));
        String detectedText = OCREngine.detectText(bitmap);
        mEdit = (EditText) findViewById(R.id.confirmET);
        imgView = (ImageView)findViewById(R.id.imageView2);
        if (detectedText.equals("L"))
        {
            detectedText = "M";
        }
        mEdit.setText(detectedText);
        imgView.setImageBitmap(bitmap);
    }

    public void Search(View V){
        String searchText2 = mEdit.getText().toString();
        Intent toResult = new Intent(this, Result.class);
        toResult.putExtra("TO_RESULT2",searchText2);
        startActivity(toResult);
    }
}
