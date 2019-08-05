package com.cc.service;

import com.cc.pojo.Account;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2019/8/5 9:42
 **/
@Repository("accountDao")
public interface IAccountService {
	/**
	 * 查询所有用户信息
	 * @return
	 */
	List<Account> findAllAccount();

	/**
	 * 通过id查询单个用户信息
	 * @param id 用户id
	 * @return
	 */
	Account findAccountById(Integer id);

	/**
	 * 增加一个用户及相关数据
	 * @param account 用户pojo
	 */
	void addAccount(Account account);

	/**
	 * 转账服务
	 * @param a 转出账户
	 * @param b 转入账户
	 * @param money 转账金额
	 */
	void transferOfAccount(Account a, Account b, float money);
}
