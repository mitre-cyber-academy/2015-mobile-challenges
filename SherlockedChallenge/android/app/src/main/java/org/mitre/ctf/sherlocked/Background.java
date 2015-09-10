package org.mitre.ctf.sherlocked;

import android.app.IntentService;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;


public class Background extends IntentService {
    private final String TAG = "SHERLOCKED";
    private String masterPasscode = "";
    private String masterOrder = "";
    private int count = 0;
    private boolean bool = true;

    public Background() {
        super("");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public Background(String name) {
        super(name);
    }

    String passcodeOrder;

    @Override
    protected void onHandleIntent(Intent intent) {
        masterPasscode = intent.getStringExtra("passcode");
        masterOrder = intent.getStringExtra("order");

        new Timer().scheduleAtFixedRate(new TimerTask() {
            final String passcode = masterPasscode;
            final String order = masterOrder;

            @Override
            public void run() {
                Log.d(TAG, ">broadcasting intent");
                Intent intent = new Intent();
                intent.putExtra("data", getNextData());
                intent.setAction(Intent.ACTION_ATTACH_DATA);
                Log.d(TAG,"Intent { act=android.intent.action.ATTACH_DATA flg=0x10000000 (has extras) }");
                sendBroadcast(intent);
            }
        }, 0, 5000);//put here time 1000 milliseconds=1 second
    }

    public String getNextData () {
        char to_return;

        to_return = bool ? masterPasscode.charAt((count / 2) % masterPasscode.length()) : masterOrder.charAt((count / 2) % masterOrder.length());

        bool = !bool;
        count++;

        return Character.toString(to_return);
    }
}
