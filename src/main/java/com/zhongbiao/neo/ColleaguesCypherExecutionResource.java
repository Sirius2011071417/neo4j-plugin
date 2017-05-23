package com.zhongbiao.neo;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.bind.JAXBElement;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.string.UTF8;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import com.domain.Query;

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
    public Response findColleagues(@PathParam("id") String id){

        final Map<String, Object> params = MapUtil.map( "org_id", id);

        StreamingOutput stream = new StreamingOutput()
        {
            @Override
            public void write( OutputStream os ) throws IOException, WebApplicationException
            {
                JsonGenerator jg = objectMapper.getJsonFactory().createJsonGenerator( os, JsonEncoding.UTF8 );
                jg.writeStartObject();
                jg.writeFieldName( "org" );
                jg.writeStartArray();
                
                
                try (Transaction tx = graphDb.beginTx();
                      Result result = graphDb.execute(colleaguesQuery(), params))
                {
                    while (result.hasNext()) {
                        Map<String,Object> row = result.next();
                        jg.writeStartObject();
                        jg.writeFieldName("StartNode");
                        jg.writeStartObject();
                        jg.writeFieldName("org_id");
                        jg.writeString(((Node) row.get("o")).getProperty("org_id").toString());
                        jg.writeFieldName("org_name");
                        jg.writeString(((Node) row.get("o")).getProperty("org_name").toString());
                        jg.writeEndObject();
                  
                        jg.writeFieldName("relationship");
                        jg.writeStartObject();
                        jg.writeFieldName("rel_events");
                        
//                        jg.writeStartArray();
//                        List list = (List) (((Relationship) row.get("r")).getProperty("rel_events"));
//                        for(int i=0;i<list.size();i++) {
//                        	jg.writeString((String) list.get(i));
//                        }
//                        jg.writeEndArray();
                        jg.writeString(((Relationship) row.get("r")).getProperty("rel_events").toString());
                        jg.writeFieldName("events_cnt");
                        jg.writeString(((Relationship) row.get("r")).getProperty("events_cnt").toString());
                        jg.writeEndObject();
                     
                        jg.writeFieldName("EndNode");
                        jg.writeStartObject();
                        jg.writeFieldName("org_id");
                        jg.writeString(((Node) row.get("o1")).getProperty("org_id").toString());
                        jg.writeFieldName("org_name");
                        jg.writeString(((Node) row.get("o1")).getProperty("org_name").toString());
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
        return Response.ok().entity(stream).type( MediaType.APPLICATION_JSON).build();
    }

    private String colleaguesQuery(){
        return "MATCH (o:org {org_id: $org_id })-[r]-(o1:org) RETURN o,r,o1";
    }
}