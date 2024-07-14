package com.techtravelcoder.educationalbooks.pdf;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import androidx.core.content.ContextCompat;

import com.techtravelcoder.educationalbooks.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileDownloader {

    public static void downloadFile(Context context, String fileUrl, String fileName, DownloadListener listener) {
        new DownloadFileTask(context, fileName, listener).execute(fileUrl);
    }

    public interface DownloadListener {
        void onDownloadComplete(File file);
        void onDownloadFailed(Exception e);
    }

    private static class DownloadFileTask extends AsyncTask<String, Integer, File> {
        private Context context;
        private String fileName;
        private DownloadListener listener;
        private Exception exception;
        private ProgressDialog progressDialog;

        public DownloadFileTask(Context context, String fileName, DownloadListener listener) {
            this.context = context;
            this.fileName = fileName;
            this.listener = listener;
            this.progressDialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Processing Books !!");
            progressDialog.setMessage("Processing Books takes some times.");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
            progressDialog.setCancelable(false);
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.alert_back);
            if (progressDialog.getWindow() != null) {
                progressDialog.getWindow().setBackgroundDrawable(drawable);
            }


        }

        @Override
        protected File doInBackground(String... urls) {
            String fileUrl = urls[0];
            InputStream input = null;
            FileOutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(fileUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    throw new Exception("Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage());
                }

                int fileLength = connection.getContentLength();

                input = new BufferedInputStream(connection.getInputStream());
                File directory = new File(context.getFilesDir(), "Pintu");
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                File file = new File(directory, fileName);
                output = new FileOutputStream(file);

                byte[] data = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    if (fileLength > 0) {
                        publishProgress((int) (total * 100 / fileLength));
                    }
                    output.write(data, 0, count);
                }

                return file;
            } catch (Exception e) {
                this.exception = e;
                return null;
            } finally {
                try {
                    if (input != null) {
                        input.close();
                    }
                    if (output != null) {
                        output.close();
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                } catch (Exception ignored) {}
            }
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            if (progressDialog.isIndeterminate()) {
                progressDialog.setIndeterminate(false);
            }
            progressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(File file) {
            progressDialog.dismiss();
            if (file != null) {
                listener.onDownloadComplete(file);

            } else {

                listener.onDownloadFailed(exception);
            }
        }
    }
}
