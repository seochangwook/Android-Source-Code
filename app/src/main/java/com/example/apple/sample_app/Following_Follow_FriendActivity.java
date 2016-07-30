package com.example.apple.sample_app;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

public class Following_Follow_FriendActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageView people_image;
    Button my_info_button;
    CheckBox checkbox;
    Switch switch_widget;

    /**
     * 하단 메뉴관련 버튼
     **/
    Button button_menu_1;
    Button button_menu_2;
    Button button_menu_3;
    Button button_menu_4;
    Button button_menu_5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_drawable); //drawer를 변경. 다른 네비게이션 뷰가 나온다.//

        button_menu_1 = (Button) findViewById(R.id.button_menu_1);
        button_menu_2 = (Button) findViewById(R.id.button_menu_2);
        button_menu_3 = (Button) findViewById(R.id.button_menu_3);
        button_menu_4 = (Button) findViewById(R.id.button_menu_4);
        button_menu_5 = (Button) findViewById(R.id.button_menu_5);

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

        /** 하단메뉴관련 이벤트 처리 **/
        button_menu_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Following_Follow_FriendActivity.this, "버튼메뉴 1 클릭", Toast.LENGTH_SHORT).show();
            }
        });

        button_menu_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Following_Follow_FriendActivity.this, "버튼메뉴 2 클릭", Toast.LENGTH_SHORT).show();
            }
        });

        button_menu_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Following_Follow_FriendActivity.this, "버튼메뉴 3 클릭", Toast.LENGTH_SHORT).show();
            }
        });

        button_menu_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Following_Follow_FriendActivity.this, "버튼메뉴 4 클릭", Toast.LENGTH_SHORT).show();
            }
        });

        button_menu_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Following_Follow_FriendActivity.this, "버튼메뉴 5 클릭", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.friend_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.friend_menu_1) {
            Toast.makeText(Following_Follow_FriendActivity.this, "검색", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
