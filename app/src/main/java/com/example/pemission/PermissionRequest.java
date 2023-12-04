package com.example.pemission;

import android.app.Activity;
import androidx.core.app.ActivityCompat;
import java.lang.ref.WeakReference;
import java.util.List;

public class PermissionRequest {
    private final WeakReference<Activity> weakActivity;
    private final List<String> permissions;
    private final int requestCode;

    public PermissionRequest(Activity activity, List<String> permissions, int requestCode) {
        weakActivity = new WeakReference<>(activity);
        this.permissions = permissions;
        this.requestCode = requestCode;
    }

    public void retry() {
        Activity activity = weakActivity.get();
        if (activity != null) {
            ActivityCompat.requestPermissions(activity, permissions.toArray(new String[0]), requestCode);
        }
    }
}