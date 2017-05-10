package chalmers.eda397g1.resources;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by elias on 2017-04-22.
 */

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    Context context;

    public DownloadImageTask(ImageView bmImage, Context context) {
        this.bmImage = bmImage;
        this.context = context;
    }
    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
        this.context = null;
    }
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        if(result != null) {
            if (context != null) {
                Resources res = context.getResources();
                RoundedBitmapDrawable dr =
                        RoundedBitmapDrawableFactory.create(res, result);
                dr.setCornerRadius(Math.max(result.getWidth(), result.getHeight()) / 2.0f);
                bmImage.setImageDrawable(dr);
            } else {
                bmImage.setImageBitmap(result);
            }
        }
    }
}
