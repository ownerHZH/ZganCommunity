package com.zgan.community.adapter;

import java.util.List;

import com.zgan.community.R;
import com.zgan.community.activity.CommunityPolicitalDetailActivity;
import com.zgan.community.data.ContentData;
import com.zgan.community.data.MSZW_BGDD;
import com.zgan.community.data.News;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommunityNewsAdapter extends BaseAdapter {

	private LayoutInflater inflater;//�õ�һ��LayoutInfalter�����������벼�� 
	private List<News> dataList;
	private Context con;

	/**
	 * ���캯��
	 * @param context
	 * @param i 
	 */
	public CommunityNewsAdapter(Context context,List<News> list)
	{
		con=context;
		inflater=LayoutInflater.from(context);
		dataList=list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;        
		if (convertView == null) {

			convertView = inflater.inflate(R.layout.community_political_item,null);	
			holder = new ViewHolder();

			/**�õ������ؼ��Ķ���*/                    
			holder.title = (TextView) convertView.findViewById(R.id.itemTitle);
			//holder.date = (TextView) convertView.findViewById(R.id.itemDate);

			convertView.setTag(holder);//��ViewHolder����                   
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();//ȡ��ViewHolder����                  
		}

		/**����TextView��ʾ������ ��Title����¼�*/ 
			final News news=dataList.get(position);
			holder.title.setText(news.getTitle());
			holder.title.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent=new Intent(con,CommunityPolicitalDetailActivity.class);
				    Bundle bundle=new Bundle();
					bundle.putSerializable("newsData", news);
					bundle.putInt("pageNumber", 20);
					intent.putExtras(bundle);
					con.startActivity(intent);
				}
			});

		/**
		 * ΪItem�����ʽ
		 */
		/*if (dataList.size() == 1) {
			convertView.setBackgroundResource(R.drawable.circle_list_single);
		} else if (dataList.size() > 1) {
			if (position == 0) {
				convertView.setBackgroundResource(R.drawable.circle_list_top);
			} else if (position == (dataList.size() - 1)) {
				convertView.setBackgroundResource(R.drawable.circle_list_bottom);
			} else {
				convertView.setBackgroundResource(R.drawable.circle_list_middle);
			}
		}*/

		return convertView;
	}

	/**
	 * ��ſؼ�
	 * */
	public final class ViewHolder{
		public TextView title;   
	}
}



