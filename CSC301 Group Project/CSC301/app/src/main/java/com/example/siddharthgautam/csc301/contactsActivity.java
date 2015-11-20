package com.example.siddharthgautam.csc301;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import ca.toronto.csc301.chat.ConnectedThread;
import ca.toronto.csc301.chat.ConnectionsList;

public class contactsActivity extends AppCompatActivity {
    private BluetoothAdapter bluetooth;
    private ListView contactsList;
    private ArrayList cL = new ArrayList();
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bluetooth = BluetoothAdapter.getDefaultAdapter();
        setContentView(R.layout.contacts_page);
        contactsList = (ListView)findViewById(R.id.listContacts);
        updateContactsList();

        // allow each item in contacts to be clickable 
        contactsList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String deviceName = contactsList.getItemAtPosition(position).toString();
                BluetoothDevice device = MainActivity.getDeviceByName(deviceName);
                if(device != null) {//for paired devices
                    ConnectedThread t = ConnectionsList.getInstance().getConnectedThread((device));
                    if (t == null) {//no connection available, try to connect
                        ConnectionsList.getInstance().makeConnectionTo(device);
                    }
                }
                if(ConnectionsList.getInstance().isDeviceInNetwork(device.getAddress()) == false){
                    Toast.makeText(getApplicationContext(), deviceName + " is currently not in the network." +
                            "   Trying to connect..", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(contactsActivity.this, chatActivity.class);
                intent.putExtra("BluetoothDevice", device);
                startActivity(intent);
            }
        });
    }

    private void updateContactsList(){
        Set<BluetoothDevice> d = bluetooth.getBondedDevices();
        Iterator<BluetoothDevice> i = d.iterator();

        while(i.hasNext()){
            BluetoothDevice device = i.next();
            ConnectionsList.getInstance().makeConnectionTo(device);
            String device_name = device.getName();
            cL.add(device_name);
            /**if(device_name.contains("BlueM-")){ //Doesnt properly work for s6 edge so temporarily disabled(other wise I cant test..)
                cL.add(device_name.substring(6));
            }
            else //Exception for testing "Priyen Galaxy S6 edge" because the setName method apparntly has no effect on it
            if(device_name == "Priyen Galaxy S6 edge"){
                cL.add(device_name);
            }**/
        }
        // now add from network devices
        Iterator<String> di = ConnectionsList.getInstance().getNamesOfConnectedDevices().iterator();
        while(di.hasNext()){
            String name = di.next();
            if(cL.contains(name)){
                continue;
            }
            cL.add(name);
        }
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, cL);
        contactsList.setAdapter(adapter);
    }

}
