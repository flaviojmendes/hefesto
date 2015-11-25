package com.fjmob.ponto.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.fjmob.ponto.R;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

/**
 * Created by flavio on 23/11/15.
 */
public class LoadingTask  extends AsyncTask<String, Integer, Boolean> {

    private View view;

    public LoadingTask(View view) {
        this.view = view;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        ProgressBarCircularIndeterminate prog = (ProgressBarCircularIndeterminate) getView().findViewById(R.id.loadingIcon);
        prog.setVisibility(View.VISIBLE);
    }

    @Override
    protected Boolean doInBackground(String... params) {
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        ProgressBarCircularIndeterminate prog = (ProgressBarCircularIndeterminate) getView().findViewById(R.id.loadingIcon);
        prog.setVisibility(View.INVISIBLE);
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
