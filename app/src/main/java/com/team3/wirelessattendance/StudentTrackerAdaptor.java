package com.team3.wirelessattendance;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class StudentTrackerAdaptor extends CursorAdapter {

    public StudentTrackerAdaptor(Context context, Cursor cursor){
        super(context,cursor,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(parent.getContext()).inflate(R.layout.student,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView tvRoll = (TextView)view.findViewById(R.id.roll_view);
        TextView tvName = (TextView)view.findViewById(R.id.name_view);
        TextView tvAddress = (TextView)view.findViewById(R.id.address_view);

        tvAddress.setText(cursor.getString(cursor.getColumnIndexOrThrow("address")));
        tvName.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
        tvRoll.setText(String.format("%02d",Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("roll")))));

    }
}