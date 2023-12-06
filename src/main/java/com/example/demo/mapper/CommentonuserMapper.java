package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Commentonuser;
import com.example.demo.entity.Comments;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentonuserMapper {
    @Insert("insert into Commentonuser values(#{comment_id}, #{comment_content}, #{uid1}, #{uid2}, " +
            "#{create_time}, #{update_time});")
    public int insert(Commentonuser commentonuser);

    @Delete("delete from Commentonuser where Commentonuser.comment_id = #{comment_id};")
    public int deleteById(int comment_id);

    @Insert("update Commentonuser " +
            "set Commentonuser.comment_content = #{comment_content}, " +
            "Commentonuser.uid1 = #{uid1}, Commentonuser.uid2 = #{uid2}, " +
            "Commentonuser.create_time = #{create_time}, Commentonuser.update_time = #{update_time} " +
            "where Commentonuser.comment_id = #{comment_id}")
    public int updateById(Commentonuser commentonuser);


    @Select("select * from Commentonuser where comment_id = #{comment_id};")
    public List<Commentonuser> selectById(int comment_id);

    @Select("select * from Commentonuser where uid1 = #{uid};")
    public List<Commentonuser> selectBySenderId(int uid);

    @Select("select * from Commentonuser where uid2 = #{uid};")
    public List<Commentonuser> selectByReceiverId(int uid);

}