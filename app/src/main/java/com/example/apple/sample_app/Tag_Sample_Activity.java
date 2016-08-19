package com.example.apple.sample_app;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import jp.wasabeef.richeditor.RichEditor;
import mabbas007.tagsedittext.TagsEditText;
import me.gujun.android.taggroup.TagGroup;

public class Tag_Sample_Activity extends AppCompatActivity implements TagsEditText.TagsEditListener {

    /**
     * HashTag관련
     **/
    private static final String TAG = "Tap_Sample_Activity";
    Button enroll_tag_button;
    List<String> tag_array = new ArrayList<>(); //태그배열(원본 에터에서 가져온 데이터)//
    List<String> tag_layout_array = new ArrayList<>();
    String plain_text = null;
    private TagsEditText mTagsEditText; //태그를 지정할 수 있는 에디트 택스트.//
    //태그 스타일//
    private TagGroup mDefaultTagGroup;
    private TagGroup mSmallTagGroup;
    private TagGroup mLargeTagGroup;
    private TagGroup mBeautyTagGroup;
    private TagGroup mBeautyInverseTagGroup;
    private RichEditor mEditor;
    private TextView mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_sample_layout);

        mTagsEditText = (TagsEditText) findViewById(R.id.tagsEditText);
        enroll_tag_button = (Button) findViewById(R.id.enroll_tag_button);

        mBeautyTagGroup = (TagGroup) findViewById(R.id.tag_group_beauty);

        //해시테그를 입력받을 에디트 텍스트 설정.//
        mTagsEditText.setHint("Enter names of fruit");
        mTagsEditText.setTagsListener(this);
        mTagsEditText.setTagsWithSpacesEnabled(true);
        mTagsEditText.setThreshold(1);

        mEditor = (RichEditor) findViewById(R.id.editor);
        mPreview = (TextView) findViewById(R.id.preview);

        //RichEditor설정.//
        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(22);
        mEditor.setEditorFontColor(Color.RED);

        mEditor.setPadding(10, 10, 10, 10);
        mEditor.insertImage("/ https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv96cmTJb3dI5zni04JuC8WVy1FVpbeU9tMP6W76E85uk4KrXXGrxya2DAXY0tbeT77zq2Vh-pIX4WV6WMqRYpV5Sw6pGFn3Eo1I0XRmnj-DWaxWaGhdRUAQrxC8kQ2h_RTzOhUPSWoAUrcp7IRy6ebj9SSn3UHABhKmz3uN-EEUzANL0Ejg",
                "dachshund");
        mEditor.setPlaceholder("Insert text here...");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        enroll_tag_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < tag_array.size(); i++) {
                    Log.d("tag name : ", "" + tag_array.get(i).toString());
                }

                int last_index = tag_array.size();

                if (last_index > 0) {
                    String original_tag_str = tag_array.get(last_index - 1).toString().replace("[", ""); //양쪽 공백제거.//
                    original_tag_str = original_tag_str.replace("]", "");

                    Log.d("original str : ", original_tag_str);

                    String str_data_array[] = original_tag_str.split(",");

                    for (int i = 0; i < str_data_array.length; i++) {
                        tag_layout_array.add("#" + str_data_array[i].trim());
                    }

                    for (int i = 0; i < tag_layout_array.size(); i++) {
                        //해시태그 모양 만들기.//
                        Log.d("array: ", "#" + tag_layout_array.get(i).toString());
                    }

                    //만들어진 태그값을 전용 레이아웃에 구성//
                    //mTagGroup.setTags(str_data_array);
                    mBeautyTagGroup.setTags(tag_layout_array);

                    //초기화.//
                    tag_layout_array.clear();
                    mTagsEditText.setText("");
                    mTagsEditText.setTags(null); //저장된 태그 내용 초기화.//
                } else {
                    Log.d("ERROR : ", "태그정보가 없습니다");
                }
            }
        });

        mBeautyTagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                Toast.makeText(Tag_Sample_Activity.this, "태그 클릭 : " + tag, Toast.LENGTH_SHORT).show();
            }
        });

        /** RichEditorr관련 메소드 **/
        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                plain_text = text;

                Log.d("text : ", plain_text);

                mPreview.setText(text);

                plain_text = null;
            }
        });

        findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.undo();
            }
        });

        findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.redo();
            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBold();
            }
        });

        findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setItalic();
            }
        });

        findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setSubscript();
            }
        });

        findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setSuperscript();
            }
        });

        findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setStrikeThrough();
            }
        });

        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setUnderline();
            }
        });

        findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(1);
            }
        });

        findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(2);
            }
        });

        findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(3);
            }
        });

        findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(4);
            }
        });

        findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(5);
            }
        });

        findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(6);
            }
        });

        findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                mEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setIndent();
            }
        });

        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setOutdent();
            }
        });

        findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignLeft();
            }
        });

        findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignCenter();
            }
        });

        findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignRight();
            }
        });

        findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBlockquote();
            }
        });

        findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.insertImage("/storage/emulated/0/AppInventor/assets/robot.jpg",
                        "dachshund");

                Log.d("editor : ", "image insert success");
            }
        });

        findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.insertLink("https://github.com/wasabeef", "wasabeef");
            }
        });
        findViewById(R.id.action_insert_checkbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.insertTodo();
            }
        });


    }

    @Override
    public void onTagsChanged(Collection<String> tags) {
        Log.d(TAG, "Tags changed: ");
        Log.d(TAG, Arrays.toString(tags.toArray()));

        //Collection된 태그의 정보를 배열로 이동.//
        tag_array.addAll(Arrays.asList(String.valueOf(tags)));
    }

    @Override
    public void onEditingFinished() {
        Log.d(TAG, "OnEditing finished");
    }
}
