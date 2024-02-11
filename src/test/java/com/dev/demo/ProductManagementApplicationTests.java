package com.dev.demo;

import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import net.minidev.json.JSONObject;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
class ProductManagementApplicationTests {


	@Autowired
	private MockMvc mvc;
	
	@Test
	@Order(1)
	public void getnullProducts() throws Exception{

		mvc.perform(get("/products")
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().is(400));
	}
	
	@Test
	@Order(2)
	public void createProducts() throws Exception {

		mvc.perform(post("/products")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(getProductDetails("Pen","Ball point, Ink pen",1010.0,5).toJSONString()))
				.andExpect(MockMvcResultMatchers.status().is(201));

		mvc.perform(post("/products")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(getProductDetails("Pencil","Provided with eraser backside",510.0,10).toJSONString()))
				.andExpect(MockMvcResultMatchers.status().is(201));

		mvc.perform(post("/products")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(getProductDetails("Scale","Transparent",700.0,70).toJSONString()))
				.andExpect(MockMvcResultMatchers.status().is(201));
	}
	
	public JSONObject getProductDetails( String name , String description,double price,  int quantity){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		map.put("description", description);
		map.put("price", price);
		map.put("quantity", quantity);
		return new JSONObject(map);

	}

	@Test
	@Order(3)
	public void getfullProducts() throws Exception {
		mvc.perform(get("/products")
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				//"Pen","Ball point, Ink pen","Parker",1010.0,5
				.andExpect(jsonPath("$.[0].name", containsStringIgnoringCase("Pen")))
				.andExpect(jsonPath("$.[0].description", containsStringIgnoringCase("Ball point, Ink pen")))
				.andExpect(jsonPath("$.[0].price", Matchers.is(1010.0)))
				.andExpect(jsonPath("$.[0].quantity", Matchers.is(5)))
				//"Pencil","Provided with eraser backside","Apsara",510,0,10
				.andExpect(jsonPath("$.[1].name", containsStringIgnoringCase("Pencil")))
				.andExpect(jsonPath("$.[1].description", containsStringIgnoringCase("Provided with eraser backside")))
				.andExpect(jsonPath("$.[1].price", Matchers.is(510.0)))
				.andExpect(jsonPath("$.[1].quantity", Matchers.is(10)))
				//"Scale","Transparent","Classic",700.0,70
				.andExpect(jsonPath("$.[2].name", containsStringIgnoringCase("Scale")))
				.andExpect(jsonPath("$.[2].description", containsStringIgnoringCase("Transparent")))
				.andExpect(jsonPath("$.[2].price", Matchers.is(700.0)))
				.andExpect(jsonPath("$.[2].quantity", Matchers.is(70)));
	}
	
	@Test
	@Order(4)
	public void putProducttwithId() throws Exception{
		mvc.perform(put("/products/3")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(getProductDetails("Scale","Transparent",2000,8)
						.toJSONString())).andExpect(MockMvcResultMatchers.status().isOk());

		mvc.perform(get("/products")
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[2].name", containsStringIgnoringCase("Scale")))
				.andExpect(jsonPath("$.[2].description", containsStringIgnoringCase("Transparent")))
				.andExpect(jsonPath("$.[2].price", Matchers.is(2000.0)))
				.andExpect(jsonPath("$.[2].quantity", Matchers.is(8)));
	

	}

	@Test
	@Order(5)
	public void deleteProductbyId() throws Exception{

		mvc.perform(delete("/products/4")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		mvc.perform(delete("/products/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

	}
	
	@Test
	@Order(6)
	public void updateProductPrice() throws Exception{

		mvc.perform(put("/products/updatePrice/3")
						.contentType(MediaType.APPLICATION_JSON)
						.param("type", "discount")
						.param("value", "10.0"))
						.andExpect(status().isOk())
						.andReturn();
		mvc.perform(get("/products")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.[1].name", containsStringIgnoringCase("Scale")))
		.andExpect(jsonPath("$.[1].description", containsStringIgnoringCase("Transparent")))
		.andExpect(jsonPath("$.[1].price", Matchers.is(1800.0)))
		.andExpect(jsonPath("$.[1].quantity", Matchers.is(8)));
			
		mvc.perform(put("/products/updatePrice/2")
				.contentType(MediaType.APPLICATION_JSON)
				.param("type", "tax")
				.param("value", "10.0"))
				.andExpect(status().isOk())
				.andReturn();
		
		mvc.perform(get("/products")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.[0].name", containsStringIgnoringCase("Pencil")))
			.andExpect(jsonPath("$.[0].description", containsStringIgnoringCase("Provided with eraser backside")))
			.andExpect(jsonPath("$.[0].price", Matchers.is(561.0)))
			.andExpect(jsonPath("$.[0].quantity", Matchers.is(10)));
	}

}
