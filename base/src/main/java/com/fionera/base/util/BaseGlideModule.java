package com.fionera.base.util;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/**
 * BaseGlideModule
 * Created by fionera on 17-8-11 in RxShop.
 */
@GlideModule
public class BaseGlideModule
        extends AppGlideModule {

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
