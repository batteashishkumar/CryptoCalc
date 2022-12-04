package com.ashish.cryptocalc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

public class InputFragment extends Fragment {

    private InputViewModel mViewModel;
    NavController navController=null;
    EditText et_investamount,et_RTP,et_BF,et_SF,et_ricksPer,et_SELLrate;
    private boolean Navigate;
    InputData inputData=new InputData();
    SingleActivityViewModel singleActivityViewModel=null;
    FragmentInterface fragmentInterface=null;

    public  InputFragment(SingleActivityViewModel singleActivityViewModel, FragmentInterface fragmentInterface){
        this.singleActivityViewModel=singleActivityViewModel;
        this.fragmentInterface=fragmentInterface;
    }
    public  InputFragment(){
        this.singleActivityViewModel=singleActivityViewModel;
        this.fragmentInterface=fragmentInterface;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.input_fragment, container, false);
        et_investamount=view.findViewById(R.id.et_investamount);
        et_RTP=view.findViewById(R.id.et_RTP);
        et_BF=view.findViewById(R.id.et_BF);
        et_SF=view.findViewById(R.id.et_SF);
        et_ricksPer=view.findViewById(R.id.et_ricksPer);
        et_SELLrate=view.findViewById(R.id.et_SELLrate);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_Calculate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Calculate();


            }
        });
    }
    public void Calculate() {
        Navigate=true;
        try {
            inputData.Investment = Integer.parseInt(et_investamount.getText().toString());
        }catch (Exception e){
            if (Navigate){
                Navigate=false;
                Toast.makeText(getContext(), "Enter Proper Investment Amount", Toast.LENGTH_SHORT).show();
            }

        }
        try {
            inputData.RealtimePrice = Integer.parseInt(et_RTP.getText().toString());
        }catch (Exception e){
            if (Navigate){
                Navigate=false;
                Toast.makeText(getContext(), "Enter Proper RealTimePrice Amount", Toast.LENGTH_SHORT).show();
            }

        }
        try {
            inputData.BuyingFee = Integer.parseInt(et_BF.getText().toString());
        }catch (Exception e){
            if (Navigate){
                Navigate=false;
                Toast.makeText(getContext(), "Enter Proper Buying Fee Amount", Toast.LENGTH_SHORT).show();
            }

        }
        try {
            inputData.SellingFee = Integer.parseInt(et_SF.getText().toString());
        }catch (Exception e){
            if (Navigate){
                Navigate=false;
                Toast.makeText(getContext(), "Enter Proper Selling fee Amount", Toast.LENGTH_SHORT).show();
            }

        }try {
            inputData.RiskPercent = Integer.parseInt(et_ricksPer.getText().toString());
        }catch (Exception e){
            if (Navigate){
                Navigate=false;
                Toast.makeText(getContext(), "Enter Proper Risk Percentage ", Toast.LENGTH_SHORT).show();
            }
        }try {
            inputData.SellRate = Integer.parseInt(et_SELLrate.getText().toString());
        }catch (Exception e){
            if (Navigate){
                Navigate=false;
                Toast.makeText(getContext(), "Enter Proper at what rate you want to sell", Toast.LENGTH_SHORT).show();
            }

        }
        if (Navigate){
            singleActivityViewModel.setmInputData(inputData);
            fragmentInterface.clickCalculate();
        }

    }

}