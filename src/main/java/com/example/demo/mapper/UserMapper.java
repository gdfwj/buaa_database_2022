package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Users;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper{

    @Select("select * from Users")
    public List<Users> selectAll();

    @Insert("insert into Users values(#{uid}, #{username}, #{password}, #{email}, #{phone}, #{is_admin}, #{user_status}, " +
            "#{register_time}, #{token}, #{user_img_url});")
    public int insert(Users user);

    @Select("select * from Users where username = #{username};")
    public List<Users> findName(String username);

    @Select("select * from Users where username = #{username} and password = #{password};")
    public List<Users> login(String username, String password);

    @Insert("update Users " +
            "set uid = #{uid}, username = #{username}, password = #{password}, email = #{email}, phone = #{phone}, " +
            "is_admin = #{is_admin}, user_status = #{user_status}, register_time = #{register_time}, token = #{token}, " +
            "user_img_url = #{user_img_url} where uid = #{uid};")
    public int update(Users user);

    @Select("select * from Users where uid = #{uid};")
    public List<Users> selectList(int uid);

    @Select("select * from Users where token = #{token};")
    public List<Users> selectToken(String token);

    @Update("update Users set token = \" \" where token = #{token};")
    public int deleteToken(String token);
}