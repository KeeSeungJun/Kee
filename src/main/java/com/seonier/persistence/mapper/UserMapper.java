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

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.seonier.persistence.model.User;

@Mapper
public interface UserMapper {

	/** PK(USR_NO)로 조회 */
//	@Select("""
//        SELECT
//          USR_NO       AS userNo,
//          USR_ID       AS userId,
//          PASSWD       AS passwd,
//          USR_NM       AS userName,
//          USR_MBTLNUM  AS mobileNumber,
//          USR_GRP_ID   AS userGroupId,
//          USE_AT       AS useAT,
//          CREAT_ID     AS createId,
//          UPDT_ID      AS updateId,
//          USR_ADDR     AS userAddr,
//          USR_ADDR_LAT AS userAddrLat,
//          USR_ADDR_LON AS userAddrLon,
//          USR_JOB_SCR  AS userJobScr,
//          USR_GENDER   AS gender,
//          -- health 필드들…
//          USR_HEALTH1  AS userHealth1,
//          USR_HEALTH2  AS userHealth2,
//          USR_HEALTH3  AS userHealth3,
//          USR_HEALTH4  AS userHealth4,
//          USR_HEALTH5  AS userHealth5,
//          USR_HEALTH6  AS userHealth6,
//          USR_HEALTH7  AS userHealth7,
//          USR_HEALTH8  AS userHealth8,
//          USR_HEALTH_ECT AS userHealthEct
//        FROM USR_INFO
//        WHERE USR_NO = #{userNo}
//          AND USE_AT = 'YES'
//        """)
	User findByUserNo(long userNo);

	/** USR_ID 로 조회 */
//	@Select("""
//        SELECT
//          USR_NO       AS userNo,
//          USR_ID       AS userId,
//          PASSWD       AS passwd,
//          USR_NM       AS userName,
//          USR_MBTLNUM  AS mobileNumber,
//          USR_GRP_ID   AS userGroupId,
//          USE_AT       AS useAT,
//          CREAT_ID     AS createId,
//          UPDT_ID      AS updateId,
//          USR_ADDR     AS userAddr,
//          USR_ADDR_LAT AS userAddrLat,
//          USR_ADDR_LON AS userAddrLon,
//          USR_JOB_SCR  AS userJobScr,
//          USR_GENDER   AS gender,
//          -- health 필드들…
//          USR_HEALTH1  AS userHealth1,
//          USR_HEALTH2  AS userHealth2,
//          USR_HEALTH3  AS userHealth3,
//          USR_HEALTH4  AS userHealth4,
//          USR_HEALTH5  AS userHealth5,
//          USR_HEALTH6  AS userHealth6,
//          USR_HEALTH7  AS userHealth7,
//          USR_HEALTH8  AS userHealth8,
//          USR_HEALTH_ECT AS userHealthEct
//        FROM USR_INFO
//        WHERE USR_ID = #{userId}
//          AND USE_AT = 'YES'
//        """)
	User findByUserId(String userId);

	/** 새 사용자 등록 (예시) */
//	@Insert("""
//        INSERT INTO USR_INFO
//          (USR_ID, PASSWD, USR_NM, USR_MBTLNUM, USR_GRP_ID,
//           CREAT_ID, UPDT_ID)
//        VALUES
//          (#{userId}, #{passwd}, #{userName}, #{mobileNumber}, #{userGroupId},
//           #{createId}, #{updateId})
//        """)
	@Options(useGeneratedKeys = true, keyProperty = "userNo", keyColumn = "USR_NO")
	void insertUser(User user);
}
