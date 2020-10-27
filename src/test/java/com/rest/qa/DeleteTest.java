package com.rest.qa;

import com.rest.qa.models.TriangleModel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static com.rest.qa.values.CommonValues.HTTP_CODE_NOT_FOUND;
import static org.testng.Assert.assertEquals;

public class DeleteTest extends AbstractTest {

    static final TriangleModel triangle1 = new TriangleModel(1, 2, 2);
    static final TriangleModel triangle2 = new TriangleModel(2, 3, 4);

    @AfterMethod
    public void afterMethod() {
        new Request().cleanTriangles();
    }

    @Test(description = "triangle2 should remain after deleting of triangle1",
            testName = "One triangle")
    public void oneTriangleTest() {
        triangle1.post();
        triangle2.post();
        triangle1.delete();
        List<TriangleModel> triangles = new Request().getAll().asTriangles();
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(triangles.size(), 1, "Amount of triangles after deleting");
        soft.assertEquals(triangles.get(0), triangle2, "Remaining triangle");
        soft.assertEquals(new Request().not200().getTriangle(triangle1).statusCode(), HTTP_CODE_NOT_FOUND, "Status code on get deleted ID");
        soft.assertAll();
    }

    @Test(description = "We can delete all triangles and post any new triangle after that",
            testName = "All triangles")
    public void allTrianglesTest() {
        triangle1.post();
        triangle1.delete();
        triangle2.post();
        assertEquals(new Request().getAll().asTriangles().size(), 1, "Amount of triangles after deleting");
    }

    @Test(description = "We can try delete wrong triangle ID",
            testName = "Wrong triangle ID")
    public void wrongIDTest() {
        triangle1.post();
        TriangleModel triangle3 = new TriangleModel(1, 1, 1);
        triangle3.setId("WrongID");
        triangle3.delete();
        assertEquals(new Request().getAll().asTriangles().size(), 1, "Amount of triangles after deleting");
    }

    @Test(description = "We can try delete already deleted triangle ID",
            testName = "Already deleted triangle ID")
    public void deletedIDTest() {
        triangle1.post();
        triangle2.post();
        triangle1.delete();
        triangle1.delete();
        List<TriangleModel> triangles = new Request().getAll().asTriangles();
        assertEquals(triangles.size(), 1, "Amount of triangles after deleting");
        assertEquals(triangles.get(0), triangle2, "Remaining triangle");
    }
}
