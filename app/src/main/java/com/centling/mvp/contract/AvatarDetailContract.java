package com.centling.mvp.contract;

import android.graphics.Bitmap;

import com.centling.mvp.BasePresenter;
import com.centling.mvp.BaseView;

import io.reactivex.Observable;

/**
 * AvatarDetailContract
 * Created by fionera on 17-2-21 in sweeping_robot.
 */

public interface AvatarDetailContract {
    interface View
            extends BaseView<Presenter> {
        void updateAvatar(Bitmap avatar);
    }

    interface Presenter
            extends BasePresenter {
        void tryToTakePhoto();

        void tryToSelectPhoto();

        void uploadAvatarFile();
    }

    interface Model {
        Observable<String> updateAvatar(String base64);
    }
}