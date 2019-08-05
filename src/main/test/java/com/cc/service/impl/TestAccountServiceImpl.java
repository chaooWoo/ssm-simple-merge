package com.cc.service.impl;

import com.cc.service.IAccountService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2019/8/5 9:54
 **/
public class TestAccountServiceImpl {
	@Test
	public void test01(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		IAccountService accountService = ac.getBean("accountService",IAccountService.class);
		accountService.findAllAccount();
	}
}
