//package com.hm.his.task;
//
//import java.util.Date;
//import java.util.List;
//
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.lang3.time.DateFormatUtils;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import com.hm.his.framework.utils.AmtUtils;
//import com.hm.his.module.drug.model.DrugTrade;
//import com.hm.his.module.drug.model.DrugTypeEnum;
//import com.hm.his.module.drug.pojo.SaleWayPojo;
//import com.hm.his.module.drug.service.DrugService;
//import com.hm.his.module.drug.service.DrugTradeService;
//import com.hm.his.module.order.dao.OrderDrugMapper;
//import com.hm.his.module.order.model.OrderDrug;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath:spring-config.xml", "classpath:mybatis.xml" })
//public class OrderDrugTask {
//	@Autowired
//	OrderDrugMapper orderDrugMapper;
//	@Autowired
//	DrugService drugService;
//	@Autowired
//	DrugTradeService drugTradeService;
//
//	/**
//	 * 4.20已经执行完毕
//	 * @description  因为一阶段的订单中的诊所饮片的单位没有设置，导致没有计算出进货价和利润，此方法用来补救
//	 * @date 2016年4月8日
//	 * @author lipeng
//	 * @throws Exception
//	 */
//	public void updatePurchasePriceAndProfit() throws Exception {
//		String workDate = "2016-04-20"; // 防止maven打包，或者其他途径误操作，执行完之后去掉@Test
//		Date date = new Date();
//		String dateStr = DateFormatUtils.format(date, "yyyy-MM-dd");
//		System.out.println("修改一阶段的订单中的诊所饮片的进货价和利润-----------------------开始，执行日期：" + dateStr);
//		if (!dateStr.equals(workDate)) {
//			System.out.println("执行日期不对，结束；应该执行日期" + workDate + ",现在日期：" + dateStr);
//			return;
//		}
//		OrderDrug queryParam = new OrderDrug();
//		queryParam.setDataSource(0);
//		queryParam.setDrugType(DrugTypeEnum.herbal.getDrugType());
//		List<OrderDrug> list = orderDrugMapper.selectList(queryParam); // orderDrugMapper.selectByDataSourceAndDrugType(0,
//																		// DrugTypeEnum.herbal.getDrugType());
//		if (CollectionUtils.isEmpty(list)) {
//			System.out.println("没有需要修改的数据");
//		} else {
//			System.out.println("需要处理的数据有" + list.size() + "个");
//			for (OrderDrug orderDrug : list) {
//				Double purchasePrice = this.getPurchasePrice(orderDrug.getDrugId(), orderDrug.getSaleUnit(), orderDrug.getSaleChannel());
//				orderDrug.setPurchasePrice(purchasePrice);
//				Double profit = this.getProfit(purchasePrice, orderDrug.getSalePrice());
//				orderDrug.setProfit(profit);
//				orderDrugMapper.update(orderDrug);
//			}
//		}
//		System.out.println("修改一阶段的订单中的诊所饮片的进货价和利润-----------------------结束");
//	}
//
//	/**
//	 *  4.20已经执行完毕
//	 * @description 修改一阶段的订单中的惠每药物的drugType和厂家和规则
//	 * @date 2016年4月8日
//	 * @author lipeng
//	 * @throws Exception
//	 */
//	public void updateHuiMeiDrug() throws Exception {
//		String workDate = "2016-04-20";// 防止maven打包，或者其他途径误操作，执行完之后去掉@Test
//		Date date = new Date();
//		String dateStr = DateFormatUtils.format(date, "yyyy-MM-dd");
//		System.out.println("修改一阶段的订单中的惠每药物的drugType和厂家和规则-----------------------开始，执行日期：" + dateStr);
//		if (!dateStr.equals(workDate)) {
//			System.out.println("执行日期不对，结束；应该执行日期" + workDate + ",现在日期：" + dateStr);
//			return;
//		}
//		OrderDrug queryParam = new OrderDrug();
//		queryParam.setDataSource(1);
//		List<OrderDrug> list = orderDrugMapper.selectList(queryParam);
//		if (CollectionUtils.isEmpty(list)) {
//			System.out.println("没有需要修改的数据");
//		} else {
//			System.out.println("需要处理的数据有" + list.size() + "个");
//			for (OrderDrug orderDrug : list) {
//				// 惠每的药
//				DrugTrade drugTrade = drugTradeService.getDrugById(orderDrug.getDrugId());
//				if (drugTrade != null) {
//					orderDrug.setManufacturer(drugTrade.getManufacturer());
//					orderDrug.setSpecification(drugTrade.getSpecification());
//					orderDrug.setDrugType(drugTrade.getDrugType());
//					orderDrugMapper.update(orderDrug);
//				}
//			}
//		}
//
//		System.out.println("修改一阶段的订单中的惠每药物的drugType和厂家和规则-----------------------结束");
//	}
//
//	/**
//	 * 
//	 * @description 获取进货价
//	 * @date 2016年3月28日
//	 * @author lipeng
//	 * @param drugId
//	 * @param saleUnit
//	 * @return
//	 */
//	private Double getPurchasePrice(Long drugId, String saleUnit, Integer saleChannel) {
//		if (drugId == null || saleUnit == null || saleChannel == null) {
//			return null;
//		}
//		List<SaleWayPojo> saleWayPojos = drugService.searchDrugSaleWayById(saleChannel, drugId);
//		if (CollectionUtils.isEmpty(saleWayPojos)) {
//			return null;
//		}
//		SaleWayPojo saleWayPojo = null;
//		for (SaleWayPojo pojo : saleWayPojos) {
//			if (saleUnit.equals(pojo.getUnit())) {
//				saleWayPojo = pojo;
//				break;
//			}
//		}
//		return saleWayPojo == null ? null : saleWayPojo.getPurchasePrice();
//	}
//
//	/**
//	 * 
//	 * @description 获取利润
//	 * @date 2016年3月28日
//	 * @author lipeng
//	 * @param purchasePrice
//	 * @param salePrice
//	 * @return
//	 */
//	private Double getProfit(Double purchasePrice, Double salePrice) {
//		if (purchasePrice == null || salePrice == null) {
//
//			return 0D;
//		}
//		return AmtUtils.subtract(salePrice, purchasePrice);
//	}
//}
