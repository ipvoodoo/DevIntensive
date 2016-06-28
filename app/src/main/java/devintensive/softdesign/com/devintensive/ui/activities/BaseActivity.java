package devintensive.softdesign.com.devintensive.ui.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import devintensive.softdesign.com.devintensive.R;
import devintensive.softdesign.com.devintensive.utils.ConstantManager;

public class BaseActivity extends AppCompatActivity {
    public static final String TAG = ConstantManager.TAG_PREFIX + "BaseActivity";
    protected ProgressDialog mProgressDialog;

    //    Для показа загрузки
    public void showProgress() {
        if (mProgressDialog == null) {
//    Подгружаем кастомный стиль для диалога
            mProgressDialog = new ProgressDialog(this, R.style.custom_dialog);
//            Диалог нельзя отменить клавишей Cancel
            mProgressDialog.setCancelable(false);
//            Задаем прозрачный фон
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            Покажем диалог
            mProgressDialog.show();
//            Передаем макет диалога
            mProgressDialog.setContentView(R.layout.progress_splash);
        } else {
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.progress_splash);
        }
    }

    //    Для скрытия загрузки
    public void hideProgress() {
        if (mProgressDialog != null) {
//    Если диалог показывается в данный момент, скрываем его
            if (mProgressDialog.isShowing()) {
                mProgressDialog.hide();
            }
        }
    }

    //    Для показа ошибок
    public void showError(String message, Exception error) {
        showToast(message);
        Log.e(TAG, String.valueOf(error));
    }

    //    Для показа Тостов
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }
}
