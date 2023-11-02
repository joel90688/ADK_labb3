#!/bin/bash

for i in {1..10}
do
  for j in {1..10}
  do
    for k in {1..10}
    do

      # Generate output from flowgen
      output=$(./flowgen $i $j $k)

      if [[ ! $output =~ ^[0-9] ]]; then
        echo "Output does not start with a number. Skipping combination: $i $j $k"
        continue
      fi

      java_output=$(java BipRed2 <<< "$output")
      maxflow_output=$(./maxflow <<< "$output")

      difference=$(diff <(echo "$java_output") <(echo "$maxflow_output"))

      # Check if the difference variable is empty
      if [ -n "$difference" ]; then
        echo "The outputs are different for combination: $i $j $k"
        echo $output
        exit 1
      else
        echo "same"
      fi
    done
  done
done

echo "All combinations tested. The outputs are the same."