package com.example.siow.mrmta;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.ByteArrayOutputStream;

public class ImagingTools {
    public static String BitmapToString(Bitmap bitmap) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String temp = Base64.encodeToString(b, Base64.DEFAULT);
            return temp;
        } catch (NullPointerException e) {
            return null;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    public static Bitmap StringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (NullPointerException e) {
            e.getMessage();
            return null;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    public static Bitmap binarize(Bitmap bmp)
    {
        Mat img = Mat.zeros(bmp.getHeight(),bmp.getWidth(), CvType.CV_8UC1);
        Utils.bitmapToMat(bmp, img);
        Mat grayImg = Mat.zeros(img.size(), CvType.CV_8UC1);
        Mat binImg = Mat.zeros(img.size(), CvType.CV_8UC1);

        int GRAYLEVEL = 256;
        int[] histMat = new int[256];
        double[] prob = new double[256];
        double[] omega = new double[256]; /* prob of graylevels */
        double[] myu = new double[256];   /* mean value for separation */
        double max_sigma;
        double[] sigma = new double[256]; /* inter-class variance */
        int i, j, x, y; /* Loop variable */
        int thresholdMat; /* threshold for binarization */

        //grayscale
        for (i = 0; i < img.rows(); i++){
            for (j = 0; j < img.cols(); j++){
                int newValue = (int)Math.round((img.get(i,j)[0] + img.get(i,j)[1] + img.get(i,j)[2])/3);
                grayImg.put(i, j, newValue);
            }
        }

	    /* Histogram generation */
        for (int jj = 0; jj < 256; jj++) histMat[jj] = 0;
        for (i = 0; i < img.rows(); i++)
            for (j = 0; j < img.cols(); j++) {
                histMat[(int)img.get(i,j)[0]]++;
            }
	/* calculation of probability density */
        for (i = 0; i < GRAYLEVEL; i++) {
            prob[i] = (double)histMat[i] / (img.rows() * img.cols());
        }

	/* omega & myu generation */
        omega[0] = prob[0];
        myu[0] = 0.0;       /* 0.0 times prob[0] equals zero */
        for (i = 1; i < GRAYLEVEL; i++) {
            omega[i] = omega[i - 1] + prob[i];
            myu[i] = myu[i - 1] + i*prob[i];
        }

	/* sigma maximization
	sigma stands for inter-class variance
	and determines optimal threshold value */
        thresholdMat = 0;
        max_sigma = 0.0;
        for (i = 0; i < GRAYLEVEL - 1; i++) {
            if (omega[i] != 0.0 && omega[i] != 1.0)
                sigma[i] = Math.pow(myu[GRAYLEVEL - 1] * omega[i] - myu[i], 2) /
                        (omega[i] * (1.0 - omega[i]));
            else
                sigma[i] = 0.0;
            if (sigma[i] > max_sigma) {
                max_sigma = sigma[i];
                thresholdMat = i+50;
            }
        }

        //segmentation the cropped picture

        for (i = 1; i < img.rows() - 1; i++){
            for (j = 1; j < img.cols() - 1; j++){
                if (img.get(i,j)[0] > thresholdMat){
                    binImg.put(i, j, 255);
                }
                else{
                    binImg.put(i, j, 0);
                }
            }
        }
        Utils.matToBitmap(binImg, bmp);
        return bmp;
    }



    public Bitmap binarize2(Bitmap bmp)
    {
        Mat img = Mat.zeros(bmp.getHeight(),bmp.getWidth(), CvType.CV_8UC1);
        Utils.bitmapToMat(bmp, img);

        return null;
    }
}

