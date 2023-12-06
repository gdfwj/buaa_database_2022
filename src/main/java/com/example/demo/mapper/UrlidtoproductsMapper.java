package com.example.demo.mapper;

import com.example.demo.entity.Urlidtoproducts;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UrlidtoproductsMapper {
    @Insert("insert into UrlidToProducts values(#{urlid}, #{pid});")
    public int insert(Urlidtoproducts urlidtoproducts);

    @Delete("delete from UrlidToProducts where urlid = #{urlid};")
    public int deleteById(int urlid);

    @Insert("update UrlidToProducts " +
            "set pid = #{pid} where urlid = #{urlid};")
    public int updateById(Urlidtoproducts urlidtoproducts);

    @Select("select * from UrlidToProducts where urlid = #{urlid};")
    public List<Urlidtoproducts> selectById(int urlid);

    @Select("select * from UrlidToProducts where pid = #{pid};")
    public List<Urlidtoproducts> selectByPid(int pid);

    @Select("select UrlidToUrl.url from UrlidToUrl, UrlidToProducts where " +
            "UrlidToProducts.urlid=UrlidToUrl.urlid and UrlidToProducts.pid = #{pid};")
    public List<String> selectUrlByPid(int pid);
}
