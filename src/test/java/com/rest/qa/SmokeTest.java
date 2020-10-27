package com.rest.qa;

import com.rest.qa.models.TriangleModel;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class SmokeTest extends AbstractTest {

    final TriangleModel triangle = new TriangleModel(3, 4, 5);

    @BeforeClass
    public void beforeClass() {
        triangle.post().asTriangle();
    }

    @Test(description = "Get perimeter should return right perimeter value",
            testName = "Calculation of perimeter")
    public void perimeterTest() {
        assertEquals(triangle.getPerimeter(), triangle.calcPerimeter());
    }

    @Test(description = "Get area should return right area value",
            testName = "Calculation of area")
    public void areaTest() {
        assertEquals(triangle.getArea(), triangle.calcArea());
    }
}
