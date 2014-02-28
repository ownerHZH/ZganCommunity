package com.zgan.community.baidu;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionInfo;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.zgan.community.R;
import com.zgan.community.tools.MainAcitivity;

public class ZganCommunityMapShow extends MainAcitivity {
	private MapView mMapView = null;
	private MKSearch mSearch = null; // ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��
	/**
	 * �����ؼ������봰��
	 */
	String data;
	private AutoCompleteTextView keyWorldsView = null;
	private ArrayAdapter<String> sugAdapter = null;
	private int load_Index;
	private Button back;
	private Button listshow;
	private TextView title;
	static List<MKPoiInfo> mkPoiInfos=new ArrayList<MKPoiInfo>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ZganCommunityApplictation app = (ZganCommunityApplictation) this
				.getApplication();
		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(this);
			app.mBMapManager.init(ZganCommunityApplictation.strKey,
					new ZganCommunityApplictation.MyGeneralListener());
		}
		setContentView(R.layout.line_map);
		back = (Button) findViewById(R.id.back);
		listshow = (Button) findViewById(R.id.golist);
		title = (TextView) findViewById(R.id.title);

		back.setOnClickListener(l);
		listshow.setOnClickListener(l);
		mMapView = (MapView) findViewById(R.id.bmapsView);
		GeoPoint point =new GeoPoint((int) (29.526199 * 1E6),
				(int) (106.717878 * 1E6));  
		//�ø����ľ�γ�ȹ���һ��GeoPoint����λ��΢�� (�� * 1E6)  
		mMapView.getController().setCenter(point);//���õ�ͼ���ĵ�  
		mMapView.getController().enableClick(true);
		mMapView.getController().setZoom(19);

		// ��ʼ������ģ�飬ע�������¼�����
		mSearch = new MKSearch();
		mSearch.init(app.mBMapManager, new MKSearchListener() {
			// �ڴ˴�������ҳ���
			@Override
			public void onGetPoiDetailSearchResult(int type, int error) {
				if (error != 0) {
					Toast.makeText(ZganCommunityMapShow.this, "��Ǹ��δ�ҵ����",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(ZganCommunityMapShow.this, "�ɹ����鿴����ҳ��",
							Toast.LENGTH_SHORT).show();
				}
			}

			/**
			 * �ڴ˴���poi�������
			 */
			public void onGetPoiResult(MKPoiResult res, int type, int error) {
				// ����ſɲο�MKEvent�еĶ���
				if (error != 0 || res == null) {
					Toast.makeText(ZganCommunityMapShow.this, "��Ǹ��δ�ҵ����",
							Toast.LENGTH_LONG).show();
					mkPoiInfos.clear();
					return;
				}
				// ����ͼ�ƶ�����һ��POI���ĵ�
				if (res.getCurrentNumPois() > 0) {
					// ��poi�����ʾ����ͼ��
					MyPoiOverlay poiOverlay = new MyPoiOverlay(
							ZganCommunityMapShow.this, mMapView, mSearch);
					mkPoiInfos= res.getAllPoi();				
					poiOverlay.setData(res.getAllPoi());
					mMapView.getOverlays().clear();
					mMapView.getOverlays().add(poiOverlay);
					mMapView.refresh();
					// ��ePoiTypeΪ2��������·����4��������·��ʱ�� poi����Ϊ��
					for (MKPoiInfo info : res.getAllPoi()) {
						if (info.pt != null) {
							mMapView.getController().animateTo(info.pt);
							break;
						}
					}
				} else if (res.getCityListNum() > 0) {
					// ������ؼ����ڱ���û���ҵ����������������ҵ�ʱ�����ذ����ùؼ�����Ϣ�ĳ����б�
					String strInfo = "��";
					for (int i = 0; i < res.getCityListNum(); i++) {
						strInfo += res.getCityListInfo(i).city;
						strInfo += ",";
					}
					strInfo += "�ҵ����";
					Toast.makeText(ZganCommunityMapShow.this, strInfo,
							Toast.LENGTH_LONG).show();
				}
			}

			public void onGetDrivingRouteResult(MKDrivingRouteResult res,
					int error) {
			}

			public void onGetTransitRouteResult(MKTransitRouteResult res,
					int error) {
			}

			public void onGetWalkingRouteResult(MKWalkingRouteResult res,
					int error) {
			}

			public void onGetAddrResult(MKAddrInfo res, int error) {
			}

			public void onGetBusDetailResult(MKBusLineResult result, int iError) {
			}

			/**
			 * ���½����б�
			 */
			@Override
			public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
				if (res == null || res.getAllSuggestions() == null) {
					return;
				}
				sugAdapter.clear();
				for (MKSuggestionInfo info : res.getAllSuggestions()) {
					if (info.key != null)
						sugAdapter.add(info.key);
				}
				sugAdapter.notifyDataSetChanged();

			}

			@Override
			public void onGetShareUrlResult(MKShareUrlResult result, int type,
					int error) {
				// TODO Auto-generated method stub

			}
		});
		if (getIntent().getExtras() != null) {
			data = getIntent().getExtras().getString("button_key");
			searchButtonProcess(data);
			title.setText(data);
		}

	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mMapView.destroy();
		mSearch.destory();
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}

	private void initMapView() {
		mMapView.setLongClickable(true);
		mMapView.getController().setZoom(12);
		mMapView.getController().enableClick(true);
		mMapView.setBuiltInZoomControls(true);
	}

	OnClickListener l = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;

			case R.id.golist:
				Intent intent = new Intent(ZganCommunityMapShow.this,
						ZganCommunityListShow.class);
				Bundle bundle = new Bundle();
				bundle.putString("button_key", data);
				intent.putExtras(bundle);
				startActivity(intent);
				break;

			}
		}
	};

	/**
	 * Ӱ��������ť����¼�
	 * 
	 * @param v
	 */
	public void searchButtonProcess(String data) {
		mSearch.poiSearchNearBy(data, new GeoPoint((int) (29.526199 * 1E6),
				(int) (106.717878 * 1E6)), 500);
		
		// mSearch.poiSearchInCity("����", "��ʳ");
	}

}
