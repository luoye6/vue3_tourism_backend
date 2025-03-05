package com.xiaobaitiao.springbootinit.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaobaitiao.springbootinit.model.entity.Notice;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 公告视图
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Data
public class NoticeVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 公告标题
     */
    private String noticeTitle;

    /**
     * 公告内容
     */
    private String noticeContent;
    /**
     * 发布公告的管理员 id
     */
    private Long noticeAdminId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    /**
     * 创建用户信息
     */
    private UserVO user;


    private static final long serialVersionUID = 1L;

    /**
     * 封装类转对象
     *
     * @param noticeVO
     * @return
     */
    public static Notice voToObj(NoticeVO noticeVO) {
        if (noticeVO == null) {
            return null;
        }
        Notice notice = new Notice();
        BeanUtils.copyProperties(noticeVO, notice);
        return notice;
    }

    /**
     * 对象转封装类
     *
     * @param notice
     * @return
     */
    public static NoticeVO objToVo(Notice notice) {
        if (notice == null) {
            return null;
        }
        NoticeVO noticeVO = new NoticeVO();
        BeanUtils.copyProperties(notice, noticeVO);
        return noticeVO;
    }
}
