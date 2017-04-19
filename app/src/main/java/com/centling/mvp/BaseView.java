package com.centling.mvp;

/**
 * BaseView
 * Created by fionera on 17-2-8 in sweeping_robot.
 */

public interface BaseView<P extends BasePresenter> extends BindHelper {
    void setPresenter(P presenter);

    void onAttachPresenter();

    void onDetachPresenter();
}
