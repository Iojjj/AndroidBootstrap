package com.github.iojjj.bootstrap;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.iojjj.bootstrap.social.SocialNetworksTestFragment;

/**
 * Main demo activity.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, SocialNetworksTestFragment.newInstance())
                    .commit();
        }
    }
}
