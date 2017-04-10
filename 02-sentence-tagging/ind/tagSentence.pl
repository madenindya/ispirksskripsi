#!/usr/local/bin/perl
# Tag Sentence

use strict;
use warnings;

my $EOS = "<EOS>";
my $globpath = "./../../00-data";

sub tag_sentence {
	my ($dir, $file, $rel, %map, $postag) = (@_);

	# all necessary path
	my $path = "$globpath/wiki/wiki-ind-s/$dir/$file";
	my $opath = ">>$globpath/wiki/wiki-ind-tagged-v2/$rel/sentences_$dir.corpus";  # change this

	# 1: log for checkpoint
	open(OUT_LOG, ">>tmp_log") or die $!;
	print "Start tagging..\n";
	print OUT_LOG "input : $path\n";
	print OUT_LOG "output: $opath\n\n";

	# 2: check which relation
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

	my $l;
	my $count = 0;
	my $regex1 = '(.+)_';

	# 3: baca setiap baris dalam korpus
	while ($l = <IN>) {
		$count += 1;

		chop($l);
		if ($postag) {
			#
		} else {
			# 4: jadiin lowercase
			$l = lc $l; 
		}

		foreach my $kecil (keys %map) {
			# jika mengandung seed <kecil>
			if ($l =~ /$kecil/) {
				# ... dan seed <besar>
				foreach my $besar (keys $map{$kecil}) {
					if ($l =~ /$besar/) {

						my $line = "";
						my @tokens = split(/\s+/, $l);						
						
						# 5: iterate tokens 
						foreach my $token (@tokens) {
							if (length $token < 1) {
								next;
							}
							my $back = "";

							if ($postag) {
								# 5.5: ambil word tanpa tag
								if ($token =~ /^(.+)_/) {
									# 6: tag token
									if ($1 =~ /^$kecil$/) {
										$token = "<$relk>$token<$relk>";
									} elsif ($token =~ /^$besar$/) {
										$token = "<$relb>$token<$relb>";
									}
								}
							} else {	
								# 6: tag token
								if ($token =~ /^$kecil$/) {
									$token =~ s/$kecil/<$relk>$kecil<$relk>/;
								} elsif ($token =~ /^$besar$/) {
									$token =~ s/$besar/<$relb>$besar<$relb>/;
								}
							}

							# update line
							$line = "$line $token";
						}

						# 7: tulis ke file
						if ($line =~ /$relk/) {
							if ($line =~ /$relb/) {
								$line = "<start>$line <end>";

								$line =~ s/\s+/ /g;
								print OUT "$line\n";								
							}
						}
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
