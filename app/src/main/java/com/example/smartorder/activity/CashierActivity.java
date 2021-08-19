package com.example.smartorder.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartorder.R;
import com.example.smartorder.constants.Constants;
import com.example.smartorder.fragment.cashier.CashierFragment;
import com.example.smartorder.support.Support;

public class CashierActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);
//        Support.hideSoftKeyboard(CashierActivity.this);
        getSupportFragmentManager().beginTransaction().replace(R.id.frq, new CashierFragment(), Constants.fragmentCashier).commit();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}