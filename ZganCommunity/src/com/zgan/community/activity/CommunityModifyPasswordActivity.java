package com.zgan.community.activity;

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
import com.zgan.community.R.layout;
import com.zgan.community.R.menu;
import com.zgan.community.data.Recinfo;
import com.zgan.community.tools.MainAcitivity;
import com.zgan.community.tools.ZganCommunityStaticData;

import android.os.Bundle;
import android.os.Looper;
import android.app.Activity;
import android.content.SharedPreferences;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CommunityModifyPasswordActivity extends MainAcitivity {

	private Button back;
	private TextView title;

	private EditText oldpass;
	private EditText newpass;
	private EditText renewpass;
	private Button save;
	SharedPreferences preferences;
	SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_modify_password);
		preferences = getSharedPreferences("zgan_data", MODE_PRIVATE);
		editor = preferences.edit();
		back = (Button) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		title.setText("�����޸�");

		oldpass = (EditText) findViewById(R.id.oldPassword);
		newpass = (EditText) findViewById(R.id.newPassword);
		renewpass = (EditText) findViewById(R.id.renewPassword);

		save = (Button) findViewById(R.id.buttonCommit);

		back.setOnClickListener(l);// ����ע�����
		save.setOnClickListener(l);// �����޸�ȷ�ϰ�ťע������¼�
	}

	// ������
	private OnClickListener l = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;

			case R.id.buttonCommit:
				UpPassWord(); // ����ȷ�ϲ���
				finish();
				break;

			default:
				break;
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.activity_community_modify_password,menu);
		return true;
	}

	protected void UpPassWord() {
		// TODO Auto-generated method stub
		if (oldpass.getText().toString() == null
				&& newpass.getText().toString() == null
				&& renewpass.getText().toString() == null) {
			Toast.makeText(getApplicationContext(), "����������", Toast.LENGTH_SHORT)
					.show();

		} else {
			if (!(newpass.getText().toString()).equals(renewpass.getText()
					.toString())) {
				Toast.makeText(getApplicationContext(), "2�����벻һ��",
						Toast.LENGTH_SHORT).show();

			} else {
				SetPassWord();
			}
		}
	}

	private void SetPassWord() {
		// TODO Auto-generated method stub

		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String new_password = newpass.getText().toString();
				String old_password = oldpass.getText().toString();
				Log.i("old_password", "" + old_password);
				Log.i("new_password", "" + new_password);

				HttpGet get = new HttpGet(
						"http://community1.zgantech.com/ZganSetPwd.aspx?did="
								+ ZganCommunityStaticData.User_Number + "&pwd="
								+ old_password + "&Npwd=" + new_password);
				// http://community1.zgantech.com/ZganSetPwd.aspx?did=15923258890&pwd=1234&Npwd=12345
				HttpClient client = new DefaultHttpClient();
				HttpResponse httpResponse;
				try {
					httpResponse = client.execute(get);
					HttpEntity entity = httpResponse.getEntity();
					StatusLine line = httpResponse.getStatusLine();
					Log.i("linecode", "" + line.getStatusCode());
					if (line.getStatusCode() == 200) {
						String st = EntityUtils.toString(entity);

						Log.i("st", "" + st);

						JSONObject jsonObject = new JSONObject(st);
						String status = jsonObject.getString("status");
						Log.i("status", "" + status);
						if ("0".equals(status)) {
							String msg = jsonObject.getString("msg");
							Log.i("msg", "" + Html.fromHtml(msg).toString());
							Looper.prepare();
							// dialog.dismiss();
							Toast.makeText(getApplicationContext(), "�޸�����ɹ���",
									Toast.LENGTH_SHORT).show();
							editor.putString("password", new_password);
							editor.commit();
							ZganCommunityStaticData.User_PassWord = preferences
									.getString("password", null);
							Looper.loop();

						} else {
							Looper.prepare();
							// dialog.dismiss();
							String msg = jsonObject.getString("msg");
							Log.i("msg", "" + Html.fromHtml(msg).toString());
							Toast.makeText(getApplicationContext(), "" + msg,
									Toast.LENGTH_SHORT).show();
							Looper.loop();
						}
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				super.run();

			}
		}.start();

	}

}
