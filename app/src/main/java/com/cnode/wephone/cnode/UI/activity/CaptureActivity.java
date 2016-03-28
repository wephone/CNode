package com.cnode.wephone.cnode.UI.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cnode.wephone.cnode.App;
import com.cnode.wephone.cnode.MainActivity;
import com.cnode.wephone.cnode.R;
import com.cnode.wephone.cnode.UI.baseActivity;
import com.cnode.wephone.cnode.Utils.CommonUtils;
import com.cnode.wephone.cnode.Utils.constant.IntentAction;
import com.cnode.wephone.cnode.Utils.constant.Params;
import com.cnode.wephone.cnode.Utils.volley.UrlHelper;
import com.cnode.wephone.cnode.Utils.volley.VolleyErrorHelper;
import com.cnode.wephone.cnode.Utils.volley.VolleyHelper;
import com.cnode.wephone.cnode.widget.ProgressDialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


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

//            Toast.makeText(this, "扫描结果: " + result.getContents(), Toast.LENGTH_LONG).show();
            //扫了正确的条码后变成wephone了
            final String access_token=result.getContents();
            Map<String, String> map = new HashMap<>();
            map.put(Params.ACCESS_TOKEN, result.getContents());
            JSONObject params = new JSONObject(map);
            final JsonObjectRequest request = new JsonObjectRequest(UrlHelper.ACCESS_TOKEN, params, new Response.Listener<JSONObject>() {//我觉得可能是发我的accesstoken去给他识别 匹配了就发我的json给我
                //有params时则为post方法  应该是传递参数的意思
                @Override
                public void onResponse(JSONObject response) {
                    if (response != null) {
                        try {

                            boolean success = response.getBoolean("success");
                            if (success) {
                                App.getContext().access_token = access_token;
                                String login_name = response.getString(Params.LOGIN_NAME);
                                String avatar_url = response.getString(Params.AVATAR_URL);
                                CommonUtils.saveStringToLocal(Params.ACCESS_TOKEN, access_token);
                                CommonUtils.saveStringToLocal(Params.LOGIN_NAME, login_name);
                                CommonUtils.saveStringToLocal(Params.AVATAR_URL, avatar_url);
                                CommonUtils.showToast("正在登陆...");
//                                sActivity.sendBroadcast(new Intent(IntentAction.LOGIN));
                                sendBroadcast(new Intent(IntentAction.LOGIN));//发了广播之后 不用关掉左侧菜单栏 名字头像就会更改了
// 测试下finish后 请求还会不会被加入到请求队列中 我觉得应该会 不然就不会变名字了
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                        if (access_token!=null){
                            CommonUtils.showToast("非登陆条码");
                            VolleyHelper.cancelPendingRequests(access_token);//Cannot cancelAll with a null tag
                        }else {
                            finish();
                        }
                    }
                }
            },new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof TimeoutError) {
                        CommonUtils.showToast(R.string.generic_server_down);
                    } else {
                        if (access_token!=null){
                            VolleyHelper.cancelPendingRequests(access_token);//Cannot cancelAll with a null tag 避免按下强制返回后accesstoken为空导致错误
                            CommonUtils.showToast("非登陆条码");//扫别的码时 是错误response
                        }else {
                            finish();
                        }
                    }
                }
            });
            VolleyHelper.addToRequestQueue(request, access_token);
            finish();
//            progress.hide();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            finish();
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
