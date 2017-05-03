package net.eclissi.lucasop.ioboss.utils;

import com.android.volley.VolleyError;

import org.json.JSONArray;

/**
 * Created by rohitp on 2/25/2016.
 */
public interface IResult {
    public void notifySuccess(String requestType, JSONArray response);
    public void notifyError(String requestType, VolleyError error);
}
