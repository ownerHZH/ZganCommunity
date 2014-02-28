package com.zgan.community.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.zgan.community.R;
import com.zgan.community.adapter.CommunityNewsAdapter;
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
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

public class CommunityNewNotificationActivity extends MainAcitivity {

	private Button back;
	private TextView title;

	private ListView list;
	private ListView list2;
	// private List<String> dList;//װ�����ݵ�List

	private Context con;
	private TabHost tabHost;
	private TabWidget tabWidget;
	private GestureDetector detector;

	private Handler handler;
	private MyProgressDialog dialog;
	int did = 1;
	int sid = 1;

	private List<News> newsList=new ArrayList<News>();
	private List<News> newsList_s=new ArrayList<News>();
	private MyProgressDialog pdialog;
	private CommunityNewsAdapter communityNewsAdapter;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_policital_news);

		back = (Button) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		title.setText(R.string.community_notification_title);

		con = CommunityNewNotificationActivity.this;
		list = (ListView) findViewById(R.id.listViewPolitical);
		list2 = (ListView) findViewById(R.id.listViewPolitical2);
		ButtonClickListener l = new ButtonClickListener();
		back.setOnClickListener(l);

		handler = new Handler();
		/*
		 * dialog = new ProgressDialog(this); dialog.setTitle(null);
		 * dialog.setMessage("�����У����Ժ�"); dialog.show();
		 */
		dialog = new MyProgressDialog(this);
		dialog.start("�����У����Ժ�...");

		initTabHost(); // ��ʼ��TabHost
		// setTabViewParas();//����Tab��ʾ����
		// tabHost.setCurrentTab(1);
		tabHost.setOnTabChangedListener(new TabChangeListener());
		// tabHost.setCurrentTab(0);
		// �����ڲ����ʼ��
		detector = new GestureDetector(new MySimpleGestureDetector());
		getData();// ��ȡ��������   ��ҵ֪ͨ
		//getData_s(); //������������
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
				if(newsList.size()>0)
				{
					showData(1);
				}else
				{
					getData();
				}
				
			} else if (str.equals("tab2")) {
				if(newsList_s.size()>0)
				{
					showData(2);
				}else
				{
					getData_s();
				}				
			}
		}
	}

	/**
	 * ����Tab�л�����
	 */
	public void setTabBackground() {
		// ����Tab����
		View v0=tabWidget.getChildTabViewAt(0);
		View v1=tabWidget.getChildTabViewAt(1);
		//int count = tabWidget.getChildCount();
		if (tabHost.getCurrentTab() == 0) {
			//v.setBackgroundColor(Color.WHITE);
			// ����������Լ�����һ��ͼƬ��Ϊ��������				
		    v0.setBackgroundDrawable(getResources().getDrawable(R.drawable.wu2));
		    v1.setBackgroundDrawable(getResources().getDrawable(R.drawable.she1));
			
		} else {
			//v.setBackgroundColor(Color.GRAY);
		    v0.setBackgroundDrawable(getResources().getDrawable(R.drawable.wu1));
		    v1.setBackgroundDrawable(getResources().getDrawable(R.drawable.she2));
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
				.setIndicator(null,
						null).setContent(R.id.listViewPolitical));

		tabHost.addTab(tabHost
				.newTabSpec("tab2")
				.setIndicator(null, null)
				.setContent(R.id.listViewPolitical2));
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
		Intent intent = null;

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;

			default:
				break;
			}
		}

	}

	private void showData(int i) {
		// dList=new ArrayList<String>();
		if (i == 1) {
			communityNewsAdapter=new CommunityNewsAdapter(con, newsList);
			list.setDivider(null);// ����ListViewû�зָ���
			list.setAdapter(communityNewsAdapter);
			communityNewsAdapter.notifyDataSetChanged();
		} else {
			communityNewsAdapter=new CommunityNewsAdapter(con, newsList_s);
			list2.setDivider(null);// ����ListViewû�зָ���
			list2.setAdapter(communityNewsAdapter);
			communityNewsAdapter.notifyDataSetChanged();
		}

	}

	// ��ȡ��������
	private void getData() {
		// TODO Auto-generated method stub
		//newsList = new ArrayList<News>();

		HttpClientService svr = new HttpClientService(
				AppConstants.HttpHostAdress+"zgannews.aspx");//"http://community1.zgantech.com/ZganNews.aspx?did=15923258890"
		//����
		svr.addParameter("did",ZganCommunityStaticData.User_Number);
		svr.addParameter("method","news_wy");
				
		HttpAndroidTask task = new HttpAndroidTask(con, svr,
				new HttpResponseHandler() {
					// ��Ӧ�¼�
					@SuppressWarnings("unchecked")
					public void onResponse(Object obj) {
						pdialog.stop();
						JsonEntity jsonEntity = GsonUtil.parseObj2JsonEntity(
								obj,con,false);
						if (jsonEntity.getStatus() == 1) {
							handler.post(none);
						} else if (jsonEntity.getStatus() == 0) {
								newsList=(List<News>) GsonUtil.getData(
										jsonEntity,AppConstants.type_newsList);	
								
								if(newsList.size()>0)
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
						pdialog = new MyProgressDialog(context);
						DialogUtil.setAttr4progressDialog(pdialog);
					}
				});
		task.execute(new String[] {});	
	}
	
	//������������
	private void getData_s() {
		// TODO Auto-generated method stub
		//newsList = new ArrayList<News>();

		HttpClientService svr = new HttpClientService(
				AppConstants.HttpHostAdress+"zgannews.aspx");//"http://community1.zgantech.com/ZganNews.aspx?did=15923258890"
		//����
		svr.addParameter("did",ZganCommunityStaticData.User_Number);
		svr.addParameter("method","news_cq");
				
		HttpAndroidTask task = new HttpAndroidTask(con, svr,
				new HttpResponseHandler() {
					// ��Ӧ�¼�
					@SuppressWarnings("unchecked")
					public void onResponse(Object obj) {
						pdialog.stop();
						JsonEntity jsonEntity = GsonUtil.parseObj2JsonEntity(
								obj,con,false);
						if (jsonEntity.getStatus() == 1) {
							handler.post(none_s);
						} else if (jsonEntity.getStatus() == 0) {
								newsList_s=(List<News>) GsonUtil.getData(
										jsonEntity,AppConstants.type_newsList);	
								
								if(newsList_s.size()>0)
				                {
				                	//�����ݵ�ʱ�����
									handler.post(r_s);
				                }else
				                {
				                	//û������ʱ����ʾ
				                	handler.post(none_s);
				                }														
						}														
					}
				}, new HttpPreExecuteHandler() {
					public void onPreExecute(Context context) {
						pdialog = new MyProgressDialog(context);
						DialogUtil.setAttr4progressDialog(pdialog);
					}
				});
		task.execute(new String[] {});	
	}
	
	
	// ���ݼ�����֮��Ĳ���
			Runnable r_s = new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					// dialog.dismiss();
					//dialog.stop();
					if (newsList_s.size() <= 0) {
						Toast.makeText(con, "��������������", 1).show();					
					} else {
						//tabHost.setCurrentTabByTag("tab2");// ѡ�е�һ��Tab
						showData(2);// ��ʼ������
					}

				}
			};

		// �����ݴ������
		Runnable none_s = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// dialog.dismiss();
				//dialog.stop();
				Toast.makeText(con, "��������������", 2).show();
			}
		};

		
	// �����ݴ������
		Runnable none = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// dialog.dismiss();
				//dialog.stop();
				Toast.makeText(con, "û�пɹ����ص�����", 2).show();
			}
		};

		
	// ���ݼ�����֮��Ĳ���
	Runnable r = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// dialog.dismiss();
			//dialog.stop();
			if (newsList.size() <= 0) {
				Toast.makeText(con, "û������", 2).show();
			} else {
				tabHost.setCurrentTabByTag("tab1");// ѡ�е�һ��Tab
				showData(1);// ��ʼ������
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

