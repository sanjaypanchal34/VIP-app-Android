package com.vip.marrakech.helpers;

import android.annotation.SuppressLint;

public class AsyncTask<T,DATA> {


    public AsyncTask(OnAsyncTaskListener<T,DATA> listener, DATA data) {
        new AsyncTAskClass(listener,data).execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTAskClass extends android.os.AsyncTask<Void, Void, T> {

        private  DATA data;
        private  OnAsyncTaskListener<T,DATA> listener;

        AsyncTAskClass(OnAsyncTaskListener<T,DATA> listener, DATA data) {
            this.listener = listener;
            this.data = data;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onPreTask();
        }

        @Override
        protected T doInBackground(Void... voids) {
            return listener.onBackground(data);
        }

        @Override
        protected void onPostExecute(T o) {
            super.onPostExecute(o);
            listener.onPostTask(o,data);
        }
    }

    public interface OnAsyncTaskListener<T,DATA> {
        T onBackground(DATA data);

        void onPreTask();

        void onPostTask(T object, DATA data);
    }
}