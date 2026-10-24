package com.c2w.controller.admin;

import java.util.Map;

import com.c2w.dao.admin.DashBoardData;

public class DashBoardController {
    DashBoardData dashBoardData=new DashBoardData();
    public Map<String,Object> goToCardsData(){
        return dashBoardData.getCardsData();
    }

    public Map<String,Integer> goToStatusData(){
        return dashBoardData.getOrderStatusData();
    }

    public Map<String,Integer> goToCategoryData(){
        return dashBoardData.getProductCategoryData();
    }

    public Map<String,Integer> gotoTopSellingProducts(){
        return dashBoardData.getTopSellingProducts();
    }

    public Map<String,Integer> gotoOrderGrowth(){
        return dashBoardData.getOrderGrowth();
    }
}
