package net.eclissi.lucasop.ioboss.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by lucasoprana on 05/01/2017.
 */

public class SendPostRequest extends AsyncTask<String, Context, String> {

    protected void onPreExecute(){}

    protected String doInBackground(String... arg0) {
        Log.i("BlueRemote", "dobackground");
        String stato = arg0[0];
        String entity_id = arg0[1];
        String api_pass = arg0[2];

        Log.i("BlueRemote", "post -" + stato + " " + entity_id + " " + api_pass);
        try {

            URL url = new URL("https://eclissi.duckdns.org/api/services/input_select/select_option?api_password="+ api_pass); // here is your URL path

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("entity_id", entity_id);
//"input_select.luca_status"
            postDataParams.put("option", stato);

            Log.e("params",postDataParams.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestProperty("Accept","application/json");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            //writer.write(getPostDataString(postDataParams));
            writer.write(postDataParams.toString());

            writer.flush();
            writer.close();
            os.close();

            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in=new BufferedReader(new
                        InputStreamReader(
                        conn.getInputStream()));

                StringBuffer sb = new StringBuffer("");
                String line="";

                while((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }

                in.close();
                return sb.toString();

            }
            else {
                return new String("false : "+responseCode);
            }
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String result) {
        Log.i("BlueRemote", result);
        //     Toast.makeText(getApplicationContext(), result,
        //             Toast.LENGTH_LONG).show();
    }
}
