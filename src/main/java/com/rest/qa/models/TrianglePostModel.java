package com.rest.qa.models;

import org.json.JSONObject;

import static java.lang.Double.parseDouble;

public class TrianglePostModel {
    private String separator;
    private final String input;

    public String getSeparator() {
        return separator;
    }

    public String getInput() {
        return input;
    }

    public TrianglePostModel(String separator, String input) {
        this.separator = separator;
        this.input = input;
    }

    public TrianglePostModel(TriangleModel triangle, String... separator) {
        if (separator.length > 0) this.separator = separator[0];
        String inputSeparator = this.separator!=null ? this.separator:";";
        input = triangle.getFirstSide() + inputSeparator +
                triangle.getSecondSide() + inputSeparator +
                triangle.getThirdSide();
    }

    public TriangleModel toTriangleModel() {
        return new TriangleModel(
                parseDouble(input.split(separator)[0]),
                parseDouble(input.split(separator)[1]),
                parseDouble(input.split(separator)[2]));
    }

    public JSONObject toJson() {
        return new JSONObject(this);
    }
}
