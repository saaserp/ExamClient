package com.wencai.app;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wencai.model.QuestionBankService;

public class ClassicsActivity extends BaseActivity {
	private QuestionBankService questionBankService;
	private TextView tv_title;
	private TextView tv_classics_question;
	private TextView tv_classics_answer;
	private Button btn_classics_show_answer;
	private boolean bl_answer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		bl_answer = true;
		setContentView(R.layout.activity_classics);
		questionBankService = new QuestionBankService();
		Map<String, Object> enerty = questionBankService.getEntry(this, String.valueOf(getIntent().getIntExtra("questionId", 1)));
		tv_title= (TextView) findViewById(R.id.tv_title);
		tv_classics_question = (TextView) findViewById(R.id.tv_classics_question);
		tv_classics_answer = (TextView) findViewById(R.id.tv_classics_answer);
		btn_classics_show_answer = (Button) findViewById(R.id.btn_classics_show_answer);
		tv_title.setText(getResources().getString(R.string.classics_title));
		tv_classics_question.setText(enerty.get("question").toString());
		tv_classics_question.setTextSize(22);
		tv_classics_answer.setText(enerty.get("answer").toString());
		tv_classics_answer.setTextSize(18);
		tv_classics_answer.setVisibility(LinearLayout.INVISIBLE);
	}

	public void showAnswer(View view) {
		if (bl_answer) {
			btn_classics_show_answer.setText(getResources().getString(
					R.string.classics_not_show_answer));
			tv_classics_answer.setVisibility(LinearLayout.VISIBLE);
			bl_answer = false;
		} else {
			btn_classics_show_answer.setText(getResources().getString(
					R.string.classics_show_answer));
			tv_classics_answer.setVisibility(LinearLayout.INVISIBLE);
			bl_answer = true;
		}
	}

	
}
