package com.fengxiang.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderStatisticsVO implements Serializable {
    //待发货数量
    private Integer toBeConfirmed;

    //运输中数量
    private Integer confirmed;

    //已完成数量
    private Integer deliveryInProgress;
}
