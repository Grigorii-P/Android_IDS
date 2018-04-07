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

    List<String> weakPasswords = new ArrayList<>();
    List<String> usernames = new ArrayList<>();
    List<String> passwords = new ArrayList<>();
    TextView textView = (TextView) findViewById(R.id.textView2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            InputStream is = getAssets().open("weaks.txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String[] array = (new String(buffer)).split("\n");
            List<String> weakPasswords = Arrays.asList(array);
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

        for (int i = 0; i < passwords.size(); i++) {
            String temp = passwords.get(i);
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
        EditText et1 = (EditText)findViewById(R.id.editText2);
        String usr = et1.getText().toString();
        EditText et2 = (EditText)findViewById(R.id.editText);
        String psw = et2.getText().toString();



        if (usernames.contains(usr)) textView.setText("The username already exists");
        else if (isPswWeak(psw)) textView.setText("The password is too weak");
        else {
            textView.setText("Congrats!");
        }
    }


    public void logIn(View view) {
//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);
    }
}
