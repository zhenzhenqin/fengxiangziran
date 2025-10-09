package com.fengxiang.service;
import com.fengxiang.vo.BusinessDataVO;
import com.fengxiang.vo.GoodOverViewVO;
import com.fengxiang.vo.OrderOverViewVO;

public interface WorkspaceService {

    /**
     * 获得今日运营数据
     * @return
     */
    BusinessDataVO getBusinessData();

    /**
     * 获得订单统计数据
     * @return
     */
    OrderOverViewVO getOrderOverView();

    /**
     * 获得菜品统计数据
     * @return
     */
    GoodOverViewVO getGoodOverView();

}
