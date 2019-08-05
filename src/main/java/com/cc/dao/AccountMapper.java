package com.cc.dao;

import com.cc.pojo.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2019/8/5 9:40
 **/
@Repository
public interface AccountMapper {

	/**
	 * 查询所有用户信息
	 * @return
	 */
	@Select("select * from account")
	List<Account> findAllAccount();

	/**
	 * 通过id查询单个用户信息
	 * @param id 用户id
	 * @return
	 */
	@Select("select * from account where id = #{accountId}")
	Account findAccountById(@Param("accountId") Integer id);

	/**
	 * 向数据库增加一条数据
	 * @param account 用户pojo
	 */
	@Insert("insert into account(name,money) values(#{account.name},#{account.money})")
	void addAccount(@Param("account") Account account);

	/**
	 * 根据用户id修改用户账户中的钱
	 * @param account 账户信息
	 */
	@Update("update account set money = #{account.money} where id = #{account.id}")
	void updateAccountMoneyById(@Param("account")Account account);
}
