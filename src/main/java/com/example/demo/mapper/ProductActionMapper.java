package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Productaction;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductActionMapper{
    @Insert("insert into ProductAction values(#{paid}, #{uid}, #{pid}, #{action_type}, #{action_time}, #{status});")
    public int insert(Productaction productaction);

    @Delete("delete from ProductAction where paid = #{paid};")
    public int deleteById(int paid);

    @Insert("update ProductAction " +
            "set uid = #{uid}, pid = #{pid}, action_type = #{action_type}, " +
            "action_time = #{action_time}, status = #{status} where paid = #{paid};")
    public int updateById(Productaction productaction);

    @Select("select * from ProductAction where paid = #{paid};")
    public List<Productaction> selectById(int paid);

    @Select("select action_type from ProductAction where pid = #{pid};")
    public List<Integer> selectTypeByPid(int pid);

    @Select("select * from ProductAction where uid = #{uid};")
    public List<Productaction> selectByUid(int uid);

    @Select("select * from from ProductAction where pid = #{pid} and action_type=1;")
    public List<Productaction> selectBuy(int uid);
}
