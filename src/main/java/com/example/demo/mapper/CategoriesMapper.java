package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Categories;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoriesMapper{
    @Insert("insert into Categories values(#{category_id}, #{category_name});")
    public int insert(Categories category);

    @Delete("delete from Categories where Categories.category_id = #{category_id};")
    public int deleteById(int category_id);

    @Insert("update Categories " +
            "set category_name = #{category_name} where category_id = #{category_id}")
    public int update(Categories category);

    @Select("select * from Categories")
    public List<Categories> selectAll();

    @Select("select * from Categories where category_id = #{category_id};")
    public List<Categories> selectById(int category_id);
}
