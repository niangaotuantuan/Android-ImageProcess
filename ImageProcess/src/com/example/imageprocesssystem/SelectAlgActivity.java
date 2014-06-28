package com.example.imageprocesssystem;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
public class SelectAlgActivity extends Activity implements OnItemClickListener{
	private static final String DYNAMICACTION_Broadcast = "Broadcast.selectAlg";
	private ListView listView;
	public void sendFlagToActivity(String flag){
		Intent intent = new Intent();
		intent.setAction(DYNAMICACTION_Broadcast);
		intent.putExtra("selectFlag", flag);
		sendBroadcast(intent);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Choose Image processing type!");
		listView = new ListView(this);
		List<String> list=getData();
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,list);
		listView.setAdapter(adapter);
		setContentView(listView);
		listView.setOnItemClickListener(this);//绑定监听接口	
	}
	private List<String> getData(){
		List<String> data = new ArrayList<String>();
		data.add("椒盐噪声");
		data.add("中值滤波");
		data.add("平滑滤波");
		return data;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		//finish();
		String posString=Integer.toString(position);
		sendFlagToActivity(posString);
		finish();
	}
}
