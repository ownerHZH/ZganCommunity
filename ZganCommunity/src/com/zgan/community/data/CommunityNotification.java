package com.zgan.community.data;

import java.util.Date;
/**
 * ����֪ͨʵ����
 * @author Hzh
 *
 */
public class CommunityNotification {
	private int id;	         //��������ID
	private String title;    //����
	private String content;  //��������
	private Date contentTime;//����ʱ��
	private String publisher;//������
	private int communityId; //����ID
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getContentTime() {
		return contentTime;
	}
	public void setContentTime(Date contentTime) {
		this.contentTime = contentTime;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public int getCommunityId() {
		return communityId;
	}
	public void setCommunityId(int communityId) {
		this.communityId = communityId;
	}

}
