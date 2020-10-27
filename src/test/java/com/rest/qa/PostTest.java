package com.rest.qa;

import com.rest.qa.models.TriangleModel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.rest.qa.values.CommonValues.*;
import static org.testng.Assert.assertEquals;

public class PostTest extends AbstractTest {

    static TriangleModel triangle = new TriangleModel(1, 2, 2);

    @AfterMethod
    public void afterMethod() {
        new Request().cleanTriangles();
    }

    @Test(description = "Post triangle returns this triangle with right ID",
            testName = "postTriangleTest")
    public void postTriangleTest() {
        TriangleResponse response = triangle.post();
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(response.statusCode(), HTTP_CODE_OK, "Status code");
        soft.assertEquals(response.asTriangle(), triangle, "Triangle from post");
        soft.assertEquals(triangle.get().asTriangle(), triangle, "Triangle got by ID");
        soft.assertAll();
    }

    @Test(description = "Service can save only " + MAX_TRIANGLES_AMOUNT + "triangles",
            testName = "Maximum amount")
    public void maxAmountTest() {
        TriangleResponse response = new Request().not200().postTriangle(triangle);
        int counter = 0;
        while (response.statusCode()!=422) {
            counter++;
            response = new Request().not200().postTriangle(triangle);
        }
        new Request().not200().postTriangle(triangle).then().statusCode(HTTP_CODE_UNPROCESSIBLE);
        assertEquals(counter, MAX_TRIANGLES_AMOUNT, "Maximum saved amount");
    }

    @Test(description = "Maximum amount does not change if delete one triangle from the full storage",
            testName = "Maximum amount after deleting")
    public void maxAmountAfterDeletingTest() {
        TriangleResponse response = new Request().not200().postTriangle(triangle);
        while (response.statusCode()!=HTTP_CODE_UNPROCESSIBLE) {
            response = new Request().not200().postTriangle(triangle);
            if (response.statusCode()==HTTP_CODE_OK) triangle = response.asTriangle();
        }
        triangle.delete();
        triangle.post();
        new Request().not200().postTriangle(triangle).then().statusCode(HTTP_CODE_UNPROCESSIBLE);
    }
}
