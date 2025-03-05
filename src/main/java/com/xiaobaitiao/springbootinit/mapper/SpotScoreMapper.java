package com.xiaobaitiao.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaobaitiao.springbootinit.model.entity.SpotScore;
import org.apache.ibatis.annotations.Select;

/**
 * @author zhao9
 * @description 针对表【spot_score】的数据库操作Mapper
 * @createDate 2025-02-26 13:49:52
 * @Entity generator.domain.SpotScore
 */
public interface SpotScoreMapper extends BaseMapper<SpotScore> {
    /**
     * 查询景点的平均评分
     *
     * @param spotId 景点 ID
     * @return 平均评分
     */
    @Select("SELECT AVG(score) AS averageScore FROM spot_score WHERE spotId = #{spotId} AND isDelete = 0")
    Double getAverageScoreBySpotId(Long spotId);
}




