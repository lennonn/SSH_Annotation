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
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.ExceptionMapping;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


import com.opensymphony.xwork2.ActionSupport;
import com.zl.dto.domain.Category;
import com.zl.dto.domain.FileSource;
import com.zl.service.facade.UploadService;

/**
 * @author zlennon
 *
 */
@ParentPackage("struts-default")
@ResultPath("/WEB-INF/jsp/")
@Action(value = "error", exceptionMappings = { @ExceptionMapping(exception = "java.lang.Exception", result = "/WEB/INF/jsp/common/errorPage.jsp", params = { "error","value" }) })
public class UploadAction extends ActionSupport {

	/**
	 *
	 */
	private static final long serialVersionUID = 3536238274545006462L;
	// input
	private File upload; // ��װ�ϴ����ļ� ��ʱ�����
	private String uploadContentType; // ����û��ϴ��ļ�������
	private String uploadFileName; // ����û��ϴ��ļ�������
	private List<File> uploads = new ArrayList<File>(); // ʹ�ü��������з�װ����ļ��ϴ�
	private List<String> uploadsContentType = new ArrayList<String>();
	private List<String> uploadsFileName = new ArrayList<String>();

	// output
	private String serverFileName; // ���������������
	private long uploadFileSize;

	private List<String> serverFileNames = new ArrayList<String>();
	private List<Long> uploadsFileSizes = new ArrayList<Long>();

	// service

	UploadService uploadService;

	FileSource fileSource;
	@Autowired
	@Qualifier("fileSource")
	public void setFileSource(FileSource fileSource) {
		this.fileSource = fileSource;
	}
	@Autowired
	@Qualifier("uploadService")
	public void setUploadService(UploadService uploadService) {
		this.uploadService = uploadService;
	}

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

	@Actions(value={@Action(value="uploadSingle",results = { @Result(name = "success", location = "/common/sucess.jsp") }, interceptorRefs = {
			@InterceptorRef(value = "fileUpload", params = { "allowedTypes",
					"text/plain,image/x-png,image/bmp,image/jpeg",
					"maximumSize", "2097152" }),
			@InterceptorRef("defaultStack") })
	}
	)
	public String uploadSingle() {
		// ��ServletContext().getReaPath("/upload")�л�ȡϵͳ�����ʵ��·��
		// /uploadΪ�Զ�����ļ��У�������ǰ����
		String serverPath = ServletActionContext.getServletContext()
				.getRealPath("/images/strutsupload");
		// �����ļ��ϴ�������·����File.separator��ȡ��ͬϵͳ��'/'
		serverFileName = serverPath + File.separator + uploadFileName;
		try {
			copyFile(upload, serverFileName); // ���ļ����Ƶ�ʵ��Ӳ��
			uploadFileSize = upload.length();
			// �����ļ���Ϣ�����ݿ�

			fileSource.setFileName(uploadFileName);
			fileSource.setFileUrl(serverPath);
			fileSource.setFileSize(uploadFileSize);
			fileSource.setUploadDate(new Date());
			fileSource.setUploadUser("test");
			Category category = new Category();
			if (uploadContentType.startsWith("image"))
				category.setCateName("picture");
			category.setCateDesc("ͼƬ�ļ�");
			uploadService.saveFileInfo(fileSource);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return SUCCESS;
	}

	public String uploadsUsingList() {
		String result = null;
		for (int i = 0; i < uploads.size(); i++) {
			upload = uploads.get(i);
			uploadFileName = uploadsFileName.get(i);
			result = uploadSingle();
			uploadsFileSizes.add(uploadFileSize);
			serverFileNames.add(serverFileName);
		}
		return result;
	}

	private void copyFile(File src, String dest) throws Exception {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				src));
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
				new FileOutputStream(dest)));
		int count; // ÿ��д���ļ��Ĵ�С
		byte[] b = new byte[512]; // ÿ�ξ����ܶ�ȡ�Ĵ�С
		while ((count = in.read(b)) != -1) {
			out.write(b, 0, count);
		}
		in.close();
		out.close();
	}
}
