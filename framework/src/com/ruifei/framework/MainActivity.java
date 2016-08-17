package com.ruifei.framework;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.ruifei.framework.fragment.ManageFragment;

public class MainActivity extends Activity {

    private ManageFragment manageFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        manageFragment = new ManageFragment();
    }

    public void startFragment()
    {

    }
}
