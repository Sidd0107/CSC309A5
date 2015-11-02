package com.example.siddharthgautam.csc301;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ca.toronto.csc301.chat.ServerThread;

public class StartScreenActivity extends AppCompatActivity {

    private Button connectButton;
    private Button openChatsButton;
    private Button openMainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        initializeViews();
    }

    private void initializeViews(){
        connectButton = (Button) findViewById(R.id.connectButton);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartScreenActivity.this, chatActivity.class));
            }
        });

        openChatsButton = (Button) findViewById(R.id.openChatsButton);
        openChatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartScreenActivity.this, chatActivity.class)); // open chats
            }
        });

        openMainButton = (Button) findViewById(R.id.openMainButton);
        openChatsButton.setOnClickListener(new View.OnClickListener(){
        @Override
            public void onClick(View v){
                openMain(v);
            }
        });

        ServerThread serverThread = new ServerThread();
        serverThread.run();
    }

    public void openMain(View view) {
        Intent intent = new Intent(StartScreenActivity.this, MainActivity.class);
        StartScreenActivity.this.startActivity(intent);
    }

}
