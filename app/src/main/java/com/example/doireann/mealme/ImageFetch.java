package com.example.doireann.mealme;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Doireann on 2018-04-04.
 */

public class ImageFetch extends AsyncTask<String, Void, Bitmap> {
    private Listener listener;


    public ImageFetch(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onImageLoaded(Bitmap bitmap);
    }



    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            listener.onImageLoaded(bitmap);
        } else {

        }
    }

    @Override
    protected Bitmap doInBackground(String... args) {

        try {

            return BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
