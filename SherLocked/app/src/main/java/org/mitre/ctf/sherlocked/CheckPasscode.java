package org.mitre.ctf.sherlocked;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CheckPasscode extends AsyncTask {
    public AsyncResponse delegate;
    public String passcode = null;
    public String order = null;
    public String url = null;
    public String requestType = null;

    public CheckPasscode(AsyncResponse response, String p, String o, String url, String requestType) {
        this.delegate = response;
        this.passcode = p;
        this.order = o;
        this.url = url;
        this.requestType = requestType;
    }

    @Override
    protected String doInBackground(Object... params) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(this.url);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestType);

            //This should only happen during a POST
            if ((passcode != null && !passcode.isEmpty())) {
                Log.d("SHERLOCKED", "setting POST params...");
                String formData = "passcode=" + passcode + "&order=" + order;
                Log.d("SHERLOCKED", "formdata: " + formData);
                //formData = formData.substring(0, formData.length() - 1);
                //formData = URLEncoder.encode(formData, "UTF-8");
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(formData);
                wr.flush();
                wr.close();
            }

            InputStream content = connection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(content));
            String line;
            sb = new StringBuilder();
            while ((line = in.readLine()) != null)
                sb.append(line);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    protected void onPostExecute(Object result) {
        delegate.processFinish((String)result);
    }
}
