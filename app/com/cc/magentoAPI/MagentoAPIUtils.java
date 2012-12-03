package com.cc.magentoAPI;

import com.cc.ecs.Order;
import com.cc.ecs.Product;

public class MagentoAPIUtils {

	public static void mapOrders() {
		String qo = MagentoAPIService.queryOrders();
		Order o = new Order();
	}

	public static void mapProducts() {
		String qp = MagentoAPIService.queryProducts();
		Product p = new Product();
	}
}
