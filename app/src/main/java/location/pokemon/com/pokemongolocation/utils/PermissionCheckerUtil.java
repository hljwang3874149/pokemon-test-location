package location.pokemon.com.pokemongolocation.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.util.SimpleArrayMap;

/**
 * ==================================================
 * 项目名称：mobike_operations
 * 创建人：wangxiaolong
 * 创建时间：16/7/1 下午5:54
 * 备注：
 * Version：
 * ==================================================
 */
public class PermissionCheckerUtil {
    private static final SimpleArrayMap<String, Integer> MIN_SDK_PERMISSIONS;

    static {
        MIN_SDK_PERMISSIONS = new SimpleArrayMap<>(6);
        MIN_SDK_PERMISSIONS.put("com.android.voicemail.permission.ADD_VOICEMAIL", 14);
        MIN_SDK_PERMISSIONS.put("android.permission.BODY_SENSORS", 20);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_CALL_LOG", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_EXTERNAL_STORAGE", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.USE_SIP", 9);
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_CALL_LOG", 16);
    }

    public static boolean isCheckOk(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            if (PermissionChecker.checkSelfPermission(context, permission) != PermissionChecker.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static boolean isOnRequestPermissions(Activity context, int requestCode, String[] permissions) {
        if (!isCheckOk(context, permissions)) {
            ActivityCompat.requestPermissions(context, permissions, requestCode);
            return false;
        }
        return true;
    }

    private static boolean permissionExists(String permission) {
        // Check if the permission could potentially be missing on this device
        Integer minVersion = MIN_SDK_PERMISSIONS.get(permission);
        // If null was returned from the above call, there is no need for a device API level check for the permission;
        // otherwise, we check if its minimum API level requirement is met
        return minVersion == null || Build.VERSION.SDK_INT >= minVersion;
    }

    public static void onRequestPermissionResult(@NonNull int[] grantResults, onPermissionListener listener) {
        for (int i = 0, k = grantResults.length; i < k; i++) {
            if (grantResults[i] != PermissionChecker.PERMISSION_GRANTED) {
                listener.onDenyListener();
                return;
            }
        }
        listener.onGrantListener();
    }

    public interface onPermissionListener {

        void onDenyListener();

        void onGrantListener();

    }
}
