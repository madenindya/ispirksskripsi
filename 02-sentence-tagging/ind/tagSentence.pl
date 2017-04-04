#!/usr/local/bin/perl
# Tag Sentence

use strict;
use warnings;

my $EOS = "<EOS>";
my $globpath = "./../../00-data";

sub tag_sentence {
	my ($dir, $file, $rel, %map) = (@_);

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
		$l = lc $l; # 4: jadiin lowercase
		$l =~ s/\(\)//g; # ini suka ada

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

							# 5.5: tambahin rule jika token berhimpit dengan 1 karakter non alphanumeric
							# hanya yang di depan/belakang aja
							while ($token =~ /^([^A-z0-9])/) {
								$line = "$line $1";
								$token =~ s/^[^A-z0-9]//;	
							}
							my $back = "";
							while ($token =~ /([^A-z0-9])$/) {
								$back = "$1 $back";
								$token =~ s/[^A-z0-9]$//;	
							}

							# 6: tag token
							if ($token =~ /^$kecil$/) {
								$token =~ s/$kecil/<$relk>$kecil<$relk>/;
							} elsif ($token =~ /^$besar$/) {
								$token =~ s/$besar/<$relb>$besar<$relb>/;
							}

							# update line
							$line = "$line $token";
							if (length $back > 0) {
								$line = "$line $back";
							}
						}

						# 7: tulis ke file
						if ($line =~ /$relk/) {
							if ($line =~ /$relb/) {
								$line = "<start>$line <end>\n";
								$line =~ s/\s\s+/ /g;
								print OUT $line;								
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
