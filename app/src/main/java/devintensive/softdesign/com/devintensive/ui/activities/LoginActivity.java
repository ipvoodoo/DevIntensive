package devintensive.softdesign.com.devintensive.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import devintensive.softdesign.com.devintensive.R;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    //    TextInputLayout tilEmail;
//    TextInputLayout tilPassword;
    EditText etEmail;
    EditText etPassword;
    FloatingActionButton fabAdd;
    Button mEnterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = (EditText) findViewById(R.id.et_login);
        etPassword = (EditText) findViewById(R.id.et_password);

        fabAdd = (FloatingActionButton) findViewById(R.id.dialog_fab_add);
        fabAdd.setOnClickListener(this);

        mEnterButton = (Button) findViewById(R.id.dialog_btn_ok);
        mEnterButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_fab_add:
                etEmail.setEnabled(true);
                etEmail.setFocusable(true);
                etEmail.setFocusableInTouchMode(true);
                etEmail.requestFocus();
                etPassword.setEnabled(true);
                etPassword.setFocusable(true);
                etPassword.setFocusableInTouchMode(true);
                // TODO: 08.07.2016 Добавить действие для кнопки
                showToast("ФАБ ТЫКНУЛИ!!! УРА !!!");
                break;
            case R.id.dialog_btn_ok:
                // TODO: 08.07.2016 Добавить действие для кнопки
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

        }
    }
}
