package com.rest.qa;

import com.rest.qa.models.ErrorResponseModel;
import com.rest.qa.models.TriangleModel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Date;

import static com.rest.qa.values.CommonValues.*;
import static com.rest.qa.Request.EMPTY_HEADER;

public class ErrorResponseTest extends AbstractTest {

    @Test(description = "Error response of 'Not Found' request should contain proper fields",
            testName = "Error response 'Not Found'")
    public void errorNotFoundTest() {
        ErrorResponseModel response = new Request().not200().getTriangle("wrongID").asError();
        SoftAssert soft = new SoftAssert();

        soft.assertEquals((double) (response.getTimestamp() / 1000), (double) (new Date().getTime() / 1000), 60 * 60 * 24, "Timestamp");
        soft.assertEquals(response.getStatus(), HTTP_CODE_NOT_FOUND, "Status");
        soft.assertEquals(response.getError(), "Not Found", "Error");
        soft.assertEquals(response.getException(), "com.natera.test.triangle.exception.NotFoundException", "Exception");
        soft.assertEquals(response.getMessage(), "Not Found", "Message");
        soft.assertEquals(response.getPath(), PATH_TRIANGLE + "wrongID", "Path");

        soft.assertAll();
    }

    @Test(description = "Error response of 'Unauthorized' request should contain proper fields",
            testName = "Error response 'Unauthorized'")
    public void errorUnauthorizedTest() {
        ErrorResponseModel response = new Request().not200().header(EMPTY_HEADER).getAll().asError();
        SoftAssert soft = new SoftAssert();

        soft.assertEquals((double) (response.getTimestamp() / 1000), (double) (new Date().getTime() / 1000), 60 * 60 * 24, "Timestamp");
        soft.assertEquals(response.getStatus(), HTTP_CODE_UNAUTHORIZED, "Status");
        soft.assertEquals(response.getError(), "Unauthorized", "Error");
        soft.assertFalse(response.getException()==null || response.getException().isBlank(), "Exception");
        soft.assertEquals(response.getMessage(), "No message available", "Message");
        soft.assertEquals(response.getPath(), PATH_TRIANGLE + PATH_ALL);

        soft.assertAll();
    }

    @Test(description = "Error response of 'Unprocessible' request should contain proper fields",
            testName = "Error response 'Unprocessible'")
    public void errorUnprocessibleTest() {
        ErrorResponseModel response = new Request().not200().postTriangle("").asError();
        SoftAssert soft = new SoftAssert();

        soft.assertEquals((double) (response.getTimestamp() / 1000), (double) (new Date().getTime() / 1000), 60 * 60 * 24, "Timestamp");
        soft.assertEquals(response.getStatus(), HTTP_CODE_UNPROCESSIBLE, "Status");
        soft.assertEquals(response.getError(), "Unprocessable Entity", "Error");
        soft.assertEquals(response.getException(), "com.natera.test.triangle.exception.UnprocessableDataException", "Exception");
        soft.assertEquals(response.getMessage(), "Cannot process input", "Message");
        soft.assertEquals(response.getPath(), PATH_TRIANGLE);

        soft.assertAll();
    }

    @Test(description = "Error response if 'Limit exceeded' should contain proper fields",
            testName = "Error response 'Limit exceeded'")
    public void errorLimitExceededTest() {
        TriangleResponse response = new Request().not200().postTriangle(new TriangleModel(3, 4, 5));
        while (response.statusCode()!=HTTP_CODE_UNPROCESSIBLE)
            response = new Request().not200().postTriangle(new TriangleModel(3, 4, 5));

        new Request().cleanTriangles();

        ErrorResponseModel errResponse = response.asError();
        SoftAssert soft = new SoftAssert();

        soft.assertEquals((double) (errResponse.getTimestamp() / 1000), (double) (new Date().getTime() / 1000), 60 * 60 * 24, "Timestamp");
        soft.assertEquals(errResponse.getStatus(), HTTP_CODE_UNPROCESSIBLE, "Status");
        soft.assertEquals(errResponse.getError(), "Unprocessable Entity", "Error");
        soft.assertEquals(errResponse.getException(), "com.natera.test.triangle.exception.LimitExceededException", "Exception");
        soft.assertEquals(errResponse.getMessage(), "Limit exceeded", "Message");
        soft.assertEquals(errResponse.getPath(), PATH_TRIANGLE);

        soft.assertAll();
    }
}
