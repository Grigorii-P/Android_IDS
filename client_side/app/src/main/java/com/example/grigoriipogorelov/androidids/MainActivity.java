package com.example.grigoriipogorelov.androidids;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String IP = "http://10.91.50.3:5000";
    List<String> weakPasswords = new ArrayList<>();
    List<String> usernames = new ArrayList<>();
    String usr = null;
    String psw = null;
    EditText et1 = null;
    EditText et2 = null;
    TextView textView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView2);

        try {
            InputStream is = getAssets().open("weaks.txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String[] array = (new String(buffer)).split("\n");
            weakPasswords = Arrays.asList(array);
        }
        catch (IOException e) {
            System.out.print(e);
        }

    }

    public float LevensteinDist(String S1, String S2) {
        int m = S1.length(), n = S2.length();
        int[] D1;
        int[] D2 = new int[n + 1];

        for(int i = 0; i <= n; i ++)
            D2[i] = i;

        for(int i = 1; i <= m; i ++) {
            D1 = D2;
            D2 = new int[n + 1];
            for(int j = 0; j <= n; j ++) {
                if(j == 0) D2[j] = i;
                else {
                    int cost = (S1.charAt(i - 1) != S2.charAt(j - 1)) ? 1 : 0;
                    if(D2[j - 1] < D1[j] && D2[j - 1] < D1[j - 1] + cost)
                        D2[j] = D2[j - 1] + 1;
                    else if(D1[j] < D1[j - 1] + cost)
                        D2[j] = D1[j] + 1;
                    else
                        D2[j] = D1[j - 1] + cost;
                }
            }
        }
        int len1 = S1.length();
        int len2 = S2.length();
        if (len1 > len2) return (float)D2[n]/len1;
        else return (float)D2[n]/len2;
    }

    public boolean isPswWeak(String s) {
        int len = s.length();
        List<String> similarPasw = new ArrayList<>();

        for (int i = 0; i < weakPasswords.size(); i++) {
            String temp = weakPasswords.get(i);
            if (temp.length() == len) similarPasw.add(temp);
        }

        if (!similarPasw.isEmpty()) {
            for (int i = 0; i < similarPasw.size(); i++) {
                String temp = similarPasw.get(i);
                if (temp.equals(s)) {
                    return true;
                }
                if (LevensteinDist(temp,s) < 0.5) {
                    return true;
                }
            }
        }
        return false;
    }

    public void signUp(View view) {
        et1 = (EditText)findViewById(R.id.editText2);
        usr = et1.getText().toString();
        et2 = (EditText)findViewById(R.id.editText);
        psw = et2.getText().toString();

        if (usernames.contains(usr)) textView.setText("The username already exists");
        else if (isPswWeak(psw) || psw.length() == 0) textView.setText("The password is too weak");
        else {
            usernames.add(usr);
            new POST().execute(IP + "/signup");
        }
    }


    public void logIn(View view) {
        et1 = (EditText)findViewById(R.id.editText2);
        usr = et1.getText().toString();
        et2 = (EditText)findViewById(R.id.editText);
        psw = et2.getText().toString();

        new POST().execute(IP + "/login");
    }


    public class POST extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) /*throws IOException MalformedURLException*/ {

            URL url;
            String response = null;
            HttpURLConnection urlConnection;
            InputStream inputStream;
            byte[] data;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);

                urlConnection.setRequestProperty("Content-Length", "" + Integer.toString((usr+"_"+psw).getBytes().length));
                OutputStream os = urlConnection.getOutputStream();
                data = (usr+"_"+psw).getBytes("UTF-8");
                os.write(data);
                os.close();

                urlConnection.connect();

                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                Scanner s = new Scanner(inputStream).useDelimiter("\\A");
                response = s.next();
                if (response.equals("good request")) textView.setText("Congrats!");
                else textView.setText("Bad request");
                inputStream.close();

            } catch (IOException e) {
            }
            return response;
        }
    }
}
