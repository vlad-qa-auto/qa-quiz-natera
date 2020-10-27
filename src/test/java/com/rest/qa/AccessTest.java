package com.rest.qa;

import com.rest.qa.models.TriangleModel;
import io.restassured.http.Header;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.rest.qa.values.CommonValues.HTTP_CODE_NOT_FOUND;
import static com.rest.qa.values.CommonValues.HTTP_CODE_UNAUTHORIZED;
import static com.rest.qa.Request.EMPTY_HEADER;
import static com.rest.qa.Request.getDefHeader2;

public class AccessTest extends AbstractTest {

    final TriangleModel triangle1 = new TriangleModel(3, 4, 5);
    final TriangleModel triangle2 = new TriangleModel(2, 4, 5);
    final TriangleModel triangle3 = new TriangleModel(1, 4, 3);
    final Request requestWrong = new Request().not200();
    final Request requestAuth2 = new Request().not200();

    @DataProvider(name = "headers")
    public static Object[] headers() {
        return new Object[]{
                EMPTY_HEADER,
                new Header("Admin", "123456"),
                new Header(authName, "123456"),
                new Header("Admin", authKey)};
    }

    @BeforeClass
    public void beforeClass() {
        triangle1.post();
        requestAuth2.header(getDefHeader2()).cleanTriangles();
        requestAuth2.postTriangle(triangle3);
    }

    @Test(description = "POST wrong credentials should return error code " + HTTP_CODE_UNAUTHORIZED + " and No new records",
            testName = "POST wrong credentials",
            dataProvider = "headers")
    public void postWrongAuthTest(Header header) {
        SoftAssert soft = new SoftAssert();

        soft.assertEquals(requestWrong.header(header).postTriangle(triangle2).statusCode(), HTTP_CODE_UNAUTHORIZED, "Status code");
        soft.assertEquals(new Request().getAll().asTriangles().size(), 1, "Amount of records on the server");

        soft.assertAll();
    }

    @Test(description = "DELETE with wrong credentials should return error code " + HTTP_CODE_UNAUTHORIZED + " and No changes on server",
            testName = "DELETE with wrong credentials",
            dataProvider = "headers")
    public void deleteWrongAuthTest(Header header) {
        SoftAssert soft = new SoftAssert();

        soft.assertEquals(requestWrong.header(header).deleteTriangle(triangle1).statusCode(), HTTP_CODE_UNAUTHORIZED, "Status code");
        soft.assertEquals(new Request().getAll().asTriangles().size(), 1, "Amount of records on the server");

        soft.assertAll();
    }

    @Test(description = "GET /all with wrong credentials should return error code " + HTTP_CODE_UNAUTHORIZED,
            testName = "GET /all with wrong credentials",
            dataProvider = "headers")
    public void getAllWrongAuthTest(Header header) {
        requestWrong.header(header).getAll().then().statusCode(HTTP_CODE_UNAUTHORIZED);
    }

    @Test(description = "GET a triangle with wrong credentials should return error code " + HTTP_CODE_UNAUTHORIZED,
            testName = "GET a triangle with wrong credentials",
            dataProvider = "headers")
    public void getTriangleWrongAuthTest(Header header) {
        requestWrong.header(header).getTriangle(triangle1).then().statusCode(HTTP_CODE_UNAUTHORIZED);
    }

    @Test(description = "GET a triangle with credentials of another user should return error code " + HTTP_CODE_NOT_FOUND,
            testName = "GET a triangle of another user")
    public void getTriangleAnotherUserTest() {
        requestAuth2.getTriangle(triangle1).then().statusCode(HTTP_CODE_NOT_FOUND);
    }

    @Test(description = "GET perimeter with wrong credentials should return error code " + HTTP_CODE_UNAUTHORIZED,
            testName = "GET perimeter with wrong credentials",
            dataProvider = "headers")
    public void getPerimeterWrongAuthTest(Header header) {
        requestWrong.header(header).getPerimeter(triangle1).then().statusCode(HTTP_CODE_UNAUTHORIZED);
    }

    @Test(description = "GET area with wrong credentials should return error code " + HTTP_CODE_UNAUTHORIZED,
            testName = "GET area with wrong credentials",
            dataProvider = "headers")
    public void getAreaWrongAuthTest(Header header) {
        requestWrong.header(header).getArea(triangle1).then().statusCode(HTTP_CODE_UNAUTHORIZED);
    }
}
