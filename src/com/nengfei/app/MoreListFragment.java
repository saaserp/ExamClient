package com.nengfei.app;

import java.util.ArrayList;

import com.nengfei.adapter.MyTextAdapter;
import com.nengfei.backup.PullDataTask;
import com.nengfei.login.LoginActivity;
import com.nengfei.util.CallBack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A list fragment representing a list of Detail. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link MoreDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class MoreListFragment extends Fragment implements OnItemClickListener {
	boolean isExit = false;
	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onMoreItemSelected(int position);
	}

	private ListView listview;
	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onMoreItemSelected(int position) {
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public MoreListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	ArrayList<String> contents;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_more, null);
		listview = (ListView) view.findViewById(R.id.listview_morelist);
		contents = new ArrayList<String>();

		String[] items = getActivity().getResources().getStringArray(R.array.more_items);
		for (int i = 0; i < items.length; i++) {
			contents.add(items[i]);
		}

		contents.add(getActivity().getResources().getString(R.string.share_app_to_friend));
		//这里是重新选择数据库的代码
	//	contents.add("重新选择题库");
	 

		listview.setAdapter(new MyTextAdapter(getActivity(), contents));
		listview.setOnItemClickListener(this);
		listview.setPadding(0, 5, 0, 0);
		view.findViewById(R.id.exit).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (isExit == false) {
					isExit = true;
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								Thread.sleep(3000);

							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							isExit = false;
						}
					}).start();
					if (!LoginActivity.haslogin()) {
						Toast.makeText(getActivity(), "再按一次跳转至登录", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getActivity(), "再按一次退出账号", Toast.LENGTH_SHORT).show();
					}
				} else if (isExit == true) {
					if (LoginActivity.uid == null || LoginActivity.uid.equals("")
							|| LoginActivity.uid.equals("anonymous")) {
						getActivity().getSharedPreferences("user", Activity.MODE_PRIVATE).edit()
								.putString("uid", "anonymous").commit();

						getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));

					} else {

						new PullDataTask(getActivity(), new CallBack() {

							@Override
							public String done(boolean b) {
								// TODO Auto-generated method stub

								if (b) {

									getActivity().getSharedPreferences("user", Activity.MODE_PRIVATE).edit()
											.putString("uid", "anonymous").commit();
									getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
									LoginActivity.uid = "";

								} else {
									Toast.makeText(getActivity(), "无法退出当前账户", Toast.LENGTH_SHORT).show();
								}
								return null;
							}

						}).execute();

					}
				}
			}
		});

		return view;

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
		}
		// view.setBackgroundResource(R.drawable.bg_main_more);

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException("Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	void toBack(View v) {

	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		listview.setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			listview.setItemChecked(mActivatedPosition, false);
		} else {
			listview.setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		mCallbacks.onMoreItemSelected(position);
	}
}
