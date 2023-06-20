package com.example.nativecodetest;

import android.os.Bundle;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import xyz.stackbox.android.sdk.Outlet;
import xyz.stackbox.android.sdk.SbxSdk;

import io.flutter.embedding.android.FlutterActivity;

public class MainActivity extends FlutterActivity {

    private static final String CHANNEL = "flutter.native/helper";

    private String TAG = "MyActivity";
    private int REQUEST_CODE = 1010;
    // Context context = getApplicationContext();
    // Context context = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // GeneratedPluginRegistrant.registerWith(this);
        new MethodChannel(getFlutterEngine().getDartExecutor().getBinaryMessenger(), CHANNEL).setMethodCallHandler(
                new MethodChannel.MethodCallHandler() {
                    @Override
                    public void onMethodCall(MethodCall call, MethodChannel.Result result) {
                        if (call.method.equals("helloFromNativeCode")) {
                            // String greetings = helloFromNativeCode();
                            // result.success(greetings);

                            try {
                                SbxSdk.initialize(MainActivity.this, "238353be-469f-4345-a450-9b8c6ee604f4",
                                        "https://api.staging.stackbox.xyz");
                                // use <stackbox-api-url> = "https://api.stackbox.xyz" for production
                                // environment
                                // use <stackbox-api-url> = "https://api.staging.stackbox.xyz" for UAT/staging
                                // environment

                                HashMap<String, Object> fields = new HashMap<>();
                                fields.put("address1", "Test Address");
                                fields.put("gstFiled", "51515151515");
                                fields.put("contactPerson", "Person A");
                                fields.put("city", "Bengaluru");

                                Outlet outlet = new Outlet("<name>", "<phone>", fields);

                                SbxSdk.Options options = new SbxSdk.Options()
                                        .setDynamicForm(true)
                                        .setPhoneVerification(true)
                                        .setGeoTag(true)
                                        .setPageSubtitle("Subtitle")
                                        .setPageTitle("Title")
                                        .setOutlet(outlet); // can be set to null if no pre-filling needed

                                try {
                                    SbxSdk.openGeoTagForm(MainActivity.this, REQUEST_CODE, options);
                                } catch (SbxSdk.NotAuthorizedException | SbxSdk.NotSyncedException
                                        | IllegalArgumentException ex) {
                                    Log.e(TAG, ex.getMessage());
                                }
                            } catch (Exception exception) {
                                Log.e("Exception", exception.getMessage());
                            }

                        }
                    }
                });

        // new MethodChannel(getFlutterEngine().getDartExecutor().getBinaryMessenger(),
        // CHANNEL1)
        // .setMethodCallHandler(
        // (call, result) -> {
        // //Log.wtf("abc result ",sharedText.toString());
        // if (call.method.contentEquals("getSharedText")) {
        // //result.success("My sharedText");
        // Log.d("Gamification", "result.success " + sharedText);
        // result.success(sharedText);
        // sharedText = "Default";
        // }
        // }
        // );

        // @Override
        // public void configureFlutterEngine(FlutterEngine flutterEngine) {
        // GeneratedPluginRegistrant.registerWith(flutterEngine);
        //
        // // Set up the platform channel
        // MethodChannel channel = new
        // MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(),
        // "com.example.my_channel");
        // channel.setMethodCallHandler((call, result) -> {
        // if (call.method.equals("add")) {
        // int a = call.argument("a");
        // int b = call.argument("b");
        // int sum = MyJavaClass.add(a, b);
        // result.success(sum);
        // } else {
        // result.notImplemented();
        // }
        // });
        // }

    }

    private String helloFromNativeCode() {
        return "Hello Ankur from Native Android Code";
    }

    @Override
    protected void onResume() {
        super.onResume();
        SbxSdk.sync(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String payload = data.getStringExtra("PAYLOAD");
                try {
                    JSONObject json = new JSONObject(payload);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Log.i(TAG, payload);
            } else {
                // failure
                int errorCode = data.getIntExtra("errorCode", 0);
                String errorMessage = data.getStringExtra("errorMessage");
                Log.e(TAG, errorMessage);
            }
        }
    }

}
