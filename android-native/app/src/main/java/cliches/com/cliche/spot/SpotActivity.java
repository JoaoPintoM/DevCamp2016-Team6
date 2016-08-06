package cliches.com.cliche.spot;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.StringRes;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import cliches.com.cliche.R;
import cliches.com.cliche.databinding.ActivitySpotBinding;
import cliches.com.cliche.models.Spot;
import timber.log.Timber;

public class SpotActivity extends AppCompatActivity implements SpotPresenter.ViewActions {

    public static final String SPOT_KEY = "SPOT_KEY";
    private static final int REQUEST_IMAGE_CAPTURE = 42;

    private SpotPresenter mPresenter;
    private ActivitySpotBinding mViewBinding;

    private MaterialDialog mUploadingDialog;

    private File mSavedPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Spot spot = (Spot) getIntent().getSerializableExtra(SPOT_KEY);
        if(spot== null) {
            Timber.w("Trying to open SpotActivity without a spot");
            return;
        }

        mPresenter = new SpotPresenter(this, spot);
        mViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_spot);
        mViewBinding.setPresenter(mPresenter);

        setSupportActionBar(mViewBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mViewBinding.collapsingToolbar.setTitle(spot.name);
    }

    @Override
    public void openPhotoCaptureScreen() {
        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mSavedPicture = generatePictureFilename();

        if (photoIntent.resolveActivity(getPackageManager()) != null && mSavedPicture != null) {
            photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mSavedPicture));
            startActivityForResult(photoIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void showUploadingDialog() {
        if(mUploadingDialog == null) {
            mUploadingDialog = new MaterialDialog.Builder(this)
                    .content(R.string.dialog_picture_upload)
                    .progress(true, 0)
                    .progressIndeterminateStyle(true)
                    .build();
        }

        mUploadingDialog.show();
    }

    @Override
    public void hideUploadingDialog() {
        if(mUploadingDialog != null) {
            mUploadingDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE_CAPTURE
                && resultCode == RESULT_OK
                && mSavedPicture != null) {
            Timber.d("We got a picture !");

            mPresenter.sendPicture(mSavedPicture);
        }
    }

    @Override
    public void showErrror(@StringRes int errorMeassageToDisplay) {
        new MaterialDialog.Builder(this)
                .content(errorMeassageToDisplay)
                .positiveText(R.string.dialog_ok)
                .build()
                .show();
    }

    private File generatePictureFilename() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timeStamp = dateFormat.format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagePath = null;
        try {
            imagePath = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            Timber.d(e, "Could not generate file to store image");
        }

        return imagePath;
    }
}
