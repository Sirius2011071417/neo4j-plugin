package com.dao;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.MapUtil;


public class NeoDao {
	
	private GraphDatabaseService graphDb;
	
	public NeoDao(GraphDatabaseService graphDb) {
		this.graphDb = graphDb;
	}
	
	public List<Map> match(String org_id) {
		String query = "MATCH (o:Org {org_id: $org_id })-[r]-(o1:Org) RETURN o,r,o1";
		Map<String, Object> params = MapUtil.map( "org_id", org_id);
		List list = new ArrayList();
		try (Transaction tx = graphDb.beginTx();
                Result result = graphDb.execute(query, params)) {
              while (result.hasNext()) {
                  Map<String,Object> row = result.next();
                  list.add(row);
              }
              tx.success();
        }
		return list;
	}
	
	public List match(String startNode, String endNode) {
		String query = "MATCH (o:Org {org_id: $startNode })-[r]-(o1:Org{org_id: $endNode}) RETURN o,r,o1";
		Map<String, Object> params = MapUtil.map("startNode", startNode, "endNode", endNode);
		List list = new ArrayList();
		try (Transaction tx = graphDb.beginTx();
                Result result = graphDb.execute(query, params)) {
              while (result.hasNext()) {
                  Map<String,Object> row = result.next();
                  list.add(row);
              }
              tx.success();
        }
		return list;
	}
	
	public List getPageData(String org_id, int startindex,int pagesize) {
		String query = "MATCH (o:Org {org_id: $org_id })-[r]-(o1:Org) RETURN o,r,o1 skip $startindex limit $pagesize";
		Map<String, Object> params = MapUtil.map("org_id", org_id, "startindex", startindex, "pagesize", pagesize);
		List list = new ArrayList();
		try (Transaction tx = graphDb.beginTx();
                Result result = graphDb.execute(query, params)) {
              while (result.hasNext()) {
                  Map<String,Object> row = result.next();
                  list.add(row);
              }
              tx.success();
        }
		return list;
	}
	
	public List getPageData(String startNode, String endNode, int startindex,int pagesize) {
		String query = "MATCH (o:Org {org_id: $startNode })-[r]-(o1:Org{org_id: $endNode}) RETURN o,r,o1 skip $startindex limit $pagesize";
		Map<String, Object> params = MapUtil.map("startNode", startNode, "endNode", endNode, "startindex", startindex, "pagesize", pagesize);
		List list = new ArrayList();
		try (Transaction tx = graphDb.beginTx();
                Result result = graphDb.execute(query, params)) {
              while (result.hasNext()) {
                  Map<String,Object> row = result.next();
                  list.add(row);
              }
              tx.success();
        }
		return list;
	}
	
	public Long getTotalCount(String org_id) {
		String query = "MATCH (o:Org {org_id: $org_id })-[r]-(o1:Org) RETURN count(*) as cnt";
		Map<String, Object> params = MapUtil.map( "org_id", org_id);
		Long cnt = null;
		try (Transaction tx = graphDb.beginTx();
                Result result = graphDb.execute(query, params)) {
              while (result.hasNext()) {
                  Map<String,Object> row = result.next();
                  cnt = (Long) row.get("cnt");
              }
              tx.success();
        }
		return cnt;
	}
	
	public Long getTotalCount(String startNode, String endNode) {
		System.out.println(startNode + " " + endNode);
		String query = "MATCH (o:Org {org_id: $startNode })-[r]-(o1:Org{org_id: $endNode}) RETURN count(*) as cnt";
		Map<String, Object> params = MapUtil.map("startNode", startNode, "endNode", endNode);
		Long cnt = null;
		try (Transaction tx = graphDb.beginTx();
                Result result = graphDb.execute(query, params)) {
              while (result.hasNext()) {
                  Map<String,Object> row = result.next();
                  System.out.println(row);
                  cnt = (Long) row.get("cnt");
              }
              tx.success();
        }
		return cnt;
	}
}
