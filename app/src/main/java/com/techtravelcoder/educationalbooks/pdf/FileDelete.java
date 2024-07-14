package com.techtravelcoder.educationalbooks.pdf;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;

public class FileDelete {

    public static void deleteAllDownloadedFiles(Context context, DeletionListener listener) {
        new DeleteFilesTask(context, listener).execute();
    }

    public interface DeletionListener {
        void onDeletionComplete();
        void onDeletionFailed(Exception e);
    }

    private static class DeleteFilesTask extends AsyncTask<Void, Void, Boolean> {
        private Context context;
        private DeletionListener listener;
        private Exception exception;

        public DeleteFilesTask(Context context, DeletionListener listener) {
            this.context = context;
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            File directory = new File(context.getFilesDir(), "Pintu");
            if (directory.exists() && directory.isDirectory()) {
                File[] files = directory.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (!file.delete()) {
                            exception = new Exception("Failed to delete file: " + file.getName());
                            return false;
                        }
                    }
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                listener.onDeletionComplete();
            } else {
                listener.onDeletionFailed(exception);
            }
        }
    }
}
