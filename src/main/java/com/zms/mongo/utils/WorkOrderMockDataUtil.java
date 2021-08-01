package com.zms.mongo.utils;

import com.zms.mongo.model.WorkOrderPosDo;

import java.util.Date;

/**
 * @author : zms
 * @create : 2021/7/31
 * @desc : 工单 LBS 数据模拟类
 */
public abstract class WorkOrderMockDataUtil {


    public static WorkOrderPosDo mock(){
        WorkOrderPosDo workOrderPosDo = new WorkOrderPosDo();
        workOrderPosDo.setCityId(5);
        workOrderPosDo.setBizType(1);
        workOrderPosDo.setType(mockWorkOrderType());
        workOrderPosDo.setWoId(mockWorkOrderId());
        workOrderPosDo.setCenterPos(GeoGraphicalPointGenerateUtil.generatePointDocument());
        workOrderPosDo.setPriority(mockPriority());
        workOrderPosDo.setSecondType(workOrderPosDo.getType()*1000);
        workOrderPosDo.setStatus(mockPriority());
        workOrderPosDo.setTargetId(0);
        workOrderPosDo.setTargetType("0##0");
        workOrderPosDo.setGmtCreate(new Date());
        workOrderPosDo.setGmtModify(new Date());
        workOrderPosDo.setLocateTime(new Date());
        return workOrderPosDo;
    }

    /**
     * @author : zms
     * @create : 2021/7/31
     * @desc : mock 工单类型
     */

    public static int mockWorkOrderType(){
        return (int) (Math.random() * 9) +1;
    }

    /**
     * @author : zms
     * @create : 2021/7/31
     * @desc : mock 工单 id
     */

    public static long mockWorkOrderId(){
        return (long) ((long) 100000000 +( Math.random() * 4000));
    }

    /**
     * @author : zms
     * @create : 2021/7/31
     * @desc : mock 工单优先级
     */
    public static int mockPriority(){
        return (int) (Math.random() * 5);
    }



    public static void main(String[] args) {
        System.out.println("3412221971108154723".length());
    }

}
