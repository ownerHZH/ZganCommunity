package com.zgan.community.activity;

import com.zgan.community.R;
import com.zgan.community.baidu.ZganCommunityMapShow;
import com.zgan.community.tools.MainAcitivity;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class CommunityServiceActivity extends MainAcitivity {

	private Button back;           //����
	private TextView title;        //����
	
	private Button move;           //���
	private Button unlock;         //��������
	private Button plumber;        //ˮ��ά��
	private Button pcrepair;       //����ά��
	private Button recycle;        //���û���
	private Button pet;            //����֮��
	//private Button psychological;  //������
	//private Button health;         //������ѯ
	//private Button law;            //������ѯ
	private Button pipe;           //��ͨ�ܵ�
	private Button matron;         //��ɩ
	private Button cleaning;       //����
	//private Button nanny;          //��ķ
	
	private Context con;
	private Intent intent=null;    //װ����ת����� ��ͼ
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_service);
		
		back=(Button) findViewById(R.id.back);
		title=(TextView) findViewById(R.id.title);
		title.setText(R.string.community_service);    //���� ��������
		
		move=(Button) findViewById(R.id.buttonMove);
		unlock=(Button) findViewById(R.id.buttonUnlock);
		plumber=(Button) findViewById(R.id.buttonPlumber);
		pcrepair=(Button) findViewById(R.id.buttonPCrepair);
		recycle=(Button) findViewById(R.id.buttonRecycle);
		pet=(Button) findViewById(R.id.buttonPet);
		pipe=(Button) findViewById(R.id.buttonPipe);
		matron=(Button) findViewById(R.id.buttonMatron);
		cleaning=(Button) findViewById(R.id.buttonCleaning);
		
		con=CommunityServiceActivity.this;                     //��ʼ��Context
		
        ButtonClickListener l=new ButtonClickListener();       //��ť�����������ʼ��
		
        back.setOnClickListener(l);                            //��ťע�������
        move.setOnClickListener(l);
        unlock.setOnClickListener(l);
        plumber.setOnClickListener(l);
        pcrepair.setOnClickListener(l);
        recycle.setOnClickListener(l);
        pet.setOnClickListener(l);
        pipe.setOnClickListener(l);
        matron.setOnClickListener(l);
        cleaning.setOnClickListener(l);
	}

	public class ButtonClickListener implements View.OnClickListener
	{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				//���ص����Ӧ�¼�
				finish();
				break;
            case R.id.buttonMove:
            	//��ҵ����Ӧ�¼�
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "��������");
				startActivity(intent);
				break;
            case R.id.buttonUnlock:
            	//������������Ӧ�¼�
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "ˮ��ά��");
				startActivity(intent);
				break;
            case R.id.buttonPlumber:
            	//ˮ��ά�޵����Ӧ�¼�
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "��������");
				startActivity(intent);
				break;
             case R.id.buttonPCrepair:
            	//����ά�޵����Ӧ�¼�
            	 intent=new Intent(con,ZganCommunityMapShow.class);
             	intent.putExtra("button_key", "��ҹ�˾");
 				startActivity(intent);
 				break;
            case R.id.buttonRecycle:
            	//���û��յ����Ӧ�¼�
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "��ɩ��ķ");
				startActivity(intent);
				break;
            case R.id.buttonPet:
            	//����֮�ҵ����Ӧ�¼�
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "���ֻ���");
				startActivity(intent);
				break;
            case R.id.buttonPipe:
            	//��ͨ�ܵ������Ӧ�¼�
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "�ҵ�ά��");
				startActivity(intent);
				break;
            case R.id.buttonMatron:
            	//��ɩ�����Ӧ�¼�
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "�ܵ���ͨ");
				startActivity(intent);
				break;
            case R.id.buttonCleaning:
            	//��������Ӧ�¼�
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "ϴ�·���");
				startActivity(intent);
				break;

			default:
				break;
			}
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_community_service, menu);
		return true;
	}

}
