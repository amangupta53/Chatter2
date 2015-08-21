package com.fishmethis.aman.chatter2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.UUID;


public class SetUsrF extends ActionBarActivity {
    final String aid= UUID.randomUUID().toString();
    private SharedPreferences sp;
    private int flag=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_usr_f);
        Button button = (Button)findViewById(R.id.button3);
        EditText ed2 = (EditText)findViewById(R.id.editText2);
        sp = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("aid",aid);
        editor.apply();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Hold on..",Toast.LENGTH_SHORT);
                EditText ed1 = (EditText) findViewById(R.id.editText);
                EditText email = (EditText) findViewById(R.id.editText2);
                final String usr = ed1.getText().toString();
                final String emailtext = email.getText().toString();
                sp = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                RequestParams rp = new RequestParams();
                rp.put("usr", usr);
                rp.put("aid", aid);
                rp.put("emailid",emailtext);
                rp.put("key","fjnFF2rfaads32edfa#@");
                AsyncHttpClient client = new AsyncHttpClient();
                if (flag == 1) {
                    client.post("http://192.168.1.6:8000/ChatterBox/usr1", rp, new TextHttpResponseHandler() {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Toast.makeText(getApplicationContext(), "Error:" + statusCode, Toast.LENGTH_SHORT).show();
                            Log.d("Failed:", statusCode + "");
                        }
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            SharedPreferences.Editor editor = sp.edit();
                            Log.d("String coming in:", "'" + responseString + "'");
                            if (responseString.contains("set")) {
                                editor.putString("usr", usr);
                                editor.putString("emailid",emailtext);
                                editor.apply();
                                Toast.makeText(getApplicationContext(), "User set as:" + usr, Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "User Not Available", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("emailid",emailtext);
                    editor.apply();
                    Toast.makeText(getApplicationContext(), "Email ID already in use", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),oldusr.class);
                    startActivity(intent);

                }
            }
        });
        ed2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                RequestParams rp = new RequestParams();
                EditText ed3 = (EditText) findViewById(R.id.editText);
                AsyncHttpClient cl = new AsyncHttpClient();
                rp.put("usr", ed3.getText().toString());
                EditText ed4 = (EditText) findViewById(R.id.editText2);
                rp.put("emailid",ed4.getText().toString());
                rp.put("aid", aid);
                if (!(ed4.getText().toString().equals(""))) {
                    cl.post("http://192.168.1.6:8000/ChatterBox/usr2", rp, new TextHttpResponseHandler() {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Toast.makeText(getApplicationContext(), "Error:" + statusCode, Toast.LENGTH_SHORT).show();
                            Log.d("FailedWatch:", statusCode + "");
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            if (responseString.contains("set")) {
                                flag=1;
                                TextView tv = (TextView) findViewById(R.id.textView4);
                                tv.setText(s + " is Available");
                            } else if(responseString.contains("emailerror")) {
                                flag = 0;
                                TextView tv = (TextView) findViewById(R.id.textView4);
                                tv.setText("Email ID already in use");
                            }
                        }
                    });
                } else {
                    TextView tv = (TextView) findViewById(R.id.textView4);
                    Log.d("diag:", "inside else");
                    tv.setText("Please enter an EmailID");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


}
