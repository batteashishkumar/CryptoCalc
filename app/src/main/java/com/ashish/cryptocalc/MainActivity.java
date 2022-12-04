package com.ashish.cryptocalc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText et_investamount,et_RTP,et_BF,et_SF,et_ricksPer,et_SELLrate;
    InputData inputData=new InputData();
    boolean Navigate=true;
    Enum stringEnum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String s=getPackageManager().getInstalledPackages(0).toString();

        et_investamount=findViewById(R.id.et_investamount);
        et_RTP=findViewById(R.id.et_RTP);
        et_BF=findViewById(R.id.et_BF);
        et_SF=findViewById(R.id.et_SF);
        et_ricksPer=findViewById(R.id.et_ricksPer);
        et_SELLrate=findViewById(R.id.et_SELLrate);
    }

    public void Calculate(View view) {
        Navigate=true;
        try {
            inputData.Investment = Integer.parseInt(et_investamount.getText().toString());
        }catch (Exception e){
            if (Navigate){
                Navigate=false;
                Toast.makeText(getApplicationContext(), "Enter Proper Investment Amount", Toast.LENGTH_SHORT).show();
            }

        }
        try {
            inputData.RealtimePrice = Integer.parseInt(et_RTP.getText().toString());
        }catch (Exception e){
            if (Navigate){
                Navigate=false;
                Toast.makeText(getApplicationContext(), "Enter Proper RealTimePrice Amount", Toast.LENGTH_SHORT).show();
            }

        }
        try {
            inputData.BuyingFee = Integer.parseInt(et_BF.getText().toString());
        }catch (Exception e){
            if (Navigate){
                Navigate=false;
                Toast.makeText(getApplicationContext(), "Enter Proper Buying Fee Amount", Toast.LENGTH_SHORT).show();
            }

        }
        try {
            inputData.SellingFee = Integer.parseInt(et_SF.getText().toString());
        }catch (Exception e){
            if (Navigate){
                Navigate=false;
                Toast.makeText(getApplicationContext(), "Enter Proper Selling fee Amount", Toast.LENGTH_SHORT).show();
            }

        }try {
            inputData.RiskPercent = Integer.parseInt(et_ricksPer.getText().toString());
        }catch (Exception e){
            if (Navigate){
                Navigate=false;
                Toast.makeText(getApplicationContext(), "Enter Proper Risk Percentage ", Toast.LENGTH_SHORT).show();
            }
        }try {
            inputData.SellRate = Integer.parseInt(et_SELLrate.getText().toString());
        }catch (Exception e){
            if (Navigate){
                Navigate=false;
                Toast.makeText(getApplicationContext(), "Enter Proper at what rate you want to sell", Toast.LENGTH_SHORT).show();
            }

        }
        if (Navigate){
            Intent intent=new Intent(this,MainActivity2.class);
            intent.putExtra("InputData",inputData);
            startActivity(intent);
        }

    }
}