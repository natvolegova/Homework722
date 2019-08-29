package com.example.homework722;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText etNumber;
    EditText etText;
    Button btnCall;
    Button btnSendSms;
    Button btnCallPr;
    Button btnSendSmsPr;
    String callNumber;
    String smsText;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 11;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //обработка звонка напрямую
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callNumber();
            }
        });
        //обработка СМС напрямую
        btnSendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSMS();
            }
        });
        //обработка звонка с предпросмотром
        btnCallPr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callNumber = etNumber.getText().toString();
                if (!callNumber.equals("")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + callNumber));
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, getResources().getText(R.string.msg_error_no_number), Toast.LENGTH_LONG).show();
                }
            }
        });
        //обработка СМС с предпросмотром
        btnSendSmsPr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callNumber = etNumber.getText().toString();
                smsText = etText.getText().toString();
                if (!callNumber.equals("")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("smsto:"));
                    intent.setType("vnd.android-dir/mms-sms");
                    intent.putExtra("address", callNumber);
                    intent.putExtra("sms_body", smsText);
                    startActivity(Intent.createChooser(intent, getResources().getText(R.string.txt_chooser)));
                } else {
                    Toast.makeText(MainActivity.this, getResources().getText(R.string.msg_error_no_number), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // Проверяем результат запроса на право позвонить
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callNumber();
                } else {
                    finish();
                }
            }
            ;
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                // Проверяем результат запроса на право отправить смс
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMS();
                } else {
                    finish();
                }
            }
        }
    }

    //обработка звонка
    private void callNumber() {
        callNumber = etNumber.getText().toString();
        if (!callNumber.equals("")) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // Разрешение не получено
                // Делаем запрос на добавление разрешения звонка
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
            } else {
                // Разрешение уже получено
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + callNumber));
                startActivity(intent);
            }
        } else {
            Toast.makeText(MainActivity.this, getResources().getText(R.string.msg_error_no_number), Toast.LENGTH_LONG).show();
        }
    }

    //обработка смс
    private void sendSMS() {
        callNumber = etNumber.getText().toString();
        smsText = etText.getText().toString();
        if (!callNumber.equals("")) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                // Разрешение не получено
                // Делаем запрос на добавление разрешения звонка
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            } else {
                // Разрешение уже получено
                SmsManager smgr = SmsManager.getDefault();
                smgr.sendTextMessage(callNumber, null, smsText, null, null);
            }
        } else {
            Toast.makeText(MainActivity.this, getResources().getText(R.string.msg_error_no_number), Toast.LENGTH_LONG).show();
        }
    }

    private void initView() {
        etNumber = findViewById(R.id.et_number);
        etText = findViewById(R.id.et_text);
        btnCall = findViewById(R.id.btn_call);
        btnSendSms = findViewById(R.id.btn_send_sms);
        btnCallPr = findViewById(R.id.btn_call_pr);
        btnSendSmsPr = findViewById(R.id.btn_send_sms_pr);
    }
}
