package com.example.om.basestructure.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.om.basestructure.R;
import com.example.om.basestructure.ui.fragment.base.BaseFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServerFragment extends BaseFragment {

    public static final String FRAGMENT_TAG = ServerFragment.class.getSimpleName();
    private ProgressBar progressBar;
    private Button btnServer;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ServerAsync serverAsync = new ServerAsync(progressBar);
            serverAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    };


    public ServerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_server, container, false);
        btnServer = (Button) view.findViewById(R.id.btn_server_fragment_server);
        btnServer.setOnClickListener(onClickListener);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar_fragment_server);
        return view;
    }

    private static class ServerAsync extends AsyncTask<Void, Integer, JSONArray> {

        private ProgressBar progressBar;

        public ServerAsync(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected JSONArray doInBackground(Void... voids) {
            try {
                URL url = new URL("https://jsonplaceholder.typicode.com/posts/");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setReadTimeout(15000);
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                inputStream.close();

                return new JSONArray(sb.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            progressBar.setVisibility(View.GONE);
        }
    }
}
