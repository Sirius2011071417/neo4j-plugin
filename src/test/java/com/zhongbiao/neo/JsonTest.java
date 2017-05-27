package com.zhongbiao.neo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;


public class JsonTest {
	
	@Test
	public void jtest() throws JsonParseException, JsonMappingException, IOException {
		String s = "{\"to\": \"http://localhost:7474/db/data/node/29\",\"type\": \"Subject\", \"list\": [\"a\", \"b\"]}";
		ObjectMapper mapper = new ObjectMapper();
		Map map = mapper.readValue(s, Map.class);
		List l = (List) map.get("list");
		System.out.println(l);
	}
}
