package com.rest.qa;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.Header;
import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.lang.reflect.Method;

import static java.util.Arrays.stream;

public abstract class AbstractTest implements ITest {
    String testName;
    static String serviceUrl;
    static String authName;
    static String authKey;
    static String authKey2;

    static boolean requestsLogging;

    @BeforeSuite
    @Parameters({"serviceUrl", "authName", "authKey", "authKey2", "requestsLogging"})
    public void beforeSuite(ITestContext context, String serviceUrl, String authName, String authKey, String authKey2, boolean requestsLogging) {
        title(context.getName(), '+');

        AbstractTest.serviceUrl = serviceUrl;
        AbstractTest.authName = authName;
        AbstractTest.authKey = authKey;
        AbstractTest.authKey2 = authKey2;
        AbstractTest.requestsLogging = requestsLogging;

        RestAssured.baseURI = serviceUrl;
        Request.setDefHeader(new Header(authName, authKey));
        Request.setDefHeader2(new Header(authName, authKey2));

        if (requestsLogging) RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        RestAssured.get();                                           // Check connection. Ignore tests if no connection.
    }

    @BeforeClass
    public void beforeAbstractClass() {
        new Request().cleanTriangles();
        title(this.getClass().getSimpleName(), '=');
    }

    @Override
    public String getTestName() {                                   // Beautify html report
        return testName!=null ? testName:this.getClass().getSimpleName();
    }

    @BeforeMethod
    public void beforeAbstractMethod(Method method, Object[] testData) {
        Test test = method.getAnnotation(Test.class);
        testName = test.testName();
        title(test.description() + (test.dataProvider().isEmpty() ? "":
                "\n" + test.dataProvider() + ": " + stream(testData).findFirst().orElse("")), '-');
    }

    public void title(String title, char liner) {
        for (int i = 0; i < 100; i++) System.out.print(liner);
        System.out.println(); //yes, I know about loggers :)
        System.out.println(title);
        for (int i = 0; i < 100; i++) System.out.print(liner);
        System.out.println();
    }
}
