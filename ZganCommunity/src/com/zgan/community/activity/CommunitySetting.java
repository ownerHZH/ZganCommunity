package com.zgan.community.activity;

import java.util.Calendar;

import com.zgan.community.R;
import com.zgan.community.R.layout;
import com.zgan.community.R.menu;
import com.zgan.community.activity.CommunityPolicitalActivity.ButtonClickListener;
import com.zgan.community.tools.MainAcitivity;
import com.zgan.community.tools.SlipButton;
import com.zgan.community.tools.SlipButton.OnChangedListener;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class CommunitySetting extends MainAcitivity {

	private Button back;
	private TextView title;
	private Context con;
	
	private LinearLayout about;//����
	private LinearLayout feedback;//�������
	//private Button messageSwitch;
	private Button passwordChange;//�����޸�
	private RadioGroup sexRadioGroup;//�Ա�sexRadioGroup
	private EditText birthText;//������ʾ��
	private Button birthChange;//�����޸İ�ť
	private EditText nicknameText;//�ǳ���ʾ��
	private Button nicknameChange;//�ǳ��޸İ�ť
	private TextView balcony;//¥����
	private TextView phone;//�ֻ�����
	private SlipButton messageSwitch;//��Ϣ���Ϳ���
	
	private boolean clicked=false;
	
	//����һ����������ʶDatePickerDialog  
    private static final int DATE_PICKER_ID = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_setting);
		
		back = (Button) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		title.setText(R.string.personal_settings);
		con = CommunitySetting.this;
		
		about=(LinearLayout) findViewById(R.id.about);//����
		feedback=(LinearLayout) findViewById(R.id.feedback);//�������
		passwordChange=(Button) findViewById(R.id.passwordChange);//�����޸�
		sexRadioGroup=(RadioGroup) findViewById(R.id.sexRadioGroup);//�Ա�sexRadioGroup
		birthText=(EditText) findViewById(R.id.birthText);//������ʾ��
		birthChange=(Button) findViewById(R.id.birthChange);//�����޸İ�ť
		nicknameText=(EditText) findViewById(R.id.nicknameText);//�ǳ���ʾ��
		nicknameChange=(Button) findViewById(R.id.nicknameChange);//�ǳ��޸İ�ť
		balcony=(TextView) findViewById(R.id.balcony);//¥����
		phone=(TextView) findViewById(R.id.phone);//�ֻ�����
		messageSwitch=(SlipButton) findViewById(R.id.slipbutton);
				
		back.setOnClickListener(l);
		passwordChange.setOnClickListener(l);
		feedback.setOnClickListener(l);
		about.setOnClickListener(l);
		nicknameChange.setOnClickListener(l);
		birthChange.setOnClickListener(l);
		
		messageSwitch.SetOnChangedListener(new OnChangedListener() {
			
			@Override
			public void OnChanged(boolean CheckState) {
				Toast.makeText(con, ""+CheckState, Toast.LENGTH_SHORT).show();
			}
		});
		
	}
	
	
	private OnClickListener l=new OnClickListener() {
		
		@SuppressWarnings("deprecation")
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;
			case R.id.passwordChange:
				 //�����޸Ĳ���
				Intent intent=new Intent(con,CommunityModifyPasswordActivity.class);
				startActivity(intent);
				break;
			case R.id.feedback:
				//�������
				Intent i=new Intent(con,CommunitySettingFeedBack.class);
				startActivity(i);
				break;
			case R.id.about:
				//����
				
				break;
			case R.id.nicknameChange:
				//�޸��ǳ�
				buttonToggleAction(nicknameChange,nicknameText);
				
				break;
			case R.id.birthChange:
				//����
				showDialog(DATE_PICKER_ID);
				break;

			default:
				break;
			}
		}
	};

	
	public void buttonToggleAction(Button button,EditText editText)
	{
		if(clicked)
		{
			clicked=false;
		}else
		{
			clicked=true;
		}
		if(clicked)
		{
			editText.setEnabled(true);
			editText.requestFocus();
		}else
		{
			editText.setEnabled(false);
			editText.clearFocus();
		}
	}
	
	/** 
     * �ڲ������࣬ʵ��DatePickerDialog.OnDateSetListener�ӿڣ���дonDateSet()���� 
     * ������DatePickerDialog��������Date�Ժ����·��и���Set����ť����ʾȷ�����á������ 
     * ��ť�������ʱ�򣬾�ִ�������onDateSet()������ 
     */  
    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {  
        public void onDateSet(DatePicker view, int year, int monthOfYear,  
                int dayOfMonth) {  
            birthText.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
        }  
    };  
      
    /** 
     * ��дActivity��onCreateDialog()������������showDialog()������ʱ�򣬾�ִ������ 
     * ��ʾDatePickerDialog����������Ĭ��date������Ĳ���ָ����Ϊ2014-Fre-17. 
     * �·ݵ�1��ʾ���£�0��ʾһ�¡� 
     */  
    protected Dialog onCreateDialog(int id) {  
    	Calendar calendar = Calendar.getInstance();  
        
        Dialog dialog = null;
        switch(id){  
        case DATE_PICKER_ID:  
        dialog = new DatePickerDialog(this, 
        		onDateSetListener, 
                calendar.get(Calendar.YEAR), 
                calendar.get(Calendar.MONTH), 
                calendar.get(Calendar.DAY_OF_MONTH));   
        }  
          
        return dialog;  
    } 
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_community_setting, menu);
		return false;
	}

}
