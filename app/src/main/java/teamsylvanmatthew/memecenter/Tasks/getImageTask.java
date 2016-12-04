package teamsylvanmatthew.memecenter.Tasks;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;

public class getImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView imageView;

    public getImageTask(ImageView bmImage) {
        this.imageView = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urlDisplay = urls[0];

        Bitmap mIcon = null;
        try {
            InputStream in = new java.net.URL(urlDisplay).openStream();
            mIcon = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mIcon;
    }

    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
    }
}