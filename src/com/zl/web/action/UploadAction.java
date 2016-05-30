/**
 *
 */
package com.zl.web.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;



/**
 * @author zlennon
 *
 */
public class UploadAction extends ActionSupport {

	/**
	 *
	 */
	private static final long serialVersionUID = 3536238274545006462L;
		//input
		private File upload;   //��װ�ϴ����ļ� ��ʱ�����
		private String uploadContentType; //����û��ϴ��ļ�������
		private String uploadFileName;      //����û��ϴ��ļ�������
		private List<File> uploads = new ArrayList<File>(); //ʹ�ü��������з�װ����ļ��ϴ�
		private List<String> uploadsContentType = new ArrayList<String>();
		private List<String> uploadsFileName = new ArrayList<String>();



		//output
		private String serverFileName; //���������������
		private long uploadFileSize;

		private List<String> serverFileNames = new ArrayList<String>();
		private List<Long> uploadsFileSizes = new ArrayList<Long>();







		public File getUpload() {
			return upload;
		}
		public void setUpload(File upload) {
			this.upload = upload;
		}
		public String getUploadContentType() {
			return uploadContentType;
		}
		public void setUploadContentType(String uploadContentType) {
			this.uploadContentType = uploadContentType;
		}
		public String getUploadFileName() {
			return uploadFileName;
		}
		public void setUploadFileName(String uploadFileName) {
			this.uploadFileName = uploadFileName;
		}

		public String getServerFileName() {
			return serverFileName;
		}
		public void setServerFileName(String serverFileName) {
			this.serverFileName = serverFileName;
		}
		public long getUploadFileSize() {
			return uploadFileSize;
		}
		public void setUploadFileSize(long uploadFileSize) {
			this.uploadFileSize = uploadFileSize;
		}
		public List<File> getUploads() {
			return uploads;
		}
		public void setUploads(List<File> uploads) {
			this.uploads = uploads;
		}
		public List<String> getUploadsContentType() {
			return uploadsContentType;
		}
		public void setUploadsContentType(List<String> uploadsContentType) {
			this.uploadsContentType = uploadsContentType;
		}
		public List<String> getUploadsFileName() {
			return uploadsFileName;
		}
		public void setUploadsFileName(List<String> uploadsFileName) {
			this.uploadsFileName = uploadsFileName;
		}
		public List<String> getServerFileNames() {
			return serverFileNames;
		}
		public void setServerFileNames(List<String> serverFileNames) {
			this.serverFileNames = serverFileNames;
		}
		public List<Long> getUploadsFileSizes() {
			return uploadsFileSizes;
		}
		public void setUploadsFileSizes(List<Long> uploadsFileSizes) {
			this.uploadsFileSizes = uploadsFileSizes;
		}
		public String uploadSingle(){
			//��ServletContext().getReaPath("/upload")�л�ȡϵͳ�����ʵ��·�� /uploadΪ�Զ�����ļ��У�������ǰ����
			String serverPath = ServletActionContext.getServletContext().getRealPath("/upload");
			//�����ļ��ϴ�������·����File.separator��ȡ��ͬϵͳ��'/'
			serverFileName = serverPath+File.separator+uploadFileName;
			try {
				copyFile(upload, serverFileName); //���ļ����Ƶ�ʵ��Ӳ��
				uploadFileSize = upload.length();
			} catch (Exception e) {
				e.printStackTrace();
				return "error";
			}
			return SUCCESS;
		}
		public String uploadsUsingList(){
			String result = null;
			for(int i = 0 ; i<uploads.size();i++){
				upload = uploads.get(i);
				uploadFileName = uploadsFileName.get(i);
				result = uploadSingle();
				uploadsFileSizes.add(uploadFileSize);
				serverFileNames.add(serverFileName);
			}
			return result;
		}

		private void copyFile(File src,String dest) throws Exception{
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(src));
			DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(dest)));
			int count; //ÿ��д���ļ��Ĵ�С
			byte[] b = new byte[512]; //ÿ�ξ����ܶ�ȡ�Ĵ�С
			while((count=in.read(b))!=-1){
				out.write(b,0,count);
			}
			in.close();
			out.close();
		}
}
