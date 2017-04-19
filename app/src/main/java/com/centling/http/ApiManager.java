package com.centling.http;

import android.text.TextUtils;

import com.centling.entity.BaseEntity;
import com.centling.entity.CatalogBean;
import com.centling.entity.CatalogGoodsBean;
import com.centling.entity.Empty;
import com.centling.entity.FriendBean;
import com.centling.entity.GoodsDetailBean;
import com.centling.entity.HomeBean;
import com.centling.entity.LoginBean;

import org.json.JSONObject;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * TestApiManager
 * Created by fionera on 17-2-23 in MVPPractice.
 */

public class ApiManager {

    public static Observable<HomeBean> getHomePage() {
        return Api.getInstance().getApiService().getHomePage().map(new CommonFilter<>()).compose(
                ApiManager.httpTransformer());
    }

    public static Observable<CatalogBean> getCatalogList() {
        return Api.getInstance().getApiService().getCatalogList().map(new CommonFilter<>()).compose(
                ApiManager.httpTransformer());
    }

    public static Observable<CatalogGoodsBean> getCatalogGoodsList(String gc_id, int curPage,
                                                                   int pageSize) {
        return Api.getInstance().getApiService().getCatalogGoodsList(gc_id, curPage, pageSize).map(
                new CommonFilter<>()).compose(ApiManager.httpTransformer());
    }

    public static Observable<CatalogGoodsBean> getGoodsList(String keyword, int curPage,
                                                            int pageSize, String season,
                                                            String orderKey, String order) {
        return Api.getInstance().getApiService().getGoodsList(keyword, curPage, pageSize, season,
                orderKey, order).map(new CommonFilter<>()).compose(ApiManager.httpTransformer());
    }

    public static Observable<CatalogGoodsBean> getDiscountList(int curPage, int pageSize) {
        return Api.getInstance().getApiService().getDiscountList(curPage, pageSize).map(
                new CommonFilter<>()).compose(ApiManager.httpTransformer());
    }

    public static Observable<String> getGoodsDetail(String goods_commonid, String first_goods_id,
                                                    String key) {
        return Api.getInstance().getNoGsonApiService().getGoodsDetail(goods_commonid,
                first_goods_id, key).map(new NormalFilter()).compose(ApiManager.httpTransformer());
    }

    public static Observable<Empty> getIsBirthDay(Map<String, String> info) {
        return Api.getInstance().getApiService().getIsBirthDay(info).map(new CommonFilter<>())
                .compose(ApiManager.httpTransformer());
    }

    public static Observable<Empty> addToCart(Map<String, String> info) {
        return Api.getInstance().getApiService().addToCart(info).map(new CommonFilter<>()).compose(
                ApiManager.httpTransformer());
    }

    public static Observable<Empty> addToFootprint(Map<String, String> info) {
        return Api.getInstance().getApiService().addToFootprint(info).map(new CommonFilter<>())
                .compose(ApiManager.httpTransformer());
    }

    public static Observable<Empty> addToFavorite(Map<String, String> info) {
        return Api.getInstance().getApiService().addToFavorite(info).map(new CommonFilter<>())
                .compose(ApiManager.httpTransformer());
    }

    public static Observable<String> addFriend(Map<String, String> info) {
        return Api.getInstance().getNoGsonApiService().addFriend(info).map(new NormalFilter(true))
                .compose(ApiManager.httpTransformer());
    }

    public static Observable<FriendBean> searchFriend(Map<String, String> info) {
        return Api.getInstance().getApiService().searchFriend(info).map(new CommonFilter<>())
                .compose(ApiManager.httpTransformer());
    }

    public static Observable<LoginBean> isThirdLogin(Map<String, String> info) {
        return Api.getInstance().getApiService().isThirdLogin(info).map(new CommonFilter<>())
                .compose(ApiManager.httpTransformer());
    }

    public static Observable<LoginBean> login(Map<String, String> info) {
        return Api.getInstance().getApiService().login(info).map(new CommonFilter<>())
                .compose(ApiManager.httpTransformer());
    }

    public static Observable<Empty> bindThirdLogin(Map<String, String> info) {
        return Api.getInstance().getApiService().bindThirdLogin(info).map(new CommonFilter<>())
                .compose(ApiManager.httpTransformer());
    }

    private static class NormalFilter
            implements Function<ResponseBody, String> {
        private boolean needFilter = true;

        NormalFilter() {
        }

        NormalFilter(boolean needFilter) {
            this.needFilter = needFilter;
        }

        @Override
        public String apply(ResponseBody t) throws Exception {
            String json = t.string();
            JSONObject jsonObject = new JSONObject(json);
            if (!needFilter) {
                return t.string();
            }
            if (200 != jsonObject.getInt("statusCode")) {
                throw new HttpTimeException(jsonObject.getString("statusMsg"));
            }
            return json;
        }
    }

    private static class CommonFilter<T>
            implements Function<BaseEntity<T>, T> {
        @Override
        public T apply(BaseEntity<T> t) throws Exception {
            if (200 != t.statusCode) {
                throw new HttpTimeException(t.statusMsg);
            }
            return t.result;
        }
    }

    private static class HttpTimeException
            extends RuntimeException {
        HttpTimeException(String message) {
            super(message);
        }
    }

    private static <T> ObservableTransformer<T, T> httpTransformer() {
        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(
                AndroidSchedulers.mainThread());
    }
}
