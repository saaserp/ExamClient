package com.wencai.adapter;

import java.util.List;

import com.wencai.app.ClassicsListFragment;
import com.wencai.app.ExamFragment;
import com.wencai.app.MoreListFragment;
import com.wencai.widget.IconPagerAdapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainTabPagerAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
	private List<String> titles;
	private List<Integer> icons; 
	private static final int PAGE_EXAM=0;
	private static final int PAGE_CLASSICS=1;
	private static final int PAGE_MORE=2;
	public MainTabPagerAdapter(List<String> titles, List<Integer> icons,
			FragmentManager fm) {
		super(fm);
		this.titles = titles;
		this.icons = icons;	
	}

	public List<String> getTitles() {
		return titles;
	}

	public List<Integer> getIcons() {
		return icons;
	}

	@Override
	public int getIconResId(int index) {
		// TODO Auto-generated method stub
		return icons.get(index % icons.size());
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return titles.size();
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		Fragment fragment=null;
		switch(position){
		case PAGE_EXAM:

			fragment=new ExamFragment();
			break;
		case PAGE_CLASSICS:
			fragment=new ClassicsListFragment();
			break;
		case PAGE_MORE:
			fragment=new MoreListFragment();
			break;
		default:
			break;
		}
		return fragment;
	}

	@Override
	public String getTextResId(int index) {
		// TODO Auto-generated method stub

		return titles.get(index);
	}


}
