/*Copyright 2014 Bhavit Singh Sengar
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package com.vwuilbea.mymoviecatalog.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class RestClient {

    private static final String URL_TEST = "http://google.com";
    private static final String CHARSET_UTF8 = "UTF-8";

    private Map<String, String> params = new HashMap<String, String>();
    private String url;
    private String headerName;
    private String headerValue;

    private ExecutionListener executionListener;

    public RestClient(String url) {
        this(url, null);
    }

    public RestClient(String url, ExecutionListener executionListener) {
        this.url = url;
        this.executionListener = executionListener;
    }


    public void addHeader(String name, String value) {
        headerName = name;
        headerValue = value;
    }

    public void addParam(String key, String value) {
        params.put(key, value);
    }

    public void executeGet() {  // If you want to use post method to hit server
        try {
            URL url = new URL(URL_TEST);
            ExecuteGet executeGet = new ExecuteGet();
            executeGet.execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private class ExecuteGet extends AsyncTask<URL, Integer, String> {

        @Override

        protected void onPreExecute() {
            if (params.size() > 0) {
                boolean first = true;
                url += "?";
                for (String key : params.keySet()) {
                    try {
                        if (first) {
                            first = false;
                            url += key + "=" + URLEncoder.encode(params.get(key), CHARSET_UTF8);
                        } else {
                            url += "&" + key + "=" + URLEncoder.encode(params.get(key),CHARSET_UTF8);
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected String doInBackground(URL... urls) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet;
            httpGet = new HttpGet(url);
            httpGet.setHeader(headerName, headerValue);
            HttpResponse response;
            try {
                response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity);
                Log.v("ExecutePost", "URL:" + url + ", header: " + headerName + ":" + headerValue + ",\nresult:" + result);
                return result;
            } catch (IOException e) {
                if (executionListener != null) executionListener.onExecutionFailed(url, e);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            if (executionListener != null) executionListener.onExecutionProgress(url, progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (executionListener != null && result!=null) executionListener.onExecutionFinished(url, result);
        }
    }

    public interface ExecutionListener {

        public void onExecutionFinished(String url, String result);

        public void onExecutionProgress(String url, Integer progress);

        public void onExecutionFailed(String url, Exception e);
    }
}