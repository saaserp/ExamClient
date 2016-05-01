package com.nengfei.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.nengfei.model.ExamResultEntry;
import com.nengfei.model.ExamResultService;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class RecordActivity extends BaseActivity {
	TextView tv_title;
	ListView lv_record;
	ArrayList<ExamResultEntry> entrylist;
	ExamResultService examResultService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_record);
		tv_title = (TextView) findViewById(R.id.tv_title);
		lv_record = (ListView) findViewById(R.id.lv_record);
		entrylist = new ArrayList<ExamResultEntry>();
		tv_title.setText(getResources().getString(R.string.record_title));
		ExamResultService examResultService = new ExamResultService();
		final ArrayList<Map<String, Object>> getEntryList = examResultService
				.getRecordEntryList(this);
		ArrayList<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
		int count=1;
		for (Map<String, Object> entry : getEntryList) {
			Map<String, Object> itemMap = new HashMap<String, Object>();
			itemMap.put("image", R.drawable.saturn__jiakao_faxian_shai_icon_chengji);
			itemMap.put(
					"content",
					getString(R.string.record_times) + count
							+ getString(R.string.record_exam) + "("
							+ entry.get("totalScore")
							+ getString(R.string.record_score) + ")");
			itemMap.put("dateTime", entry.get("dateTime"));
			itemList.add(itemMap);
			entrylist.add(new ExamResultEntry((Integer) entry.get("_id"),
					(Integer) entry.get("totalScore"), (Integer) entry
							.get("rightCount"), (Integer) entry
							.get("wrongCount"), (Integer) entry
							.get("totalCount"), (String) entry.get("dateTime"),
					(String) entry.get("useTime")));
			count++;
		}
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, itemList,
				R.layout.list_record, new String[] { "image", "content",
						"dateTime" }, new int[] { R.id.image,
						R.id.record_times, R.id.record_datatimes });
		simpleAdapter.notifyDataSetChanged();
		lv_record.setAdapter(simpleAdapter);
		lv_record.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(RecordActivity.this,
						DetailsRecordActivity.class);
				intent.putExtra("MODE", 2);
				intent.putExtra("examResult", entrylist.get(position));
				startActivity(intent);
				finish();
			}
		});
	}

	public void clearRecord(View view) {
		examResultService = new ExamResultService();
		for (ExamResultEntry entry : entrylist) {
			examResultService.delete(RecordActivity.this, entry.get_id());
		}
		finish();
	}
}
