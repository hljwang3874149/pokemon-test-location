package location.pokemon.com.pokemongolocation.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * ==================================================
 * 项目名称：PokemonGOLocation
 * 创建人：wangxiaolong
 * 创建时间：16/7/25 下午6:19
 * 备注：
 * Version：
 * ==================================================
 */
public class SharePrefereceManager {
    private static SharedPreferences mPreferences;
    private static final String KEY_NAME = "location";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LNG = "lng";
    private volatile static SharePrefereceManager mInstance;
    private String mLng, mLat;

    public SharePrefereceManager(Context mCtx) {
        mPreferences = mCtx.getSharedPreferences(KEY_NAME, Context.MODE_PRIVATE);
    }

    public static void initPreference(Context mCtx) {
        mInstance = new SharePrefereceManager(mCtx);
    }

    public  static SharePrefereceManager getInstance() {
        return mInstance;
    }

    public void putLng(String lng) {
        mLng = lng;
        mPreferences.edit().putString(KEY_LNG, lng).apply();
    }

    public void putLat(String lat) {
        mLat = lat;
        mPreferences.edit().putString(KEY_LAT, lat).apply();
    }

    public String getLng() {
        if (TextUtils.isEmpty(mLng)) {
            mLng = mPreferences.getString(KEY_LNG, "");
        }
        return mLng;
    }

    public String getLat() {
        if (TextUtils.isEmpty(mLat)) {
            mLat = mPreferences.getString(KEY_LAT, "");
        }
        return mLat;
    }

    public void putLocation(String lng ,String lat) {
        putLat(lat);
        putLng(lng);
    }


}
