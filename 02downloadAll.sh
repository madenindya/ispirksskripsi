#!/bin/bash

url="http://wordnet-rdf.princeton.edu/wn31/1";
echo "" > data-n-kata-rel;

while IFS='' read -r line || [[ -n "$line" ]]; do
    hit="$url$line";
    curl $hit > tmp-n.html;
    perl 03prosesDownloaded.pl;
    ./04prosesRelation.sh;
done < "data.noun.modif.index"
