package com.cc.controller;

import com.cc.pojo.Account;
import com.cc.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2019/8/5 10:36
 **/
@Controller("accountController")
@RequestMapping("/account")
public class AccountController {
	@Autowired
	private IAccountService accountService;

	@RequestMapping("/findAll")
	public String findAllAccount() {
		System.out.println("视图层ok");
		accountService.findAllAccount();
		return "success";
	}

	@RequestMapping("/findAccountById")
	public String findAccountById(String id) {
		accountService.findAccountById(1);
		return "success";
	}

	@RequestMapping("/addAccount")
	public String addAccount(Account account) {
		accountService.addAccount(account);
		return "success";
	}

	@RequestMapping("/transfer")
	public String transferOfAccount() {
		Account a = new Account();
		a.setId(4);
		Account b = new Account();
		b.setId(5);
		accountService.transferOfAccount(a, b, 200.0f);
		return "success";
	}
}
