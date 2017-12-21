package ca.ulaval.glo4002.commons;

import ca.ulaval.glo4002.billing.resource.RestTestSuite;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class RestTestBase
{
    protected RequestSpecification givenBaseRequest()
    {
        return given().accept(ContentType.JSON)
                .port(RestTestSuite.TEST_SERVER_PORT)
                .contentType(ContentType.JSON);
    }

    protected Map<String, Object> buildPathParams(String key, Object value)
    {
        Map<String, Object> params = new HashMap<>();
        params.put(key, value);
        return params;
    }
}
