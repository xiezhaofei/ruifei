package test;

import com.ruifei.android.R;
import com.ruifei.framework.fragment.BaseFragment;

/**
 * Created by Administrator on 2016/8/18.
 */
public class TestFragment2 extends BaseFragment {

    public TestFragment2() {
    }

    public TestFragment2(boolean canSlide) {
        super(canSlide);
    }

    @Override
    public int getContainerLayoutId() {
        return R.layout.test_fragment2_lay;
    }
}
