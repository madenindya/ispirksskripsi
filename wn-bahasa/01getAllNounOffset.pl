#!/usr/local/bin/perl

open (IN, "data.noun.modif");
open (OUT, ">data.noun.modif.index");

my $line;
while ($line = <IN>) {
	chop($line);

	my @tokens = split(/\s/, $line);

	my $index = "$tokens[0]-$tokens[2]";

	print OUT "$index\n";
}

close(IN);
close(OUT);