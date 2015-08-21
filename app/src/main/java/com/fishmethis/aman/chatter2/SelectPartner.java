package com.fishmethis.aman.chatter2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SelectPartner extends ActionBarActivity {
    private final String uriSel="";
    protected final String uripop="";
    SharedPreferences sp;
    final Pattern p = Pattern.compile("|%eid|(.+?)|%/eid|");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_partner);
        ListView l = (ListView)findViewById(R.id.listView);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(uripop,new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Error:" + statusCode, Toast.LENGTH_SHORT).show();
                Log.d("Failed:", statusCode + "");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Matcher m = p.matcher(responseString);
                m.find();
                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1);
                for (int i=1;i<=m.groupCount();i++){
                    adapter.add(m.group(i));
                }
                ListView lv = (ListView)findViewById(R.id.listView);
                lv.setAdapter(adapter);
            }
        });
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String sel;
                sel = parent.getItemAtPosition(position).toString();
             /*   RequestParams rp = new RequestParams();
                AsyncHttpClient client = new AsyncHttpClient();
                rp.put("selp",sel); */
                sp = getSharedPreferences("partner",MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("partner",sel);
                editor.apply();
                Intent i = new Intent(getApplicationContext(), Chatter.class);
                startActivity(i);
               /* client.post(uriSel, rp, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Toast.makeText(getApplicationContext(), "Error:" + statusCode, Toast.LENGTH_SHORT).show();
                        Log.d("Failed:", statusCode + "");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        if (responseString.contains("settttted")) {
                            Intent i = new Intent(getApplicationContext(), Chatter.class);
                            startActivity(i);
                            finish();
                        } else
                            Toast.makeText(getApplicationContext(), "Some Error Occured. Response:" + responseString, Toast.LENGTH_SHORT).show();
                    }
                }); */
            }
        });

    }

}
