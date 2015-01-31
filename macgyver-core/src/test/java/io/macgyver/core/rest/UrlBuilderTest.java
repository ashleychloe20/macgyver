package io.macgyver.core.rest;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class UrlBuilderTest {

	
	@Test(expected=IllegalStateException.class)
	public void testNoBase() {
	
		new UrlBuilder().build();
	
		
	}
	
	@Test
	public void testEncodePathPart() {
		Assertions.assertThat(new UrlBuilder().encodePathPart("abc")).isEqualTo("abc");
		Assertions.assertThat(new UrlBuilder().encodePathPart("/a/b/c/")).isEqualTo("/a/b/c/");
		Assertions.assertThat(new UrlBuilder().encodePathPart("/a/ /+/%")).isEqualTo("/a/+/%2B/%25");
	}
	
	@Test
	public void testConcat() {
	
		Assertions.assertThat(new UrlBuilder().base("http://www.example.com").build()).isEqualTo("http://www.example.com");
		Assertions.assertThat(new UrlBuilder().base("http://www.example.com").path("").build()).isEqualTo("http://www.example.com");
		Assertions.assertThat(new UrlBuilder().base("http://www.example.com").path(null).build()).isEqualTo("http://www.example.com");

		Assertions.assertThat(new UrlBuilder().base("http://www.example.com").path("abc").build()).isEqualTo("http://www.example.com/abc");
		Assertions.assertThat(new UrlBuilder().base("http://www.example.com/").path("abc").build()).isEqualTo("http://www.example.com/abc");

		Assertions.assertThat(new UrlBuilder().base("http://www.example.com").path("/abc").build()).isEqualTo("http://www.example.com/abc");

		Assertions.assertThat(new UrlBuilder().base("http://www.example.com/").path("/abc").build()).isEqualTo("http://www.example.com/abc");

	}
	
	@Test
	public void testQueryString() {
		Assertions.assertThat(new UrlBuilder().base("http://www.example.com/").path("/foo").queryParam("a","b").build()).isEqualTo("http://www.example.com/foo?a=b");	

		Assertions.assertThat(new UrlBuilder().base("http://www.example.com/").path("/foo").queryParam("a","hello world").build()).isEqualTo("http://www.example.com/foo?a=hello+world");	
	}
	@Test
	public void testUrlEncode() {
		Assertions.assertThat(new UrlBuilder().base("http://www.example.com/").path("/abc def").build()).isEqualTo("http://www.example.com/abc+def");

	}
}
