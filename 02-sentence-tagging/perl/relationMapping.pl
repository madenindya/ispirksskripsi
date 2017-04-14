#!/usr/local/bin/perl
# RELATION MAPPING

use strict;
use warnings;

my $EOS = "<EOS>";

sub create_mapping {
	my ($seed, $dump_file, $multi, $postag) = (@_);

	print "create mapping:\n";
	print "from: $seed\n";
	print "to  : $dump_file\n";
	print "multi? $multi\n";
	print "postag? $postag\n"; 

	open(IN, $seed) or die $!; 												
	open(OUT, $dump_file) or die $!;	

	my %kecil_besar;

	my $line;
	my $count = 0;
	while($line = <IN>) {
		chop($line);

		my @tokens = split(/\s\#\#\s/, $line);

		my $besar = $tokens[1];
		my $kecil = $tokens[0];

		if ($kecil eq $besar) {
			next;
		}

		if (!$multi && $line =~ /\_/) {
			next;
		} else {
			$line =~ s/\_/ /g;
		}

		$kecil_besar{$kecil}{$besar}++;
		$count += 1;
	}
	print "Count RELATION => $count\n";

	my $total_kecil = scalar (keys %kecil_besar);
	# print OUT_LOG "Total unique: $total_kecil\n";

	use Data::Dumper;
	print OUT Dumper \%kecil_besar;
	close(IN);
	close(OUT);	

	print "end, bye\n\n";

	return \%kecil_besar;
}
