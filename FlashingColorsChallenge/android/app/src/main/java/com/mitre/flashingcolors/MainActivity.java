package com.mitre.flashingcolors;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.lang.Math;


public class MainActivity extends ActionBarActivity {

    private MainActivity main_activity = null;
    private static final String TAG = "COLORCLICKAPP";
    Button color_check, change_bkg, change_bkg_submit;
    TextView topcolor_text, bottomcolor_text;
    EditText enter_colors;
    Boolean BottomGreen = false;
    Boolean FirstRun = true;
    int topcolor_num, bottomcolor_num, color_count = 0;
    String color_code, filecolor, colorserver_response = "";
    String team_color_code = "roygongerd";

    final Handler handler = new Handler();
    final Runnable topcolors = new Runnable()
    {
        public void run()
        {
            Random rand = new Random();
            topcolor_num = rand.nextInt(7)+1;
            switch (topcolor_num) {
                case 1:   topcolor_text.setTextColor(Color.parseColor("#FF6600"));
                    Log.i("CtFEasy", "NOT GREEN");
                    break;
                case 2: topcolor_text.setTextColor(Color.parseColor("#CCFF33"));
                    Log.i("CtFEasy", "NOT GREEN");
                    break;
                case 3: topcolor_text.setTextColor(Color.parseColor("#FFFF66"));
                    Log.i("CtFEasy", "NOT GREEN");
                    break;
                case 4: topcolor_text.setTextColor(Color.parseColor("#FFCC00"));
                    Log.i("CtFEasy", "NOT GREEN");
                    break;
                case 5: if(BottomGreen){
                    Log.e("CtFEasy", "BOTH BOTH BOTH BOTH GREEN");
                    }
                    color_check.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(), "nice one", Toast.LENGTH_SHORT).show();
                        }

                    });
                    topcolor_text.setTextColor(Color.parseColor("#00FF00"));
                    Log.i("CtFEasy", "GREEN");
                    break;
                case 6: topcolor_text.setTextColor(Color.parseColor("#FF9900"));
                    Log.i("CtFEasy", "NOT GREEN");
                    break;
                case 7: topcolor_text.setTextColor(Color.parseColor("#FF0000"));
                    Log.i("CtFEasy", "NOT GREEN");
                    break;
            }
            handler.postDelayed(this, 500);
        }
    };
    final Runnable bottomcolors = new Runnable()
    {
        public void run()
        {
            Random rand = new Random();
            bottomcolor_num = rand.nextInt(7)+1;
            color_check.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "nah", Toast.LENGTH_SHORT).show();
                }
            });
            switch (bottomcolor_num) {
                case 1: bottomcolor_text.setTextColor(Color.parseColor("#FF6600"));
                    BottomGreen=false;
                    color_code+="ro";
                    break;
                case 2: bottomcolor_text.setTextColor(Color.parseColor("#CCFF33"));
                    BottomGreen=false;
                    color_code+="yg";
                    break;
                case 3: bottomcolor_text.setTextColor(Color.parseColor("#FFFF66"));
                    BottomGreen=false;
                    color_code+="yl";
                    break;
                case 4: bottomcolor_text.setTextColor(Color.parseColor("#FFCC00"));
                    BottomGreen=false;
                    color_code+="oy";
                    break;
                case 5: bottomcolor_text.setTextColor(Color.parseColor("#00FF00"));
                    BottomGreen=true;
                    color_code+="ge";
                    break;
                case 6: bottomcolor_text.setTextColor(Color.parseColor("#FF9900"));
                    BottomGreen=false;
                    color_code+="on";
                    break;
                case 7: bottomcolor_text.setTextColor(Color.parseColor("#FF0000"));
                    BottomGreen=false;
                    color_code+="rd";
                    break;
            }
            color_count++;
            if (color_count == 5){
                if(!FirstRun) {
                    (new SendTask()).execute("colorserve/" + color_code);
                    filecolor = color_code;
                }
                color_code = "";
                color_count = 0;
                FirstRun=false;
            }
            handler.postDelayed(bottomcolors, 1000);
        }
    };
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main_activity= this;
        setContentView(R.layout.activity_main);

        //TODO USED BY GRADER AND TO ACCESS SERVER DO NOT ALTER
        loadServerInfo();

        color_check = (Button) findViewById(R.id.color_check);
        change_bkg = (Button) findViewById((R.id.change_symbol));
        change_bkg_submit = (Button) findViewById((R.id.change_symbol_submit));
        topcolor_text = (TextView) findViewById(R.id.topcolor_text);
        bottomcolor_text = (TextView) findViewById(R.id.bottomcolor_text);
        enter_colors = (EditText) findViewById(R.id.color_code_entry);
        enter_colors.setTextColor(Color.parseColor("#FFFFFF"));
        enter_colors.setHintTextColor(Color.parseColor("#FFFFFF"));

        color_check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "nah", Toast.LENGTH_SHORT).show();
            }
        });
        change_bkg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.e("CtFEasy", "Symbol Change");
                (new SendTask()).execute("colorserve/" + team_color_code);
                filecolor = team_color_code;
                change_bkg.setVisibility(View.GONE);
                enter_colors.setVisibility(View.VISIBLE);
                change_bkg_submit.setVisibility(View.VISIBLE);
            }
        });
        change_bkg_submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String bkg = "";
                String newbkgt="";
                String newbkgb="";
                try {
                    FileInputStream fis = new FileInputStream(new File(getApplicationContext().getCacheDir(), enter_colors.getText().toString()+".txt"));
                    InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(isr);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        bkg+=line;
                    }
                } catch (FileNotFoundException e){
                    if (enter_colors.getText().toString().length() == 10 && (enter_colors.getText().toString().contains("rd") || enter_colors.getText().toString().contains("ro") || enter_colors.getText().toString().contains("on")|| enter_colors.getText().toString().contains("oy") || enter_colors.getText().toString().contains("yl") || enter_colors.getText().toString().contains("yg") || enter_colors.getText().toString().contains("ge"))) {
                        (new ColorSendTask()).execute("colorserve/" + enter_colors.getText().toString());
                        if (colorserver_response != null) {
                            bkg = colorserver_response;
                        }
                        else {
                            bkg = enter_colors.getText().toString();
                        }
                    }
                    else {
                        bkg = enter_colors.getText().toString();
                    }
                } catch (java.io.IOException e){
                    bkg = enter_colors.getText().toString();
                    Log.e("CtFEasy", "IO error");
                }
                if (bkg.length()!=0) {
                    for (int i = 0; i < Math.floor(1176/bkg.length()+.5); i++) {
                        newbkgt += bkg;
                    }
                    for (int i = 0; i < Math.floor(1008/bkg.length()+.5); i++) {
                        newbkgb += bkg;
                    }
                    topcolor_text.setText(newbkgt);
                    bottomcolor_text.setText(newbkgb);
                }
                change_bkg.setVisibility(View.VISIBLE);
                enter_colors.setVisibility(View.GONE);
                change_bkg_submit.setVisibility(View.GONE);
            }
        });

        handler.postDelayed(topcolors, 0);
        handler.postDelayed(bottomcolors, 0);
    }

    private static long dirSize(File dir) {

        if (dir.exists()) {
            long result = 0;
            File[] fileList = dir.listFiles();
            for(int i = 0; i < fileList.length; i++) {
                // Recursive call if it's a directory
                if(fileList[i].isDirectory()) {
                    result += dirSize(fileList [i]);
                } else {
                    // Sum the file size in bytes
                    result += fileList[i].length();
                }
            }
            return result; // return the file size
        }
        return 0;
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("CtFHard", "**************** File /data/data/appdir/" + s + " CLEARED *******************");
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    //TODO DO NOT REMOVE NEED THIS FOR GRADER AND INTEGRITY CHECK
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.option_color_submit) {
            Context mContext = getApplicationContext();
            AlertDialog.Builder builder = new AlertDialog.Builder(main_activity);
            // Get the layout inflater
            LayoutInflater layoutInflater = main_activity.getLayoutInflater();
            final View inflator = layoutInflater.inflate(R.layout.dialog_layout, null);
            builder.setView(inflator);

            final EditText colorSubmitField = (EditText) inflator.findViewById(R.id.color_code);

            builder.setPositiveButton(R.string.color_code_submit, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    String colorCode = colorSubmitField.getText().toString();
                    (new SendTask()).execute("colorserve/" + colorCode);
                    filecolor = colorCode;
                }
            });
            /*TODO OLD
            LayoutInflater inflater = main_activity.getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.dialog_layout, null));

            // Get items in listener and set action
            final EditText colorSubmitField = (EditText) inflater.findViewById(R.id.color_code);
            Button colorSubmitBtn = (Button) findViewById(R.id.submit_color_code);
            colorSubmitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String colorCode = colorSubmitField.getText().toString();
                    (new SendTask()).execute("colorserve/" + colorCode);
                }
            });
            */
            /*
            builder.setView(inflater.inflate(R.layout.dialog_layout, null))
                    // Add action buttons
                    .setPositiveButton(R.string.color_code_submit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // sign in the user ...
                        }
                    });
                    */
            builder.show();
        }

        return super.onContextItemSelected(item);

    }

    @SuppressWarnings("unchecked")

    private class SendTask extends AsyncTask {
        protected String doInBackground(Object... args) {
            String message = (String) args[0];
            return ServerInterface.sendMsg(message);
        }

        protected void onPostExecute(Object objResult) {
            if (objResult != null && objResult instanceof String) {
                String result = (String) objResult;
                Context mContext = getApplicationContext();
                try {
                    long size = dirSize(mContext.getCacheDir());
                    long max_size = 2400L;
                    Log.e("CtFEasy", "Size of cache: " + String.valueOf(size));
                    if(size>max_size){
                        clearApplicationData();
                    }
                    File file = new File(getApplicationContext().getCacheDir(), filecolor + ".txt");
                    file.createNewFile();
                    FileOutputStream out = new FileOutputStream(file);
                    out.write(result.getBytes());
                    out.close();
                    Log.i("CtFEasy", filecolor + ".txt");
                } catch (Exception e) {
                    Log.e("CtFEasy", "File not found");
                }
                ClipboardManager clipboard = (ClipboardManager)
                        getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("simple text", result);
                clipboard.setPrimaryClip(clip);
            }
        }
    }

        @SuppressWarnings("unchecked")

        private class ColorSendTask extends AsyncTask {
            protected String doInBackground(Object... args) {
                String message = (String) args[0];
                return ServerInterface.sendMsg(message);
            }

            protected void onPostExecute(Object objResult) {
                if (objResult != null && objResult instanceof String) {
                    String result = (String) objResult;
                    colorserver_response = result;
                }
            }
        }

    //TODO USED BY GRADER AND TO CONNECT TO SERVER, DO NOT ALTER FUNCTION
    public void loadServerInfo() {
        BufferedReader reader = null;
        String ipPort = "";
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("server.txt")));

            // do reading - should only need first line
            ipPort  = reader.readLine();
            ServerInterface.SERVER_URL = "http://"+ipPort+"/";
            Log.d(TAG, "SERVER URL: " + ServerInterface.SERVER_URL);
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
}
