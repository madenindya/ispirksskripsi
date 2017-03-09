#!/usr/local/bin/perl

use strict;
use warnings;

open(IN, "tmp_seed"); 						# change isi this
open(OUT, ">en_sentence_hh.corpus");		# change this
open(OUT_LOG, ">tmp_log");

my %hypo_hypers;

my $line;
while($line = <IN>) {
	chop($line);

	my @tokens = split(/\//, $line);
	if (scalar @tokens < 2) {
		next;
	}

	my @hypers = split(/\,/, $tokens[0]);
	my @hypos = split(/\,/, $tokens[1]);

	foreach my $hypo (@hypos) {
		if (length $hypo > 0) {
			$hypo =~ s/\_/ /g;
			foreach my $hype (@hypers) {
				$hype =~ s/\_/ /g;
				if (length $hype > 0) {

					my @new_hypes;
					push @{$hypo_hypers{$hypo}}, $hype

				}
			}
		}
	}
}


print OUT_LOG "\n\n\n";
my $total_hypo = scalar (keys %hypo_hypers);
print OUT_LOG "Total unique mero: $total_hypo\n";

use Data::Dumper;
print OUT Dumper \%hypo_hypers;
close(IN);
close(OUT);

print "IDENTIC...\n";

######################################################################################
# Mapping with corpus identic
open (IN, "tmp_identic");					# change this
open (OUT, ">>parallel_corpus_hh.corpus");	# change this

my $count = 0;
while(my $l = <IN>) {
	$count += 1;
	print "$count \n";

	chop($l);
    $l = lc $l;

	# iterate hypo
	# harus merupakan word
	foreach my $hypo (keys %hypo_hypers){

		if ($l =~ /$hypo/) {

			foreach my $hype (@{$hypo_hypers{$hypo}}) {
				
				if ($l =~ /$hype/) {

					my $line = $l;

					$line =~ s/$hypo/<hyponym>$hypo<hyponym>/g;
					if ($line =~ /[a-z\.\-0-9]<hyponym>[a-z\.\-0-9]+<hyponym>/) {
						next;
					}
					if ($line =~ /<hyponym>[a-z\.\-0-9]+<hyponym>[a-z\.\-0-9]/) {
						next;
					}


					$line =~ s/$hype/<hypernym>$hype<hypernym>/g;
					if ($line =~ /[a-z\.\-0-9]<hypernym>[a-z\.\-0-9]+<hypernym>/) {
						next;
					}
					if ($line =~ /<hypernym>[a-z\.\-0-9]+<hypernym>[a-z\.\-0-9]/) {
						next;
					}

					if ($line =~ /></) {
						next;
					}

					print OUT_LOG "\nfound mero:$hypo! & holo:$hype!\n";

					print OUT "$line\n";
				}
			}

		}
	}

}


close (IN);
close(OUT);
close(OUT_LOG);