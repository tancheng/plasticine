#!/bin/bash

if [ $# -ne 1 ]; then
  echo "Usage: $0 <top_module_name>"
  exit -1
fi

TOP_MODULE_NAME=$1
SESSION_NAME=$TOP_MODULE_NAME

# Create a new screen session in detached mode
screen -d -m -S $SESSION_NAME

# For each subdirectory in the current directory, create a new screen window and launch job
for f in *; do
  if [ -d $f ]; then
    CMD="cd $f; source scripts/setup.sh; TOP_MODULE=$TOP_MODULE_NAME make synth"

    # Creates a new screen window with title '$f' in existing screen session
    screen -S $SESSION_NAME -X screen -t $f

    # Launch $CMD in newly created screen window
    screen -S $SESSION_NAME -p $f -X stuff "$CMD\n"
  fi
done
