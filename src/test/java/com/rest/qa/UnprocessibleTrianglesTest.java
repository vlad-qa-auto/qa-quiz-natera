package com.rest.qa;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.rest.qa.values.CommonValues.HTTP_CODE_UNPROCESSIBLE;

public class UnprocessibleTrianglesTest extends AbstractTest {

    @AfterMethod
    public void afterMethod(){
        new Request().cleanTriangles();
    }

    @DataProvider(name = "Nonexistent triangle")
    public static Object[] nonexistentTriangle() {
        return new Object[]{
                "1; 1; 3",
                "1; 3; 1",

                "3; 0; 5",
                "0; 4; 5",
                "0; 0; Infinity",
                "3; 4; 0f",

                "1.1111111111111111; 1.1111111111111111; 2.2222222222222225",

                "Infinity; 1; 1",
                "1; 1; Infinity",

                "3; 4; 10f",
                "2.2d; 2.2d; 4.5f",
                "3; 2e2; 3"};
    }

    @Test(description = "Nonexistent triangle in input should bring an error " + HTTP_CODE_UNPROCESSIBLE,
            testName = "Nonexistent triangle",
            dataProvider = "Nonexistent triangle")
    public void nonexistentTriangleTest(String input) {
        new Request().not200().postTriangle(input).then().statusCode(HTTP_CODE_UNPROCESSIBLE);
    }

   @DataProvider(name = "Negative side")
    public static Object[] negativeSide() {
        return new Object[]{
                "-3; 4; 5",
                "3; -4.5; 5",
                "3; 4; -5",

                "-Infinity; Infinity; Infinity",
                "-Infinity; -Infinity; 2",

                "-3.1d; 2; 2",
                "3d; -2f; 2",
                "3; 2; -2d",
                "3; -5e-1; 3"};
    }

    @Test(description = "Negative side in input should bring an error " + HTTP_CODE_UNPROCESSIBLE,
            testName = "Negative side",
            dataProvider = "Negative side")
    public void negativeSideTest(String input) {
        new Request().not200().postTriangle(input).then().statusCode(HTTP_CODE_UNPROCESSIBLE);
    }
}
