package chalmers.eda397g1.resources;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by elias on 2017-04-22.
 */

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    boolean round;

    public DownloadImageTask(ImageView bmImage, Boolean round) {
        this.bmImage = bmImage;
        this.round= round;
    }
    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
        this.round = false;
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
        if(round) result = Images.roundCornerImage(result,40);
        bmImage.setImageBitmap(result);
    }
}
