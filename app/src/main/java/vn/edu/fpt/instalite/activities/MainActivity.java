package vn.edu.fpt.instalite.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import vn.edu.fpt.instalite.R;
import vn.edu.fpt.instalite.adapters.MainAdapter;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        viewPager = findViewById(R.id.view_pager);

        setUpViewPager();
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
//                selectedFragment = new HomeFragment();
                viewPager.setCurrentItem(0);
            } else if (itemId == R.id.nav_search) {
//                selectedFragment = new SearchFragment();
                viewPager.setCurrentItem(1);
            } else if (itemId == R.id.nav_addpost) {
//                selectedFragment = new AddPostFragment();
                viewPager.setCurrentItem(2);
            } else if (itemId == R.id.nav_notification) {
                viewPager.setCurrentItem(3);
//                selectedFragment = new NotificationFragment();
            } else if (itemId == R.id.nav_profile) {
                viewPager.setCurrentItem(4);
            }
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameContainer, fragment)
                .commit();
    }

    private void setUpViewPager() {
        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(mainAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigation.setSelectedItemId(R.id.nav_home);
                        break;
                    case 1:
                        bottomNavigation.setSelectedItemId(R.id.nav_search);
                        break;
                    case 2:
                        bottomNavigation.setSelectedItemId(R.id.nav_addpost);
                        break;
                    case 3:
                        bottomNavigation.setSelectedItemId(R.id.nav_notification);
                        break;
                    case 4:
                        bottomNavigation.setSelectedItemId(R.id.nav_profile);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPager.setOffscreenPageLimit(4);
    }

}