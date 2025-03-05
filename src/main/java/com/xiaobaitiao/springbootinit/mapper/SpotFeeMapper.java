package com.xiaobaitiao.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaobaitiao.springbootinit.model.entity.Post;
import com.xiaobaitiao.springbootinit.model.entity.SpotFee;

import java.util.Date;
import java.util.List;

/**
* @author zhao9
* @description 针对表【spot_fee】的数据库操作Mapper
* @createDate 2025-02-26 15:12:37
* @Entity generator.domain.SpotFee
*/
public interface SpotFeeMapper extends BaseMapper<SpotFee> {
    /**
     * 根据 ID 查询门票信息并加锁
     */
     SpotFee selectByIdWithLock(Long id);
}




