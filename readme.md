# Triangle Service Tests

Found bugs:
- Typo in error message exception class name `com.natera.test.triangle.exception.NotFounException`
- Missed field `exception` in error "Unauthorized" response body.

Maybe bugs (maybe features, poor documentation):
- Service can store 11 entities. Documentation mentioned 10 entities,
but no direct description what happens with the next entities.
- More than 3 values in input does not bring an error.
- Negative values in input does not bring an error. Negative values in input saves like positive.
- Some symbols do not allow as separator (no restrictions in a documentation).
- Documentation uses word **unprocessible**, but service uses **unprocessable**.
May gives some issues.
- Very small/big numbers could be rounded during post.
- Accuracy of type `Double` could be not enough for some situations.
- Need add information of allowed number formats in the documentation.

### About Tests
Tests can be run:
- from IntelliJ IDEA: use saved Run/Debug configurations  
**Smoke Test** or **Regression Test**
- from command line: `.\gradlew smoke` or `.\gradlew regression`

For setting up tests parameters (service URL, tokens, etc.) and combining tests used YAML files:
- smoke.yaml (7 tests)
- regression.yaml (210 tests)

For better testing nice to have direct access to storage and additional user access token.

The paths used in this service are very simple to use any URIBuilder with URISyntaxExceptions.

