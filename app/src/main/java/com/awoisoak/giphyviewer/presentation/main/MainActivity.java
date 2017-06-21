package com.awoisoak.giphyviewer.presentation.main;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.awoisoak.giphyviewer.R;
import com.awoisoak.giphyviewer.presentation.main.fragment1.PlaceholderFragment1;
import com.awoisoak.giphyviewer.presentation.main.fragment2.PlaceholderFragment2;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    //TODO look for Post,awoisoak,WP strings

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.container) ViewPager mViewPager;
    @BindView(R.id.tabDots) TabLayout mTabLayout;
    @BindView(R.id.toolbar_title) TextView mToolbarTitle;

    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        // Create the adapter that will return the corresponding fragment
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(sectionsPagerAdapter);

        //Workaround to add the 'dots' to the ViewPager
        mTabLayout.setupWithViewPager(mViewPager);

        setToolbarTitle();

    }

    private void setToolbarTitle() {
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mToolbarTitle.setText("Giphy Viewer");
                        break;
                    case 1:
                        mToolbarTitle.setText("Favourites");
                        break;
                    default:
                        Log.e(TAG,"unexpected page selected: "+position);
                }
            }
        });
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return PlaceholderFragment1.newInstance(position + 1);
                case 1:
                    return PlaceholderFragment2.newInstance(position + 1);
                default:
                    Log.e(TAG, "Error position not expected | position " + position);
                    return null;
            }
        }

        @Override
        public int getCount() {
            //Number of pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "";
                case 1:
                    return "";
            }
            return null;
        }
    }
}
