package com.zhongbiao.neo;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.bind.JAXBElement;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.string.UTF8;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import com.domain.Page;
import com.service.Service;

/**
 * Hello world!
 *
 */
@Path("/uuid")
public class ColleaguesCypherExecutionResource{
    private final ObjectMapper objectMapper;
    private GraphDatabaseService graphDb;

    public ColleaguesCypherExecutionResource( @Context GraphDatabaseService graphDb )
    {
        this.graphDb = graphDb;
        this.objectMapper = new ObjectMapper();
    }

    @GET
//    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findColleagues(@PathParam("id") String id, @DefaultValue("1") @QueryParam("page") String page, @DefaultValue("15") @QueryParam("page_size") String page_size){

    	Service service = new Service(this.objectMapper, this.graphDb);
    	Page p = service.getPageData(Integer.parseInt(page), Integer.parseInt(page_size), id);
        return Response.ok().entity(p.getStream()).build();
    }
    
    @GET
    @Path("/relations")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findOrgId(@QueryParam("startNode") String startNode, @QueryParam("endNode") String endNode, @DefaultValue("1") @QueryParam("page") String page, @DefaultValue("15") @QueryParam("page_size") String page_size) {
    	Service service = new Service(this.objectMapper, this.graphDb);
    	Page p = service.getPageData(Integer.parseInt(page), Integer.parseInt(page_size), startNode, endNode);
        return Response.ok().entity(p.getStream()).build();
    }
    
    @POST
    @Path("/post")
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(String content) {
    	ObjectMapper mapper = new ObjectMapper();
    	Map map = null;
		try {
			map = mapper.readValue(content, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		int pageSize = (int) map.getOrDefault("page_size", 15);
		int page = (int) map.getOrDefault("page", 1);
		Map query = (Map) map.get("query");
		String startNode = (String) query.get("start_node");
		String endNode = (String) query.get("end_node");
		
		Service service = new Service(this.objectMapper, this.graphDb);
		Page p = service.getPageData(page, pageSize, startNode, endNode);
		
    	return Response.ok().entity(p.getStream()).build();
    }
}



