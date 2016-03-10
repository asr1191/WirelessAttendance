package com.team3.wirelessattendance;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ViewAtt extends AppCompatActivity {

    String day;
    StuDatabaseHelper dbHelper;
    StudentTrackerAdaptor studentTrackerAdaptor;
    Cursor view_cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_att);

        day = getIntent().getStringExtra("date");

        dbHelper = StuDatabaseHelper.getInstance(getApplicationContext());
        ListView listView = (ListView)findViewById(R.id.view_list);
        TextView textView = (TextView)findViewById(R.id.dv_view);

        textView.setText(getIntent().getStringExtra("date_f"));

        view_cursor = dbHelper.viewAttendance(day);
        studentTrackerAdaptor = new StudentTrackerAdaptor(this, view_cursor);

        listView.setAdapter(studentTrackerAdaptor);
    }


}
