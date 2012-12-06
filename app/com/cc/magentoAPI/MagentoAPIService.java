package com.cc.magentoAPI;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import play.Logger;
import play.db.*;

public class MagentoAPIService {
	
	public static String queryProducts() {
		String products = "select sku, name, msrp from catalog_product_flat_1 LIMIT 1";
		return query(products);
	}
	
	public static String queryImages() {
		String images = "select sku, img.value from catalog_product_flat_1 p, catalog_product_entity_media_gallery where p.entity_id=img.entity_id"; // updated_at > Now.addMinutes(-20)
		return query(images);
	}
	
	public static String queryRMAs() {
		String rmas = "select order_id from enterprise_rma";
		return query(rmas);
	}
	
	public static String queryOrders() {
		String orders = "Select increment_id, status, grand_total from sales_flat_order LIMIT 1"; // Where updated_at > Now.addMinutes(-20) AND created_at > Now.addMinutes(-20)
		return query(orders);
	}

	public static String query(String query) {
		DataSource ds = DB.getDataSource("magento");
		Connection conn = DB.getConnection("magento");
		return query(conn, query);
	}
	
	public static String query(Connection conn, String query) {
		String result = "";
		try {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(query);
			while (rs.next()) {
			    // use a java.io.InputStream to get the data
			    java.io.InputStream ip = rs.getAsciiStream(1);
			    // process the stream--this is just a generic way to print the data
			    int c;
			    int columnSize = 0;
			    byte[] buff = new byte[128];
			    for (;;) {
			        int size = ip.read(buff);
			        if (size == -1)
			            break;
			        columnSize += size;
			        String chunk = new String(buff, 0, size);
			        Logger.info(chunk);
			        result += chunk+"\n";
			    }
			}
			rs.close();
			s.close();
			//conn.commit();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
