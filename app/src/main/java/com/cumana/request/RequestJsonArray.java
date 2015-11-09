package com.cumana.request;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by Javier on 03-11-2015.
 */
public class RequestJsonArray extends JsonRequest<JSONArray> {

    public RequestJsonArray(int method, String url, JSONArray jsonRequest,Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener, errorListener);
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            // solution 1:
            //String jsonString = new String(response.data, "UTF-8");
            // solution 2:
            //response.headers.put("Content-Type","application/json");

            String utf8String = new String(response.data, "UTF-8");
            return Response.success(new JSONArray(utf8String), HttpHeaderParser.parseCacheHeaders(response));

            /*return Response.success(new JSONArray(new String(response.data, HttpHeaderParser.parseCharset(response.headers))),
                    HttpHeaderParser.parseCacheHeaders(response));*/
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

}