package location.pokemon.com.pokemongolocation;

import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;

/**
 * ==================================================
 * 项目名称：PokemonGOLocation
 * 创建人：wangxiaolong
 * 创建时间：16/7/25 下午5:03
 * 备注：
 * Version：
 * ==================================================
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
}
