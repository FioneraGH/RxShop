package com.centling.mvp.presenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.media.ExifInterface;
import android.support.v4.content.FileProvider;
import android.util.Base64;

import com.centling.activity.BaseActivity;
import com.centling.event.UserRelationEvent;
import com.centling.mvp.contract.AvatarDetailContract;
import com.centling.mvp.model.AvatarDetailModelImpl;
import com.centling.util.SPUtil;
import com.centling.util.ShowToast;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import rx_activity_result2.Result;
import rx_activity_result2.RxActivityResult;
import timber.log.Timber;

/**
 * AvatarDetailPresenterImpl
 * Created by fionera on 2017/02/21
 */

public class AvatarDetailPresenterImpl
        implements AvatarDetailContract.Presenter {

    private Activity activity;
    private AvatarDetailContract.View mView;
    private AvatarDetailContract.Model mModel;

    private String filePath;
    private Uri photoUri;

    public AvatarDetailPresenterImpl(AvatarDetailContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void init() {
        mModel = new AvatarDetailModelImpl();

        if (mView != null) {
            mView.setPresenter(this);
            mView.onAttachPresenter();
            activity = (Activity) mView;
        }
        filePath = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/avatar.png";
        File file = new File(filePath);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            photoUri = Uri.fromFile(file);
        } else {
            photoUri = FileProvider.getUriForFile(activity,
                    activity.getPackageName() + ".FileProvider", file);
        }
        Timber.d("AVATAR CAMERA URI:" + photoUri);
    }

    @Override
    public void unInit() {
        if (mView != null) {
            ((BaseActivity) mView).dismissLoading();
            mView.onDetachPresenter();
        }
    }

    @Override
    public void tryToTakePhoto() {
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED) || photoUri == null) {
            ShowToast.show("无外部存储设备");
            return;
        }
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        RxActivityResult.on(activity).startIntent(intent).flatMap(activityResult -> {
            if (Activity.RESULT_OK != activityResult.resultCode()) {
                throw new RuntimeException("已取消");
            }
            return tryToCropPhoto(null);
        }).subscribe(activityResult -> {
            if (Activity.RESULT_OK == activityResult.resultCode() && activityResult
                    .data() != null) {
                uploadAvatarFile();
            } else {
                ShowToast.show("已取消");
            }
        }, throwable -> ShowToast.show(throwable.getMessage()));
    }

    @Override
    public void tryToSelectPhoto() {
        Intent pictures = new Intent();
        pictures.setAction(Intent.ACTION_PICK);
        pictures.setType("image/*");
        RxActivityResult.on(activity).startIntent(pictures).flatMap(activityResult -> {
            if (Activity.RESULT_OK != activityResult.resultCode() || activityResult
                    .data() == null) {
                throw new RuntimeException("已取消");
            }
            return tryToCropPhoto(activityResult.data().getData());
        }).subscribe(activityResult -> {
            if (Activity.RESULT_OK == activityResult.resultCode() && activityResult
                    .data() != null) {
                uploadAvatarFile();
            } else {
                ShowToast.show("已取消");
            }
        }, throwable -> ShowToast.show(throwable.getMessage()));
    }

    private Observable<Result<Activity>> tryToCropPhoto(Uri uri) {
        if (uri == null) {
            /*
              尝试解决图片在个别机器上图片旋转的问题
             */
            int degree = readPictureDegree(filePath);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = 2;
            Bitmap originBitmap = BitmapFactory.decodeFile(filePath, opts);

            uri = Uri.parse(MediaStore.Images.Media.insertImage(activity.getContentResolver(),
                    rotatingImageView(originBitmap, degree), null, null));
            Timber.d("AVATAR CROP URI:%s", uri);
        } else {
            Timber.d("AVATAR CROP URI:%s", uri);
        }
        return beginCrop(uri);
    }

    /**
     * 读取图片角度
     */
    private int readPictureDegree(String picturePath) {
        try {
            ExifInterface exifInterface = new ExifInterface(picturePath);
            int type = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (type) {
                case ExifInterface.ORIENTATION_NORMAL:
                    return 0;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * 旋转角度
     */
    private Bitmap rotatingImageView(Bitmap bitmap, int angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,
                true);
    }

    /**
     * 开始裁剪
     */
    private Observable<Result<Activity>> beginCrop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(filePath)));
        return RxActivityResult.on(activity).startIntent(intent);
    }

    @Override
    public void uploadAvatarFile() {
        Bitmap finalAvatar = BitmapFactory.decodeFile(filePath);
        ((BaseActivity) mView).showLoading("正在上传头像");
        Observable.create((ObservableOnSubscribe<File>) e -> {
            File file = new File(filePath);
            Timber.d(file.getAbsolutePath());
            e.onNext(file);
            e.onComplete();
        }).compose(mView.bindUntil(ActivityEvent.DESTROY)).flatMap(file -> {
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            finalAvatar.compress(Bitmap.CompressFormat.JPEG, 100, bStream);
            String base64 = Base64.encodeToString(bStream.toByteArray(), Base64.DEFAULT);
            return mModel.updateAvatar(base64);
        }).subscribe(s -> {
            ((BaseActivity) mView).dismissLoading();
            ShowToast.show("保存头像成功！");
            mView.updateAvatar(finalAvatar);
            JSONObject object = new JSONObject(s);
            JSONObject resultObj = object.getJSONObject("result");
            String url = resultObj.getString("url");
            SPUtil.setString("userAvatar", url);
            EventBus.getDefault().post(new UserRelationEvent.UpdateAvatarEvent());
        }, throwable -> {
            ((BaseActivity) mView).dismissLoading();
            ShowToast.show(throwable.getMessage());
        });
    }
}