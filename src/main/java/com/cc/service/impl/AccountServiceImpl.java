package com.cc.service.impl;

import com.cc.dao.AccountMapper;
import com.cc.pojo.Account;
import com.cc.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2019/8/5 11:23
 **/
@Service("accountService")
public class AccountServiceImpl implements IAccountService {
	@Autowired
	private AccountMapper accountDao;

	@Override
	public List<Account> findAllAccount() {
		List<Account> allAccount = accountDao.findAllAccount();
		System.out.println("业务层Ok");
		System.out.println(allAccount);
		return allAccount;
	}

	@Override
	public Account findAccountById(Integer id) {
		return accountDao.findAccountById(id);
	}

	@Override
	public void addAccount(Account account) {
		accountDao.addAccount(account);
	}

	/**
	 * 转账服务，检测spring的事务控制
	 *
	 * @param a     转出账户
	 * @param b     转入账户
	 * @param money 转账金额
	 */
	@Override
	public void transferOfAccount(Account a, Account b, float money) {
		Account accountA = accountDao.findAccountById(a.getId());
		Account accountB = accountDao.findAccountById(b.getId());
		accountA.setMoney(accountA.getMoney() - money);
		accountB.setMoney(accountB.getMoney() + money);

		accountDao.updateAccountMoneyById(accountA);
		// 系统发生异常模拟
//		int i = 1 / 0;
		accountDao.updateAccountMoneyById(accountB);
	}


}
