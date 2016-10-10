#!/bin/bash

if [ $# -ne 1 ]; then
  echo "Usage: $0 <path_to_dir_to_be_watched>"
  exit -1
fi

DIR_PATH=$1

## Watch a folder recursively for new files created/deleted
inotifywait -r -m $DIR_PATH -e create -e moved_to -e delete |
    while read path action file; do
        echo "${path}${file}, $action"
        # do something with the file
    done
