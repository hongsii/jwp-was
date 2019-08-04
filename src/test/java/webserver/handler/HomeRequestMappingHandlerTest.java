package webserver.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import webserver.http.HttpRequest;
import webserver.http.HttpRequestTest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatusCode;
import webserver.resolver.HtmlViewResolver;

import static org.assertj.core.api.Assertions.assertThat;

public class HomeRequestMappingHandlerTest {

    private RequestMappingHandler handler;

    @BeforeEach
    void setUp() {
        handler = new HomeRequestMappingHandler(new HtmlViewResolver());
    }

    @Test
    @DisplayName("Redirect to index page")
    void doHandle() throws Exception {
        HttpRequest httpRequest = HttpRequest.parse(HttpRequestTest.createInputStream(
                "GET / HTTP/1.1",
                "Host: www.nowhere123.com",
                "Accept: image/gif, image/jpeg, */*",
                "Accept-Language: en-us",
                "Accept-Encoding: gzip, deflate",
                "User-Agent: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)"
        ));

        HttpResponse httpResponse = handler.doHandle(httpRequest);

        assertThat(httpResponse).isNotNull();
        assertThat(httpResponse.getStatusCode()).isEqualTo(HttpStatusCode.OK);
        assertThat(httpResponse.getBody()).isNotEmpty();
    }

    @DisplayName("This handler can handle root path")
    @ParameterizedTest
    @CsvSource(value = {
            "/            | true",
            "/script.html | false",
            "/script.js   | false",
    }, delimiter = '|')
    void canHandle(String path, boolean expected) throws Exception {
        HttpRequest httpRequest = HttpRequest.parse(HttpRequestTest.createInputStream(
                "GET " + path + " HTTP/1.1"
        ));

        assertThat(handler.canHandle(httpRequest)).isEqualTo(expected);
    }
}