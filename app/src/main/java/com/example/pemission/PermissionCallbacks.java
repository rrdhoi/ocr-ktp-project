package com.example.pemission;


import java.util.List;
import java.util.function.Consumer;

interface PermissionCallbacks {
    void onGranted();

    void onDenied(List<String> permissions);

    void onShowRationale(PermissionRequest permissionRequest);

    void onNeverAskAgain(List<String> permissions);
}
