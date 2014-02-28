package com.zgan.community.activity;

import java.util.ArrayList;
import java.util.List;
import com.zgan.community.R;
import com.zgan.community.data.ServiceInfo;
import com.zgan.community.adapter.CommunityServiceAdapter;
import com.zgan.community.jsontool.AppConstants;
import com.zgan.community.jsontool.DialogUtil;
import com.zgan.community.jsontool.GsonUtil;
import com.zgan.community.jsontool.HttpAndroidTask;
import com.zgan.community.jsontool.HttpClientService;
import com.zgan.community.jsontool.HttpPreExecuteHandler;
import com.zgan.community.jsontool.HttpResponseHandler;
import com.zgan.community.jsontool.JsonEntity;
import com.zgan.community.tools.MainAcitivity;
import com.zgan.community.tools.MyProgressDialog;
import com.zgan.community.tools.StringTypeToInt;
import com.zgan.community.tools.ZganCommunityStaticData;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CommunityServiceListActivity extends MainAcitivity {
	Button back;
	TextView top_title;

	ListView listview;
	
	private Context con=null;
	private MyProgressDialog pdialog;
	//private List<CommunityService> dataList;
	private List<ServiceInfo> serviceInfoList;

	private CommunityServiceAdapter communityServiceAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.line_recinfo_son);

		back = (Button) findViewById(R.id.back);
		top_title = (TextView) findViewById(R.id.title);
		listview = (ListView) findViewById(R.id.list_reinfo);
		listview.setDivider(null);
		back.setOnClickListener(listener);
		con=CommunityServiceListActivity.this;
		ShowData();

	}

	private void ShowData() {
		// TODO Auto-generated method stub
		if ((getIntent().getExtras().getString("button_key")) != null
				&& !"".equals(getIntent().getExtras().getString("button_key"))) {
			String button_key = getIntent().getExtras().getString("button_key");

			top_title.setText(button_key);
			
			//if (button_key.equals("���")) {
				serviceInfoList = new ArrayList<ServiceInfo>();

				HttpClientService svr = new HttpClientService(
						AppConstants.HttpHostAdress+"ZganCommunityService.aspx");
				//����
				svr.addParameter("did",ZganCommunityStaticData.User_Number);
				svr.addParameter("sid", StringTypeToInt.convertTypeToInt(button_key));
				
				HttpAndroidTask task = new HttpAndroidTask(con, svr,
						new HttpResponseHandler() {
							// ��Ӧ�¼�
							@SuppressWarnings("unchecked")
							public void onResponse(Object obj) {
								pdialog.stop();
								JsonEntity jsonEntity = GsonUtil.parseObj2JsonEntity(
										obj,con,false);
								if (jsonEntity.getStatus() == 1) {
									Toast.makeText(con, "û�пɲ鿴������", 2).show();
								} else if (jsonEntity.getStatus() == 0) {
									serviceInfoList=(List<ServiceInfo>) GsonUtil.getData(
											jsonEntity,AppConstants.type_serviceInfoList);	
									
									if(serviceInfoList.size()>0)
					                {
					                	//�����ݵ�ʱ�����
					                	communityServiceAdapter = new CommunityServiceAdapter(
					    						CommunityServiceListActivity.this, serviceInfoList);
					    				listview.setAdapter(communityServiceAdapter);
					                }else
					                {
					                	//û������ʱ����ʾ
					                	Toast.makeText(con, "û�пɲ鿴������", 2).show();
					                	//finish();//�����ϲ����
					                }
								}else
								{
									Toast.makeText(con, "���������ݳ���", 2).show();
								}
							}
						}, new HttpPreExecuteHandler() {
							public void onPreExecute(Context context) {
								pdialog = new MyProgressDialog(context);
								DialogUtil.setAttr4progressDialog(pdialog);
							}
						});
				task.execute(new String[] {});				           
				
			/*else
			{
				serviceInfoList = new ArrayList<ServiceInfo>();
				serviceInfoList.add(new ServiceInfo("�ипƼ���������С��", "�������ϰ�������·",
						"18623648313"));
				serviceInfoList.add(new ServiceInfo("�ипƼ���������С��", "�������ϰ�������·8��",
						"18716341029"));

				communityServiceAdapter = new CommunityServiceAdapter(
						CommunityServiceListActivity.this, serviceInfoList);
				listview.setAdapter(communityServiceAdapter);
			}*/
		}
	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	};

}
