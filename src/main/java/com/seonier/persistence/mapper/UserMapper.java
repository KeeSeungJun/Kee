//package com.seonier.persistence.mapper;
//
//import org.apache.ibatis.annotations.Mapper;
//import com.seonier.persistence.model.User;
//
//@Mapper
//public interface UserMapper {
//
//	User findByUserNo(long userNo);
//
//	User findByUserId(String userId);
//
//	void insertUser(User user);
//
//}
package com.seonier.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.seonier.persistence.model.User;

@Mapper
public interface UserMapper {

	User findByUserNo(long userNo);

	User findByUserId(String userId);

	User findByMobileNumber(String mobileNumber);

	@Options(useGeneratedKeys = true, keyProperty = "userNo", keyColumn = "USR_NO")
	void insertUser(User user);

	void updateUser(User user);

	void updateUserPassword(User user);

	//List<User> findAll();
}
