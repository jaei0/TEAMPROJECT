package com.parking.mappers;

import com.parking.entities.ParkingEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IParkingMapper {
    int insertParking(ParkingEntity parking);

}