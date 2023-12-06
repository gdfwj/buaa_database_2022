package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Rating;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RatingMapper {
    @Insert("insert into Rating values(#{rid}, #{uid1}, #{uid2}, #{rating});")
    public int insert(Rating rating);

    @Select("select * from Rating where uid1 = #{uid1} and uid2 = #{uid2}")
    public List<Rating> searchByIds(int uid1, int uid2);

    @Update("update Rating set rating = #{rating} where uid1 = #{uid1} and uid2 = #{uid2}")
    public int updateRatingByIds(Rating rating);

    @Delete("delete from Rating where uid1 = #{uid1} and uid2 = #{uid2}")
    public int deleteByIds(int uid1, int uid2);

    @Select("select * from Rating where rid = #{rid}")
    public List<Rating> searchByRid(int rid);

    @Select("select * from Rating where uid2 = #{targetUid}")
    public List<Rating> searchById(int targetUid);


}
