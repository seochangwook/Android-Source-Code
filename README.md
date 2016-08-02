# Team Project Sample Code (android) - Newsing IT App -

# Planning Sector issues 

* Project UI Prototype URL : https://ovenapp.io/view/TPndbnvzLfjpvM47zW2zUx3KPLqIz0Y0



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
* compile 'com.hkm.taglib:tag:1.6.0' // 태그관련 라이브러리
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

# Design department issues

*
