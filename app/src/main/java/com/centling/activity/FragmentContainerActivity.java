package com.centling.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.centling.R;

@Route(path = "/main/container")
public class FragmentContainerActivity
        extends BaseActivity {

    @Autowired
    String fragment_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        Fragment fragment = (Fragment) ARouter.getInstance().build(fragment_type).navigation();
        getSupportFragmentManager().beginTransaction().add(R.id.rl_container_base, fragment)
                .commit();
    }
}
