package com.scc.jittervideodemo;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.scc.jittervideodemo.fragment.ListFragment;
import com.scc.jittervideodemo.fragment.Page2Fragment;
import com.scc.jittervideodemo.fragment.PageFragment;
import com.scc.jittervideodemo.fragment.Recode2Fragment;
import com.scc.jittervideodemo.fragment.RecodeFragment;
import com.scc.jittervideodemo.utils.FragmentUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.framenlayout)
    FrameLayout framenlayout;
    @BindView(R.id.tv_page)
    TextView tvPage;
    @BindView(R.id.tv_page2)
    TextView tvPage2;
    @BindView(R.id.tv_list)
    TextView tvList;
    @BindView(R.id.tv_record)
    TextView tvRecord;
    @BindView(R.id.tv_record2)
    TextView tvRecord2;
    private PageFragment pageFragment;
    private Page2Fragment page2Fragment;
    private ListFragment listFragment;
    private RecodeFragment recodeFragment;
    private Recode2Fragment recode2Fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // 默认显示第一个
        getPageFrament();
    }

    @OnClick({R.id.tv_page, R.id.tv_page2, R.id.tv_list, R.id.tv_record, R.id.tv_record2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_page:
                getPageFrament();
                break;
            case R.id.tv_page2:
                getPage2Frament();
                break;
            case R.id.tv_list:
                getListFrament();
                break;
            case R.id.tv_record:
                getRecodeFrament();
                break;
            case R.id.tv_record2:
                getRecode2Frament();
                break;
        }
    }

    /**
     * 展示第一个tab(翻页)
     */
    private void getPageFrament(){
        if (pageFragment == null) {
            pageFragment = new PageFragment();
        }
        FragmentUtil.showFragment(this, R.id.framenlayout, pageFragment);
    }

    /**
     * 展示第二个tab(翻页2)
     */
    private void getPage2Frament(){
        if (page2Fragment == null) {
            page2Fragment = new Page2Fragment();
        }
        FragmentUtil.showFragment(this, R.id.framenlayout, page2Fragment);
    }

    /**
     * 展示第三个tab(列表)
     */
    private void getListFrament(){
        if (listFragment == null) {
            listFragment = new ListFragment();
        }
        FragmentUtil.showFragment(this, R.id.framenlayout, listFragment);
    }

    /**
     * 展示第四个tab(录制)
     */
    private void getRecodeFrament(){
        if (recodeFragment == null) {
            recodeFragment = new RecodeFragment();
        }
        FragmentUtil.showFragment(this, R.id.framenlayout, recodeFragment);
    }

    /**
     * 展示第五个tab(录制2)
     */
    private void getRecode2Frament(){
        if (recode2Fragment == null) {
            recode2Fragment = new Recode2Fragment();
        }
        FragmentUtil.showFragment(this, R.id.framenlayout, recode2Fragment);
    }
}
