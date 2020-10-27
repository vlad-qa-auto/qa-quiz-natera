package com.rest.qa;

import com.rest.qa.values.ResponseTypes;
import com.rest.qa.models.AreaResponseModel;
import com.rest.qa.models.ErrorResponseModel;
import com.rest.qa.models.PerimeterResponseModel;
import com.rest.qa.models.TriangleModel;
import io.restassured.internal.RestAssuredResponseImpl;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.response.ValidatableResponse;

import java.util.List;

import static com.rest.qa.values.ResponseTypes.*;
import static org.codehaus.groovy.runtime.InvokerHelper.asList;

public class TriangleResponse extends RestAssuredResponseImpl {

    private final Response decorated;

    public TriangleResponse(Response decorated) {
        this.decorated = decorated;
    }

    @Override
    public ResponseBody body() {
        return decorated.body();
    }

    @Override
    public ValidatableResponse then(){
        return decorated.then();
    }

    @Override
    public int statusCode() {
        return decorated.statusCode();
    }

    private ResponseTypes type() {
        if (this.statusCode() >= 400) return ERROR;
        else if (this.body().asString().startsWith("[")) return TRIANGLES;
        else return TRIANGLE;
    }

    public TriangleModel asTriangle() {
        return this.body().as(TriangleModel.class);
    }

    public ErrorResponseModel asError() {
        return this.body().as(ErrorResponseModel.class);
    }

    public List<TriangleModel> asTriangles() {
        return asList(this.body().as(TriangleModel[].class));
    }

    public double asArea() {
        return this.body().as(AreaResponseModel.class).getResult();
    }

    public double asPerimeter() {
        return this.body().as(PerimeterResponseModel.class).getResult();
    }

    @Override
    public String toString(){
        return String.valueOf(this.statusCode());
    }
}
