package com.example.pemission;


import java.util.List;
import java.util.function.Consumer;

public class PermissionCallbacksDSL implements PermissionCallbacks {
    private Runnable onGranted = () -> {};
    private Consumer<List<String>> onDenied = permissions -> {};
    private Consumer<PermissionRequest> onShowRationale = permissionRequest -> {};
    private Consumer<List<String>> onNeverAskAgain = permissions -> {};

    public void onGranted(Runnable func) {
        onGranted = func;
    }

    public void onDenied(Consumer<List<String>> func) {
        onDenied = func;
    }

    public void onShowRationale(Consumer<PermissionRequest> func) {
        onShowRationale = func;
    }

    public void onNeverAskAgain(Consumer<List<String>> func) {
        onNeverAskAgain = func;
    }

    @Override
    public void onGranted() {
        onGranted.run();
    }

    @Override
    public void onDenied(List<String> permissions) {
        onDenied.accept(permissions);
    }

    @Override
    public void onShowRationale(PermissionRequest permissionRequest) {
        onShowRationale.accept(permissionRequest);
    }

    @Override
    public void onNeverAskAgain(List<String> permissions) {
        onNeverAskAgain.accept(permissions);
    }
}