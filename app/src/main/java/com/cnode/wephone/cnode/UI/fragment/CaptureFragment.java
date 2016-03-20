package com.cnode.wephone.cnode.UI.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cnode.wephone.cnode.R;
import com.cnode.wephone.cnode.Utils.CommonUtils;
import com.cnode.wephone.library_qrcode.CaptureView;

/**
 * Created by ASUS on 2016/3/19.
 * 二维码捕获类
 */
public class CaptureFragment extends BaseFragment implements CaptureView.OnCaptureListener{

    private CaptureView captureView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        captureView = new CaptureView(sActivity);
        int w = CommonUtils.getScreenWidth(sActivity) * 4 / 5;
        captureView.setCropLayoutSize(w, w);
        captureView.setOnCaptureListener(this);
        return captureView.getView();
    }

    @Override
    public void onCapture(String result) {
        if (!TextUtils.isEmpty(result)) {
            oauth(result);
        } else {
            captureError();
        }
    }

    //扫码失败
    private void captureError() {
        CommonUtils.showToast(R.string.failure);
        captureView.restartPreview();
    }

    /**
     * 验证访问令牌
     *
     * @param access_token 令牌
     */
    private void oauth(final String access_token) {
        showProgress(R.string.on_login);
    }
}
