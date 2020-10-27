package com.rest.qa;

import com.rest.qa.models.TrianglePostModel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.rest.qa.values.CommonValues.*;
import static com.rest.qa.Request.getDefHeader;
import static io.restassured.RestAssured.given;

public class UnprocessibleBodyTest extends AbstractTest {

    @AfterMethod
    public void afterMethod() {
        new Request().cleanTriangles();
    }

    @DataProvider(name = "Input")
    public static Object[] inputs() {
        return new Object[]{
                "",
                "1",
                "1;2",
                "1;2;3;4",
                "1,2,3",
                "123"};
    }

    @Test(description = "Mistakes in input should bring an error " + HTTP_CODE_UNPROCESSIBLE,
            testName = "Mistakes in input",
            dataProvider = "Input")
    public void mistakesInInputTest(String input) {
        new Request().not200().postTriangle(input).then().statusCode(HTTP_CODE_UNPROCESSIBLE);
    }

    @DataProvider(name = "Body")
    public static Object[] bodies() {
        return new Object[]{
                "{\""+ FIELD_NAME_INPUT +"\":true}",
                "{\""+ FIELD_NAME_INPUT +"\":345}",
                "{\"separatist\":\"-\",\""+ FIELD_NAME_INPUT +"\":\"3-4-5\"}",
                "\""+ FIELD_NAME_INPUT +"\":\"3;4;5\"",
                "{\""+ FIELD_NAME_INPUT +"\":\"3;4;5\",\""+ FIELD_NAME_INPUT +"\":\"6;7;8\"}",
                "{\""+ FIELD_NAME_SEPARATOR +"\":\"\",\""+ FIELD_NAME_INPUT +"\":\"6;7;8\"}",
                "{\""+FIELD_NAME_SEPARATOR+"\":\"1\",\""+ FIELD_NAME_INPUT +"\":\"111111\"}"};
    }

    @Test(description = "Wrong body should bring an error " + HTTP_CODE_UNPROCESSIBLE,
            testName = "Wrong bodies",
            dataProvider = "Body")
    public void wrongBody(String body) {
        given().contentType("application/json").header(getDefHeader()).body(body).post().then().statusCode(HTTP_CODE_UNPROCESSIBLE);
    }

    @DataProvider(name = "Separator")
    public static Object[] separators() {
        return new Object[]{"", ","};
    }

    @Test(description = "Mistakes in separator should bring an error " + HTTP_CODE_UNPROCESSIBLE,
            testName = "Mistakes in separator",
            dataProvider = "Separator")
    public void mistakesInSeparatorTest(String separator) {
        new Request().not200().postRaw(new TrianglePostModel("3;4;5", separator).toJson()).then().statusCode(HTTP_CODE_UNPROCESSIBLE);
    }
}
