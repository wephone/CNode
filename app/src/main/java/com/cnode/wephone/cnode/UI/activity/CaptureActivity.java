package com.cnode.wephone.cnode.UI.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


/**
 * Created by ASUS on 2016/3/25.
 * zxing二维码 可以实现了
 */
public class CaptureActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new IntentIntegrator(this).setCaptureActivity(com.journeyapps.barcodescanner.CaptureActivity.class).initiateScan();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            Toast.makeText(this, "扫描结果: " + result.getContents(), Toast.LENGTH_LONG).show();
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
