package com.example.pemission;

import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;

public class PermissionUtils {

    public static void askPermissions(Activity activity, PermissionCallbacksDSL callbacks, String... permissions) {

        List<String> permissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            if (!isPermissionGranted(activity, permission)) {
                permissionsNeeded.add(permission);
            }
        }

        if (!permissionsNeeded.isEmpty()) {
            List<String> shouldShowRationalePermissions = new ArrayList<>();
            List<String> shouldNotShowRationalePermissions = new ArrayList<>();

            for (String permission : permissionsNeeded) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    shouldShowRationalePermissions.add(permission);
                } else {
                    shouldNotShowRationalePermissions.add(permission);
                }
            }

            int requestCode = PermissionsMap.put((PermissionCallbacks) callbacks);

            if (!shouldShowRationalePermissions.isEmpty()) {
                ((PermissionCallbacks) callbacks).onShowRationale(new PermissionRequest(activity, shouldShowRationalePermissions, requestCode));
                return;
            }

            if (!shouldNotShowRationalePermissions.isEmpty()) {
                ActivityCompat.requestPermissions(activity, shouldNotShowRationalePermissions.toArray(new String[0]), requestCode);
            }

        } else {
            ((PermissionCallbacks) callbacks).onGranted();
        }
    }

    public static void handlePermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        PermissionCallbacks callbacks = PermissionsMap.get(requestCode);

        boolean allGranted = true;
        List<String> neverAskAgainPermissions = new ArrayList<>();
        List<String> deniedPermissions = new ArrayList<>();

        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                allGranted = false;
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i])) {
                    neverAskAgainPermissions.add(permissions[i]);
                } else {
                    deniedPermissions.add(permissions[i]);
                }
            }
        }

        if (allGranted) {
            callbacks.onGranted();
        } else {
            if (!deniedPermissions.isEmpty()) {
                callbacks.onDenied(deniedPermissions);
            }
            if (!neverAskAgainPermissions.isEmpty()) {
                callbacks.onNeverAskAgain(neverAskAgainPermissions);
            }
        }
    }

    public static boolean isPermissionGranted(Activity activity, String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
