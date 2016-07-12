package devintensive.softdesign.com.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

import devintensive.softdesign.com.devintensive.R;
import devintensive.softdesign.com.devintensive.data.managers.DataManager;
import devintensive.softdesign.com.devintensive.data.network.req.UserLoginReq;
import devintensive.softdesign.com.devintensive.data.network.res.UserModelRes;
import devintensive.softdesign.com.devintensive.utils.NetworkStatusChecker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    //    TextInputLayout tilEmail;
//    TextInputLayout tilPassword;
    private EditText mLogin;
    private EditText mPassword;
    private FloatingActionButton fabAdd;
    private Button mEnterButton;
    private TextView mRememberPassword;
    private CoordinatorLayout mCoordinatorLayout;

    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mDataManager = DataManager.getINSTANCE();

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.login_coordinator_container);

        mLogin = (EditText) findViewById(R.id.et_login);
        mPassword = (EditText) findViewById(R.id.et_password);

        fabAdd = (FloatingActionButton) findViewById(R.id.login_fab_add);
        fabAdd.setOnClickListener(this);

        mEnterButton = (Button) findViewById(R.id.login_btn_ok);
        mEnterButton.setOnClickListener(this);

        mRememberPassword = (TextView) findViewById(R.id.login_tv_remember_password);
        mRememberPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_fab_add:
                mLogin.setEnabled(true);
                mLogin.setFocusable(true);
                mLogin.setFocusableInTouchMode(true);
                mLogin.requestFocus();
                mPassword.setEnabled(true);
                mPassword.setFocusable(true);
                mPassword.setFocusableInTouchMode(true);
                // TODO: 08.07.2016 Добавить действие для кнопки
                showToast("ФАБ ТЫКНУЛИ!!! УРА !!!");
                break;
            case R.id.login_btn_ok:
                signIn();
                break;
            case R.id.login_tv_remember_password:
                rememberPassword();
                break;

        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void rememberPassword() {
        Intent rememberIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(rememberIntent);
    }

    private void loginSuccess(UserModelRes userModel) {

        showSnackbar(userModel.getData().getToken());

        mDataManager.getPreferencesManager().saveAuthToken(userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveUserId(userModel.getData().getUser().getId());
        saveUserValues(userModel);

        Intent loginIntent = new Intent(this, MainActivity.class);
        startActivity(loginIntent);
    }

    private void signIn() {
        if (NetworkStatusChecker.isNetworkAviable(this)) {
            Call<UserModelRes> call = mDataManager.loginUser(new Date().toString(),new UserLoginReq(mLogin.getText().toString(), mPassword.getText().toString()));
            call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                    if (response.code() == 200) {
                        loginSuccess(response.body());
                    } else if (response.code() == 403) {
                        showSnackbar("Неверный логин или пароль!");
                    } else {
                        showSnackbar("Что-то пошло не так");
                    }
                }

                @Override
                public void onFailure(Call<UserModelRes> call, Throwable t) {
                    // TODO: 11.07.2016 обработать ошибки
                }
            });
        } else {
            showSnackbar("Сеть в данный момент не доступна, попробуйте позже!");
        }
    }

    private void saveUserValues(UserModelRes userModel) {
        int[] userValues = {
                userModel.getData().getUser().getProfileValues().getRating(),
                userModel.getData().getUser().getProfileValues().getLineCode(),
                userModel.getData().getUser().getProfileValues().getProjects(),
        };

        mDataManager.getPreferencesManager().saveUserProfileValues(userValues);
    }
}
