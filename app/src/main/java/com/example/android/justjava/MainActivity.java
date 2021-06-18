package com.example.android.justjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {

        CheckBox checkBoxWhippedCream = findViewById(R.id.need_whipped_cream);
        boolean hasWhippedCream = checkBoxWhippedCream.isChecked();

        CheckBox checkBoxChocolate = findViewById(R.id.need_chocolate);
        boolean hasChocolate = checkBoxChocolate.isChecked();

        EditText editText = findViewById(R.id.name_edit_text);
        String customerName = editText.getText().toString();

        int price = calculatePrice(hasWhippedCream, hasChocolate);

        String priceMessage = createOrderSummary(price, hasWhippedCream, hasChocolate, customerName);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_summary_email_subject,customerName));
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    private String createOrderSummary(int price, boolean needWhippedCream, boolean needChocolate, String customer) {

        return getString(R.string.order_summary_name,customer) +
                "\n" + getString(R.string.order_summary_whipped_cream,needWhippedCream) +
                "\n" + getString(R.string.order_summary_chocolate,needChocolate) +
                "\n" + getString(R.string.order_summary_quantity,quantity) +
                "\n" + getString(R.string.total) +": " +Currency.getInstance(Locale.getDefault()).getSymbol() + price;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int quantity) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + quantity);
    }

    /**
     * This methods increments the quantity
     */
    public void increment(View view) {
        if (quantity == 100) {
            Toast.makeText(this, "Quantity cannot be greater than 100", Toast.LENGTH_LONG).show();
            return;
        }
        quantity = quantity + 1;

        displayQuantity(quantity);
    }

    /**
     * This method decrements the quantity
     */
    public void decrement(View view) {
        if (quantity == 1) {
            Toast.makeText(this, "Quantity cannot be less than 1", Toast.LENGTH_LONG).show();
            return;
        }
        quantity = quantity - 1;

        displayQuantity(quantity);
    }

    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {

        int basePrice = 5;

        if (hasWhippedCream) {
            basePrice = basePrice + 1;
        }
        if (hasChocolate) {
            basePrice = basePrice + 2;
        }
        return quantity * basePrice;
    }
}