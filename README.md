# Team Project Sample Code (android) - Newsing IT App -

# Planning Sector issues 

* Project UI Prototype URL : https://ovenapp.io/view/TPndbnvzLfjpvM47zW2zUx3KPLqIz0Y0

<< UI Prototype image >>

<p align="left">
  <title> UI 프로토타입 기획 산출물</title>
  <div class="photoset-grid" data-layout="13">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/UI%20Prototype.jpeg" width="250" height="350">
  </div>
</p>

# Development(Android) issues

< Android Using Library list (updating...) >

* compile 'com.android.support:support-v4:23.4.0'
* compile 'com.facebook.android:facebook-android-sdk:4.8.2' //페이스북 관련 오픈소스//
* compile 'com.android.support:recyclerview-v7:23.4.0'
* compile 'com.github.iwgang:familiarrecyclerview:1.3.0' //리사이클 뷰를 리스트뷰처럼 사용할 수 있는 라이브러리 오픈소스//
* compile 'com.yqritc:recyclerview-flexibledivider:1.4.0'
* compile 'com.android.support:cardview-v7:23.4.0' //카드뷰를 사용하기 위한 라이브러리//
* compile 'com.github.siyamed:android-shape-imageview:0.9.+@aar' //이미지 동그랗게 자르기//
* compile 'com.android.support:design:23.4.0' //Metrial Design사용//
* compile 'com.github.michaldrabik:tapbarmenu:1.0.5' //TapBarMenu 관련 라이브러리.//
* compile 'com.mikepenz:actionitembadge:3.2.6@aar' //액션배지달기.//
* //SUB-DEPENDENCIES
* //Android-Iconics - used to provide an easy API for icons
* compile 'com.mikepenz:iconics-core:2.6.6@aar'
* compile 'com.specyci:residemenu:1.6+' //향상된 3D메뉴 라이브러리
* compile 'com.google.android.gms:play-services-auth:8.3.0' //구글 인증관련 라이브러리//
* compile 'com.android.support:multidex:1.0.0' //dexOutOfMemory방지 라이브러리//
* compile('com.twitter.sdk.android:twitter:1.14.1@aar') {
        transitive = true;
    } //트위터 관련 라이브러리//
* compile 'com.google.code.gson:gson:2.7' //자동화 json파싱 라이브러리.//
* compile 'com.squareup.picasso:picasso:2.3.3' //피카소 라이브러리//
* compile 'jp.wasabeef:picasso-transformations:2.1.0' //피카소의 변형.//
* compile 'jp.co.cyberagent.android.gpuimage:gpuimage-library:1.4.1'
* compile 'com.squareup.okhttp3:okhttp:3.4.1' //okHttp라이브러리.//
* compile 'com.github.franmontiel:PersistentCookieJar:v0.9.3' //OkHttp쿠키관련 라이브러리.//
* compile 'com.github.bumptech.glide:glide:3.7.0' //Glide 라이브러리.//
* compile 'jp.wasabeef:blurry:2.0.2'
* compile 'com.github.mabbas007:TagsEditText:v1.0' //해시태그 관련 라이브러리
* compile 'me.gujun.android.taggroup:library:1.4@aar' //해시태그 관련 라이브러리
* (새로운 라이브러리 추가 시 계속 업데이트)

<< Sample image >>

<p align="left">
  <title> 탭화면 마다 다르게 구성된 TabBarMenu 
  (UI/UX를 고려하면 우선은 하단 매뉴가 없어 뷰를 보는데에 방해를 주지 않고, 버튼을 눌렀을 경우 필요한 메뉴들이 나온다.)</title>
  <div class="photoset-grid" data-layout="13">
      <img src="https://github.com/seochangwook/Android-Source-Code-Team-Project-Sample-Code-/blob/master/Project_SampleImage/screen_1.png" width="250" height="350">
      <img src="https://github.com/seochangwook/Android-Source-Code-Team-Project-Sample-Code-/blob/master/Project_SampleImage/screen_2.png" width="250" height="350">
  </div>
</p>

<p align="left">
  <title> ActionBadge 이벤트 처리
  (사용자에게 '새로운 키워드 제공알람', '새로운 댓글알람'이 제공되고 해당 버튼을 클릭하여 정보를 볼 수 있다.)</title>
  <div class="photoset-grid" data-layout="13">
      <img src="https://github.com/seochangwook/Android-Source-Code-Team-Project-Sample-Code-/blob/master/Project_SampleImage/screen_3.png" width="250" height="350">
      <img src="https://github.com/seochangwook/Android-Source-Code-Team-Project-Sample-Code-/blob/master/Project_SampleImage/screen_4.png" width="250" height="350">
  </div>
</p>

<< Sample image >>

<p align="left">
  <title> 친구에 대한 팔로우/팔로잉에 대한 뷰 구현(RecyclerView, CardView사용, JSON통신) </title>
  <div class="photoset-grid" data-layout="13">
      <img src="https://github.com/seochangwook/Android-Source-Code-Team-Project-Sample-Code-/blob/master/Project_SampleImage/screen_5.png" width="250" height="350">
      <img src="https://github.com/seochangwook/Android-Source-Code-Team-Project-Sample-Code-/blob/master/Project_SampleImage/screen_6.jpeg" width="250" height="350">
  </div>
</p>

<< Sample image >>

<p align="left">
  <title> 각 Activity에 따른 다양한 NavigationDrawable구현 </title>
  <div class="photoset-grid" data-layout="13">
      <img src="https://github.com/seochangwook/Android-Source-Code-Team-Project-Sample-Code-/blob/master/Project_SampleImage/screen_7.jpeg" width="250" height="350">
      <img src="https://github.com/seochangwook/Android-Source-Code-Team-Project-Sample-Code-/blob/master/Project_SampleImage/screen_8.jpeg" width="250" height="350">
  </div>
</p>

* JSON Structure Information

<p align="left">
  <img src="https://github.com/seochangwook/Android-Source-Code-Team-Project-Sample-Code-/blob/master/Project_SampleImage/json_info_image_1.png" width="650" height="350">
</p>

<p align="left">
  <img src="https://github.com/seochangwook/Android-Source-Code-Team-Project-Sample-Code-/blob/master/Project_SampleImage/json_info_image_2.png" width="650" height="350">
</p>

<p align="left">
  <img src="https://github.com/seochangwook/Android-Source-Code-Team-Project-Sample-Code-/blob/master/Project_SampleImage/json_info_image_3.png" width="650" height="350">
</p>

* Android Network Structure Information

<p align="left">
  <img src="https://github.com/seochangwook/Android-Source-Code-Team-Project-Sample-Code-/blob/master/Project_SampleImage/android_network_info_image_1.png" width="650" height="350">
</p>

<p align="left">
  <img src="https://github.com/seochangwook/Android-Source-Code-Team-Project-Sample-Code-/blob/master/Project_SampleImage/android_network_info_image_2.png" width="650" height="350">
</p>

<p align="left">
  <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/network_info_image_1.png" width="650" height="350">
</p>

<p align="left">
  <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/network_info_image_2.png" width="650" height="350">
</p>

<p align="left">
  <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/network_info_image_3.png" width="650" height="350">
</p>

<p align="left">
  <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/network_info_image_4.png" width="650" height="350">
</p>

<p align="left">
  <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/network_info_image_5.png" width="650" height="350">
</p>

<p align="left">
  <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/network_info_image_6.png" width="650" height="350">
</p>

<p align="left">
  <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/network_info_image_7.png" width="650" height="350">
</p>

<< Sample image >>

<p align="left">
  <title> 3D(입체감)효과를 준 메뉴구현. 기존 Drawables메뉴는 메인화면을 가리지만 향상된 메뉴는 가리지 않고 뷰를 좌측, 우측으로 배치하여 UI를 더 깔끔하게 해준다. </title>
  <div class="photoset-grid" data-layout="13">
      <img src="https://github.com/seochangwook/Android-Source-Code-Team-Project-Sample-Code-/blob/master/Project_SampleImage/screen_9.jpeg" width="250" height="350">
      <img src="https://github.com/seochangwook/Android-Source-Code-Team-Project-Sample-Code-/blob/master/Project_SampleImage/screen_10.jpeg" width="250" height="350">
  </div>
</p>

<< Sample image >>

<p align="left">
  <title> 나의 카테고리 화면에 대한 프로토타입 설계결과 (4개의 스크린샷, 기본적인 버튼 LockingTest, 클릭 이벤트 처리, RecyclerView를 Grid로 배치 등 -> 나머지 하단 매뉴와 위의 검색버튼은 뷰의 사이즈를 고려 후 제작 가능))</title>
  <div class="photoset-grid" data-layout="13">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_15.png" width="250" height="350">
  </div>
</p>

<p align="left">
  <div class="photoset-grid" data-layout="13">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_11.jpeg" width="250" height="350">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_12.jpeg" width="250" height="350">
  </div>
</p>

<p align="left">
  <div class="photoset-grid" data-layout="13">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_13.jpeg" width="250" height="350">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_14.jpeg" width="250" height="350">
  </div>
</p>

<< Sample image >>

<p align="left">
  <title> 태그기능을 이용한 뷰 구현 (태그추가, 삭제, 선택 가능)</title>
  <div class="photoset-grid" data-layout="13">
     <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_16.jpeg" width="250" height="350">
     <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_17.jpeg" width="250" height="350">
  </div>
</p>

<< Sample image >>

<p align="left">
  <title> Google과 Facebook, Twitter(Fabric)을 이용한 SNS Login연동 </title>
  <div class="photoset-grid" data-layout="13">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_18.jpeg" width="250" height="350">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_19.jpeg" width="250" height="350">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_24.png" width="250" height="350">
  </div>
</p>

<p align="left">
  <div class="photoset-grid" data-layout="13">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_20.jpeg" width="250" height="350">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_21.jpeg" width="250" height="350">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_25.jpeg" width="250" height="350">
  </div>
</p>

<p align="left">
  <div class="photoset-grid" data-layout="13">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_26.jpeg" width="250" height="350">
  </div>
</p>

<< Sample image >>

<p align="left">
  <title> 상세 기사 정보 보기 화면구성 </title>
  <div class="photoset-grid" data-layout="13">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_22.jpeg" width="250" height="350">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_23.jpeg" width="250" height="350">
  </div>
</p>

<< Sample image >>

<p align="left">
  <title> 스크롤 처리에 따른 화면구성(하단 네비게이션 메뉴 구현) </title>
  <div class="photoset-grid" data-layout="13">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_27.jpeg" width="250" height="350">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_28.jpeg" width="250" height="350">
  </div>
</p>

<< Sample image >>

<p align="left">
  <title> 메뉴부분 검색뷰로 전환 기능 </title>
  <div class="photoset-grid" data-layout="13">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_29.jpeg" width="250" height="350">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_30.jpeg" width="250" height="350">
  </div>
</p>

<< Sample image >>

<p align="left">
  <title> JSON Parsing과 Picaso를 이용한 RecyclerListView처리 </title>
  <div class="photoset-grid" data-layout="13">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_31.jpeg" width="250" height="350">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_32.jpeg" width="250" height="350">
  </div>
</p>

<< Sample image >>

<p align="left">
  <title> 하나의 액티비티에 리사이클뷰 2개 배치(Picaso Library의 Transformation적용) </title>
  <div class="photoset-grid" data-layout="13">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_33.jpeg" width="250" height="350">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_34.jpeg" width="250" height="350">
  </div>
</p>

<< Sample image >>

<p align="left">
  <title> ScrollView와 RecyclerView (OkHttp3사용) </title>
  <div class="photoset-grid" data-layout="13">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_35.jpeg" width="250" height="350">
  </div>
</p>

<< Sample image >>

<p align="left">
  <title> SharedPreference와 SQLiteDatabase를 이용해서 SplashActivity와 Chatting구현(GCM적용안함) </title>
  <div class="photoset-grid" data-layout="13">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_36.jpeg" width="250" height="350">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_37.jpeg" width="250" height="350">
  </div>
</p>

<p align="left">
  <div class="photoset-grid" data-layout="13">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_38.jpeg" width="250" height="350">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_39.jpeg" width="250" height="350">
  </div>
</p>

<p align="left">
  <div class="photoset-grid" data-layout="13">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_40.jpeg" width="250" height="350">
  </div>
</p>

<< Sample image >>

<p align="left">
  <title> 서버로 이미지 업로드 및 다운로드 구현 - (issue존재 -> 해결, Okhttp의 307에러는 해결) </title>
  <div class="photoset-grid" data-layout="13">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_41.jpeg" width="250" height="350">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_42.jpeg" width="250" height="350">
  </div>
</p>

<p align="left">
  <div class="photoset-grid" data-layout="13">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_43.jpeg" width="250" height="350">
  </div>
</p>

<p align="left">
  <title> # issue name : ViewPager간 이동 시 RecyclerView same id 문제(id충돌)</title>
  <title> # issue solution : android.R.id.list라는 안드로이드 시스템에서 정의된 id를 사용하여 Pager이동 시 동적으로 변하는 id에 대응 </title>
  <div class="photoset-grid" data-layout="13">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/issue_solution_image_1.png" width="450" height="350">
  </div>
</p>

<p align="left">
  <title>android:id="@+id/list" : 해당 어플리케이션에서 ID를 패키지 명 없이 새로 만들어서 사용. 사용방법은 R.id.list로 사용.</title>
  <title>android:id="@android:id/list" : 해당 어플리케이션에서 ID를 패키지 명을 추가하여 새로 만들어서 사용. 사용방법은 android.R.id.list로 사용. android라는 패키지는 이미 안드로이드가 사용하고 있고, android.R.id.list는 이미 등록되어 있어서 기존에 등록된 (android.jar에 포함된) ID를 사용</title>
</p>

<< Sample image >>

<p align="left">
  <title> PopupWindow화면에 다양한 위젯배치(ExpandableListView 포함) </title>
  <div class="photoset-grid" data-layout="13">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_44.jpeg" width="250" height="350">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_45.jpeg" width="250" height="350">
  </div>
</p>

<< Sample image >>

<p align="left">
  <title> blurry처리, 액티비티를 다이얼로그로 생성 </title>
  <div class="photoset-grid" data-layout="13">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_46.jpeg" width="250" height="350">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_47.jpeg" width="250" height="350">
  </div>
</p>

<< Sample image >>

<p align="left">
  <title> EditText에 이미지 넣기 </title>
  <div class="photoset-grid" data-layout="13">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_48.jpeg" width="250" height="350">
  </div>
</p>

<< Sample image >>

<p align="left">
  <title> 향상된 HashTag기능 </title>
  <div class="photoset-grid" data-layout="13">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_49.jpeg" width="250" height="350">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_50.jpeg" width="250" height="350">
  </div>
</p>

<< Sample image >>

<p align="left">
  <title> SNS 자동 로그인 기능 구현 (페이스북) </title>
  <div class="photoset-grid" data-layout="13">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_51.jpeg" width="250" height="350">
      <img src="https://github.com/seochangwook/Team-Project-Sample-Code-android-/blob/master/Project_SampleImage/screen_52.jpeg" width="250" height="350">
  </div>
</p>

# Design department issues

* UI 시나리오 뉴스잉 기획안 파일 저장
