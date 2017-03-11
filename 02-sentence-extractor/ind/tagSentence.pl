#!/usr/local/bin/perl
# RELATION MAPPING

use strict;
use warnings;

my $EOS = "<EOS>";
my $globpath = "./../../00-data";

sub tag_sentence {
	my ($dir, $file) = (@_);

	print "Start tagging..";
	my $path = "$globpath/wiki/wiki-ind-s/$dir/$filename";
	print "input : $path\n";
	my $opath = ">>$globpath/wiki/wiki-ind-tagged/hh/sentences_hh_$dir.corpus";
	print "output: $opath\n";

	open(IN, $path) or die $!;
	open(OUT, $opath) or die $!;

	# todo: impl

	close(IN);
	close(OUT);

	print "end, bye";
}