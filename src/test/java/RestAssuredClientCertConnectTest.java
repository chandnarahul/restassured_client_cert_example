import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.config.SSLConfig.sslConfig;
import static org.junit.Assert.assertEquals;

public class RestAssuredClientCertConnectTest {

    @Before
    public void setUp() {
        RestAssured.config = RestAssured.config().sslConfig(
                sslConfig().with()
                        .trustStore("truststore", "abcd@1234")
                        .trustStoreType("PKCS12")
                        .and()
                        .keyStore("badssl.com-client.p12", "badssl.com")
                        .keystoreType("PKCS12")
        );
    }

    @Test
    public void should_connect_to_website_using_client_certs() {
        Response response = given().contentType(ContentType.HTML)
                .when()
                .get("https://client.badssl.com/")
                .then()
                .extract().response();
        assertEquals(HttpStatus.SC_OK, response.statusCode());
    }
}
