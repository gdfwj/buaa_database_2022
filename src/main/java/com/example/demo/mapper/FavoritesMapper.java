package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Favorites;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FavoritesMapper {
    @Insert("insert into Favorites values(#{pid}, #{uid}, #{number}, #{created_time}, #{updated_time}, #{expired_time}, #{is_valid});")
    public int insert(Favorites favorites);

    @Select("select * from Favorites where uid = #{uid} and pid = #{pid}")
    public List<Favorites> searchByIds(int uid, int pid);

    @Delete("delete from Favorites where uid = #{uid} and pid = #{pid}")
    public int deleteByIds(int uid, int pid);

    @Update("update Favorites set number = #{number} where uid = #{uid} and pid = #{pid}")
    public int updateNumberByIds(Favorites favorites);

    @Select("select * from Favorites where uid = #{uid}")
    public List<Favorites> searchByUid(int uid);
}
