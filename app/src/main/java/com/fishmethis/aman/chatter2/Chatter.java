package com.fishmethis.aman.chatter2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Chatter extends ActionBarActivity {
    SharedPreferences sp = null;
    final String uri = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatter);
        final String part = sp.getString("partner","null");
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams rp = new RequestParams();
        rp.put("partner",part);
        client.post(uri,rp, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Error:" + statusCode, Toast.LENGTH_SHORT).show();
                Log.d("Failed:", statusCode + "");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONArray jsonArray = new JSONArray(new JSONTokener(responseString));
                    List<Chatmsg> chatlist = new ArrayList<>();
                    Chatmsg ch = new Chatmsg();
                    String msg;
                    String date;
                    long id;
                    String recieverid;
                    String senderid;
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonobj = jsonArray.getJSONObject(i);
                        msg=jsonobj.getString("msg");
                        date=jsonobj.getString("datentime");
                        recieverid=jsonobj.getString("isme");
                        id=jsonobj.getInt("msgid");
                        senderid=jsonobj.getString("usrid");
                        ch.setDateTime(date);
                        ch.setId(id);
                        ch.setRecieverid(recieverid);
                        ch.setMsg(msg);
                        ch.setSenderid(senderid);
                        chatlist.add(ch);
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,chatlist);
                    ListView lv = (ListView)findViewById(R.id.listView);
                    lv.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Button sub = (Button)findViewById(R.id.button);
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText ed = (EditText)findViewById(R.id.editText);
                final String msgtosend = ed.getText().toString();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                RequestParams rp = new RequestParams();
                SharedPreferences sp = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("date",df.format(date));
                    jsonObject.put("id", sp.getString("msgid", "null"));
                    jsonObject.put("name",sp.getString("usr", ""));
                    jsonObject.put("recieverid",part );
                    jsonObject.put("msg",msgtosend);
                    jsonObject.put("senderid",sp.getString("email","null"));
                    rp.put("msg", jsonObject.toString());
                    Log.d("StringJSON: ",jsonObject.toString());
                } catch (JSONException e){
                    Log.getStackTraceString(e);
                }
                AsyncHttpClient client1 = new AsyncHttpClient();
                client1.post(uri,rp,new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Toast.makeText(getApplicationContext(), "Error:" + statusCode, Toast.LENGTH_SHORT).show();
                        Log.d("Failed:", statusCode + "");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        ListView lv = (ListView)findViewById(R.id.listView);
                        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1);

                    }
                });

            }
        });
    }

}
