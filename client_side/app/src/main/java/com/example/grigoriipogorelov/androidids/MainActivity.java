package com.example.grigoriipogorelov.androidids;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    String response;

    public class RequestFromFlask extends AsyncTask<String, Void, StringBuilder> {
        protected StringBuilder doInBackground(String... urls) /*throws IOException MalformedURLException*/ {

//            URL url = new URL(urls[0]);
            URL url = null;
//            String response = null;
            StringBuilder response = null;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
//            String result = IOUtils.toString(inputStream);
                InputStreamReader isw = new InputStreamReader(inputStream);
                int data = isw.read();
                response = new StringBuilder("");
                while (data != -1) {
                    char current = (char) data;
                    data = isw.read();
                    response.append(current);
                }
//                Scanner s = new Scanner(inputStream).useDelimiter("\\A");
//                response = s.hasNext() ? s.next() : "";
            } catch (IOException e) {
                Log.e(TAG, "Malformed (actually, IOException) URLException caught : " + e);
            }
            finally {
                urlConnection.disconnect();
            }
            return response;
        }

        protected void onPostExecute(StringBuilder result) {
            EditText et = (EditText)findViewById(R.id.editText);
            et.setGravity(Gravity.CENTER);
            et.setText(result.toString());
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendRequest(View view) {
        new RequestFromFlask().execute("http://192.168.0.105:5000/todo/api/v1.0/tasks");
    }
}
