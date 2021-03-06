package com.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import com.dao.NeoDao;
import com.domain.Page;

public class Service {
	private GraphDatabaseService graphDb;
	private ObjectMapper objectMapper;
	private NeoDao dao;

	public Service(ObjectMapper objectMapper, GraphDatabaseService graphDb) {
		this.objectMapper = objectMapper;
		this.graphDb = graphDb;
		dao = new NeoDao(this.graphDb);
	}

	public Page getPageData(int pageIndex, int pageSize, String org_id) {
		long totalCount = dao.getTotalCount(org_id);
//		int pagenum = pageIndex == null ? 1 : Integer.parseInt(pageIndex);
		Page page = new Page(totalCount, pageIndex, pageSize);
		List list = dao.getPageData(org_id, page.getStartIndex(), page.getPageSize());
		page.setList(list);
		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream os) throws IOException, WebApplicationException {
				JsonGenerator jg = objectMapper.getJsonFactory().createJsonGenerator(os, JsonEncoding.UTF8);
				jg.writeStartObject();
				jg.writeFieldName("count");
				jg.writeNumber(totalCount);
				jg.writeFieldName("page_size");
				jg.writeNumber(page.getPageSize());
				jg.writeFieldName("page");
				jg.writeNumber(pageIndex);
				jg.writeFieldName("org");
				jg.writeStartArray();

				try (Transaction tx = graphDb.beginTx()) {
					for(int i=0;i<list.size();i++) {
						Map<String, Object> row = (Map<String, Object>) list.get(i);
						jg.writeStartObject();
						jg.writeFieldName("StartNode");
						jg.writeStartObject();
						Node node = (Node) row.get("o");
						Iterable<String> props = node.getPropertyKeys();
						Iterator<String> iter = props.iterator();
						while(iter.hasNext()) {
							String name = iter.next();
							jg.writeFieldName(name);
							jg.writeString(node.getProperty(name).toString());
						}				
						jg.writeEndObject();
	
						jg.writeFieldName("relationship");
						jg.writeStartObject();
						
						Relationship ship = (Relationship) row.get("r");
						props = ship.getPropertyKeys();
						iter = props.iterator();
						while(iter.hasNext()) {
							String name = iter.next();
							jg.writeFieldName(name);
							jg.writeString(ship.getProperty(name).toString());
						}	
						jg.writeEndObject();
	
						jg.writeFieldName("EndNode");
						jg.writeStartObject();
						
						node = (Node) row.get("o1");
						props = node.getPropertyKeys();
						iter = props.iterator();
						while(iter.hasNext()) {
							String name = iter.next();
							jg.writeFieldName(name);
							jg.writeString(node.getProperty(name).toString());
						}	
						jg.writeEndObject();
						jg.writeEndObject();
					}
				 tx.success();
				}

				jg.writeEndArray();
				jg.writeEndObject();
				jg.flush();
				jg.close();
			}
		};
		page.setStream(stream);

		return page;
	}

	public Page getPageData(int pageIndex, int pageSize, String startNode, String endNode) {
		long totalCount = dao.getTotalCount(startNode, endNode);
//		int pagenum = pageIndex == null ? 1 : Integer.parseInt(pageIndex);
		Page page = new Page(totalCount, pageIndex, pageSize);
		List list = dao.getPageData(startNode, endNode, page.getStartIndex(), page.getPageSize());
		page.setList(list);
		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream os) throws IOException, WebApplicationException {
				JsonGenerator jg = objectMapper.getJsonFactory().createJsonGenerator(os, JsonEncoding.UTF8);
				jg.writeStartObject();
				jg.writeFieldName("count");
				jg.writeNumber(totalCount);
				jg.writeFieldName("page_size");
				jg.writeNumber(page.getPageSize());
				jg.writeFieldName("page");
				jg.writeNumber(pageIndex);
				jg.writeFieldName("org");
				jg.writeStartArray();
				try (Transaction tx = graphDb.beginTx()) {
					for(int i=0;i<list.size();i++) {
						Map<String, Object> row = (Map<String, Object>) list.get(i);
						jg.writeStartObject();
						jg.writeFieldName("StartNode");
						
						jg.writeStartObject();
						Node node = (Node) row.get("o");
						Iterable<String> props = node.getPropertyKeys();
						Iterator<String> iter = props.iterator();
						while(iter.hasNext()) {
							String name = iter.next();
							jg.writeFieldName(name);
							jg.writeString(node.getProperty(name).toString());
						}	
						jg.writeEndObject();
	
						jg.writeFieldName("relationship");
						jg.writeStartObject();
						Relationship ship = (Relationship) row.get("r");
						props = ship.getPropertyKeys();
						iter = props.iterator();
						while(iter.hasNext()) {
							String name = iter.next();
							jg.writeFieldName(name);
							jg.writeString(ship.getProperty(name).toString());
						}	
						jg.writeEndObject();
	
						jg.writeFieldName("EndNode");
						jg.writeStartObject();
						node = (Node) row.get("o1");
						props = node.getPropertyKeys();
						iter = props.iterator();
						while(iter.hasNext()) {
							String name = iter.next();
							jg.writeFieldName(name);
							jg.writeString(node.getProperty(name).toString());
						}	
						jg.writeEndObject();
						jg.writeEndObject();
					}
				 tx.success();
				};

				jg.writeEndArray();
				jg.writeEndObject();
				jg.flush();
				jg.close();
			}
		};
		page.setStream(stream);
		return page;
	}
}
