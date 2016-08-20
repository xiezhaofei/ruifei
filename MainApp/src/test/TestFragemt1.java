package test;

import android.view.View;
import android.widget.Button;

import com.ruifei.android.R;
import com.ruifei.framework.fragment.BaseFragment;

/**
 * Created by Administrator on 2016/8/18.
 */
public class TestFragemt1 extends BaseFragment{

    private Button button;

    public TestFragemt1() {
    }

    public TestFragemt1(boolean canSlide) {
        super(canSlide);
    }

    @Override
    public int getContainerLayoutId() {
        return R.layout.test_fragment1_lay;
    }

    @Override
    public void initUi() {
        super.initUi();
        button = (Button) findViewById(R.id.button);
    }


    @Override
    public void loadData() {
        super.loadData();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestFragment2 fragment2 = new TestFragment2(true);
                startFragment(fragment2);
            }
        });
    }
}
