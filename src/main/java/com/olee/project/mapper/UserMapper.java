package com.olee.project.mapper;

import com.olee.project.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    /**
     * 通过邮箱查询用户
     */
    @Select("SELECT userId,email,password,nickname,address,createAt,updateAt FROM olee_user WHERE email = #{email}")
    User findByEmail(@Param("email") String email);

    /**
     * 通过userId查询用户
     */
    @Select("SELECT userId,email,password,nickname,address,createAt,updateAt FROM olee_user WHERE userId = #{userId}")
    User findByUserId(@Param("userId") String userId);

    /**
     * 通过userId查询用户
     */
    @Select("SELECT userId FROM olee_user WHERE email = #{email}")
    User judgingEmailExists(@Param("email") String email);

    /**
     * 通过userId查询用户
     */
    @Select("SELECT password FROM olee_user WHERE userId = #{userId}")
    User checkPassword(@Param("userId") String userId);

    /**
     * 插入新的用户
     */
    @Insert("INSERT INTO olee_user(userId, email, password, createAt, updateAt, nickname, address) VALUES(#{userId}, #{email}, #{password}, #{createAt}, #{updateAt}, #{nickname}, #{address})")
    void insert(User user);

    /**
     * 修改用户昵称和地址
     */
    //@Update("UPDATE olee_user SET nickname=#{nickname},address=#{address} WHERE userId=#{userId}")
    @UpdateProvider(type = SqlProvider.class, method = "update")
    void updateNicknameAndAddress(User user);

    /**
     * 修改用户密码
     */
    @Update("UPDATE olee_user SET password=#{password},updateAt=#{updateAt} WHERE userId=#{userId}")
    void updatePassword(User user);


}
