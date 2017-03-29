#!/bin/bash

url="http://wordnet-rdf.princeton.edu/wn31/";
relation="";
kata="";

is_first=true;
COUNT=0;

while IFS='' read -r line || [[ -n "$line" ]]; do
    
	# get kata
    if [ "$is_first" =  true ] 
    then
    	is_first=false;
    	chrlen=${#line};
    	
    	if [ $chrlen -gt 0 ]
    	then
    		kata=$line;
	    	echo "lanjut - $kata";
	    else 
	    	echo "buuuuu";
	    	break;
	    fi
	
	else 

		if [ $(( $COUNT % 2 )) == 0 ] 
		then
			relation=$line;
		else
			hit="$url$line";
		    curl $hit > tmp-n-rel.html;
		    relatkata=$(perl 05getRelation.pl);

		    chrlen=${#relatkata};

		    if [ $chrlen -gt 0 ]
	    	then
	    		fin="$relation($kata/$relatkata)";
	    		echo $fin >> data-n-kata-rel;
		    else 
		    	echo "booooo";
		    fi

		fi

		(( COUNT++ ));

	fi

done < "tmp-data-check-ind"