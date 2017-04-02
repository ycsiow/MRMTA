package com.example.siow.mrmta;

import android.graphics.Bitmap;

import com.googlecode.tesseract.android.TessBaseAPI;

public class OCREngine {

    public static String detectText(Bitmap bitmap) {

        TessBaseAPI tessBaseAPI = new TessBaseAPI();
        String path = Main.DATA_PATH;

        tessBaseAPI.setDebug(true);
        tessBaseAPI.init(path, Main.lang);

        //tessBaseAPI.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "1234567890");
        //tessBaseAPI.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "!@#$%^&*()_+=-qwertyuiop[]}{POIU" +
        //        "YTREWQasdASDfghFGHjklJKLl;L:'\"\\|~`xcvXCVbnmBNM,./<>?");

        tessBaseAPI.setImage(bitmap);
        String text = tessBaseAPI.getUTF8Text();
        tessBaseAPI.end();

        return text;
    }
}
