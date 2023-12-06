package com.example.demo.mapper;

import com.example.demo.entity.Urlidtourl;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface Urlidtourlmapper {
    @Insert("insert into UrlidToUrl values(#{urlid}, #{url});")
    public int insert(Urlidtourl urlidtourl);

    @Delete("delete from UrlidToUrl where urlid = #{urlid};")
    public int deleteById(int urlid);

    @Insert("update UrlidToUrl " +
            "set url = #{url} where urlid = #{urlid};")
    public int updateById(Urlidtourl urlidtourl);

    @Select("select * from UrlidToUrl where urlid = #{urlid};")
    public List<Urlidtourl> selectById(int urlid);

    @Select("select * from UrlidToUrl where url = #{url};")
    public List<Urlidtourl> selectByUrl(String url);
}
