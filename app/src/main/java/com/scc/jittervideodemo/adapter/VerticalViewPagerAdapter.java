package com.scc.jittervideodemo.adapter;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;

import com.scc.jittervideodemo.fragment.VideoFragment;

import java.util.List;

/**
 * @author
 * @data 2019/11/26
 */
public class VerticalViewPagerAdapter extends PagerAdapter {
    /**
     * 视频URL
     */
    private List<String> mList;
    /**
     * FragmentManager
     */
    private FragmentManager fragmentManager;
    /**
     * 事务
     */
    private FragmentTransaction fragmentTransaction;

    public void setmList(List<String> mList) {
        this.mList = mList;
    }

    public VerticalViewPagerAdapter(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    /**
     * 当前item
     */
    private Fragment mCurrentPrimaryItem = null;

    @Override
    public int getCount() {
        return mList.size();
    }

    /**
     * 添加fragment
     *
     * @param container
     * @param position
     * @return
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (fragmentTransaction == null) {  // 开启事务
            fragmentTransaction = fragmentManager.beginTransaction();
        }
        // viewpager嵌套fragment
        VideoFragment videoFragment = new VideoFragment();
        if (mList != null && mList.size() > 0) {
            // 给fragment传递URL地址
            Bundle bundle = new Bundle();
            if (position >= mList.size()) { // 当前position超出集合长度，就%集合长度，防止越界
                bundle.putString(VideoFragment.URL, mList.get(position % mList.size()));
            } else {
                bundle.putString(VideoFragment.URL, mList.get(position));
            }
            videoFragment.setArguments(bundle);
        }

        fragmentTransaction.add(container.getId(), videoFragment,
                makeFragmentName(container.getId(), position));
        return videoFragment;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);
        // 移除viewpager
        if (fragmentTransaction == null) {
            fragmentTransaction = fragmentManager.beginTransaction();
        }
        fragmentTransaction.detach((Fragment) object);
        fragmentTransaction.remove((Fragment) object);
    }

    private String makeFragmentName(int viewId, int position) {
        return "android:switcher:" + viewId + position;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return ((Fragment) object).getView() == view;
    }


    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Fragment fragment = (Fragment) object;
        if (fragment != mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem.setMenuVisibility(false);
                mCurrentPrimaryItem.setUserVisibleHint(false);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
            mCurrentPrimaryItem = fragment;
        }
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if (fragmentTransaction != null) {
            fragmentTransaction.commitNowAllowingStateLoss();
            fragmentTransaction = null;
        }
    }
}
