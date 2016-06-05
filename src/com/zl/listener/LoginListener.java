package com.zl.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.zl.dto.domain.User;

public class LoginListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
		  System.out.println("��session����ʱ����");
		  HttpSession session=arg0.getSession();

		  User user=(User) session.getAttribute("user");
		  if(user==null){
			  System.out.println("��ǰû���û���¼"+"============create");
		  }else{
			  System.out.println("�û�:"+user.getName()+"�ѵ�¼"+"============create");
		  }
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("��session����ʱ����");
		 HttpSession session=arg0.getSession();
		 if(session==null){
			 System.out.println("��ǰû���û���¼"+"============destory");
		 }else{
			 User user=(User) session.getAttribute("user");
			 if(user==null){
				 System.out.println("��ǰû���û���¼"+"============destory");
			 }else{
				 System.out.println("��ǰ���ڵ�½���û�:"+user.getName()+"============destory");
			 }


		 }
	}

}
