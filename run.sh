#!/bin/bash

here="$(dirname -- "${BASH_SOURCE-$0}")"
cp="$(readlink -f -- "$here"/dist/org.unclesniper.intstor.jar)"
for f in "$here"/lib/*.jar; do
	cp="$cp:$(readlink -f -- "$f")"
done

java -cp "$cp" org.unclesniper.intstor.Launcher "$@"
