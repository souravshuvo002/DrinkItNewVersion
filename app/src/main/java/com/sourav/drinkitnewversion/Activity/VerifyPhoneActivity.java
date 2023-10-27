package com.sourav.drinkitnewversion.Activity;

import androidx.appcompat.app.AppCompatActivity;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.sourav.drinkitnewversion.Api.ApiClient;
import com.sourav.drinkitnewversion.Api.ApiService;
import com.sourav.drinkitnewversion.Model.Result;
import com.sourav.drinkitnewversion.R;

public class VerifyPhoneActivity extends AppCompatActivity {

    private TextView logo_name;
    private TextInputEditText editTextPassword, editTextConPassword;
    private Button btn_verify;
    private String phoneNumber;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_verify_phone);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(VerifyPhoneActivity.this);
        editor = sharedPreferences.edit();

        // getting views
        logo_name = (TextView) findViewById(R.id.logo_name);
        editTextPassword = (TextInputEditText) findViewById(R.id.input_password);
        editTextConPassword = (TextInputEditText) findViewById(R.id.input_con_password);
        btn_verify = (Button) findViewById(R.id.btn_verify);

        phoneNumber = getIntent().getStringExtra("phoneNumber");

        logo_name.setText("We verified your " + phoneNumber + " number.");

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        if (editTextPassword.getText().toString().equals(editTextConPassword.getText().toString())) {
            final android.app.AlertDialog waitingDialog = new SpotsDialog(VerifyPhoneActivity.this);
            waitingDialog.show();
            waitingDialog.setMessage("Please wait ...");


            //Defining retrofit api service
            ApiService service = ApiClient.getClientVegetables().create(ApiService.class);
            Call<Result> call = service.resetPassword(phoneNumber, editTextPassword.getText().toString());

            call.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    waitingDialog.dismiss();

                    if (!response.body().getError()) {

                        editor.clear();
                        editor.commit();

                        editor.putString("USERNAME", response.body().getUser().getUsername());
                        editor.putString("PHONE", response.body().getUser().getPhone());
                        editor.putString("PASSWORD", editTextPassword.getText().toString());
                        editor.putString("USER_ID", response.body().getUser().getId());
                        editor.apply();

                        Intent intent = new Intent(VerifyPhoneActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(VerifyPhoneActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    Toast.makeText(VerifyPhoneActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Password doesn't match", Toast.LENGTH_LONG).show();
        }
    }

}