package com.faramarz.tictacdev.parser;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = MainActivity.class.getSimpleName();
    private TextView txtResponse;
    private Button btnParse;
    private ProgressDialog pDialog;
    private ListView listView;
    private ListAdapter adapter;
    public static String url = "https://api.androidhive.info/contacts/";
    private ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind();
        btnParse.setOnClickListener(this);
        contactList = new ArrayList<>();
        parse();
    }

    private void bind() {
        txtResponse = findViewById(R.id.txtResponse);
        btnParse = findViewById(R.id.btnParse);
        listView = findViewById(R.id.list);
    }

    private HashMap<String, Object> getHashMapFromJson(String json) throws JSONException {
        HashMap<String, Object> map = new HashMap<>();
        JSONObject jsonObject = new JSONObject(json);
        for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
            String key = it.next();
            map.put(key, jsonObject.get(key));
        }
        return map;
    }

    private void parse() {
        String json = " {\"id\" : 12345, \"value\" : \"123\", \"person\" : \"1\"}";
        try {
            HashMap<String, Object> map = getHashMapFromJson(json);
            for (String key : map.keySet()) {
                txtResponse.append(key + "  " + map.get(key) + "\n");
            }
        } catch (JSONException e) {
            Toast.makeText(this, "Failed parsing " + json, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        new GetContacts().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("contacts");
                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String id = c.getString("id");
                        String name = c.getString("name");
                        String email = c.getString("email");
                        String address = c.getString("address");
                        String gender = c.getString("gender");

                        // Phone node is JSON Object
                        JSONObject phone = c.getJSONObject("phone");
                        String mobile = phone.getString("mobile");
                        String home = phone.getString("home");
                        String office = phone.getString("office");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("email", email);
                        contact.put("mobile", mobile);

                        // adding contact to contact list
                        contactList.add(contact);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            adapter = new SimpleAdapter(MainActivity.this, contactList, R.layout.list_item, new String[]{"name", "email", "mobile"},
                    new int[]{R.id.name, R.id.email, R.id.mobile});
            listView.setAdapter(adapter);
        }

    }

}
