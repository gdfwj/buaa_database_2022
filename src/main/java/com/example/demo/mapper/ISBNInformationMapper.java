package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Isbninformation;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ISBNInformationMapper{
    @Insert("insert into ISBNInformation values(#{isbn}, #{book_name}, #{book_price});")
    public int insert(Isbninformation isbnInformation);

    @Delete("delete from ISBNInformation where isbn = #{isbn};")
    public int deleteById(String isbn);

    @Select("select * from ISBNInformation where isbn = #{isbn};")
    public List<Isbninformation> selectById(String isbn);

    @Select("select * from ISBNInformation;")
    public List<Isbninformation> selectAll();

    @Update("update  ISBNInformation set book_name = #{book_name}, book_price = #{book_price} where isbn = #{isbn});")
    public int update(Isbninformation isbnInformation);
}
