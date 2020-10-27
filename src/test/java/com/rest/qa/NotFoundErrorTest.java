package com.rest.qa;

import com.rest.qa.models.TriangleModel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.rest.qa.values.CommonValues.*;
import static com.rest.qa.Request.getDefHeader;
import static io.restassured.RestAssured.given;

public class NotFoundErrorTest extends AbstractTest {

    static final TriangleModel triangle = new TriangleModel(3, 4, 5);

    @BeforeClass
    public void beforeClass() {
        triangle.post();
    }

    @AfterMethod
    public void afterMethod() {
        new Request().cleanTriangles();
    }

    @DataProvider(name = "Path")
    public static Object[] Paths() {
        return new Object[]{
                "wrongPath",
                PATH_ALL + "/wrongPath",
                triangle.getId() + "/wrongPath",
                PATH_ALL + PATH_AREA,
                PATH_ALL + PATH_PERIMETER,
                triangle.getId() + PATH_AREA + PATH_PERIMETER};
    }

    @Test(description = "GET from wrong path should return an error " + HTTP_CODE_NOT_FOUND,
            testName = "GET from wrong path",
            dataProvider = "Path")
    public void getFromWrongPathTest(String path) {
        given().header(getDefHeader()).get(path).then().statusCode(HTTP_CODE_NOT_FOUND);
    }
}
