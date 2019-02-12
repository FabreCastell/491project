package com.example.nice.a491;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class FirstFragment extends Fragment {

    private View view;
    private Button inputButton;
    private String messageText;
    private String messageUser;
    private long messageTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_first, container, false);
// get the reference of Button
        inputButton = view.findViewById(R.id.input_send);
// perform setOnClickListener on first Button
        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input = view.findViewById(R.id.input);
                messageText = input.getText().toString();
            }
        });

        return view;
    }

    public void ChatMessage(String messageText, String messageUser) {
        this.messageText = messageText;
        this.messageUser = messageUser;

        // Initialize to current time
        messageTime = new Date().getTime();
    }

    public void ChatMessage() {

    }

    public String getMessageText () {
        return messageText;
    }

    public void setMessageText (String messageText){
        this.messageText = messageText;
    }

    public String getMessageUser () {
        return messageUser;
    }

    public void setMessageUser (String messageUser){
        this.messageUser = messageUser;
    }

    public long getMessageTime () {
        return messageTime;
    }

    public void setMessageTime ( long messageTime){
        this.messageTime = messageTime;
    }

}




