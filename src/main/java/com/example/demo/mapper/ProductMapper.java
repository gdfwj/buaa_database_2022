package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Products;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductMapper{

    @Select("select * from Products where product_name like #{key} and price<=#{high} and " +
            "price >=#{low} and ${category} ${status_line};")
    public List<Products> fuzzySearch(String key, double low, double high, String category, String status_line);

    @Select("select * from Products where Products.pid = #{pid};")
    public List<Products> selectById(int pid);

    @Insert("insert into Products values(#{pid}, #{product_name}, #{price}, " +
            "#{product_number}, #{isbn}, #{category_id}, #{product_status}, " +
            "#{create_time}, #{update_time}, #{created_by_uid}, #{text});")
    public int insert(Products product);

    @Delete("delete from Products where Products.pid = #{pid};")
    public int deleteById(int pid);

    @Insert("update Products set product_name = #{product_name}, price = #{price}, " +
            "product_number = #{product_number}, isbn = #{isbn}, category_id = #{category_id}, " +
            "product_status = #{product_status}, create_time = #{create_time}, " +
            "update_time = #{update_time}, created_by_uid = #{created_by_uid}, text = #{text} where pid = #{pid};")
    public int updateById(Products product);

    @Select("select * from Products where Products.created_by_uid = #{uid};")
    public List<Products> selectByUid(int uid);

    @Select("select * from Products;")
    public List<Products> all();

    @Select("select * from Products where product_status=1;")
    public List<Products> onSell();
}
