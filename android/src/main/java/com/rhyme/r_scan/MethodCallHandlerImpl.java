package com.rhyme.r_scan;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.rhyme.r_scan.RScanCamera.RScanCameraMethodHandler;
import com.rhyme.r_scan.RScanCamera.RScanPermissions;
import com.rhyme.r_scan.RScanView.RScanViewPlugin;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.platform.PlatformViewRegistry;
import io.flutter.view.TextureRegistry;

public class MethodCallHandlerImpl implements MethodChannel.MethodCallHandler {
    private final ImageScanHelper scanHelper;
    private final MethodChannel methodChannel;

    public MethodCallHandlerImpl(
            Activity activity,
            BinaryMessenger messenger,
            RScanPermissions cameraPermissions,
            RScanPermissions.PermissionsRegistry permissionsAdder,
            TextureRegistry textureRegistry,
            PlatformViewRegistry platformViewRegistry) {

        scanHelper = new ImageScanHelper(activity);
        methodChannel = new MethodChannel(messenger, "com.rhyme_lph/r_scan");
        methodChannel.setMethodCallHandler(this);

        //注册老方式
        RScanViewPlugin.registerWith(platformViewRegistry, messenger);

        //注册新的方式
        new RScanCameraMethodHandler(
                activity,
                messenger,
                cameraPermissions,
                permissionsAdder,
                textureRegistry
        );

    }

    void stopListening() {
        methodChannel.setMethodCallHandler(null);
    }

    @Override
    public void onMethodCall(MethodCall call, @NonNull MethodChannel.Result result) {
        switch (call.method) {
            case "scanImagePath":
                scanHelper.scanImagePath(call, result);
                break;
            case "scanImageUrl":
                scanHelper.scanImageUrl(call, result);
                break;
            case "scanImageMemory":
                scanHelper.scanImageMemory(call, result);
                break;
            default:
                result.notImplemented();
                break;
        }
    }
}
