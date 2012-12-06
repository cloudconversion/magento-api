package com.cc.magentoAPI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import play.Logger;

import com.jcraft.jsch.Identity;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class MysqlTunnelSession {
	
	  private static void doSshTunnel( String strSshUser, String strSshPassword, String strSshHost, int nSshPort, String strRemoteHost, int nLocalPort, int nRemotePort ) throws JSchException {
	    final JSch jsch = new JSch();
	    Session session = jsch.getSession( strSshUser, strSshHost, 22 );

	    if(strSshPassword!=null && strSshPassword!="") session.setPassword( strSshPassword );
	    else jsch.addIdentity("/Users/jon/.ssh/id_rsa","/Users/jon/.ssh/id_rsa.pub","welcome8".getBytes());
	    //"/Users/jon/.ssh/id_rsa.pub",null);

	    final Properties config = new Properties();
	    config.put( "StrictHostKeyChecking", "no" );
	    session.setConfig( config );
	    
	    session.connect();
	    Logger.info("sessionPort="+session.getPort());
	    session.setPortForwardingL(nLocalPort, strRemoteHost, nRemotePort);
	    Logger.info("host="+session.getHost());
	  }
	  
	  public static void main(String[] args) {
		Connection con = null;
		try {
	      String strSshUser = "campchef";                  // SSH loging username
	      String strSshPassword = "";                   // SSH login password
	      String strSshHost = "outdoorcooking.com";          // hostname or ip or SSH server
	      int nSshPort = 22;                                    // remote SSH host port number
	      String strRemoteHost = "10.10.47.30";  // hostname or ip of your database server
	      int nLocalPort = 5656;                                // local port number use to bind SSH tunnel
	      int nRemotePort = 3306;                               // remote port number of your database 
	      String strDbUser = "campchef_mage";                    // database loging username
	      String strDbPassword = "BannerChitsCareenMiter";                    // database login password
	      
	      MysqlTunnelSession.doSshTunnel(strSshUser, strSshPassword, strSshHost, nSshPort, strRemoteHost, nLocalPort, nRemotePort);
	      
	      Class.forName("com.mysql.jdbc.Driver");
	      con = DriverManager.getConnection("jdbc:mysql://localhost:"+nLocalPort+"/campchef_mage_prod", strDbUser, strDbPassword);
	      Logger.info("Database connection established");
	      Logger.info("autoCommit="+con.getAutoCommit());

	      String query = "Select increment_id, status, grand_total from sales_flat_order LIMIT 1";
	      String result = MagentoAPIService.query(con, query);
	      Logger.info("result="+result);
	      
	      con.close();
	    } catch( Exception e ) {
	      if(con!=null) try { con.close(); } catch( Exception e2 ) {}
	      e.printStackTrace();
	    } finally {
	      System.exit(0);
	    }
	  }
}
