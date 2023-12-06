package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Comments;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentsMapper{
    @Insert("insert into Comments values(#{comment_id}, #{comment_content}, #{isbn}, #{uid}, " +
            "#{create_time}, #{update_time});")
    public int insert(Comments comments);

    @Delete("delete from Comments where Comments.comment_id = #{comment_id};")
    public int deleteById(int comment_id);

    @Insert("update Comments " +
            "set Comments.comment_content = #{comment_content}, " +
            "Comments.isbn = #{isbn}, Comments.uid = #{uid}, " +
            "Comments.create_time = #{create_time}, Comments.update_time = #{update_time} " +
            "where Comments.comment_id = #{comment_id}")
    public int updateById(Comments category);


    @Select("select * from Comments where comment_id = #{comment_id};")
    public List<Comments> selectById(int comment_id);

    @Select("select * from Comments where isbn = #{isbn};")
    public List<Comments> selectByISBN(String isbn);

}
