package co.viniciuspinheiro.netflixremake.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.widget.ImageView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import co.viniciuspinheiro.netflixremake.model.Category;

public class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewWeakReference;

    public ImageDownloaderTask(ImageView imageView) {
        this.imageViewWeakReference = new WeakReference<>(imageView);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String urlImg = params[0];
        HttpsURLConnection urlConnection = null;

        try {
            URL urlRequest = new URL(urlImg);

            urlConnection = (HttpsURLConnection) urlRequest.openConnection();

            int responseCode = urlConnection.getResponseCode();
            if (responseCode != 200) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();

            if (inputStream != null){
                return BitmapFactory.decodeStream(inputStream);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled())
            bitmap = null;
        ImageView imageView = imageViewWeakReference.get();
        if (imageView != null && bitmap != null){
            if (bitmap.getWidth() < imageView.getWidth() ||
                    bitmap.getHeight() < imageView.getHeight()){
                Matrix matrix = new Matrix();
                matrix.postScale((float) imageView.getWidth() / (float) bitmap.getWidth(),
            (float)imageView.getHeight() / (float) bitmap.getHeight());
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
            }

            imageView.setImageBitmap(bitmap);
        }
    }
}
