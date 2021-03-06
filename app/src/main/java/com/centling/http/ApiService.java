package com.centling.http;

import com.centling.entity.AddressOneBean;
import com.centling.entity.BaseEntity;
import com.centling.entity.CartBean;
import com.centling.entity.CatalogBean;
import com.centling.entity.CatalogGoodsBean;
import com.centling.entity.CollectionBean;
import com.centling.entity.FootPrintBean;
import com.centling.entity.FriendBean;
import com.centling.entity.GoldsRecordBean;
import com.centling.entity.GoldsRuleBean;
import com.centling.entity.HomeBean;
import com.centling.entity.LoginBean;
import com.centling.entity.MyCustomBean;
import com.centling.entity.OrderBean;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

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

    @POST(HttpConstants.COLLECTION_LIST)
    Observable<BaseEntity<CollectionBean>> getCollectionList(@Query("curpage") int curPage,
                                                             @Query("page") int pageSize,
                                                             @Body Map<String, String> info);

    @POST(HttpConstants.CUSTOM_LIST)
    Observable<BaseEntity<MyCustomBean>> getCustomList(@Query("curpage") int curPage,
                                                       @Query("page") int pageSize,
                                                       @Body Map<String, String> info);

    @POST(HttpConstants.FOOTPRINT_LIST)
    Observable<BaseEntity<FootPrintBean>> getFootprintList(@Body Map<String, String> info);

    @GET(HttpConstants.GOODS_DETAIL)
    Observable<ResponseBody> getGoodsDetail(
            @Query("goods_commonid") String goods_commonid,
            @Query("first_goods_id") String first_goods_id,
            @Query("key") String key);

    @POST(HttpConstants.IS_GET_BIRTHDAY)
    Observable<BaseEntity<Object>> getIsBirthDay(@Body Map<String, String> info);

    @POST(HttpConstants.CART_ADD)
    Observable<BaseEntity<Object>> addToCart(@Body Map<String, String> info);

    @POST(HttpConstants.FOOTPRINT_ADD)
    Observable<BaseEntity<Object>> addToFootprint(@Body Map<String, String> info);

    @POST(HttpConstants.FAVORITE_ADD)
    Observable<BaseEntity<Object>> addToFavorite(@Body Map<String, String> info);

    @POST(HttpConstants.ADD_FRIEND)
    Observable<ResponseBody> addFriend(@Body Map<String, String> info);

    @POST(HttpConstants.SEARCH_FRIEND)
    Observable<BaseEntity<FriendBean>> searchFriend(@Body Map<String, String> info);

    @POST(HttpConstants.IS_THIRD_LOGIN)
    Observable<BaseEntity<LoginBean>> isThirdLogin(@Body Map<String, String> info);

    @POST(HttpConstants.LOGIN)
    Observable<BaseEntity<LoginBean>> login(@Body Map<String, String> info);

    @POST(HttpConstants.BIND_THIRD_LOGIN)
    Observable<BaseEntity<Object>> bindThirdLogin(@Body Map<String, String> info);

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
    Observable<BaseEntity<Object>> backGold(@Body Map<String, String> info);

    @POST(HttpConstants.ORDER_SIGN)
    Observable<ResponseBody> orderSign(@Body Map<String, String> info);

    @POST(HttpConstants.CONFIRM_DLYP)
    Observable<BaseEntity<Object>> confirmDlyp(@Body Map<String, String> info);

    @POST(HttpConstants.ORDER_CANCEL)
    Observable<BaseEntity<Object>> orderCancel(@Body Map<String, String> info);

    @POST(HttpConstants.ORDER_DELETE)
    Observable<BaseEntity<Object>> orderDelete(@Body Map<String, String> info);

    @POST(HttpConstants.ORDER_REFUND)
    Observable<BaseEntity<Object>> orderRefund(@Body Map<String, String> info);

    @POST(HttpConstants.ORDER_RECEIVE)
    Observable<BaseEntity<Object>> orderReceive(@Body Map<String, String> info);

    @POST(HttpConstants.NORMAL_CART_UPDATE)
    Observable<BaseEntity<Object>> normalCartUpdate(@Body Map<String, String> info);

    @POST(HttpConstants.CART_CLEAR)
    Observable<BaseEntity<Object>> cartClear(@Body Map<String, String> info);

    @POST(HttpConstants.CART_LIST)
    Observable<BaseEntity<CartBean>> cartList(@Body Map<String, String> info);

    @POST(HttpConstants.ORDER_CONFIRM_STEP_1)
    Observable<ResponseBody> orderConfirmStep1(@Body Map<String, String> info);

    @POST(HttpConstants.ORDER_CONFIRM_OFFPAY)
    Observable<ResponseBody> orderConfirmOffPay(@Body Map<String, String> info);

    @POST(HttpConstants.ORDER_CONFIRM_GENERAL)
    Observable<ResponseBody> orderConfirmGeneral(@Body Map<String, String> info);

    @POST(HttpConstants.ORDER_CONFIRM_BIRTHDAY)
    Observable<ResponseBody> orderConfirmBirthDay(@Body Map<String, String> info);

    @POST(HttpConstants.ORDER_CONFIRM_CUSTOME)
    Observable<ResponseBody> orderConfirmCustom(@Body Map<String, String> info);

    @POST(HttpConstants.ORDER_CONFIRM_NEWTRY)
    Observable<ResponseBody> orderConfirmNewTry(@Body Map<String, String> info);

    @POST(HttpConstants.CHANGE_USER_AVATAR)
    Observable<ResponseBody> changeUserAvatar(@Body Map<String, String> info);

    @POST(HttpConstants.GOLD_BALANCE)
    Observable<ResponseBody> goldBalance(@Body Map<String, String> info);

    @POST(HttpConstants.GOLD_RECHARGE)
    Observable<ResponseBody> goldRecharge(@Body Map<String, Object> info);

    @POST(HttpConstants.GOLD_RECORD_LIST)
    Observable<BaseEntity<GoldsRecordBean>> goldRecordList(@Body Map<String, String> info);

    @POST(HttpConstants.GOLD_RULES)
    Observable<BaseEntity<GoldsRuleBean>> goldRules(@Body Map<String, String> info);

    @POST(HttpConstants.ADD_ADDRESS)
    Observable<BaseEntity<AddressOneBean>> addAddress(@Body Map<String, String> info);

    @POST(HttpConstants.DEL_ADDRESS)
    Observable<BaseEntity<Object>> delAddress(@Body Map<String, String> info);

    @POST(HttpConstants.CLEAR_FOOTPRINT)
    Observable<BaseEntity<Object>> clearFootprint(@Body Map<String, String> info);

    @POST(HttpConstants.COLLECTION_DEL)
    Observable<BaseEntity<Object>> collectionDel(@Body Map<String, String> info);

    @POST(HttpConstants.CUSTOM_DELETE)
    Observable<BaseEntity<Object>> customDelete(@Body Map<String, String> info);

    @POST(HttpConstants.CHANGE_USER_INFO)
    Observable<BaseEntity<Object>> changeUserInfo(@Body Map<String, String> info);
}
