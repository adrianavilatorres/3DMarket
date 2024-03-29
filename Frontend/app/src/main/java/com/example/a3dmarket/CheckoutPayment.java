package com.example.a3dmarket;

import android.app.DownloadManager;
import android.app.ProgressDialog;

import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;

import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.gson.Gson;

import com.google.gson.GsonBuilder;

import com.google.gson.reflect.TypeToken;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.stripe.android.ApiResultCallback;

import com.stripe.android.PaymentIntentResult;

import com.stripe.android.Stripe;

import com.stripe.android.model.ConfirmPaymentIntentParams;

import com.stripe.android.model.PaymentIntent;

import com.stripe.android.model.PaymentMethodCreateParams;

import com.stripe.android.view.CardInputWidget;

import java.io.IOException;

import java.lang.ref.WeakReference;

import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Objects;

import okhttp3.Call;

import okhttp3.Callback;

import okhttp3.MediaType;

import okhttp3.OkHttpClient;

import okhttp3.Request;

import okhttp3.RequestBody;

import okhttp3.Response;

public class CheckoutPayment extends AppCompatActivity {

    SharedPref sharedPref;

    // 10.0.2.2 is the Android emulator's alias to localhost
    // 192.168.1.6 If you are testing in real device with usb connected to same network then use your IP address

    private static final String BACKEND_URL = "http://192.168.1.138:4242/"; //4242 is port mentioned in server i.e index.js
    TextView amountText;
    CardInputWidget cardInputWidget;
    Button payButton;

    // we need paymentIntentClientSecret to start transaction
    private String paymentIntentClientSecret;
    String getUrl = "";
    //declare stripe
    private Stripe stripe;

    String amount;

    Double amountDouble=null;

    LinearLayoutCompat layout;

    private OkHttpClient httpClient;

    static ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_payment);

        sharedPref = new SharedPref(this);
        layout = (LinearLayoutCompat)findViewById(R.id.layout_pago);

        amountText = findViewById(R.id.pricePay);
        cardInputWidget = findViewById(R.id.stripeInputPay);
        payButton = findViewById(R.id.payButton);
        progressDialog = new ProgressDialog(this,ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        progressDialog.setTitle("Transaction in progress");
        progressDialog.setCancelable(false);
        httpClient = new OkHttpClient();

        TextView titulo = findViewById(R.id.titleEdit);

        if (sharedPref.loadNightModeState() == true){
            setTheme(R.style.AppThemeDark);
            layout.setBackgroundColor(Color.parseColor("#4A4A4A"));
            amountText.setTextColor(Color.parseColor("#FFFFFF"));
            payButton.setBackground(getDrawable(R.drawable.button_bg_dark));
            cardInputWidget.setBackgroundColor(Color.parseColor("#4A4A4A"));
            titulo.setTextColor(Color.parseColor("#FFFFFF"));

        }else{
            setTheme(R.style.AppThemeDay);
            layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            cardInputWidget.setBackgroundColor(Color.parseColor("#F3B200"));
        }


        TextView coste = findViewById(R.id.pricePay);
        RoundedImageView img = findViewById(R.id.imgToPay);

        Bundle extras = getIntent().getExtras();

        titulo.setText(extras.getString("name"));
        coste.setText(extras.getString("price")+"€");
        Picasso.get().load((Uri) extras.get("img")).into(img);

        getUrl = extras.getString("fileUrl");

        amount = extras.getString("price");

        //Initialize
        stripe = new Stripe(
                getApplicationContext(),
                Objects.requireNonNull("pk_test_51IyLNeBPvY3ZNIaHRY4Dl7F6d1jE9YOoopBNG82SE3B4UxU4rHC3F6XYfTGk3yosPkmFYovwLpIecrXW8VOn1BBS00X0SxnRqA")
        );


        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get Amount
                //Log.d("TAG", "onClick: " + amountText.getText().toString());
                amountDouble = Double.valueOf(amount);
                //call checkout to get paymentIntentClientSecret key
                progressDialog.show();
                startCheckout();
            }
        });
    }

    private void startCheckout() {
        {
            // Create a PaymentIntent by calling the server's endpoint.
            MediaType mediaType = MediaType.get("application/json; charset=utf-8");
            double amount= Math.round(amountDouble * 100);

            Map<String,Object> payMap=new HashMap<>();
            Map<String,Object> itemMap=new HashMap<>();

            List<Map<String,Object>> itemList =new ArrayList<>();

            payMap.put("currency","usd");
            itemMap.put("id","objet__3D");
            itemMap.put("amount",amount);
            itemMap.put("receipt_email","samuelhueso19@gmail.com");
            itemList.add(itemMap);
            payMap.put("items",itemList);
            String json = new Gson().toJson(payMap);
            RequestBody body = RequestBody.create(json, mediaType);

            Request request = new Request.Builder()
                    .url(BACKEND_URL + "create-payment-intent")
                    .post(body)
                    .build();
            httpClient.newCall(request)
                    .enqueue(new PayCallback(this));

        }
    }

    private static final class PayCallback implements Callback {
        @NonNull
        private final WeakReference<CheckoutPayment> activityRef;
        PayCallback(@NonNull CheckoutPayment activity) {
            activityRef = new WeakReference<>(activity);
        }
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            progressDialog.dismiss();
            final CheckoutPayment activity = activityRef.get();
            if (activity == null) {
                return;
            }
            activity.runOnUiThread(() ->
                    Toast.makeText(
                            activity, "Error: Server down" , Toast.LENGTH_LONG
                    ).show()
            );
        }
        @Override
        public void onResponse(@NonNull Call call, @NonNull final Response response)
                throws IOException {
            final CheckoutPayment activity = activityRef.get();
            if (activity == null) {
                return;
            }
            if (!response.isSuccessful()) {
                activity.runOnUiThread(() ->
                        Toast.makeText(
                                activity, "Error: Server is down" , Toast.LENGTH_LONG
                        ).show()
                );
            } else {
                activity.onPaymentSuccess(response);
            }
        }
    }

    private void onPaymentSuccess(@NonNull final Response response) throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> responseMap = gson.fromJson(
                Objects.requireNonNull(response.body()).string(),
                type
        );
        paymentIntentClientSecret = responseMap.get("clientSecret");

        //once you get the payment client secret start transaction
        //get card detail
        PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
        if (params != null) {
            //now use paymentIntentClientSecret to start transaction
            ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                    .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
            //start payment
            stripe.confirmPayment(CheckoutPayment.this, confirmParams);
        }
        Log.i("TAG", "onPaymentSuccess: "+paymentIntentClientSecret);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));

    }

    private final class PaymentResultCallback
            implements ApiResultCallback<PaymentIntentResult> {
        @NonNull private final WeakReference<CheckoutPayment> activityRef;
        PaymentResultCallback(@NonNull CheckoutPayment activity) {
            activityRef = new WeakReference<>(activity);
        }
        //If Payment is successful
        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            progressDialog.dismiss();
            final CheckoutPayment activity = activityRef.get();
            if (activity == null) {
                return;
            }
            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Toast toast =Toast.makeText(activity, "Ordered Successful", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                downloadAfterPay();

                Intent intent = new Intent(getApplicationContext(),Home.class);
                startActivity(intent);


            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed – allow retrying using a different payment method
                activity.displayAlert(
                        "Payment failed",
                        Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage()
                );
            }
        }
        //If Payment is not successful
        @Override
        public void onError(@NonNull Exception e) {
            progressDialog.dismiss();
            final CheckoutPayment activity = activityRef.get();
            if (activity == null) {
                return;
            }
            // Payment request failed – allow retrying using the same payment method
            activity.displayAlert("Error", e.toString());
        }
    }
    private void displayAlert(@NonNull String title,
                              @Nullable String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message);
        builder.setPositiveButton("Ok", null);
        builder.create().show();
    }





    private void downloadAfterPay() {
        Log.d("TAG", "onClick: " + getUrl);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(getUrl));
        String title = URLUtil.guessFileName(getUrl, null, null);
        request.setTitle(title);
        request.setDescription("Descargando archivo");
        String coockie = CookieManager.getInstance().getCookie(getUrl);
        request.addRequestHeader("cookie", coockie);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "titulo");

        DownloadManager downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);

        Toast.makeText(getApplicationContext(), "Comenzando descarga", Toast.LENGTH_SHORT).show();
    }


}

