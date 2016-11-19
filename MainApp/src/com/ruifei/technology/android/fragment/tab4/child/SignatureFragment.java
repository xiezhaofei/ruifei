package com.ruifei.technology.android.fragment.tab4.child;

import android.view.View;
import android.widget.TextView;

import com.ruifei.technology.android.R;
import com.ruifei.framework.fragment.BaseFragment;

/**
 * Created by Administrator on 2016/11/6.
 */

public class SignatureFragment extends BaseFragment {

    private TextView signature;

    public SignatureFragment() {
        super();
    }

    public SignatureFragment(boolean canSlide) {
        super(canSlide);
    }

    @Override
    public int getContainerLayoutId() {
        return R.layout.signature_lay;
    }



    @Override
    public void initUi() {
        super.initUi();
    }

    @Override
    public void loadData() {
        super.loadData();
    }
}
