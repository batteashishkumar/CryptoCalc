package com.ashish.cryptocalc;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.HashMap;

public class SingleActivity extends AppCompatActivity implements FragmentInterface {
    SingleActivityViewModel singleActivityViewModel=null;
    FragmentTransaction ft=getSupportFragmentManager().beginTransaction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        String s=getPackageManager().getInstalledPackages(0).toString();
        ArrayList<String> ashish=new ArrayList<>();
        ashish.add("Ananth");
        ashish.add("shiva");
        ashish.add("rohit");
//        pick(ashish);
        singleActivityViewModel= new ViewModelProvider(this).get(SingleActivityViewModel.class);
        setFragment();
    }

    private void setFragment() {
        ft=getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment, new InputFragment(singleActivityViewModel,this));
        ft.commit();
    }

    @Override
    public void clickCalculate() {
        ft=getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment, new OutputFragment(singleActivityViewModel,this,this));
        ft.addToBackStack(null);
        ft.commit();
    }
    public int solve_function(int[] A) {

        HashMap<Integer,Integer> hashMap=new HashMap<>();
        for(int i=0;i<A.length;i++){
            if (hashMap.containsKey(A[i])){
                int a=hashMap.get(A[i]);
               hashMap.put(A[i],(a+1));
            }else {
                hashMap.put(A[i],1);
            }
        }
        int greatestKey=A[0];
        int greatestCount=hashMap.get(A[0]);
        for (Integer key : hashMap.keySet()) {
            if (greatestCount<hashMap.get(key)){
                greatestCount=hashMap.get(key);
                greatestKey=key;
            }
        }
        int totalresearchsX=0;
        for (Integer key : hashMap.keySet()) {
            if (greatestKey==key){
                break;
            }
            totalresearchsX=totalresearchsX+hashMap.get(key);

        }

        if (totalresearchsX>greatestCount){
            return -1;
        }else {
            return hashMap.get(greatestKey);
        }
    }


    public boolean solve_function2(int[] A,int weight) {
        boolean flag = true;
        for (int i = 0; i < A.length; i++) {
            if (A[i] <= weight) {
                weight = weight + A[i];
            } else {
                return false;
            }
        }
        return true;
    }







}