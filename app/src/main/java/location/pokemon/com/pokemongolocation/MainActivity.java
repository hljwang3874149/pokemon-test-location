package location.pokemon.com.pokemongolocation;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import location.pokemon.com.pokemongolocation.service.WindowService;
import location.pokemon.com.pokemongolocation.utils.PermissionCheckerUtil;
import location.pokemon.com.pokemongolocation.utils.SharePrefereceManager;

public class MainActivity extends BaseActivity {

    @InjectView(R.id.longitude)
    EditText longitude;
    @InjectView(R.id.latitude)
    EditText latitude;
    @InjectView(R.id.remark)
    TextView remark;
    @InjectView(R.id.fab)
    FloatingActionButton fab;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    private String defaultLat = "41.781800", defaultLng = "123.433107"; //沈阳北站
    private final int REQUEST_CODE = 0xFF;
    private final int REQUEST_CODE_WINDOW = 0xFA;
    public static final String[] Permission = new String[]{"android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION"};
    private boolean isEnbaleLocation, isEnbaleNetTraffic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        setSupportActionBar(mToolbar);
        initView();

    }

    private void initView() {
        String mLat = SharePrefereceManager.getInstance().getLat();
        if (!TextUtils.isEmpty(mLat)) {
            defaultLat = mLat;
        }
        String mLng = SharePrefereceManager.getInstance().getLng();
        if (!TextUtils.isEmpty(mLng)) {
            defaultLng = mLng;
        }
        longitude.setText(defaultLng);
        latitude.setText(defaultLat);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(longitude.getText())) {
                    Snackbar.make(view, R.string.null_longitude, Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(latitude.getText())) {
                    Snackbar.make(view, R.string.null_latitude, Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (!isEnbaleLocation) {
                    Snackbar.make(view, R.string.open_location, Snackbar.LENGTH_SHORT)
                            .setAction(R.string.open_location, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openSettingAction();
                                }
                            }).show();
                    return;
                }
//                if (!isEnbaleNetTraffic) {
//                    Snackbar.make(view, R.string.open_nettraffic, Snackbar.LENGTH_SHORT).setAction(R.string.open_nettraffic,
//                            new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    openSettingAction();
//                                }
//                            }).show();
//                    return;
//                }
                SharePrefereceManager.getInstance().putLocation(longitude.getText().toString().trim(), latitude.getText().toString().trim
                        ());
                startService(new Intent(MainActivity.this, WindowService.class));
                finish();
            }
        });
    }

    private void openSettingAction() {
        Intent mIntent = new Intent(Settings.ACTION_SETTINGS);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isEnbaleLocation = PermissionCheckerUtil.isOnRequestPermissions(this, REQUEST_CODE, Permission);
//        isEnbaleNetTraffic = PermissionCheckerUtil.isOnRequestPermissions(this, REQUEST_CODE_WINDOW, new String[]{
//                "android.permission.SYSTEM_ALERT_WINDOW"
//        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                PermissionCheckerUtil.onRequestPermissionResult(grantResults, new PermissionCheckerUtil.onPermissionListener() {
                    @Override
                    public void onDenyListener() {
                        if (!PermissionCheckerUtil.isOnRequestPermissions(MainActivity.this, REQUEST_CODE, Permission)) {
                            Toast.makeText(MainActivity.this, R.string.open_location, Toast.LENGTH_SHORT).show();
                            openSettingAction();
                        }
                    }

                    @Override
                    public void onGrantListener() {
                        isEnbaleLocation = true;

                    }
                });
                break;
            case REQUEST_CODE_WINDOW:
                PermissionCheckerUtil.onRequestPermissionResult(grantResults, new PermissionCheckerUtil.onPermissionListener() {
                    @Override
                    public void onDenyListener() {
                        Toast.makeText(MainActivity.this, R.string.open_nettraffic, Toast.LENGTH_SHORT).show();
                        openSettingAction();
                    }

                    @Override
                    public void onGrantListener() {
                        isEnbaleNetTraffic = true;
                    }
                });
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
