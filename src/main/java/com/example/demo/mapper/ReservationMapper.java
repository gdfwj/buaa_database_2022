package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Cart;
import com.example.demo.entity.Reservation;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReservationMapper {
    @Insert("insert into Reservation values(#{pid}, #{uid}, #{number}, #{created_time}, #{updated_time}, #{expired_time}, #{is_valid});")
    public int insert(Reservation reservation);

    @Select("select * from Reservation where uid = #{uid} and pid = #{pid}")
    public List<Reservation> searchByIds(int uid, int pid);

    @Update("update Reservation set number = #{number} where uid = #{uid} and pid = #{pid}")
    public int updateNumberByIds(Reservation cart);

    @Delete("delete from Reservation where uid = #{uid} and pid = #{pid}")
    public int deleteByIds(int uid, int pid);

    @Select("select * from Reservation where uid = #{uid}")
    public List<Reservation> searchByUid(int uid);
}
