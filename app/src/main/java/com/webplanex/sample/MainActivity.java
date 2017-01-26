package com.webplanex.sample;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.Window;
import android.widget.Toast;

import com.webplanex.sample.model.DemoModel;
import com.webplanex.sample.preference.PrefEntity;
import com.webplanex.sample.preference.PreferencesUtils;
import com.webplanex.sample.rest.RestClient;
import com.webplanex.sample.rest.WebService;
import com.webplanex.sample.utils.CommonUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.tv_pin)
    AppCompatTextView tvPin;
    @Bind(R.id.et_country_code)
    AppCompatEditText etCountryCode;
    @Bind(R.id.et_contact_number)
    AppCompatEditText etContactNumber;
    @Bind(R.id.btn_get_pin)
    AppCompatButton btnGetPin;

    private Dialog dialog;
    private String pin;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //initialization
        init();
    }

    /**
     * this method initialize views and variables
     */
    private void init() {

    }

    @OnClick(R.id.btn_get_pin)
    public void onClick() {

        String countryCode = etCountryCode.getText().toString().trim();
        String contactNumber = etContactNumber.getText().toString().trim();
        if (countryCode.length() == 3) {
            if (contactNumber.length() == 10) {
                //this method call demo api
                callDemoAPI(countryCode, contactNumber);
            } else {
                Toast.makeText(MainActivity.this, R.string.str_contact_number_validation, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, R.string.str_country_code_validation, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * this method call demo api
     */
    private void callDemoAPI(final String countryCode, final String contactNumber) {

        if (CommonUtils.isNetworkAvailable(MainActivity.this)) {
            showProgressDialog();
            WebService webservice = RestClient.getMyWebservice();
            Call<DemoModel> mainResponseExerciseListCall = webservice.demo(countryCode, contactNumber);
            mainResponseExerciseListCall.enqueue(new Callback<DemoModel>() {
                @Override
                public void onResponse(Call<DemoModel> call, Response<DemoModel> response) {
                    if (response.isSuccessful()) {
                        cancelProgressDialog();
                        if (response.body() != null) {
                            pin = response.body().getPin();
                            tvPin.setText(pin);
                            token = response.body().getToken();
                            PreferencesUtils.setPreferenceString(MainActivity.this, PrefEntity.TOKEN, token);
                        }
                    }
                }

                @Override
                public void onFailure(Call<DemoModel> call, Throwable t) {

                    //cancel progress dialog
                    cancelProgressDialog();

                    //request time out
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(R.string.str_time_out).setTitle(R.string.app_name)
                            .setCancelable(false)
                            .setPositiveButton(R.string.str_ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();

                                    //call api again
                                    btnGetPin.performClick();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        } else {
            CommonUtils.displayNetworkAlert(MainActivity.this, false);
        }
    }

    /**
     * this method show progress dialog
     */
    public void showProgressDialog() {
        dialog = new Dialog(MainActivity.this, R.style.TransparentProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_layout);
        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    /**
     * this function cancel Progress Dialog
     */
    public void cancelProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

}
