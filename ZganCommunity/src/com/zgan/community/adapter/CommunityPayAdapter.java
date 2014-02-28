package com.zgan.community.adapter;

import java.util.List;
import java.util.Map;

import com.zgan.community.R;
import com.zgan.community.data.CommunityService;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CommunityPayAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;//�õ�һ��LayoutInfalter�����������벼�� 
	private List<Map<String,String>> list;
	
	/**
	 * ���캯��
	 * @param context
	 */
	public CommunityPayAdapter(Context context,List list)
	{
		this.context=context;
		inflater=LayoutInflater.from(context);
		this.list=list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
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
        	 
	          convertView = inflater.inflate(R.layout.community_pay_item,null);	
	          holder = new ViewHolder();
	          
	         /**�õ������ؼ��Ķ���*/                    
	         holder.date = (TextView) convertView.findViewById(R.id.payItemDate);
	         holder.money = (TextView) convertView.findViewById(R.id.payItemOwnMoney);
	         
	         convertView.setTag(holder);//��ViewHolder����                   
         }
         else
         {
             holder = (ViewHolder)convertView.getTag();//ȡ��ViewHolder����                  
         }

         /**����TextView��ʾ������*/            
         holder.date.setText(list.get(position).get("date").toString());
         holder.money.setText("��"+list.get(position).get("money").toString());
         
         /*if (list.size() == 1) {
             convertView.setBackgroundResource(R.drawable.circle_list_single);
         } else if (list.size() > 1) {
             if (position == 0) {
                 convertView.setBackgroundResource(R.drawable.circle_list_top);
             } else if (position == (list.size() - 1)) {
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
		public TextView date;      
		public TextView money;    
	}
}
	
	

