package com.centling.http;

import com.centling.entity.BaseEntity;
import com.centling.entity.CatalogBean;
import com.centling.entity.CatalogGoodsBean;
import com.centling.entity.Empty;
import com.centling.entity.FriendBean;
import com.centling.entity.GoodsDetailBean;
import com.centling.entity.HomeBean;
import com.centling.entity.LoginBean;

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
}
