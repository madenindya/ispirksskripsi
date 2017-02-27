#!/usr/local/bin/perl

open (IN, "tmp-n-rel.html");

my $line;
my $synset = "";

while ($line = <IN>) {
	chop($line);

	if ($line =~ /xml:lang="ind"/) {
		$line = <IN>;
		$line = <IN>;
		$line =~ s/^\s+//;
		$line =~ s/<\/td>\s*$//;
		$synset = $synset . "," . $line;
	}
}

$synset =~ s/^,//;

print "$synset\n";

close(IN);