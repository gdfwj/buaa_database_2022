package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.accessory.HistoryInfo;
import com.example.demo.entity.Useraction;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserActionMapper{
    @Insert("insert into UserAction values(#{uaid}, #{uid1}, #{uid2}, #{action_type}, #{action_time}, #{status});")
    public int insert(Useraction useraction);

    @Delete("delete from UserAction where uaid = #{uaid};")
    public int deleteById(int uaid);

    @Insert("update UserAction " +
            "set uid1 = #{uid1}, uid2 = #{uid2}, action_type = #{action_type}, " +
            "action_time = #{action_time}, status = #{status} where uaid = #{uaid};")
    public int updateById(Useraction useraction);

    @Select("select * from ProductAction where uaid = #{uaid};")
    public List<Useraction> selectById(int uaid);

    @Select("select * from ProductAction where uid = #{uid};")
    public List<Useraction> selectByUid(int uid);

    @Select("select Users.uid, Users.username, Products.pid, Products.product_name, ProductAction.action_time " +
            "from ProductAction, Users, Products where ProductAction.uid = #{uid} and ProductAction.action_type=1 " +
            "and ProductAction.pid=Products.pid and Products.created_by_uid=Users.uid")
    public List<HistoryInfo> selectBuyAction(int uid);

    @Select("select Users.uid, Users.username, Products.pid, Products.product_name, ProductAction.action_time " +
            "from ProductAction, Users, Products where ProductAction.uid = Users.uid and ProductAction.action_type=1 " +
            "and ProductAction.pid=Products.pid and Products.created_by_uid=#{uid}")
    public List<HistoryInfo> selectSellAction(int uid);
}
