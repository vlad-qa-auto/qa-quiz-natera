package com.rest.qa;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.apache.commons.lang3.ArrayUtils.add;
import static org.apache.commons.lang3.ArrayUtils.addAll;

public class PostTriangleBodyTest extends AbstractTest {

    @DataProvider(name = "Separators")  // no restricted separators in service documentation (but JSON)
    public static Object[] separators() {
        Object[] result = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz".split("");
        result = addAll(result, " \n\t\b\r\f +-*/=,?!@#$%^&(){}[]|~`'\"№<>0123456789".split(""));
        return add(result, "SR@№GE-SEP@R@T0R");
    }

    @Test(description = "Entity can be posted with any kind of separator",
            testName = "Separators test",
            dataProvider = "Separators")
    public void separatorTest(String separator) {
        (separator.equals("9") ? new Request().postTriangle(3, 4, 5, "9"):
                new Request().postTriangle(9.9, 9.9, 9.9, separator)).asTriangle().delete();
    }

    @DataProvider(name = "Triangle")
    public static Object[] triangles() {
        return new Object[]{
                "3; 4; 5",
                "3.3; 4.4; 5.5",
                " 3.3; 4; 5.5 ",
                "3;4.4;5",
                "3;4; 5.5"};
    }

    @Test(description = "Input can contain integer and double numbers", // Special types of numbers (3f, Infinity, etc.) are not documented.
            testName = "Input",                                         // Double is not documented too, but very useful case.
            dataProvider = "Triangle")
    public void inputTest(String input) {
        new Request().postTriangle(input);
    }
}
