#!/usr/local/bin/perl
# RELATION MAPPING

use strict;
use warnings;

my $EOS = "<EOS>";
my $globpath = "./../../00-data";

sub tag_sentence {
	my ($dir, $file, $rel, %map) = (@_);

	open(OUT_LOG, ">>tmp_log") or die $!;

	print "Start tagging..\n";
	my $path = "$globpath/wiki/wiki-ind-s/$dir/$file";
	print OUT_LOG "input : $path\n";
	my $opath = ">>$globpath/wiki/wiki-ind-tagged-v2/$rel/sentences_$dir.corpus";  # change this
	print OUT_LOG "output: $opath\n\n";

	my $relk;
	my $relb;
	if ($rel =~ /^hh$/) {
		$relk = "hyponym";
		$relb = "hypernym";
	} else {
		$relk = "meronym";
		$relb = "holonym";
	}

	open(IN, $path) or die $!;
	open(OUT, $opath) or die $!;

	# todo: impl
	my $l;
	my $count = 0;
	while ($l = <IN>) {
		chop($l);
		$l = lc $l;

		$count += 1;

		# iterate kecil, harus merupakan word
		foreach my $kecil (keys %map) {
			if ($l =~ /$kecil/) {

				foreach my $besar (keys $map{$kecil}) {
					if ($l =~ /$besar/) {

						# print OUT_LOG "$count --> found: $kecil - $besar\n";
						my $line = $l;

						$line =~ s/$kecil/<$relk>$kecil<$relk>/g;
						if ($line =~ /[a-z\.\-0-9]<$relk>[a-z\.\-0-9]+<$relk>/) {
							next;
						}
						if ($line =~ /<$relk>[a-z\.\-0-9]+<$relk>[a-z\.\-0-9]/) {
							next;
						}

						$line =~ s/$besar/<$relb>$besar<$relb>/g;
						if ($line =~ /[a-z\.\-0-9]<$relb>[a-z\.\-0-9]+<$relb>/) {
							next;
						}
						if ($line =~ /<$relb>[a-z\.\-0-9]+<$relb>[a-z\.\-0-9]/) {
							next;
						}

						if ($line =~ /></) {
							next;
						}

						print OUT "$line\n";
					}
				}

			}
		}

	}

	close(IN);
	close(OUT);
	close(OUT_LOG);

	print "end, bye\n\n";
}