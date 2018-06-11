package com.crossover.techtrial.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.crossover.techtrial.model.Article;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ArticleControllerTest {

	@Autowired
	private TestRestTemplate template;

	@Before
	public void setup() throws Exception {

	}

	@Test
	public void testArticleShouldBeCreated() throws Exception {
		HttpEntity<Object> article = getHttpEntity("{\"email\": \"user2@gmail.com\", \"title\": \"hello2\" }");
		ResponseEntity<Article> resultAsset = template.postForEntity("/articles", article, Article.class);
		Assert.assertNotNull(resultAsset.getBody().getId());
	}

	private HttpEntity<Object> getHttpEntity(Object body) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new HttpEntity<Object>(body, headers);
	}
	
	@Test
	public void testArticleShouldBeFetched() throws Exception {
		ResponseEntity<Article> resultAsset = template.getForEntity("/articles/2", Article.class);
		Assert.assertNotNull(resultAsset.getBody().getId());
	}
	
	@Test
	public void testArticleShouldBeUpdated() throws Exception {
		String id = "3";
		ResponseEntity<Article> resultAsset = template.getForEntity("/articles/"+id, Article.class);
		boolean op = putIndex(id, resultAsset.getBody());
		Assert.assertTrue(op);
	}
	
	public boolean putIndex(String id, Article article) throws Exception {
		article.setContent("I am updated");
		final String endpoint = "/articles/" + id;
		HttpHeaders requestHeaders = new HttpHeaders();
		List<MediaType> mediaTypeList = new ArrayList<MediaType>();
		mediaTypeList.add(MediaType.APPLICATION_JSON);
		requestHeaders.setAccept(mediaTypeList);
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<?> requestEntity = new HttpEntity<Object>(article, requestHeaders);

		// Create the HTTP PUT request,
		ResponseEntity<Article> response = template.exchange(endpoint,
				HttpMethod.PUT, requestEntity, Article.class, id);
		if (response == null) {
			return false;
		}
		return HttpStatus.OK.equals(response.getStatusCode());
	}

	@Test
	public void testArticleShouldBeDeleted() throws Exception {
		ResponseEntity<Article> resultAsset = template.getForEntity("/articles/3", Article.class);
		Assert.assertNotNull(resultAsset.getBody().getId());
	}
	
}
