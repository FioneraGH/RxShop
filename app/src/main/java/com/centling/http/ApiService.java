package com.centling.http;

import com.centling.entity.BaseEntity;
import com.centling.entity.CatalogBean;
import com.centling.entity.CatalogGoodsBean;
import com.centling.entity.Empty;
import com.centling.entity.FriendBean;
import com.centling.entity.GoodsDetailBean;
import com.centling.entity.HomeBean;
import com.centling.entity.LoginBean;
import com.centling.entity.OrderBean;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryName;

/**
 * ApiService
 */

public interface ApiService {
    @GET(HttpConstants.HOME_PAGE)
    Observable<BaseEntity<HomeBean>> getHomePage();

    @GET(HttpConstants.CATALOG_LIST)
    Observable<BaseEntity<CatalogBean>> getCatalogList();

    @GET(HttpConstants.CATALOG_GOODS_LIST)
    Observable<BaseEntity<CatalogGoodsBean>> getCatalogGoodsList(@Query("gc_id") String gc_id,
                                                                 @Query("curpage") int curPage,
                                                                 @Query("page") int pageSize);

    @POST(HttpConstants.ORDER_LIST)
    Observable<BaseEntity<OrderBean>> getOrderList(@Query("curpage") int curPage,
                                                   @Query("page") int pageSize,
                                                   @Body Map<String, String> info);

    @GET(HttpConstants.GOODS_LIST)
    Observable<BaseEntity<CatalogGoodsBean>> getGoodsList(@Query(value = "keyword",encoded = true) String keyword,
                                                          @Query("curpage") int curPage,
                                                          @Query("page") int pageSize,
                                                          @Query("season") String season,
                                                          @Query("orderKey") String orderKey,
                                                          @Query("order") String order);

    @GET(HttpConstants.DISCOUNT_LIST)
    Observable<BaseEntity<CatalogGoodsBean>> getDiscountList(@Query("curpage") int curPage,
                                                             @Query("page") int pageSize);

    @GET(HttpConstants.GOODS_DETAIL)
    Observable<ResponseBody> getGoodsDetail(
            @Query("goods_commonid") String goods_commonid,
            @Query("first_goods_id") String first_goods_id,
            @Query("key") String key);

    @POST(HttpConstants.IS_GET_BIRTHDAY)
    Observable<BaseEntity<Empty>> getIsBirthDay(@Body Map<String, String> info);

    @POST(HttpConstants.CART_ADD)
    Observable<BaseEntity<Empty>> addToCart(@Body Map<String, String> info);

    @POST(HttpConstants.FOOTPRINT_ADD)
    Observable<BaseEntity<Empty>> addToFootprint(@Body Map<String, String> info);

    @POST(HttpConstants.FAVORITE_ADD)
    Observable<BaseEntity<Empty>> addToFavorite(@Body Map<String, String> info);

    @POST(HttpConstants.ADD_FRIEND)
    Observable<ResponseBody> addFriend(@Body Map<String, String> info);

    @POST(HttpConstants.SEARCH_FRIEND)
    Observable<BaseEntity<FriendBean>> searchFriend(@Body Map<String, String> info);

    @POST(HttpConstants.IS_THIRD_LOGIN)
    Observable<BaseEntity<LoginBean>> isThirdLogin(@Body Map<String, String> info);

    @POST(HttpConstants.LOGIN)
    Observable<BaseEntity<LoginBean>> login(@Body Map<String, String> info);

    @POST(HttpConstants.BIND_THIRD_LOGIN)
    Observable<BaseEntity<Empty>> bindThirdLogin(@Body Map<String, String> info);

    @POST(HttpConstants.FETCH_MEMBER_POINTS)
    Observable<ResponseBody> fetchMemberPoint(@Body Map<String, String> info);

    @POST(HttpConstants.TRACK_UNREAD_MSG)
    Observable<ResponseBody> trackUnreadMsg(@Body Map<String, String> info);

    @POST(HttpConstants.IS_VIP)
    Observable<ResponseBody> isVip(@Body Map<String, String> info);

    @POST(HttpConstants.ORDER_PREPAY)
    Observable<ResponseBody> orderPrepay(@Body Map<String, String> info);

    @POST(HttpConstants.ORDER_PAY_AGAIN)
    Observable<ResponseBody> orderPayAgain(@Body Map<String, String> info);

    @POST(HttpConstants.ORDER_SIGN_WX)
    Observable<ResponseBody> orderSignWx(@Body Map<String, Object> info);

    @POST(HttpConstants.BACK_GOLD)
    Observable<BaseEntity<Empty>> backGold(@Body Map<String, String> info);

    @POST(HttpConstants.ORDER_SIGN)
    Observable<ResponseBody> orderSign(@Body Map<String, String> info);

    @POST(HttpConstants.CONFIRM_DLYP)
    Observable<BaseEntity<Empty>> confirmDlyp(@Body Map<String, String> info);

    @POST(HttpConstants.ORDER_CANCEL)
    Observable<BaseEntity<Empty>> orderCancel(@Body Map<String, String> info);

    @POST(HttpConstants.ORDER_DELETE)
    Observable<BaseEntity<Empty>> orderDelete(@Body Map<String, String> info);

    @POST(HttpConstants.ORDER_REFUND)
    Observable<BaseEntity<Empty>> orderRefund(@Body Map<String, String> info);

    @POST(HttpConstants.ORDER_RECEIVE)
    Observable<BaseEntity<Empty>> orderReceive(@Body Map<String, String> info);
}
