package com.zgan.community.downtools;

/**
 * ���ػص�
 * @author 
 *
 */
public interface DownloadFileCallback {
	void downloadSuccess(Object obj);//���سɹ�
	void downloadError(Exception e,String msg);//����ʧ��
}
