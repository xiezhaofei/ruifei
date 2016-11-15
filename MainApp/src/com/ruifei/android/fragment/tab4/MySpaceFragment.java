package com.ruifei.android.fragment.tab4;

import android.view.View;
import android.widget.TextView;

import com.ruifei.android.R;
import com.ruifei.android.fragment.tab4.child.SignatureFragment;
import com.ruifei.framework.fragment.BaseFragment;

/**
 * Created by Administrator on 2016/8/24.
 */
public class MySpaceFragment extends BaseFragment {

    private TextView sig;

    @Override
    public int getContainerLayoutId() {
        return R.layout.myspace_fragment_lay;
    }

    @Override
    public void initUi() {
        sig = (TextView)findViewById(R.id.sig);
        super.initUi();
    }

    @Override
    public void loadData() {
        sig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignatureFragment fragment = new SignatureFragment(true);
                startFragment(fragment);
            }
        });
        super.loadData();
    }
}
