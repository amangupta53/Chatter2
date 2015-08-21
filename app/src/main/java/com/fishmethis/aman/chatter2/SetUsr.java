package com.fishmethis.aman.chatter2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;


public class SetUsr extends ActionBarActivity {
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_usr);
        Button button = (Button)findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText ed = (EditText) findViewById(R.id.editText);
                final String usr = ed.getText().toString();
                sp = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                RequestParams rp = new RequestParams();
                rp.put("usr", usr);
                rp.put("emailid",sp.getString("emailid","null"));
                rp.put("key","fjnFF2rfaads32edfa#@");
                AsyncHttpClient client = new AsyncHttpClient();
                client.post("http://192.168.1.6:8000/ChatterBox/usr3b", rp, new TextHttpResponseHandler() {
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
                                editor.apply();
                                Toast.makeText(getApplicationContext(), "User set as:" + usr, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "User Not Available", Toast.LENGTH_SHORT).show();
                            }
                        }
                });
            }

        });

    }


}
