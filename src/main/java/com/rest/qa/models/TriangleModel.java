package com.rest.qa.models;

import com.rest.qa.Request;
import com.rest.qa.TriangleResponse;

import java.util.Objects;

public class TriangleModel {
    private String id;
    private double firstSide;
    private double secondSide;
    private double thirdSide;

    public TriangleModel() {
    }

    public TriangleModel(double... sides) {
        firstSide = sides[0];
        secondSide = sides[1];
        thirdSide = sides[2];
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getFirstSide() {
        return firstSide;
    }

    public double getSecondSide() {
        return secondSide;
    }

    public double getThirdSide() {
        return thirdSide;
    }

    @Override
    public boolean equals(Object o) {
        if (this==o) return true;
        if (o==null || getClass()!=o.getClass()) return false;
        TriangleModel triangle = (TriangleModel) o;
        return Objects.equals(id, triangle.id) &&
                Objects.equals(firstSide, triangle.firstSide) &&
                Objects.equals(secondSide, triangle.secondSide) &&
                Objects.equals(thirdSide, triangle.thirdSide);
    }

    public TriangleResponse post() {
        TriangleResponse response = new Request().postTriangle(this);
        id = response.asTriangle().getId();
        return response;
    }

    public void delete() {
        new Request().deleteTriangle(id);
    }

    public double getPerimeter() {
        return new Request().getPerimeter(id).asPerimeter();
    }

    public double getArea() {
        return new Request().getArea(id).asArea();
    }

    public TriangleResponse get() {
        return new Request().getTriangle(id);
    }

    public double calcPerimeter() {
        return firstSide + secondSide + thirdSide;
    }

    public double calcArea() {
        double halfPrmtr = calcPerimeter() / 2.0;
        return Math.sqrt(halfPrmtr * (halfPrmtr - firstSide) * (halfPrmtr - secondSide) * (halfPrmtr - thirdSide));
    }

    @Override
    public String toString() {
        return String.format("(%s; %s; %s)", firstSide, secondSide, thirdSide);
    }
}
