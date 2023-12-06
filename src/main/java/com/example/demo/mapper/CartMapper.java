package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Cart;
import com.example.demo.entity.Favorites;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartMapper {
    @Insert("insert into Cart values(#{pid}, #{uid}, #{number}, #{created_time}, #{updated_time}, #{expired_time}, #{is_valid});")
    public int insert(Cart cart);

    @Select("select * from Cart where uid = #{uid} and pid = #{pid}")
    public List<Cart> searchByIds(int uid, int pid);

    @Update("update Cart set number = #{number} where uid = #{uid} and pid = #{pid}")
    public int updateNumberByIds(Cart cart);

    @Delete("delete from Cart where uid = #{uid} and pid = #{pid}")
    public int deleteByIds(int uid, int pid);

    @Select("select * from Cart where uid = #{uid}")
    public List<Cart> searchByUid(int uid);
}
