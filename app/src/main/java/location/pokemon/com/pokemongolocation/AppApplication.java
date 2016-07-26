package location.pokemon.com.pokemongolocation;

import android.app.Application;

import location.pokemon.com.pokemongolocation.utils.SharePrefereceManager;

/**
 * ==================================================
 * 项目名称：PokemonGOLocation
 * 创建人：wangxiaolong
 * 创建时间：16/7/25 下午6:33
 * 备注：
 * Version：
 * ==================================================
 */
public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharePrefereceManager.initPreference(this);
    }
}
