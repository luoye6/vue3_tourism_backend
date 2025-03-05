package com.xiaobaitiao.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaobaitiao.springbootinit.model.entity.Spot;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author zhao9
* @description 针对表【spot】的数据库操作Mapper
* @createDate 2025-02-21 16:28:06
* @Entity generator.domain.Spot
*/
public interface SpotMapper extends BaseMapper<Spot> {
    @Select("SELECT * FROM spot where isDelete = 0 ORDER BY viewNum DESC LIMIT 10")
    List<Spot> selectTop10SpotsByViews();
}




