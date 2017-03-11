#!/usr/local/bin/perl
# RELATION MAPPING

use strict;
use warnings;

my $EOS = "<EOS>";

sub create_mapping {
	my ($seed, $dump_file, $multi) = (@_);

	print "create mapping:\n";
	print "from: $seed\n";
	print "to  : $dump_file\n";
	print "multi? $multi\n";

	open(IN, $seed); 												
	open(OUT, $dump_file);	

	my %kecil_besar;

	my $line;
	while($line = <IN>) {
		chop($line);

		$line =~ s/Synset\('[a-z\.0-9\_\-\']*'\)##//g;
		

		my @tokens = split(/\//, $line);

		my $besar = $tokens[0];
		my $kecil = $tokens[1];

		if ($kecil eq $besar) {
			next;
		}

		if (!$multi && $line =~ /\_/) {
			next;
		} else {
			$line =~ s/\_/ /g;
		}

		$kecil_besar{$kecil}{$besar}++;
	}

	my $total_kecil = scalar (keys %kecil_besar);
	print OUT_LOG "Total unique: $total_kecil\n";

	use Data::Dumper;
	print OUT Dumper \%kecil_besar;
	close(IN);
	close(OUT);	

	print "end, bye";

	return \%kecil_besar;
}