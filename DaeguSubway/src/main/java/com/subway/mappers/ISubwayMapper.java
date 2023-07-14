package com.subway.mappers;

import com.subway.entities.SubwayEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ISubwayMapper {
    int insertSubway(SubwayEntity subway);

}