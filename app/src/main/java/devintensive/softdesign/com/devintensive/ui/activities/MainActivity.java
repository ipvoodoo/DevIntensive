package devintensive.softdesign.com.devintensive.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import devintensive.softdesign.com.devintensive.R;
import devintensive.softdesign.com.devintensive.data.managers.DataManager;
import devintensive.softdesign.com.devintensive.utils.ConstantManager;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";

    private int mCurrentEditMode = 0;

    private DataManager mDataManager;

    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private FloatingActionButton mFab;
    private RelativeLayout mProfilePlaceholder;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout mAppBarLayout;
    private ImageView mProfileImage;

    private EditText mUserPhone;
    private EditText mUserMail;
    private EditText mUserVk;
    private EditText mUserGit;
    private EditText mUserBio;

    private ImageView mCall;
    private ImageView mSendMail;
    private ImageView mVk;
    private ImageView mGit;

    private LinearLayout mll;

    private List<EditText> mUserInfoViews;

    private TextView mUserValueRating;
    private TextView mUserValueCodeLines;
    private TextView mUserValueProjects;
    private List<TextView> mUserValuesViews;

    private AppBarLayout.LayoutParams mAppBarParams = null;
    private File mPhotoFile = null;
    private Uri mSelectedImage = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");

        mDataManager = DataManager.getINSTANCE();

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mProfilePlaceholder = (RelativeLayout) findViewById(R.id.profile_placeholder);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        mProfileImage = (ImageView) findViewById(R.id.user_photo_img);
        mll = (LinearLayout) findViewById(R.id.user_info_wrapper);

        mUserPhone = (EditText) findViewById(R.id.et_user_phone);
        mUserMail = (EditText) findViewById(R.id.et_user_mail);
        mUserVk = (EditText) findViewById(R.id.et_user_Vk);
        mUserGit = (EditText) findViewById(R.id.et_user_git);
        mUserBio = (EditText) findViewById(R.id.et_user_about);

        mUserValueRating = (TextView) findViewById(R.id.tv_user_rating);
        mUserValueCodeLines = (TextView) findViewById(R.id.tv_user_code_lines);
        mUserValueProjects = (TextView) findViewById(R.id.tv_user_projects);

        mUserInfoViews = new ArrayList<>();
        mUserInfoViews.add(mUserPhone);
        mUserInfoViews.add(mUserMail);
        mUserInfoViews.add(mUserVk);
        mUserInfoViews.add(mUserGit);
        mUserInfoViews.add(mUserBio);

        mUserValuesViews = new ArrayList<>();
        mUserValuesViews.add(mUserValueRating);
        mUserValuesViews.add(mUserValueCodeLines);
        mUserValuesViews.add(mUserValueProjects);

        mCall = (ImageView) findViewById(R.id.call_btn);
        mSendMail = (ImageView) findViewById(R.id.send_mail_btn);
        mVk = (ImageView) findViewById(R.id.open_vk_btn);
        mGit = (ImageView) findViewById(R.id.open_git_btn);

        mFab.setOnClickListener(this);
        mProfilePlaceholder.setOnClickListener(this);
        mCall.setOnClickListener(this);
        mSendMail.setOnClickListener(this);
        mVk.setOnClickListener(this);
        mGit.setOnClickListener(this);

        setupToolbar();
        setupDrawer();
        initUserFields();
        initUserInfoValue();


        Picasso.with(this)
                .load(mDataManager.getPreferencesManager().loadUserPhoto())
                .placeholder(R.drawable.userphoto)// TODO: 05.07.2016 добавить placeholder  и трансформ + crop
                .into(mProfileImage);

//        List<String> test = mDataManager.getPreferencesManager().loadUserProfileData();
//
//        mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
//        changeEditMode(mCurrentEditMode);


        if (savedInstanceState == null) {
//            первый запуск
            showSnackBar("Активити запущено впервые");
        } else {
//            уже запускалось
//            showSnackBar("Активити уже запускалось");
//            showToast("Активити уже запускалось");
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
            changeEditMode(mCurrentEditMode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConstantManager.REQUEST_GALLERY_PICTURE:
                if (resultCode == RESULT_OK && data != null) {
                    mSelectedImage = data.getData();

                    insertProfileImage(mSelectedImage);
                }
                break;
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if (resultCode == RESULT_OK && mPhotoFile != null) {
                    mSelectedImage = Uri.fromFile(mPhotoFile);

                    insertProfileImage(mSelectedImage);
                }
        }
    }

    private void insertProfileImage(Uri selectedImage) {
        Picasso.with(this)
                .load(selectedImage)
                .into(mProfileImage);
        mDataManager.getPreferencesManager().saveUserPhoto(selectedImage);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
//            Дровер вылезет слева
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        saveUserFields();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: ");
        outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);

    }

    public void runWithDelay() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

//                Выполнение с зедаержкой
                hideProgress();
            }
        }, 5000);
    }

    private void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
//        Находим Actionbar
        ActionBar actionBar = getSupportActionBar();

        mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbarLayout.getLayoutParams();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackBar(item.getTitle().toString());
                item.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        String uri;
        String subject;
        String body;
        switch (v.getId()) {
            case R.id.fab:
//                showSnackBar("click");
                if (mCurrentEditMode == 0) {
                    changeEditMode(1);
                    mCurrentEditMode = 1;
                } else {
                    changeEditMode(0);
                    mCurrentEditMode = 0;
                }
                break;
            case R.id.profile_placeholder:
                // TODO: 05.07.2016 сделать выбор откуда загружат фотки
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
                break;
            case R.id.call_btn:
                intent.setAction(Intent.ACTION_DIAL);
                startActivityForResult(intent, ConstantManager.DIAL_KEY);
                break;
            case R.id.send_mail_btn:
                uri = mUserMail.getText().toString();
                subject = "Subject";
                body = "E-mail body";
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{uri});
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, body);
                intent.setType("message/rfc822");
                startActivityForResult(Intent.createChooser(intent, getResources().getText(R.string.select_email_client)), ConstantManager.EMAIL_KEY);
                break;
            case R.id.open_vk_btn:
                uri = "https://vk.com/ciklodol";
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(uri));
                startActivityForResult(intent, ConstantManager.VK_KEY);
                break;
            case R.id.open_git_btn:
                uri = "https://github.com/ipvoodoo";
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(uri));
                startActivityForResult(intent, ConstantManager.GIT_KEY);
                break;
        }
    }

    /*переключает режим реактирования
    * @param mode если true режим редактирования,  если false режим просмотра
    * userValue.setFocusableInTouchMode(true) - при касании запускает режим режактирования;
*/
    private void changeEditMode(int mode) {
        if (mode == 1) {
            mFab.setImageResource(R.drawable.ic_done_black_24dp);
            for (EditText userValue : mUserInfoViews) {

                userValue.setEnabled(true);
                userValue.setFocusable(true);
                userValue.setFocusableInTouchMode(true);

                showProfilePlaceholder();
                lockToolbar();
//                Делаем невидимым тект в режиме редактирования
                mCollapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);

//                Ставим фоукс на поле с телефоном
                mUserPhone.requestFocus();
                // TODO: 08.07.2016 разобрать с форматом телефонной зоны 
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    PhoneNumberUtils.formatNumber(mUserPhone.getText().toString(), "RU");
                }
            }
        } else {
            mFab.setImageResource(R.drawable.ic_mode_edit_black_24dp);
            for (EditText userValue : mUserInfoViews) {
                userValue.setEnabled(false);
                userValue.setFocusable(false);
                userValue.setFocusableInTouchMode(false);

                hideProfilePlaceholder();
                unlockToolbar();
//                Возвращаем тексту белый цвет
                mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white));

                saveUserFields();
            }
        }
    }

    private void initUserFields() {
        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileData();
        for (int i = 0; i < userData.size(); i++) {
            mUserInfoViews.get(i).setText(userData.get(i));
        }
    }

    private void saveUserFields() {
        List<String> userData = new ArrayList<>();
        for (EditText userFieldView : mUserInfoViews) {
            userData.add(userFieldView.getText().toString());
        }
        mDataManager.getPreferencesManager().saveUserProfileData(userData);
    }

    private void initUserInfoValue() {
        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileValues();
        for (int i = 0; i < userData.size(); i++) {
                                           mUserValuesViews.get(i).setText(userData.get(i));
        }
    }


    //    Кнопка Назад
    @Override
    public void onBackPressed() {
        assert mNavigationDrawer != null;
        mNavigationDrawer.closeDrawer(GravityCompat.START);
    }

    private void loadPhotoFromGallery() {
        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        takeGalleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(takeGalleryIntent, getString(R.string.user_profile_chooser_message)), ConstantManager.REQUEST_GALLERY_PICTURE);

    }

    private void loadPhotoFromCamera() {
//        Проверяем разрешения на запись
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager
                .PERMISSION_GRANTED) {
            //        Интент для получения фотографии
            Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                // TODO: 05.07.2016 Обработать ошибку
            }
            if (mPhotoFile != null) {
                // TODO: 05.07.2016 передать фото в интент
//        Uri.fromFile(mPhotoFile) - расположение файла с фоткой
                takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, ConstantManager.CAMERA_REQUEST_PERMISSION_CODE);
            Snackbar.make(mCoordinatorLayout, "Необходимо дать разрешения для корректной работы", Snackbar.LENGTH_LONG)
                    .setAction("Разрешить", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openApplicationSettings();
                        }
                    }).show();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ConstantManager.CAMERA_REQUEST_PERMISSION_CODE && grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // TODO: 05.07.2016 обработка разрешений 
            }
        }
        if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            // TODO: 05.07.2016 обработка разрешений
        }

    }

    private void hideProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.GONE);
    }

    private void showProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.VISIBLE);
    }

    private void lockToolbar() {
//       setExpanded(...) - служит для указания полложения Аппбара свернут/развернут с анимаций/или без
        mAppBarLayout.setExpanded(true, true);
//        CollapsingToolbar не будет схлопываться при setScrollFlags(0)
        mAppBarParams.setScrollFlags(0);
        mCollapsingToolbarLayout.setLayoutParams(mAppBarParams);
    }

    private void unlockToolbar() {
        //        CollapsingToolbar не будет схлопываться при этих скроллфлагах
        mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        mCollapsingToolbarLayout.setLayoutParams(mAppBarParams);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case ConstantManager.LOAD_PROFILE_PHOTO:
                String[] selectItems = {getString(R.string.user_profile_dialog_gallery),
                        getString(R.string.user_profile_dialog_camera),
                        getString(R.string.user_profile_dialog_cancel)};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.user_profile_dialog_title));
                builder.setItems(selectItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int choiceItem) {
                        switch (choiceItem) {
                            case 0:
                                // TODO: 05.07.2016 Загрузить из галлереи
                                loadPhotoFromGallery();
                                showSnackBar("Загрузить из галлереи");
                                break;
                            case 1:
                                loadPhotoFromCamera();
                                // TODO: 05.07.2016 Загрузить с камеры
//                                showSnackBar("Загрузить из камеры");
                                break;
                            case 2:
                                dialog.cancel();
                                // TODO: 05.07.2016 Отмена
//                                showSnackBar("Отмена");
                                break;
                        }
                    }
                });
                return builder.create();
//            Вернем null, если диалог не создался
            default:
                return null;
        }
    }

    //        Для записи Bitmap от камеры
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        Получаем путь к файлу
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, image.getAbsolutePath());

//        Сообщаем галлереи , что появилась новая запись с фотографией
        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        return image;
    }

    public void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));

        startActivityForResult(appSettingsIntent, ConstantManager.PERMISSION_REQUEST_SETTINGS_CODE);
    }


}
