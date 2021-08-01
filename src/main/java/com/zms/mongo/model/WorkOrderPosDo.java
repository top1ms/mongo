package com.zms.mongo.model;

import lombok.Data;
import org.bson.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : zms
 * @create : 2021/7/31
 * @desc : 工单MongoLBS
 */

@Data
public class WorkOrderPosDo implements Serializable {

    /**
     * 城市 Id
     */
    private Integer cityId;

    /**
     * 业务线
     */
    private Integer bizType;

    /**
     * 工单类型
     */
    private Integer type;

    /**
     * 工单 id
     */
    private Long woId;

    /**
     * 中心点
     */
    private Document centerPos;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 二级工单类型
     */
    private Integer secondType;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 目标id
     */
    private Integer targetId;

    /**
     * 目标类型
     */
    private String targetType;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModify;

    /**
     * 本地时间
     */
    private Date locateTime;


}
