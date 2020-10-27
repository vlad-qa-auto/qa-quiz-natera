package com.rest.qa;

import com.rest.qa.models.TriangleModel;
import com.rest.qa.models.TrianglePostModel;
import io.restassured.http.Header;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;

import static com.rest.qa.values.CommonValues.*;
import static io.restassured.RestAssured.given;
import static io.restassured.http.Method.*;
import static org.codehaus.groovy.vmplugin.v8.PluginDefaultGroovyMethods.stream;

public class Request {

    public static final Header EMPTY_HEADER = new Header("empty", "header");
    private static Header defHeader;
    private static Header defHeader2;

    public Request() {
    }

    public static void setDefHeader(Header header) {
        defHeader = header;
    }

    public static Header getDefHeader() {
        return defHeader;
    }

    public static void setDefHeader2(Header header) {
        defHeader2 = header;
    }

    public static Header getDefHeader2() {
        return defHeader2;
    }

    private String path = "";
    private Header header;
    private JSONObject body;
    private boolean check200 = true;

    public Request not200() {
        check200 = false;
        return this;
    }

    public Request header(Header header) {
        this.header = header;
        return this;
    }

    private TriangleResponse request(Method method) {
        RequestSpecification requestSpec = given().basePath(path).contentType("application/json");

        Header finalHeader = header!=null ? header:defHeader;
        if (finalHeader!=EMPTY_HEADER) requestSpec.header(finalHeader);

        Response response = switch (method) {
            case POST -> requestSpec.body(body.toString()).post().thenReturn();
            case GET -> requestSpec.get().thenReturn();
            case DELETE -> requestSpec.delete().thenReturn();
            default -> throw new RuntimeException("Wrong method " + method);
        };

        if (check200) response.then().statusCode(HTTP_CODE_OK);
        return new TriangleResponse(response);
    }

    public TriangleResponse postRaw(JSONObject body) {
        this.body = body;
        return request(POST);
    }

    public TriangleResponse postTriangle(double firstSide, double secondSide, double thirdSide, String... separator) {
        return postTriangle(new TriangleModel(firstSide, secondSide, thirdSide), separator);
    }

    public TriangleResponse postTriangle(String sides) {
        return postRaw(new JSONObject().put(FIELD_NAME_INPUT, sides));
    }

    public TriangleResponse postTriangle(TriangleModel triangle, String... separator) {
        TriangleResponse response = postRaw(new TrianglePostModel(triangle, separator).toJson());
        if (response.statusCode()==HTTP_CODE_OK) triangle.setId(response.asTriangle().getId());
        return response;
    }

    public TriangleResponse deleteTriangle(String id) {
        this.path = id;
        return request(DELETE);
    }

    public TriangleResponse deleteTriangle(TriangleModel triangle) {
        return deleteTriangle(triangle.getId());
    }

    private TriangleResponse get(String path) {
        this.path = path;
        return request(GET);
    }

    public TriangleResponse getTriangle(String id) {
        return get(id);
    }

    public TriangleResponse getTriangle(TriangleModel triangle) {
        return getTriangle(triangle.getId());
    }

    public TriangleResponse getAll() {
        return get(PATH_ALL);
    }

    public TriangleResponse getArea(String id) {
        return get(id + PATH_AREA);
    }

    public TriangleResponse getArea(TriangleModel triangle) {
        return getArea(triangle.getId());
    }

    public TriangleResponse getPerimeter(String id) {
        return get(id + PATH_PERIMETER);
    }

    public TriangleResponse getPerimeter(TriangleModel triangle) {
        return getPerimeter(triangle.getId());
    }

    public void cleanTriangles() {
        stream(getAll().asTriangles()).forEach(tr -> deleteTriangle(tr.getId()));
    }
}
