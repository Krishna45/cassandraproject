package com.spring.cassandraproject;

import java.sql.Connection;
import java.util.Iterator;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;

/**
 * Hello world!
 *
 */
public class App 
{
	private static String host="";
	private static int port=9042;
	private static String userName="cassandra";
	private static String password="cassandra";
	private static String clusterName="Test Cluster";
	private static int MAX_CONNECTION=100;
	private static int CORE_CONNECTION=25;
	
	static Session getSession()
	{
		PoolingOptions poolingOptions=new PoolingOptions();
		poolingOptions.setMaxConnectionsPerHost(HostDistance.LOCAL, MAX_CONNECTION);
		poolingOptions.setCoreConnectionsPerHost(HostDistance.LOCAL, CORE_CONNECTION);
		Cluster cluster = Cluster.builder()
									.addContactPoint("127.0.0.1")
									.withPort(port)
									.withCredentials(userName, password)
									.withPoolingOptions(poolingOptions)
									.withClusterName(clusterName)
									.build();
		Session session = cluster.connect( );
		return session;
	}
	
	static void createKeyspace(String keyspace)
	{
		String query="CREATE KEYSPACE " + keyspace + " WITH REPLICATION " +"= {'class':'SimpleStrategy', 'replication_factor':3};";
		Session session=getSession();
		session.execute(query);
		session.execute("use "+keyspace);
		System.out.println("Keyspace created");
	}
	static void createColumnFamily()
	{
		String query="CREATE TABLE cms.products (listingId varchar, sellerId varchar, productId varchar, ssp int, sla int, stock int, primary key(productId, listingId))";
		Session session=getSession();
		session.execute(query);
	}
	static void insert()
	{
		Insert insertStatement=QueryBuilder.insertInto("cms", "products");
		insertStatement.value("productid", "1");
		insertStatement.value("listingid", "2");
		insertStatement.value("sellerid", "8");
		insertStatement.value("sla", 10);
		insertStatement.value("ssp", 18);
		insertStatement.value("stock", 20);
		Session session=getSession();
		session.execute(insertStatement);
	}
	static void fetch()
	{
		Statement statement=QueryBuilder.select().all().from("cms", "products");
		Session session=getSession();
		ResultSet rs=session.execute(statement);
		System.out.println(rs.all());
	}
	
    public static void main( String[] args )
    {
    	String keyspace="cms";
    	//CREATING KEYSPACE
    	//createKeyspace(keyspace);  
    	//CREATING COLUMN FAMILY
    	//createColumnFamily();
    	//INSERT DATA
    	//insert();
    	//fetch data
    	fetch();
    }
}
