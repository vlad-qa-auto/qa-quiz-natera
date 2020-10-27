package com.rest.qa;

import com.rest.qa.models.TriangleModel;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.rest.qa.values.CommonValues.HTTP_CODE_OK;

public class GetTest extends AbstractTest {

    static final TriangleModel triangle1 = new TriangleModel(1, 2, 2);
    static final TriangleModel triangle2 = new TriangleModel(2, 3, 4);
    static final TriangleModel triangle3 = new TriangleModel(3, 4, 5);

    @BeforeClass
    public void beforeClass() {
        new Request().cleanTriangles();
        triangle1.post();
        triangle2.post();
        triangle3.post();
    }

    @Test(description = "Get triangle should return right triangle",
            testName = "Get triangle")
    public void getTriangleTest() {
        TriangleResponse response = triangle1.get();
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(response.statusCode(), HTTP_CODE_OK, "Status code");
        soft.assertEquals(response.asTriangle(), triangle1, "Triangle got by ID");
        soft.assertAll();
    }

    @Test(description = "Get /all should return all triangles",
            testName = "Get /all")
    public void getAllTest() {
        TriangleResponse response = new Request().getAll();
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(response.statusCode(), HTTP_CODE_OK, "Status code");
        soft.assertTrue(response.asTriangles().contains(triangle1), "triangle1 is in the response");
        soft.assertTrue(response.asTriangles().contains(triangle2), "triangle2 is in the response");
        soft.assertTrue(response.asTriangles().contains(triangle3), "triangle3 is in the response");
        soft.assertAll();
    }
}
