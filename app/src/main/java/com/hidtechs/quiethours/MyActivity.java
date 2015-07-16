package com.hidtechs.quiethours;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.math.BigDecimal;

public class MyActivity extends AppCompatActivity implements View.OnClickListener {

    SwitchCompat s1, s2;
    LinearLayout timerow;
    Intent intent;
    TextView t1, t2, ft, tt;
    SharedPreferences preferences;
    BroadcastReceiver mReceiver;
    IntentFilter filter;
    TelephonyManager telephonyManager;
    AudioManager audioManager;
    String number, ftenthHour, fonceHour, ftenthMinute, fonceMinute, ttenthHour, tonceHour, ttenthMinute, tonceMinute;
    KeyguardManager keyguardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        PhoneStateListener callStateListener = new PhoneStateListener() {
            @SuppressLint("NewApi")
            public void onCallStateChanged(int state, String incomingNumber) {
                number = incomingNumber;

                // If phone ringing
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    if (preferences.getBoolean("allcontacts", false) && preferences.getBoolean("silentButton", false) && preferences.getBoolean("callsButton", false) && contactExists(this, number)) {
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    }
                }
                // If incoming call received
                if (state == TelephonyManager.CALL_STATE_OFFHOOK) {

                }
                //Normal state
                if (state == TelephonyManager.CALL_STATE_IDLE) {
                    if (preferences.getBoolean("silentButton", false) && keyguardManager.isKeyguardLocked()) {
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    }
                }
            }
        };
        telephonyManager.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        preferences = getSharedPreferences("MyFiles", MODE_PRIVATE);
        boolean rbpref = preferences.getBoolean("repeatButton", false);
        boolean sbpref = preferences.getBoolean("silentButton", false);
        setContentView(R.layout.activity_my);

        s1 = (SwitchCompat) findViewById(R.id.silent_switch1);
        s2 = (SwitchCompat) findViewById(R.id.repeat_switch);
        t1 = (TextView) findViewById(R.id.setTime);
        t2 = (TextView) findViewById(R.id.exceptions_settings);
        ft = (TextView) findViewById(R.id.main_from_time);
        tt = (TextView) findViewById(R.id.main_to_time);
        timerow = (LinearLayout) findViewById(R.id.linearLayout3);

        s1.setOnClickListener(this);
        s2.setOnClickListener(this);
        t1.setOnClickListener(this);
        t2.setOnClickListener(this);

        s1.setChecked(sbpref);
        s2.setChecked(rbpref);

        isRepeatOn();

    }

    public boolean contactExists(PhoneStateListener context, String number) {
        try {
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
            Cursor cursor = getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, number, null, null);
            try {
                if (cursor.moveToFirst()) {
                    return true;
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        } catch (IllegalArgumentException iae) {
            return false;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!preferences.getBoolean("silentButton", false)) {
            unregisterReceiver(mReceiver);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int fromHour_x = preferences.getInt("fromHour", 22);
        int fromMinute_x = preferences.getInt("fromMinute", 0);
        ftenthHour = String.valueOf(fromHour_x / 10);
        fonceHour = String.valueOf(fromHour_x % 10);
        ftenthMinute = String.valueOf(fromMinute_x / 10);
        fonceMinute = String.valueOf(fromMinute_x % 10);
        ft.setText("From " + ftenthHour + "" + fonceHour + " : " + ftenthMinute + "" + fonceMinute);
        int toHour_x = preferences.getInt("toHour", 6);
        int toMinute_x = preferences.getInt("toMinute", 0);
        ttenthHour = String.valueOf(toHour_x / 10);
        tonceHour = String.valueOf(toHour_x % 10);
        ttenthMinute = String.valueOf(toMinute_x / 10);
        tonceMinute = String.valueOf(toMinute_x % 10);
        tt.setText(" to " + ttenthHour + "" + tonceHour + " : " + ttenthMinute + "" + tonceMinute);
    }


    private void isRepeatOn() {
        if (!s2.isChecked()) {
            timerow.setVisibility(timerow.GONE);
        } else {
            timerow.setVisibility(timerow.VISIBLE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == s1.getId()) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("silentButton", s1.isChecked());
            editor.commit();
        }
        if (v.getId() == s2.getId()) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("repeatButton", s2.isChecked());
            editor.commit();
            if (s2.isChecked()) {
                timerow.setVisibility(timerow.VISIBLE);
            } else {
                timerow.setVisibility(timerow.GONE);
            }
        }

        if (v.getId() == t1.getId()) {
            intent = new Intent(this, SetTimeActivity.class);
            startActivity(intent);
        }

        if (v.getId() == t2.getId()) {
            intent = new Intent(this, ExceptionActivity.class);
            startActivity(intent);

        }
    }

}

