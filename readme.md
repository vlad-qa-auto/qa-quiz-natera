# Triangle Service Tests

Found bugs:
- Typo in error message exception class name `com.natera.test.triangle.exception.NotFounException`
- Missed field `exception` in error "Unauthorized" response body

Possible bugs (maybe features, poor documentation):
- Service can store 11 entities. The documentation mentioned 10 entities, but no direct description of what happens with the next entities.
- More than 3 values in the input do not bring an error.
- Negative values in the input do not bring an error. Negative values in input are saved as positive.
- Some symbols are not allowed to be used as separator (no restrictions in the documentation).
- The documentation uses the word **unprocessible**, but service uses **unprocessable**. May cause some issues.
- Very small/big numbers could be rounded during post.
- Accuracy of type Double could be not enough for some situations.
- Need to add information of allowed number formats in the documentation.

###About Tests
Tests can be run:
- from IntelliJ IDEA: use saved Run/Debug configurations **Smoke Test** or **Regression Test**
- from the command line: `.\gradlew smoke` or `.\gradlew regression`

For setting up tests parameters (service URL, tokens, etc.) and combining tests, the following YAML files were used:
- smoke.yaml (7 tests)
- regression.yaml (210 tests)

For better testing, it is nice to have direct access to storage and an additional user access token.

The paths in this service are very simple to use any URIBuilder with URISyntaxExceptions