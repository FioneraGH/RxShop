package com.centling.http;

import com.centling.entity.AddressOneBean;
import com.centling.entity.BaseEntity;
import com.centling.entity.CartBean;
import com.centling.entity.CatalogBean;
import com.centling.entity.CatalogGoodsBean;
import com.centling.entity.CollectionBean;
import com.centling.entity.FootPrintBean;
import com.centling.entity.FriendBean;
import com.centling.entity.HomeBean;
import com.centling.entity.LoginBean;
import com.centling.entity.MyCustomBean;
import com.centling.entity.OrderBean;

import org.json.JSONObject;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.http.Body;

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

    public static Observable<OrderBean> getOrderList(int curPage, int pageSize,
                                                     Map<String, String> info) {
        return Api.getInstance().getApiService().getOrderList(curPage, pageSize, info).map(
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

    public static Observable<CollectionBean> getCollectionList(int curPage, int pageSize,
                                                               Map<String, String> info) {
        return Api.getInstance().getApiService().getCollectionList(curPage, pageSize, info).map(
                new CommonFilter<>()).compose(ApiManager.httpTransformer());
    }

    public static Observable<MyCustomBean> getCustomList(int curPage, int pageSize,
                                                         Map<String, String> info) {
        return Api.getInstance().getApiService().getCustomList(curPage, pageSize, info).map(
                new CommonFilter<>()).compose(ApiManager.httpTransformer());
    }

    public static Observable<FootPrintBean> getFootprintList(Map<String, String> info) {
        return Api.getInstance().getApiService().getFootprintList(info).map(new CommonFilter<>())
                .compose(ApiManager.httpTransformer());
    }

    public static Observable<String> getGoodsDetail(String goods_commonid, String first_goods_id,
                                                    String key) {
        return Api.getInstance().getNoGsonApiService().getGoodsDetail(goods_commonid,
                first_goods_id, key).map(new NormalFilter()).compose(ApiManager.httpTransformer());
    }

    public static Observable<Object> getIsBirthDay(Map<String, String> info) {
        return Api.getInstance().getApiService().getIsBirthDay(info).map(new CommonFilter<>())
                .compose(ApiManager.httpTransformer());
    }

    public static Observable<Object> addToCart(Map<String, String> info) {
        return Api.getInstance().getApiService().addToCart(info).map(new CommonFilter<>()).compose(
                ApiManager.httpTransformer());
    }

    public static Observable<Object> addToFootprint(Map<String, String> info) {
        return Api.getInstance().getApiService().addToFootprint(info).map(new CommonFilter<>())
                .compose(ApiManager.httpTransformer());
    }

    public static Observable<Object> addToFavorite(Map<String, String> info) {
        return Api.getInstance().getApiService().addToFavorite(info).map(new CommonFilter<>())
                .compose(ApiManager.httpTransformer());
    }

    public static Observable<String> orderSign(Map<String, String> info) {
        return Api.getInstance().getApiService().orderSign(info).map(new NormalFilter()).compose(
                ApiManager.httpTransformer());
    }

    public static Observable<Object> backGold(Map<String, String> info) {
        return Api.getInstance().getApiService().backGold(info).map(new CommonFilter<>()).compose(
                ApiManager.httpTransformer());
    }

    public static Observable<Object> confirmDlyp(Map<String, String> info) {
        return Api.getInstance().getApiService().confirmDlyp(info).map(new CommonFilter<>())
                .compose(ApiManager.httpTransformer());
    }

    public static Observable<Object> orderCancel(Map<String, String> info) {
        return Api.getInstance().getApiService().orderCancel(info).map(new CommonFilter<>())
                .compose(ApiManager.httpTransformer());
    }

    public static Observable<Object> orderDelete(Map<String, String> info) {
        return Api.getInstance().getApiService().orderDelete(info).map(new CommonFilter<>())
                .compose(ApiManager.httpTransformer());
    }

    public static Observable<Object> orderRefund(Map<String, String> info) {
        return Api.getInstance().getApiService().orderRefund(info).map(new CommonFilter<>())
                .compose(ApiManager.httpTransformer());
    }

    public static Observable<Object> orderReceive(Map<String, String> info) {
        return Api.getInstance().getApiService().orderReceive(info).map(new CommonFilter<>())
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
        return Api.getInstance().getApiService().login(info).map(new CommonFilter<>()).compose(
                ApiManager.httpTransformer());
    }

    public static Observable<Object> bindThirdLogin(Map<String, String> info) {
        return Api.getInstance().getApiService().bindThirdLogin(info).map(new CommonFilter<>())
                .compose(ApiManager.httpTransformer());
    }

    public static Observable<String> fetchMemberPoint(Map<String, String> info) {
        return Api.getInstance().getNoGsonApiService().fetchMemberPoint(info).map(
                new NormalFilter()).compose(ApiManager.httpTransformer());
    }

    public static Observable<String> trackUnreadMsg(Map<String, String> info) {
        return Api.getInstance().getNoGsonApiService().trackUnreadMsg(info).map(new NormalFilter())
                .compose(ApiManager.httpTransformer());
    }

    public static Observable<String> isVip(Map<String, String> info) {
        return Api.getInstance().getNoGsonApiService().isVip(info).map(new NormalFilter()).compose(
                ApiManager.httpTransformer());
    }

    public static Observable<String> orderPrepay(Map<String, String> info) {
        return Api.getInstance().getNoGsonApiService().orderPrepay(info).map(new NormalFilter())
                .compose(ApiManager.httpTransformer());
    }

    public static Observable<String> orderPayAgain(Map<String, String> info) {
        return Api.getInstance().getNoGsonApiService().orderPayAgain(info).map(new NormalFilter())
                .compose(ApiManager.httpTransformer());
    }

    public static Observable<String> orderSignWx(Map<String, Object> info) {
        return Api.getInstance().getNoGsonApiService().orderSignWx(info).map(new NormalFilter())
                .compose(ApiManager.httpTransformer());
    }

    public static Observable<Object> normalCartUpdate(Map<String, String> info) {
        return Api.getInstance().getApiService().normalCartUpdate(info).map(new CommonFilter<>())
                .compose(ApiManager.httpTransformer());
    }

    public static Observable<Object> cartClear(Map<String, String> info) {
        return Api.getInstance().getApiService().cartClear(info).map(new CommonFilter<>()).compose(
                ApiManager.httpTransformer());
    }

    public static Observable<CartBean> cartList(Map<String, String> info) {
        return Api.getInstance().getApiService().cartList(info).map(new CommonFilter<>()).compose(
                ApiManager.httpTransformer());
    }

    public static Observable<String> orderConfirmStep1(Map<String, String> info) {
        return Api.getInstance().getNoGsonApiService().orderConfirmStep1(info).map(
                new NormalFilter()).compose(ApiManager.httpTransformer());
    }

    public static Observable<String> orderConfirmOffPay(Map<String, String> info) {
        return Api.getInstance().getNoGsonApiService().orderConfirmOffPay(info).map(
                new NormalFilter()).compose(ApiManager.httpTransformer());
    }

    public static Observable<String> orderConfirmGeneral(Map<String, String> info) {
        return Api.getInstance().getNoGsonApiService().orderConfirmGeneral(info).map(
                new NormalFilter()).compose(ApiManager.httpTransformer());
    }

    public static Observable<String> orderConfirmBirthDay(Map<String, String> info) {
        return Api.getInstance().getNoGsonApiService().orderConfirmBirthDay(info).map(
                new NormalFilter()).compose(ApiManager.httpTransformer());
    }

    public static Observable<String> orderConfirmCustom(Map<String, String> info) {
        return Api.getInstance().getNoGsonApiService().orderConfirmCustom(info).map(
                new NormalFilter()).compose(ApiManager.httpTransformer());
    }

    public static Observable<String> orderConfirmNewTry(Map<String, String> info) {
        return Api.getInstance().getNoGsonApiService().orderConfirmNewTry(info).map(
                new NormalFilter()).compose(ApiManager.httpTransformer());
    }

    public static Observable<String> changeUserAvatar(Map<String, String> info) {
        return Api.getInstance().getNoGsonApiService().changeUserAvatar(info).map(
                new NormalFilter()).compose(ApiManager.httpTransformer());
    }

    public static Observable<AddressOneBean> addAddress(Map<String, String> info) {
        return Api.getInstance().getApiService().addAddress(info).map(new CommonFilter<>()).compose(
                ApiManager.httpTransformer());
    }

    public static Observable<Object> delAddress(Map<String, String> info) {
        return Api.getInstance().getApiService().delAddress(info).map(new CommonFilter<>()).compose(
                ApiManager.httpTransformer());
    }

    public static Observable<Object> clearFootprint(Map<String, String> info) {
        return Api.getInstance().getApiService().clearFootprint(info).map(new CommonFilter<>())
                .compose(ApiManager.httpTransformer());
    }

    public static Observable<Object> collectionDel(Map<String, String> info) {
        return Api.getInstance().getApiService().collectionDel(info).map(new CommonFilter<>())
                .compose(ApiManager.httpTransformer());
    }

    public static Observable<Object> customDelete(Map<String, String> info) {
        return Api.getInstance().getApiService().customDelete(info).map(new CommonFilter<>())
                .compose(ApiManager.httpTransformer());
    }

    public static Observable<Object> changeUserInfo(Map<String, String> info) {
        return Api.getInstance().getApiService().changeUserInfo(info).map(new CommonFilter<>())
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
            if (!needFilter) {
                return json;
            }
            JSONObject jsonObject = new JSONObject(json);
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
