package com.rest.qa;

import com.rest.qa.models.TriangleModel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;

import static com.rest.qa.values.CommonValues.HTTP_CODE_OK;
import static org.testng.Assert.assertEquals;

public class PerimeterTest extends AbstractTest {

    @AfterMethod
    public void afterMethod() {
        new Request().cleanTriangles();
    }

    @DataProvider(name = "Triangle")
    public static Object[] triangles() {
        return new Object[]{
                "3; 4; 5",
                "0; 4; 4",
                "3; 0; 3",
                "5; 5; 0",
                "1; 1; 2",
                "3000000; 4000000; 5000000",
                "1.11; 1.11; 2.22",
                "3.1; 4; 5",
                "3.0; 4.1; 5",
                "0.0000000001; 0.0000000001; 0.0000000001",
                "0.00000001; 10000000; 10000000"};
    }

    @Test(description = "Get perimeter should return right perimeter value",
            testName = "Calculation of perimeter",
            dataProvider = "Triangle")
    public void calculationTest(String sides) {
        assertEquals(new Request().postTriangle(sides).asTriangle().getPerimeter(),
                new TriangleModel(Arrays.stream(sides.split("; ")).mapToDouble(Double::valueOf).toArray()).calcPerimeter());
    }

    @Test(description = "Get perimeter should return code " + HTTP_CODE_OK,
            testName = "Get perimeter " + HTTP_CODE_OK)
    public void responseOKTest() {
        TriangleModel triangle = new TriangleModel(1, 2, 2);
        triangle.post();
        new Request().getPerimeter(triangle.getId()).then().statusCode(HTTP_CODE_OK);
    }
}
