package controllers;

import com.cc.magentoAPI.MagentoAPIService;

import play.mvc.*;

import views.html.*;

public class Application extends Controller {
	
  public static Result index() {
	  
	String result = MagentoAPIService.queryOrders();
	  
    return ok(index.render(result));
  }
  
}