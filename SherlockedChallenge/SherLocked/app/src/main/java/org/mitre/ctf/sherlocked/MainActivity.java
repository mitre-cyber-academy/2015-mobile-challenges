package org.mitre.ctf.sherlocked;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Random;


public class MainActivity extends Activity implements AsyncResponse {
    private int count = 0;
    private char[] passcode = new char[4];
    private static String masterPasscode = "SHER";
    private static String masterOrder = "0123";
    private String passcodeOrder = "";
    private static Type type = null;
    private static String ipPort = "";
    private static String url = "";

    private final String TAG = "SHERLOCKED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //TODO REQUIRED FOR GRADER DON'T CHANGE
        loadServerInfo();
        url = "http://"+ipPort;

        //Retrieve Master passcode/order
        Log.d(TAG, "retrieving passcode...");
        type = Type.PASSCODE;
        String loc_url = url+"/passcode";
        String requestType = "GET";
        (new CheckPasscode(this, null, null, loc_url, requestType)).execute();


        Intent intent = new Intent(this, Background.class);
        intent.putExtra("passcode", masterPasscode);
        intent.putExtra("order", masterOrder);
        this.startService(intent);


        Log.d(TAG, masterPasscode);
        Log.d(TAG, masterOrder);

        final EditText editTextOne = (EditText) findViewById(R.id.one);
        editTextOne.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        editTextOne.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        editTextOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) { 
                    editTextOne.setFocusable(false);
                    addToPasscode(s.charAt(0), 0);
                } else if (count == 0) {
                    editTextOne.setFocusableInTouchMode(true);
                    resetPasscode();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final EditText editTextTwo = (EditText) findViewById(R.id.two);
        editTextTwo.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        editTextTwo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        editTextTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) {
                    editTextTwo.setFocusable(false);
                    addToPasscode(s.charAt(0), 1);
                } else if (count == 0) {
                    editTextTwo.setFocusableInTouchMode(true);
                    resetPasscode();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final EditText editTextThree = (EditText) findViewById(R.id.three);
        editTextThree.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        editTextThree.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        editTextThree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) {
                    editTextThree.setFocusable(false);
                    addToPasscode(s.charAt(0), 2);
                } else if (count == 0) {
                    editTextThree.setFocusableInTouchMode(true);
                    resetPasscode();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final EditText editTextFour = (EditText) findViewById(R.id.four);
        editTextFour.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        editTextFour.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        editTextFour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) {
                    editTextFour.setFocusable(false);
                    addToPasscode(s.charAt(0), 3);
                } else if (count == 0) {
                    editTextFour.setFocusableInTouchMode(true);
                    resetPasscode();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void processFinish(String output) {
        //TODO get passcode
        Log.d(TAG, "Async Response: " + output);
        try {
            //JSONObject json = new JSONObject(output);
            if (type == Type.PASSCODE) {
                masterPasscode = output;
                SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("passcode", output);
                editor.apply();
                //get order
                type = Type.ORDER;
                String loc_url = url+"/order";
                String requestType = "GET";
                (new CheckPasscode(this, null, null, loc_url, requestType)).execute();
            } else if (type == Type.ORDER) {
                masterOrder = output;
                SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("passcode", output);
                editor.apply();
                type = null;
            } else if (type == Type.FLAG) {
                JSONObject json = new JSONObject(output);
                String flag = json.getString("flag");
                Log.d(TAG, "FLAG IS: " + flag);
                Toast.makeText(this, flag, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Log.e(TAG, "JSON EXCEPTION: " + e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void addToPasscode (char c, int place) {
        System.out.println(">Character added to passcode (char=" + c + ", position=" + place + ")");
        passcode[place] = c;
        passcodeOrder += place;
        count++;
        if (count == 4) {
            submitPasscode();
        }
    }

    private void resetPasscode () {
        count = 0;
        passcode = new char[4];
        passcodeOrder = "";
    }

    private void submitPasscode () {
        Log.d(TAG, "Passcode submitted");
        Log.d(TAG, "Expected " + masterPasscode + ", got " + new String(passcode));
        Log.d(TAG, "Expected " + masterOrder + ", got " + passcodeOrder);
        //Retrieve Master passcode/order
        type = Type.FLAG;
        String loc_url = url+"/challenge";
        String requestType = "POST";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i<passcode.length; i++) {
            sb.append(passcode[i]);
        }
        (new CheckPasscode(this, sb.toString(), passcodeOrder, loc_url, requestType)).execute();

        //Reset the fields
        resetPasscode();
        EditText editTextOne = (EditText) findViewById(R.id.one);
        editTextOne.setText("");
        EditText editTextTwo = (EditText) findViewById(R.id.two);
        editTextTwo.setText("");
        EditText editTextThree = (EditText) findViewById(R.id.three);
        editTextThree.setText("");
        EditText editTextFour = (EditText) findViewById(R.id.four);
        editTextFour.setText("");

    }

    public String jumble (String str) {
        char[] arr = str.toCharArray();
        for (int i=str.length()-1; i>=0; i--) {
            int rand = ((int) (Math.random() * i)) + 1;
            char a_j = arr[rand];
            char a_i = arr[i];
            arr[rand] = a_i;
            arr[i] = a_j;
        }
        return new String(arr);
    }

    private String getRandString(int strLen) {
        String validChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < strLen) {
            int index = (int) (rnd.nextFloat() * validChars.length());
            salt.append(validChars.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    private enum Type {
        PASSCODE, FLAG, ORDER
    }

    private String getCertificateSHA1Fingerprint() {
        PackageManager pm = this.getPackageManager();
        String packageName = this.getPackageName();
        int flags = PackageManager.GET_SIGNATURES;
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(packageName, flags);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Signature[] signatures = packageInfo.signatures;
        byte[] cert = signatures[0].toByteArray();
        InputStream input = new ByteArrayInputStream(cert);
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        X509Certificate c = null;
        try {
            c = (X509Certificate) cf.generateCertificate(input);
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        String hexString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(c.getEncoded());
            hexString = byte2HexFormatted(publicKey);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        return hexString;
    }

    public static String byte2HexFormatted(byte[] arr) {
        StringBuilder str = new StringBuilder(arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();
            if (l == 1) h = "0" + h;
            if (l > 2) h = h.substring(l - 2, l);
            str.append(h.toUpperCase());
            if (i < (arr.length - 1)) str.append(':');
        }
        return str.toString();
    }

    //TODO USED BY GRADER AND TO CONNECT TO SERVER, DO NOT ALTER FUNCTION
    public void loadServerInfo() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("server.txt")));

            // do reading - should only need first line
            ipPort  = reader.readLine();
        } catch (IOException e) {
            //log the exception
            Log.e(TAG, "Error Loading server info...");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                    Log.e(TAG, "Error closing reader...");
                }
            }
        }
    }

    private static String BROADCAST_ACTION = "org.mitre.ctf.sherlocked.INTENT";
    @Override
    protected void onResume() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BROADCAST_ACTION);
        registerReceiver(receiver, filter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Toast.makeText(getApplicationContext(), "received", Toast.LENGTH_SHORT);
            Log.d(TAG, "SHERLOCK INTENT RECEIVED");
            type = Type.FLAG;
            String loc_url = url+"/challenge";
            String requestType = "POST";
            String loc_passcode = intent.getStringExtra("passcode");
            String loc_order = intent.getStringExtra("order");
            Log.d(TAG, "LOCAL PASSCODE AND ORDER" + loc_passcode + " " + loc_order);
            (new CheckPasscode(MainActivity.this, loc_passcode, loc_order, loc_url, requestType)).execute();
        }
    };
}



