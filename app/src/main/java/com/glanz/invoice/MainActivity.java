package com.glanz.invoice;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;

public class MainActivity extends Activity implements TextView.OnEditorActionListener {

    //instance variables

    private EditText inputeditText;
    private TextView percentTextView;
    private TextView discountAmountTextView;
    private TextView totalAmountTextView;
    private String subtotalString;
    private SharedPreferences savedValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get references of the widgets from the R class
        inputeditText = findViewById(R.id.inputeditText);
        percentTextView = findViewById(R.id.percentTextView);
        discountAmountTextView =  findViewById(R.id.discountAmountTextView);
        totalAmountTextView =  findViewById(R.id.totalAmountTextView);
        inputeditText.setOnEditorActionListener(this);
        //get shared preferences object
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

    @Override
    public void onPause(){
        //save the instance variables
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putString("subtotalString", subtotalString);
        editor.commit();
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
        //get the instance variables
        subtotalString = savedValues.getString("subtotalString", "");
        //set fahrenheit on inputEditText
        inputeditText.setText(subtotalString);
        //calculate and display
        calculateAndDisplay();
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if(actionId == EditorInfo.IME_ACTION_DONE) {
            calculateAndDisplay();
        }
        return false;
    }

    private void calculateAndDisplay() {
        //get subtotal
        subtotalString = inputeditText.getText().toString();
        float subtotal;
        if (subtotalString.equals("")) {
            subtotal = 0;
        } else {
            subtotal = Float.parseFloat(subtotalString);
        }

        //get the discount percent
        float discountPercent = 0;
        if (subtotal >= 200) {
            discountPercent = .2f;
        } else if(subtotal >= 100){
            discountPercent = .1f;
        }else{
            discountPercent = 0;
        }

        //calculate the discount
        float discountAmount = subtotal * discountPercent;
        float total = subtotal - discountAmount;

        //display the data on the layout
        NumberFormat percent = NumberFormat.getPercentInstance();
        percentTextView.setText(percent.format(discountPercent));

        //display the discount amount and total
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        discountAmountTextView.setText(currency.format(discountAmount));
        totalAmountTextView.setText(currency.format(total));
    }
}
