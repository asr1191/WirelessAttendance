package com.team3.wirelessattendance;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class List extends AppCompatActivity {


    private BluetoothAdapter btAdapter;
    private BluetoothDevice remoteDevice;

    String today;
    StuDatabaseHelper dbHelper;
    StudentTrackerAdaptor studentTrackerAdaptor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        today = getIntent().getStringExtra("date");
        dbHelper = StuDatabaseHelper.getInstance(getApplicationContext());
        ListView listView = (ListView) findViewById(R.id.studs_list);

        Cursor test1 = dbHelper.InitDay(today);
        studentTrackerAdaptor = new StudentTrackerAdaptor(this,test1);
        listView.setAdapter(studentTrackerAdaptor);
    }

    BroadcastReceiver discoveryResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            String remoteAddress = remoteDevice.getAddress();

            dbHelper.BroadReceive(remoteAddress, today, studentTrackerAdaptor);
            //TODO Make this to display name
            Toast.makeText(List.this, remoteAddress+" found!", Toast.LENGTH_SHORT).show();
        }
    };


    public void stopscan(View view){
        if (btAdapter.isDiscovering()) {
            btAdapter.cancelDiscovery();
            Toast.makeText(List.this, "Cancelling scan..", Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(List.this, "No scan in progress..", Toast.LENGTH_SHORT).show();

    }

    public void btscan(View view){
        if(!btAdapter.isEnabled())
        {
            String actionreq = BluetoothAdapter.ACTION_REQUEST_ENABLE;
            startActivity(new Intent(actionreq));
        }

        else if(btAdapter.startDiscovery()) {
            String toastText="Scanning.. Other devices must be discoverable..";
            Toast toast = Toast.makeText(List.this, toastText, Toast.LENGTH_SHORT);
            //Textview with id 'message' must be a textview associated with toasts
            //That means changes to that textview must produce changes in the toast
            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
            if(v!=null){
                v.setGravity(Gravity.CENTER);
            }
            toast.show();
            registerReceiver(discoveryResult, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        }
    }
}


