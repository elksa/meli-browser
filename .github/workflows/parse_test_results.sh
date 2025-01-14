#!/bin/bash

# Directory containing the test result files
TEST_RESULTS_DIR="test-results/app/build/test-results/testDebugUnitTest"

# Initialize total counters
total_tests=0
total_skipped=0
total_failures=0
total_errors=0
total_time=0

# Iterate through each XML file in the directory
for file in "$TEST_RESULTS_DIR"/*.xml; do
  # Extract the required attributes using xmllint
  tests=$(xmllint --xpath 'string(/testsuite/@tests)' "$file")
  skipped=$(xmllint --xpath 'string(/testsuite/@skipped)' "$file")
  failures=$(xmllint --xpath 'string(/testsuite/@failures)' "$file")
  errors=$(xmllint --xpath 'string(/testsuite/@errors)' "$file")
  time=$(xmllint --xpath 'string(/testsuite/@time)' "$file")

  # Sum the values to the total counters
  total_tests=$((total_tests + tests))
  total_skipped=$((total_skipped + skipped))
  total_failures=$((total_failures + failures))
  total_errors=$((total_errors + errors))
  total_time=$(echo "$total_time + $time" | bc)
done

# Print the total amounts
echo "Tests execution summary:"
echo "Tests executed: $total_tests"
echo "Tests skipped: $total_skipped"
echo "Tests failed: $total_failures"
echo "Total errors: $total_errors"
echo "Total time: $total_time"
