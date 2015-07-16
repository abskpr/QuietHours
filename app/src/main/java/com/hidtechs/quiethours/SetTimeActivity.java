package com.hidtechs.quiethours;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;


public class SetTimeActivity extends AppCompatActivity implements CheckBox.OnCheckedChangeListener {

    TextView fromTime, toTime, fromTimeShow, toTimeShow;
    public static final int FROMTEXT_DIALOG_ID = 0;
    public static final int TOTEXT_DIALOG_ID = 1;
    int fromHour_x, fromMinute_x, toHour_x, toMinute_x;
    CheckBox check_all, check_mon, check_tue, check_wed, check_thu, check_fri, check_sat, check_sun;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time);

        fromTimeShow = (TextView) findViewById(R.id.from_time_show);
        toTimeShow = (TextView) findViewById(R.id.to_time_show);
        check_all = (CheckBox) findViewById(R.id.always);
        check_mon = (CheckBox) findViewById(R.id.day_mon);
        check_tue = (CheckBox) findViewById(R.id.day_tue);
        check_wed = (CheckBox) findViewById(R.id.day_wed);
        check_thu = (CheckBox) findViewById(R.id.day_thu);
        check_fri = (CheckBox) findViewById(R.id.day_fri);
        check_sat = (CheckBox) findViewById(R.id.day_sat);
        check_sun = (CheckBox) findViewById(R.id.day_sun);

        check_all.setOnCheckedChangeListener(this);
        check_mon.setOnCheckedChangeListener(this);
        check_tue.setOnCheckedChangeListener(this);
        check_wed.setOnCheckedChangeListener(this);
        check_thu.setOnCheckedChangeListener(this);
        check_fri.setOnCheckedChangeListener(this);
        check_sat.setOnCheckedChangeListener(this);
        check_sun.setOnCheckedChangeListener(this);

        preferences = getSharedPreferences("MyFiles", MODE_PRIVATE);

        fromHour_x = preferences.getInt("fromHour", 22);
        fromMinute_x = preferences.getInt("fromMinute", 00);
        String ftenthHour = String.valueOf(fromHour_x / 10);
        String fonceHour = String.valueOf(fromHour_x % 10);
        String ftenthMinute = String.valueOf(fromMinute_x / 10);
        String fonceMinute = String.valueOf(fromMinute_x % 10);
        fromTimeShow.setText(ftenthHour + "" + fonceHour + " : " + ftenthMinute + "" + fonceMinute);

        toHour_x = preferences.getInt("toHour", 06);
        toMinute_x = preferences.getInt("toMinute", 00);
        String ttenthHour = String.valueOf(toHour_x / 10);
        String tonceHour = String.valueOf(toHour_x % 10);
        String ttenthMinute = String.valueOf(toMinute_x / 10);
        String tonceMinute = String.valueOf(toMinute_x % 10);
        toTimeShow.setText(ttenthHour + "" + tonceHour + " : " + ttenthMinute + "" + tonceMinute);

        showTimePickerDialog();
    }

    public void showTimePickerDialog() {
        fromTime = (TextView) findViewById(R.id.from_time);
        fromTime.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(FROMTEXT_DIALOG_ID);
                    }
                }
        );
        toTime = (TextView) findViewById(R.id.to_time);
        toTime.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(TOTEXT_DIALOG_ID);
                    }
                }
        );
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == FROMTEXT_DIALOG_ID)
            return new TimePickerDialog(this, ftTimePickerListener, fromHour_x, fromMinute_x, false);
        else if (id == TOTEXT_DIALOG_ID)
            return new TimePickerDialog(this, ttTimePickerListener, toHour_x, toMinute_x, false);
        else return null;
    }

    protected TimePickerDialog.OnTimeSetListener ftTimePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            fromHour_x = hourOfDay;
            String tenthHour = String.valueOf(fromHour_x / 10);
            String onceHour = String.valueOf(fromHour_x % 10);
            fromMinute_x = minute;
            String tenthMinute = String.valueOf(fromMinute_x / 10);
            String onceMinute = String.valueOf(fromMinute_x % 10);
            fromTimeShow.setText(tenthHour + "" + onceHour + " : " + tenthMinute + "" + onceMinute);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("fromHour", fromHour_x);
            editor.putInt("fromMinute", fromMinute_x);
            editor.commit();
        }
    };

    protected TimePickerDialog.OnTimeSetListener ttTimePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            toHour_x = hourOfDay;
            String tenthHour = String.valueOf(toHour_x / 10);
            String onceHour = String.valueOf(toHour_x % 10);
            toMinute_x = minute;
            String tenthMinute = String.valueOf(toMinute_x / 10);
            String onceMinute = String.valueOf(toMinute_x % 10);
            toTimeShow.setText(tenthHour + "" + onceHour + " : " + tenthMinute + "" + onceMinute);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("toHour", toHour_x);
            editor.putInt("toMinute", toMinute_x);
            editor.commit();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        check_all.setChecked(preferences.getBoolean("checkAll", false));
        check_mon.setChecked(preferences.getBoolean("checkMon", false));
        check_tue.setChecked(preferences.getBoolean("checkTue", false));
        check_wed.setChecked(preferences.getBoolean("checkWed", false));
        check_thu.setChecked(preferences.getBoolean("checkThu", false));
        check_fri.setChecked(preferences.getBoolean("checkFri", false));
        check_sat.setChecked(preferences.getBoolean("checkSat", false));
        check_sun.setChecked(preferences.getBoolean("checkSun", false));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (buttonView.getId() == check_all.getId()) {
            if (check_all.isChecked()) {
                check_mon.setChecked(true);
                check_tue.setChecked(true);
                check_wed.setChecked(true);
                check_thu.setChecked(true);
                check_fri.setChecked(true);
                check_sat.setChecked(true);
                check_sun.setChecked(true);
            }
        }
       if (check_mon.isChecked() && check_tue.isChecked() && check_wed.isChecked() && check_thu.isChecked() && check_fri.isChecked() && check_sat.isChecked() && check_sun.isChecked()) {
            check_all.setChecked(true);
        } else {
            check_all.setChecked(false);
        }

        SharedPreferences.Editor editor = preferences.edit();
        Boolean all=check_all.isChecked();
        Boolean mon=check_mon.isChecked();
        Boolean tue=check_tue.isChecked();
        Boolean wed=check_wed.isChecked();
        Boolean thu=check_thu.isChecked();
        Boolean fri=check_fri.isChecked();
        Boolean sat=check_sat.isChecked();
        Boolean sun=check_sun.isChecked();
        editor.putBoolean("checkAll", all);
        editor.putBoolean("checkMon", mon);
        editor.putBoolean("checkTue", tue);
        editor.putBoolean("checkWed", wed);
        editor.putBoolean("checkThu", thu);
        editor.putBoolean("checkFri", fri);
        editor.putBoolean("checkSat", sat);
        editor.putBoolean("checkSun", sun);
        editor.commit();
    }
}
