#!/bin/bash

# Directory containing the test result files
TEST_RESULTS_DIR="test-results/app/build/test-results/testDebugUnitTest"

# Iterate through each XML file in the directory
for file in "$TEST_RESULTS_DIR"/*.xml; do
  # Extract the required attributes using xmllint
  testsuite_name=$(xmllint --xpath 'string(/testsuite/@name)' "$file")
  tests=$(xmllint --xpath 'string(/testsuite/@tests)' "$file")
  skipped=$(xmllint --xpath 'string(/testsuite/@skipped)' "$file")
  failures=$(xmllint --xpath 'string(/testsuite/@failures)' "$file")
  errors=$(xmllint --xpath 'string(/testsuite/@errors)' "$file")
  time=$(xmllint --xpath 'string(/testsuite/@time)' "$file")

  # Print the extracted information
  echo "File: $file"
  echo "testsuite name: $testsuite_name"
  echo "tests: $tests"
  echo "skipped: $skipped"
  echo "failures: $failures"
  echo "errors: $errors"
  echo "time: $time"
  echo "-----------------------------"
done