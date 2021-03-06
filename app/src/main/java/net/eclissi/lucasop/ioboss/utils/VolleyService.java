package net.eclissi.lucasop.ioboss.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by rohitp on 2/25/2016.
 */
public class VolleyService {

    IResult mResultCallback = null;
    Context mContext;

    public VolleyService(IResult resultCallback, Context context){
        mResultCallback = resultCallback;
        mContext = context;
    }



    public void postDataVolley(final String requestType, String url, JSONObject sendObj, final long dbID){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            CustomRequest jsonObj = new CustomRequest(Request.Method.POST, url, sendObj, new Response.Listener< JSONArray>(){


            //JsonObjectRequest jsonObj = new JsonObjectRequest(url,sendObj, new Response.Listener<JSONObject>() {
                @Override
                //public void onResponse(JSONObject response) {
                public void onResponse(JSONArray response) {
                    if(mResultCallback != null)
                        mResultCallback.notifySuccess(requestType,response,dbID);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(mResultCallback != null)
                        mResultCallback.notifyError(requestType,error,dbID);
                }
            });

            queue.add(jsonObj);

        }catch(Exception e){

        }
    }

    public void getDataVolley(final String requestType, String url, final long idDB){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);


          //  JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            CustomRequest jsonObj = new CustomRequest(Request.Method.GET, url, new Response.Listener< JSONArray>(){
                @Override
                public void onResponse(JSONArray response) {
                    if(mResultCallback != null)
                        mResultCallback.notifySuccess(requestType, response, idDB);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(mResultCallback != null)
                        mResultCallback.notifyError(requestType, error, idDB);
                }
            });

            //jsonObj.setRetryPolicy(new DefaultRetryPolicy(5000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsonObj);


        }catch(Exception e){

        }
    }
}
