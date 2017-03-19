#!/usr/local/bin/perl

use strict;
use warnings;

print "Sentence to be found: (Accept Regex)\n";
my $sentence = <STDIN>;

print "From file:\n";
my $filename = <STDIN>;
# chop($filename);
open(IN, $filename) or die $!;

print "Save to:\n";
my $fileout = <STDIN>;
# chop($fileout);
open(OUT, ">$fileout") or die $!;

chop($sentence);
$sentence =~ /^\s+/;
$sentence =~ /\s+$/;


print "Start\n";
my $line;
while ($line = <IN>) {
	
	if ($line =~ /$sentence/) {
		print OUT "$line\n";
	}
}
print "End, bye\n";

close(IN);
close(OUT);