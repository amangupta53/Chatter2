package com.fishmethis.aman.chatter2;

import android.content.Context;
import android.content.Intent;
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


public class oldusr extends ActionBarActivity {
    SharedPreferences sp;
    Button bt;
    EditText ed;
    private String uri2="http://192.168.1.6:8000/ChatterBox/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oldusr);
        sp = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String email = sp.getString("emailid", "null");
        bt = (Button)findViewById(R.id.button4);
        ed = (EditText)findViewById(R.id.editText3);
        final AsyncHttpClient client = new AsyncHttpClient();
        RequestParams rp = new RequestParams();
        rp.put("emailId",email);
        client.post(uri2+"usr4", rp, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Error:" + statusCode, Toast.LENGTH_SHORT).show();
                Log.d("Failed:", statusCode + "");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("Recieved String:", responseString);
                ed.setText(responseString);
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newusr = ed.getText().toString();
                RequestParams rp2 = new RequestParams();
                sp = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                String email = sp.getString("emailid", "null");
                rp2.put("usr",newusr);
                rp2.put("emailid",email);
                client.post(uri2+"usr3b", rp2, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Toast.makeText(getApplicationContext(), "Error:" + statusCode, Toast.LENGTH_SHORT).show();
                        Log.d("Failed:", statusCode + "");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        if (responseString.contains("set")){
                            Toast.makeText(getApplicationContext(),"User set as:"+newusr,Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error:" + statusCode, Toast.LENGTH_SHORT).show();
                            Log.d("Failed, string:",responseString);
                        }
                    }
                });
            }
        });
    }
}
