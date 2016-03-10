package com.team3.wirelessattendance;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;



public class scan extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    StuDatabaseHelper dbHelper;
    String date,date_f;


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        dateview = (EditText)findViewById(R.id.d_text);
        dateview.setText(dayOfMonth+" - "+(monthOfYear+1)+" - "+year);
        date = dayOfMonth+""+(monthOfYear+1)+""+year;
        date_f = dayOfMonth+" - "+(monthOfYear+1)+" - "+year;

        int num = dbHelper.getAttCount(date);
        value.setText(num+"/3");
        float per = num*100/total;
        perc.setText(String.format("%.1f",per)+"% attendance today!");

    }

    EditText dateview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month =1+c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        dateview = (EditText)findViewById(R.id.d_text);
        date = day+""+month+""+year;
        date_f = day+" - "+(month+1)+" - "+year;
        dateview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment pickerFragmentScan = new DatePickerFragment();
                pickerFragmentScan.show(getFragmentManager(), "datePicker");
            }
        });

        dateview.setText(Integer.toString(day) + " - " + Integer.toString(month) + " - " + Integer.toString(year));

        dbHelper = StuDatabaseHelper.getInstance(getApplicationContext());
    }

    TextView perc;
    TextView value;
    final int total = 3;

    @Override
    protected void onResume() {
        super.onResume();
        perc = (TextView)findViewById(R.id.attend_per);
        value = (TextView)findViewById(R.id.attend_val);

        dbHelper = StuDatabaseHelper.getInstance(getApplicationContext());

        int num = dbHelper.getAttCount(date);
        value.setText(num+"/3");
        float per = num*100/total;
        perc.setText(String.format("%.1f", per) + "% attendance today!");

    }



    public void startList(View view) {
        Intent intent = new Intent(this,List.class);

        intent.putExtra("date", date);
        Log.d("dateInIntentScan",date);
        startActivity(intent);

    }

    public void viewList(View view) {
        Intent intent = new Intent(this,ViewAtt.class);

        intent.putExtra("date",date);
        intent.putExtra("date_f",date_f);
        startActivity(intent);
    }


    public static class DatePickerFragment extends DialogFragment{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){

            //Current date is set as default date
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(),(scan)getActivity(),year,month,day);
        }

    }

}

