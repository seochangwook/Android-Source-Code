package com.example.apple.sample_app;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.sample_app.view.Fragment_Discussionview;
import com.example.apple.sample_app.view.Fragment_Keywordview;
import com.example.apple.sample_app.view.Fragment_scraptview;
import com.example.apple.sample_app.view.Fragment_settingview;
import com.michaldrabik.tapbarmenulib.TapBarMenu;

public class TabActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TapBarMenu tapBarMenu; //메뉴생성//
    /**
     * 탭바에 있는 아이템
     **/
    ImageView item_1;
    ImageView item_2;
    ImageView item_3;
    ImageView item_4;
    ImageView people_image;
    Button my_info_button;
    CheckBox checkbox;
    Switch switch_widget;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_activity_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tapBarMenu = (TapBarMenu) findViewById(R.id.tapBarMenu);

        /** TabBar에서 쓰일 아이템 리소스 **/
        item_1 = (ImageView) findViewById(R.id.item1);
        item_2 = (ImageView) findViewById(R.id.item2);
        item_3 = (ImageView) findViewById(R.id.item3);
        item_4 = (ImageView) findViewById(R.id.item4);

        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager); //뷰페이저를 적용//

        //탭바메뉴 토글적용.//
        tapBarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tapBarMenu.toggle(); //탭바메뉴 보이기//
            }
        });

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        //네비게이션 모드에서 애니메이션 적으로 나오게 하기 위해서 설정.//
        //Drawer레이아웃을 만들어서 설정.//
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //뒤에 String은 음성지원을 위해서 추가해주는 글자.//
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle); //set -> add로 변경. 인식하는 리스너//
        toggle.syncState(); //DrawerLayout과 ActionBar의 상태랑 동기화.//

        //드로어 인식에 대한 리스너//
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) //드로어가 오픈.//
            {
                //Toast.makeText(TabActivity.this, "Draw open", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerClosed(View drawerView) //드로어가 클로즈.//
            {
                //Toast.makeText(TabActivity.this, "Draw close", Toast.LENGTH_SHORT).show();

                //다시 이미지 초기화.//
                people_image.setImageResource(R.drawable.facebook_people_image);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        //뷰를 inflate할 경우 parent뷰가 무엇이 될지 우선 고려//
        View view = getLayoutInflater().inflate(R.layout.nav_header_main, navigationView); //parent로 헤더뷰를 포함하고
        //있는 NavigationView를 상속한다.//

        //네비게이션에 있는 위젯들을 초기화.//
        people_image = (ImageView) view.findViewById(R.id.people_imageview);
        my_info_button = (Button) view.findViewById(R.id.my_enroll_button);
        checkbox = (CheckBox) view.findViewById(R.id.checkBox);
        switch_widget = (Switch) view.findViewById(R.id.switch1);

        my_info_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TabActivity.this, "개인정보수정버튼 클릭", Toast.LENGTH_SHORT).show();

                people_image.setImageDrawable(getResources().getDrawable(R.drawable.apple_image_2));
            }
        });

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean is_check) {
                if (is_check == true) {
                    Toast.makeText(TabActivity.this, "체크", Toast.LENGTH_SHORT).show();
                } else if (is_check == false) {
                    Toast.makeText(TabActivity.this, "체크해제", Toast.LENGTH_SHORT).show();
                }
            }
        });

        switch_widget.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean is_checked) {
                if (is_checked == true) {
                    Toast.makeText(TabActivity.this, "스위치 On", Toast.LENGTH_SHORT).show();
                } else if (is_checked == false) {
                    Toast.makeText(TabActivity.this, "스위치 Off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(this); //네비게이션의 이벤트 리스너 장착.//

        /** TapBar토글 아이템 메뉴처리 **/
        item_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TabActivity.this, "item 1 click", Toast.LENGTH_SHORT).show();
            }
        });

        item_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TabActivity.this, "item 2 click", Toast.LENGTH_SHORT).show();
            }
        });

        item_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TabActivity.this, "item 3 click", Toast.LENGTH_SHORT).show();
            }
        });

        item_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TabActivity.this, "item4 click", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId(); //네비게이션에서 선택된 아이텀의 id값을 가져온다.//'

        if (id == R.id.nav_light) //조도센서.//
        {
            Toast.makeText(TabActivity.this, "click", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START); //왼쪽이었으니 gravity를 start로 설정.//

        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tab, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        //각 보여질 탭을 설정.//
        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0: {
                    Fragment_Keywordview keywordview = new Fragment_Keywordview();

                    /*
                    /** Fragment로 값을 전달할 필요가 있을 경우 *
                    Bundle bundle = new Bundle(); //Fragment에게 값을 전달하기 위해서 Bundle사용.//

                    //Intent일 때는 액티비티간에 데이터 전달이였다. 마찬가지로 프래그먼트도 (key,value)로 구성 후 bundle을 이용한다.//
                    bundle.putString("SERVER_IP_ADDRESS_KEY", server_ip);
                    bundle.putInt("PORT_NUMBER_KEY", Integer.parseInt(server_port_number));

                    fragment_1.setArguments(bundle); //프래그먼트에게 인자들(아규먼트)을 전송할 준비를 한다.//
                    */

                    return keywordview;
                }

                case 1: {
                    Fragment_scraptview scraptview = new Fragment_scraptview();

                    return scraptview;
                }

                case 2: {
                    Fragment_Discussionview discussionview = new Fragment_Discussionview();

                    return discussionview;
                }

                case 3: {
                    Fragment_settingview settingview = new Fragment_settingview();

                    return settingview;
                }
            }

            return null;
        }

        //탭의 개수설정.//
        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        //각 탭에서 보여질 문구설정.//
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "키워드";
                case 1:
                    return "스크랩";
                case 2:
                    return "토론방";
                case 3:
                    return "설정";
            }
            return null;
        }
    }
}
