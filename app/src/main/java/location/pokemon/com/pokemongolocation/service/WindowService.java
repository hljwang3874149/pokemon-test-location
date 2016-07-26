package location.pokemon.com.pokemongolocation.service;

import android.Manifest;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import location.pokemon.com.pokemongolocation.R;
import location.pokemon.com.pokemongolocation.utils.SharePrefereceManager;

public class WindowService extends Service implements LocationListener {


    @InjectView(R.id.float_id)
    RelativeLayout mFloatView;
    @InjectView(R.id.info)
    TextView mInfo;
    private double latitude = 31.3029742001D;
    private LocationManager locationManager;
    private double longitude = 120.60971259D;
    private LinearLayout mFloatLayout;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams wmParams;
    private final String GPS = "gps";
    private double defaultMoveValue = 0.0002d;

    @Override
    public void onCreate() {
        super.onCreate();
        createFloatView();
        initLocation();
    }

    private void createFloatView() {
        this.wmParams = new WindowManager.LayoutParams();
        Application localApplication = getApplication();
        getApplication();
        this.mWindowManager = ((WindowManager) localApplication.getSystemService(Context.WINDOW_SERVICE));
        //设置window type
        this.wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明
        this.wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        this.wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        this.wmParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        this.wmParams.x = 0;
        this.wmParams.y = 0;
        this.wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        this.wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        this.mFloatLayout = ((LinearLayout) LayoutInflater.from(getApplication()).inflate(R.layout.window_layout, null));
        ButterKnife.inject(WindowService.this, mFloatLayout);
        try {
            mWindowManager.addView(mFloatLayout, wmParams);
            mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                    .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            this.mFloatLayout.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View paramAnonymousView, MotionEvent event) {
                    WindowService.this.wmParams.x = ((int) event.getRawX() - WindowService.this.mFloatView
                            .getMeasuredWidth() / 2);
                    WindowService.this.wmParams.y = (-25 + ((int) event.getRawY() - WindowService.this.mFloatView
                            .getMeasuredHeight() / 2));
                    WindowService.this.mWindowManager.updateViewLayout(WindowService.this.mFloatLayout, WindowService.this.wmParams);
                    return false;
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, R.string.open_nettraffic, Toast.LENGTH_SHORT).show();
            stopSelf();
        }
    }

    private void initLocation() {
        this.latitude = Double.parseDouble(SharePrefereceManager.getInstance().getLat());
        this.longitude = Double.parseDouble(SharePrefereceManager.getInstance().getLng());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager
                .PERMISSION_GRANTED) {
            return;
        }
        this.locationManager = ((LocationManager) getSystemService(Context.LOCATION_SERVICE));
        try {
            this.locationManager.addTestProvider(GPS, false, true, false, false, true, true, true, 0, 5);
            this.locationManager.setTestProviderEnabled(GPS, true);
            this.locationManager.requestLocationUpdates(GPS, 0L, 0.0F, this);
            try {
                resetLocation(this.longitude, this.latitude);
            } catch (Exception e) {
                Log.e("service ", e.getMessage());
            }
        } catch (Exception e1) {
            Intent localIntent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(localIntent);
            Toast.makeText(this, R.string.loction_test, Toast.LENGTH_SHORT).show();
            stopSelf();
        }
    }


    private void resetLocation(double longitude, double latitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        Location mLocation = new Location(GPS);
        mLocation.setTime(System.currentTimeMillis());
        mLocation.setLongitude(longitude);
        mLocation.setLatitude(latitude);
        mLocation.setAccuracy(3f);
        mLocation.setAltitude(2d);
        mLocation.setTime(System.currentTimeMillis());
        mLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        locationManager.setTestProviderLocation(GPS, mLocation);
        mInfo.setText(getString(R.string.location_info, longitude, latitude));
        SharePrefereceManager.getInstance().putLocation(String.valueOf(longitude), String.valueOf(latitude));
    }


    @OnClick(R.id.quit)
    public void onQuit() {
        WindowService.this.stopSelf();
    }

    @OnClick(R.id.west)
    public void onLeftTouch() {
        resetLocation(longitude - defaultMoveValue, latitude);
    }

    @OnClick(R.id.east)
    public void onRightTouch() {
        resetLocation(longitude + defaultMoveValue, latitude);
    }

    @OnClick(R.id.north)
    public void onUpTouch() {
        resetLocation(longitude, latitude + defaultMoveValue);
    }

    @OnClick(R.id.south)
    public void onDownTouch() {
        resetLocation(longitude, latitude - defaultMoveValue);
    }


    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mFloatLayout != null) ;
        try {
            this.mWindowManager.removeView(this.mFloatLayout);
        } catch (Exception e) {
            Log.e("service", e.getMessage());
        }
    }
}
