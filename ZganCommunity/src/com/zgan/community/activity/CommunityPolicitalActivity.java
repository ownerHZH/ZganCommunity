package com.zgan.community.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.zgan.community.R;
import com.zgan.community.adapter.CommunityPolicitalAdapter;
import com.zgan.community.data.ContentData;
import com.zgan.community.data.MSZW_BGDD;
import com.zgan.community.data.News;
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
import com.zgan.community.tools.ZganCommunityStaticData;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class CommunityPolicitalActivity extends MainAcitivity {

	private Button back;
	private TextView title;

	private ListView list;
	private ListView list2;
	//private List<String> dList;//װ�����ݵ�List

	private Context con;
	private TabHost tabHost;
	private TabWidget tabWidget;
	private GestureDetector detector;

	private String st;
	private String Data;
	private Handler handler;
	private MyProgressDialog dialog;
	int did = 1;
	int sid = 1;

	private List<ContentData> contentDataList = new ArrayList<ContentData>();
	private List<MSZW_BGDD> MSZW_BGDDList = new ArrayList<MSZW_BGDD>();

	private CommunityPolicitalAdapter adapter;
	
	//private LinearLayout category;
	private Button buttonCity; //����������
	private Button buttonCounty;//��������
	private Button buttonCommonService;//��������λ

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_policital);		

		back = (Button) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		title.setText(R.string.community_policital);

		con = CommunityPolicitalActivity.this;
		list = (ListView) findViewById(R.id.listViewPolitical);
		list2 = (ListView) findViewById(R.id.listViewPolitical2);
		
		//category=(LinearLayout) findViewById(R.id.category);
		buttonCity=(Button) findViewById(R.id.buttonCity);
		buttonCounty=(Button) findViewById(R.id.buttonCounty);
		buttonCommonService=(Button) findViewById(R.id.buttonCommonService);
		
		ButtonClickListener l = new ButtonClickListener();
		back.setOnClickListener(l);
		buttonCity.setOnClickListener(l);
		buttonCounty.setOnClickListener(l);
		buttonCommonService.setOnClickListener(l);
		
		handler = new Handler();
		 
		//dialog = new MyProgressDialog(this);
		//dialog.start("�����У����Ժ�...");

		
		initTabHost(); // ��ʼ��TabHost
		// setTabViewParas();//����Tab��ʾ����
		// tabHost.setCurrentTab(1);
		tabHost.setOnTabChangedListener(new TabChangeListener());
		// tabHost.setCurrentTab(0);
		// �����ڲ����ʼ��
		detector = new GestureDetector(new MySimpleGestureDetector());
		getData();// ��ȡ��������
	}		

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return detector.onTouchEvent(event); // ����
	}

	/**
	 * ʶ�����Ƶ��ڲ���
	 * 
	 * @author Hzh
	 * 
	 */
	public class MySimpleGestureDetector extends
			GestureDetector.SimpleOnGestureListener {

		private static final int MIN_DISTANCE = 100; // ��С����
		private static final int MIN_VELOCITY = 100; // ��С��������

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (Math.abs(velocityX) > MIN_VELOCITY) {
				if ((e2.getX() - e1.getX()) > MIN_DISTANCE) { // ���һ���
					flingRight();
				} else if ((e1.getX() - e2.getX()) > MIN_DISTANCE) { // ���󻬶�
					flingLeft();
				}
			}
			return super.onFling(e1, e2, velocityX, velocityY);
		}

		public void flingLeft() {
			int currentTab = tabHost.getCurrentTab();
			int count = tabHost.getTabWidget().getChildCount();
			if (currentTab != 0) {
				currentTab--;
				switchTab(currentTab);
			} else if (currentTab == 0) {
				switchTab(count - 1);
			}
		}

		public void flingRight() {
			int currentTab = tabHost.getCurrentTab();
			int count = tabHost.getTabWidget().getChildCount();
			if (currentTab != count - 1) {
				currentTab++;
				switchTab(currentTab);
			} else if (currentTab == count - 1) {
				switchTab(0);
			}
		}

		private void switchTab(final int toTab) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					tabHost.post(new Runnable() {
						@Override
						public void run() {
							tabHost.setCurrentTab(toTab);
						}
					});
				}
			}).start();
		}
	}

	/**
	 * Tab�л�������
	 * 
	 * @return
	 */
	public class TabChangeListener implements OnTabChangeListener {

		public void onTabChanged(String str) {
			setTabBackground();
			if (str.equals("tab1")) {
				if(contentDataList.size()>0)
				{
					showData(1);
				}else
				{
					getData();
				}
				
			} else if (str.equals("tab2")) {
			    getData_b(-1);			
			}
		}
	}

	/**
	 * ����Tab�л�����
	 */
	@SuppressWarnings("deprecation")
	public void setTabBackground() {
		// ����Tab����
		View v0=tabWidget.getChildTabViewAt(0);
		View v1=tabWidget.getChildTabViewAt(1);
		//int count = tabWidget.getChildCount();
		if (tabHost.getCurrentTab() == 0) {
			//v.setBackgroundColor(Color.WHITE);
			// ����������Լ�����һ��ͼƬ��Ϊ��������				
		    v0.setBackgroundDrawable(getResources().getDrawable(R.drawable.zheng2));
		    v1.setBackgroundDrawable(getResources().getDrawable(R.drawable.ban1));
			
		} else {
			//v.setBackgroundColor(Color.GRAY);
		    v0.setBackgroundDrawable(getResources().getDrawable(R.drawable.zheng1));
		    v1.setBackgroundDrawable(getResources().getDrawable(R.drawable.ban2));
		}
	}

	/**
	 * ��ʼ��TabHost
	 */
	public void initTabHost() {
		tabHost = (TabHost) findViewById(R.id.tabhost);
		tabHost.setup();
		// ����Tab����ʽ
		tabWidget = tabHost.getTabWidget();

		tabHost.addTab(tabHost
				.newTabSpec("tab1")
				.setIndicator(null,null)
			    .setContent(R.id.listViewPolitical));

		tabHost.addTab(tabHost
				.newTabSpec("tab2")
				.setIndicator(null,null)
				.setContent(R.id.linearLayoutPolitical2));
		setTabViewParas();// ����Tab��ʾ����
		setTabBackground();// ��һ��������ʾ����ɫ
		// showData(1);//��ʾ��һҳ������

	}

	/**
	 * ����TabView�������С���߶�
	 */
	public void setTabViewParas() {
		int count = tabWidget.getChildCount();// TabHost����һ��getTabWidget()�ķ���
		for (int i = 0; i < count; i++) {
			View view = tabWidget.getChildTabViewAt(i);
			view.getLayoutParams().height = 78; // tabWidget.getChildAt(i)
			// view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
			/*final TextView tv = (TextView) view
					.findViewById(android.R.id.title);
			tv.setTextSize(20);
			tv.setTextColor(this.getResources().getColorStateList(
					android.R.color.black));*/
			Field mBottomLeftStrip;
	        Field mBottomRightStrip;
			 if (Float.valueOf(Build.VERSION.RELEASE.substring(0, 3)) <= 2.1) {
	               try {
	                  mBottomLeftStrip = tabWidget.getClass().getDeclaredField ("mBottomLeftStrip");
	                  mBottomRightStrip = tabWidget.getClass().getDeclaredField ("mBottomRightStrip");
	                  if(!mBottomLeftStrip.isAccessible()) {
	                    mBottomLeftStrip.setAccessible(true);
	                  }
	                  if(!mBottomRightStrip.isAccessible()){
	                    mBottomRightStrip.setAccessible(true);
	                  }
	                mBottomLeftStrip.set(tabWidget, getResources().getDrawable (R.drawable.no));
	                mBottomRightStrip.set(tabWidget, getResources().getDrawable (R.drawable.no));
	                 
	               } catch (Exception e) {
	                 e.printStackTrace();
	               }
	        } else {
	         
	         //�����2.2,2.3�汾����,����ʹ��һ�·���tabWidget.setStripEnabled(false)
	         //tabWidget.setStripEnabled(false);
	         
	         //���Ǻܿ����㿪����androidӦ����2.1�汾��
	         //tabWidget.setStripEnabled(false)���������޷�ʶ��������,��ʱ��Ȼ����ʹ�������
	         //����ʵ�֣����Ǵ���ĸĸ�
	         
	          try {
	           //2.2,2.3�ӿ���mLeftStrip��mRightStrip������������Ȼ���������沿���ظ���
	                 mBottomLeftStrip = tabWidget.getClass().getDeclaredField ("mLeftStrip");
	                 mBottomRightStrip = tabWidget.getClass().getDeclaredField ("mRightStrip");
	                 if(!mBottomLeftStrip.isAccessible()) {
	                   mBottomLeftStrip.setAccessible(true);
	                 }
	                 if(!mBottomRightStrip.isAccessible()){
	                   mBottomRightStrip.setAccessible(true);
	                 }
	               mBottomLeftStrip.set(tabWidget, getResources().getDrawable (R.drawable.no));
	               mBottomRightStrip.set(tabWidget, getResources().getDrawable (R.drawable.no));
	                
	              } catch (Exception e) {
	                e.printStackTrace();
	              }
	        }
		}
	}

	public class ButtonClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;
			case R.id.buttonCity:
				//���������ŵ���¼�
                 getData_b(0);
				break;
			case R.id.buttonCounty:
				//������������¼�
				getData_b(1);
				break;
			case R.id.buttonCommonService:
				//��������λ����¼�
				getData_b(2);
				break;
			default:
				break;
			}
		}

	}

	private void showData(int i) {
		// dList=new ArrayList<String>();
		if (i == 1) {
			adapter = new CommunityPolicitalAdapter(con, contentDataList, 1);
			list.setDivider(null);// ����ListViewû�зָ���
			list.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		} else {
			adapter = new CommunityPolicitalAdapter(con, MSZW_BGDDList, 2);
			list2.setDivider(null);// ����ListViewû�зָ���
			list2.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}

	}

	// ��ȡ��������
	//������������
		private void getData() {
			// TODO Auto-generated method stub
			//newsList = new ArrayList<News>();

			HttpClientService svr = new HttpClientService(
					AppConstants.HttpHostAdress+"zgancontent.aspx");//"http://community1.zgantech.com/ZganNews.aspx?did=15923258890"
			//����
			svr.addParameter("did",ZganCommunityStaticData.User_Number);
					
			HttpAndroidTask task = new HttpAndroidTask(con, svr,
					new HttpResponseHandler() {
						// ��Ӧ�¼�
						@SuppressWarnings("unchecked")
						public void onResponse(Object obj) {
							dialog.stop();
							JsonEntity jsonEntity = GsonUtil.parseObj2JsonEntity(
									obj,con,false);
							if (jsonEntity.getStatus() == 1) {
								handler.post(none);
							} else if (jsonEntity.getStatus() == 0) {
								contentDataList=(List<ContentData>) GsonUtil.getData(
											jsonEntity,AppConstants.type_contentDataList);	
									
									if(contentDataList.size()>0)
					                {
					                	//�����ݵ�ʱ�����
										handler.post(r);
					                }else
					                {
					                	//û������ʱ����ʾ
					                	handler.post(none);
					                }														
							}														
						}
					}, new HttpPreExecuteHandler() {
						public void onPreExecute(Context context) {
							dialog = new MyProgressDialog(context);
							DialogUtil.setAttr4progressDialog(dialog);
						}
					});
			task.execute(new String[] {});	
		}
		
		private void getData_b(int flid) {
			// TODO Auto-generated method stub
			//newsList = new ArrayList<News>();

			HttpClientService svr = new HttpClientService(
					AppConstants.HttpHostAdress+"zgancontent.aspx");//"http://community1.zgantech.com/ZganNews.aspx?did=15923258890"
			//����
			svr.addParameter("did",ZganCommunityStaticData.User_Number);
			if(flid==-1)
			{
				svr.addParameter("method","bgdd");
			}else
			{
				svr.addParameter("method","bgddfl");
				svr.addParameter("flid",flid);
			}
			
					
			HttpAndroidTask task = new HttpAndroidTask(con, svr,
					new HttpResponseHandler() {
						// ��Ӧ�¼�
						@SuppressWarnings("unchecked")
						public void onResponse(Object obj) {
							dialog.stop();
							JsonEntity jsonEntity = GsonUtil.parseObj2JsonEntity(
									obj,con,false);
							if (jsonEntity.getStatus() == 1) {
								handler.post(none);
							} else if (jsonEntity.getStatus() == 0) {
								MSZW_BGDDList.clear();
								MSZW_BGDDList=(List<MSZW_BGDD>) GsonUtil.getData(
											jsonEntity,AppConstants.type_mSZW_BGDDList);	
									
									if(MSZW_BGDDList.size()>0)
					                {
					                	//�����ݵ�ʱ�����
										handler.post(r_b);
					                }else
					                {
					                	//û������ʱ����ʾ
					                	handler.post(none_b);
					                }														
							}														
						}
					}, new HttpPreExecuteHandler() {
						public void onPreExecute(Context context) {
							dialog = new MyProgressDialog(context);
							DialogUtil.setAttr4progressDialog(dialog);
						}
					});
			task.execute(new String[] {});	
		}


	// �����ݴ������
	Runnable none = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// dialog.dismiss();
			dialog.stop();
			Toast.makeText(con, "û�пɹ����ص�����", 2).show();
		}
	};

	// ���ݼ�����֮��Ĳ���
	Runnable r = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// dialog.dismiss();
			if (contentDataList.size() <= 0) {
				Toast.makeText(con, "ʵʱ����û�пɼ��ص�����", 2).show();
			} else {
				tabHost.setCurrentTabByTag("tab1");// ѡ�е�һ��Tab
				showData(1);// ��ʼ������
			}

		}
	};
	
	// �����ݴ������
		Runnable none_b = new Runnable() {

			@Override
			public void run() {
				Toast.makeText(con, "û�пɹ����ص�����", 2).show();
			}
		};

		// ���ݼ�����֮��Ĳ���
		Runnable r_b = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// dialog.dismiss();
				if (MSZW_BGDDList.size() <= 0) {
					Toast.makeText(con, "û�пɼ��ص�����", 2).show();
				} else {
					tabHost.setCurrentTabByTag("tab2");// ѡ�е�һ��Tab
					showData(2);// ��ʼ������
				}

			}
		};

	public boolean isNotNull(String str) {
		return ((str != null) && (str != "") && (!str.equals(null)) && (!str
				.equals("")));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.activity_community_policital, menu);
		return true;
	}

}
