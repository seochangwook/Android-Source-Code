package com.example.apple.sample_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.apple.sample_app.widget.MenuWidget.BottomMenu;

import java.util.ArrayList;

public class Following_Follow_FriendActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageView people_image;
    Button my_info_button;
    CheckBox checkbox;
    Switch switch_widget;

    BottomMenu mBottomMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_drawable); //drawer를 변경. 다른 네비게이션 뷰가 나온다.//
        mBottomMenu = (BottomMenu) findViewById(R.id.bottom_menu_5);

        setupBottomMenuTabs(); //하단메뉴를 생성.//

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //네비게이션 모드에서 애니메이션 적으로 나오게 하기 위해서 설정.//
        //Drawer레이아웃을 만들어서 설정.//
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //뒤에 String은 음성지원을 위해서 추가해주는 글자.//
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle); //set -> add로 변경. 인식하는 리스너//
        toggle.syncState(); //DrawerLayout과 ActionBar의 상태랑 동기화.//

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
        navigationView.setNavigationItemSelectedListener(this); //네비게이션의 이벤트 리스너 장착.//

        //뷰를 inflate할 경우 parent뷰가 무엇이 될지 우선 고려//
        View view = getLayoutInflater().inflate(R.layout.nav_header_main, navigationView); //parent로 헤더뷰를 포함하고
        //있는 NavigationView를 상속한다.//

        //네비게이션에 있는 위젯들을 초기화.//
        people_image = (ImageView) view.findViewById(R.id.people_imageview);
        my_info_button = (Button) view.findViewById(R.id.my_enroll_button);
        checkbox = (CheckBox) view.findViewById(R.id.checkBox);
        switch_widget = (Switch) view.findViewById(R.id.switch1);

        /** NavigationDrawer에 있는 위젯 이벤트 처리 **/
        my_info_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Following_Follow_FriendActivity.this, "개인정보수정버튼 클릭", Toast.LENGTH_SHORT).show();

                people_image.setImageDrawable(getResources().getDrawable(R.drawable.apple_image_2));
            }
        });

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean is_check) {
                if (is_check == true) {
                    Toast.makeText(Following_Follow_FriendActivity.this, "체크", Toast.LENGTH_SHORT).show();
                } else if (is_check == false) {
                    Toast.makeText(Following_Follow_FriendActivity.this, "체크해제", Toast.LENGTH_SHORT).show();
                }
            }
        });

        switch_widget.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean is_checked) {
                if (is_checked == true) {
                    Toast.makeText(Following_Follow_FriendActivity.this, "스위치 On", Toast.LENGTH_SHORT).show();
                } else if (is_checked == false) {
                    Toast.makeText(Following_Follow_FriendActivity.this, "스위치 Off", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupBottomMenuClickListener();
    }

    private void setupBottomMenuClickListener() {
        mBottomMenu.setBottomMenuClickListener(new BottomMenu.BottomMenuClickListener() {
            @Override
            public void onItemSelected(int position, int id, boolean triggeredOnRotation) {
                // Do something when item is selected
                if (position == 1) {
                    Toast.makeText(Following_Follow_FriendActivity.this, "메뉴 2 클릭", Toast.LENGTH_SHORT).show();
                }

                if (position == 4) {
                    Toast.makeText(Following_Follow_FriendActivity.this, "메뉴 5 클릭", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Following_Follow_FriendActivity.this, Tag_Sample_Activity.class);

                    startActivity(intent);
                }
            }

            @Override
            public void onItemReSelected(int position, int id) {
                // Do something when item is re-selected
                if (position == 0) {
                    Toast.makeText(Following_Follow_FriendActivity.this, "메뉴 1 클릭", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Bottom Menu 관련
     **/
    private void setupBottomMenuTabs() {
        ArrayList<BottomMenu.BottomMenuItem> items = new ArrayList<>();
        BottomMenu.BottomMenuItem news = new BottomMenu.BottomMenuItem(R.id.bottom_bar_9, R.drawable.ic_account_balance_black_24dp, R.color.bottom_bar_unselected, R.color.bottom_bar_selected);
        BottomMenu.BottomMenuItem tw = new BottomMenu.BottomMenuItem(R.id.bottom_bar_10, R.drawable.ic_face_black_24dp, R.color.bottom_bar_unselected, R.color.bottom_bar_selected);
        BottomMenu.BottomMenuItem listing = new BottomMenu.BottomMenuItem(R.id.bottom_bar_11, R.drawable.ic_subscriptions_black_24dp, R.color.bottom_bar_unselected, R.color.bottom_bar_selected);
        BottomMenu.BottomMenuItem tv = new BottomMenu.BottomMenuItem(R.id.bottom_bar_12, R.drawable.ic_whatshot_black_24dp, R.color.bottom_bar_unselected, R.color.bottom_bar_selected);
        BottomMenu.BottomMenuItem etc = new BottomMenu.BottomMenuItem(R.id.bottom_bar_13, R.drawable.ic_whatshot_black_24dp, R.color.bottom_bar_unselected, R.color.bottom_bar_selected);

        items.add(news);
        items.add(tw);
        items.add(listing);
        items.add(tv);
        items.add(etc);

        mBottomMenu.addItems(items);
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId(); //네비게이션에서 선택된 아이텀의 id값을 가져온다.//'

        if (id == R.id.nav_share) {
            Toast.makeText(Following_Follow_FriendActivity.this, "공유기능", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_send) {
            Toast.makeText(Following_Follow_FriendActivity.this, "전송기능", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START); //왼쪽이었으니 gravity를 start로 설정.//

        return true;
    }
}
