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
import com.awoisoak.giphyviewer.presentation.main.onlinefragment.OnlineGifsFragment;
import com.awoisoak.giphyviewer.presentation.main.offlinefragment.PlaceholderFragment2;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    //TODO remove icons that are not used
    //TODO fix the bug when doanloading gifs, we disable internet, retry once wifi enabled and the whole recyclerview is binded
    //TODO fix bug when starting without network and press retry once wifi enabled
    //TODO make the trending gifs infinite too?
    //TODO dont allow landscape mode?
    //TODO Should we use interactors to access to the future local data? or access directly with a manager?
    //TODO look for visor,Post,awoisoak,WP strings
    //TODO Review the use of scopes in Dagger
    //TODO why in GifsOnlineComponent only inject GifsOnlineFragment and no GifsOnlinePresenter
    //(this will use the @inject as well as it needs access to the interactor)
    //TODO do we need a 'global' @ApplicationScope ?? maybe use it instead of @GiphyApiScope??
    //TODO in GiphyViewerApplication there is a deprecated module from Dagger, take a look once it is stable

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.container) ViewPager mViewPager;
    @BindView(R.id.tabLayout) TabLayout mTabLayout;
    @BindView(R.id.toolbar_title) TextView mToolbarTitle;

    private static final int SEARCH_TAB = 0;
    private static final int FAV_TAB = 1;
    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(sectionsPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        setTabStyle();
        setToolbarTitle();

    }

    private void setTabStyle() {
        TabLayout.Tab tabCall = mTabLayout.getTabAt(SEARCH_TAB);
        tabCall.setIcon(R.drawable.gif_selector);
        TabLayout.Tab tabCall2 = mTabLayout.getTabAt(FAV_TAB);
        tabCall2.setIcon(R.drawable.favorite_selector);
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
                case SEARCH_TAB:
                    return OnlineGifsFragment.newInstance(position + 1);
                case FAV_TAB:
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
                case SEARCH_TAB:
//                    return "Search";
                    return"";
                case FAV_TAB:
//                    return "Your Favourites";
                    return "";
            }
            return null;
        }
    }
}
