#!/usr/local/bin/perl

open (IN, "index.noun.modif");

open (OUT1, ">lemma_hyponym.txt");
open (OUT2, ">lemma_memberMeronym.txt");
open (OUT3, ">lemma_substanceMeronym.txt");
open (OUT4, ">lemma_partMeronym.txt");


my %offsetToSynset;
my %hyponymRel;		# ~
my %memberMeronymRel;		# ~
my %substanceMeronymRel;		# ~
my %partMeronymRel;		# ~

create_offsetToSynset();
print_offsetToSynset();

create_hyponymRel();
print_hyponymRel();

create_memberMeronymRel();
print_memberMeronymRel();

create_substanceMeronymRel();
print_substanceMeronymRel();

create_partMeronymRel();
print_partMeronymRel();

close(IN);
close(OUT1);
close(OUT2);
close(OUT3);
close(OUT4);
# ==========================================================================




# ==========================================================================
sub create_offsetToSynset {
	print "Start creating noun index\n";

	while ($line = <IN>) {
		chop($line);

		my @tokens = split(/\s+/, $line);
		my $lemma = $tokens[0];

		my $countOffset = $tokens[2];
		my $len = scalar @tokens;

		# print "$lemma - $countOffset - $len\n";

		for (my $i = 1; $i <= $countOffset; $i++) {
			my $key = $tokens[$len - $i];
			if ($offsetToSynset{$key}) {
				$offsetToSynset{$key} = "$offsetToSynset{$key},$lemma";
			} else {
				$offsetToSynset{$key} = "$lemma";
			}
		}
	}

	print "Finish creating noun index\n";
}
# ==========================================================================
sub create_hyponymRel {

	print "Start creating Hyponym Relation\n";
	open (IN2, "data.noun.modif");

	while ($line = <IN2>) {
		chop($line);

		my @tokens = split(/\s+/, $line);
		my $currOffset = $tokens[0]; 
	
		if ($line =~ /\~/) {
			my @calon = split(/\~/, $line); 

			my $len1 = scalar @calon;				
			for (my $i = 1; $i < $len1; $i++) {

				my @calon2 = split(/\s/, $calon[$i]);
				# ambil offset-nya
				my $hypoOffset = $calon2[1];

				if ($hyponymRel{$currOffset}) {
					$hyponymRel{$currOffset} = "$hyponymRel{$currOffset},$hypoOffset";
				} else {
					$hyponymRel{$currOffset} = "$hypoOffset";
				}

				print OUT1 "$offsetToSynset{$currOffset}/$offsetToSynset{$hypoOffset}\n";

			}
		}
	}	

	close (IN2, "data.noun.modif");
	print "Finish creating Hyponym Relation\n";
}
# ==========================================================================
sub create_memberMeronymRel {
	print "Start creating memberMeronymRel Relation\n";
	open (IN2, "data.noun.modif");

	while ($line = <IN2>) {
		chop($line);

		my @tokens = split(/\s+/, $line);
		my $currOffset = $tokens[0]; 
	
		if ($line =~ /\%m/) {
			my @calon = split(/\%m/, $line); 

			my $len1 = scalar @calon;				
			for (my $i = 1; $i < $len1; $i++) {

				my @calon2 = split(/\s/, $calon[$i]);
				# ambil offset-nya
				my $offset = $calon2[1];

				if ($memberMeronymRel{$currOffset}) {
					$memberMeronymRel{$currOffset} = "$memberMeronymRel{$currOffset},$offset";
				} else {
					$memberMeronymRel{$currOffset} = "$offset";
				}

				print OUT2 "$offsetToSynset{$currOffset}/$offsetToSynset{$offset}\n";

			}
		}
	}	

	close (IN2, "data.noun.modif");
	print "Finish creating memberMeronymRel Relation\n";
}
# ==========================================================================
sub create_substanceMeronymRel {
	print "Start creating substanceMeronymRel Relation\n";
	open (IN2, "data.noun.modif");

	while ($line = <IN2>) {
		chop($line);

		my @tokens = split(/\s+/, $line);
		my $currOffset = $tokens[0]; 
	
		if ($line =~ /\%s/) {
			my @calon = split(/\%s/, $line); 

			my $len1 = scalar @calon;				
			for (my $i = 1; $i < $len1; $i++) {

				my @calon2 = split(/\s/, $calon[$i]);
				# ambil offset-nya
				my $offset = $calon2[1];

				if ($substanceMeronymRel{$currOffset}) {
					$substanceMeronymRel{$currOffset} = "$substanceMeronymRel{$currOffset},$offset";
				} else {
					$substanceMeronymRel{$currOffset} = "$offset";
				}

				print OUT3 "$offsetToSynset{$currOffset}/$offsetToSynset{$offset}\n";
			}
		}
	}	

	close (IN2, "data.noun.modif");
	print "Finish creating memberMeronymRel Relation\n";
}
# ==========================================================================
sub create_partMeronymRel {
	print "Start creating partMeronymRel Relation\n";
	open (IN2, "data.noun.modif");

	while ($line = <IN2>) {
		chop($line);

		my @tokens = split(/\s+/, $line);
		my $currOffset = $tokens[0]; 
	
		if ($line =~ /\%p/) {
			my @calon = split(/\%p/, $line); 

			my $len1 = scalar @calon;				
			for (my $i = 1; $i < $len1; $i++) {

				my @calon2 = split(/\s/, $calon[$i]);
				# ambil offset-nya
				my $offset = $calon2[1];

				if ($partMeronymRel{$currOffset}) {
					$partMeronymRel{$currOffset} = "$partMeronymRel{$currOffset},$offset";
				} else {
					$partMeronymRel{$currOffset} = "$offset";
				}

				print OUT4 "$offsetToSynset{$currOffset}/$offsetToSynset{$offset}\n";
			}
		}
	}	

	close (IN2, "data.noun.modif");
	print "Finish creating memberMeronymRel Relation\n";
}
# ==========================================================================




# ==========================================================================
sub print_offsetToSynset {
	open (OUT, ">offset_synset.txt");

	print "Printing offsetToSynset to file\n";

	foreach  (sort keys %offsetToSynset) {
		 print OUT "$_ : $offsetToSynset{$_}\n";
	}

	print "Finish printing offsetToSynset to file\n";

	close(OUT);
}
# ==========================================================================
sub print_hyponymRel {
	open (OUT, ">offset_hyponymRel.txt");

	print "Printing hyponymRel to file\n";

	foreach  (sort keys %hyponymRel) {
		 print OUT "$_ : $hyponymRel{$_}\n";
	}

	print "Finish printing hyponymRel to file\n";

	close(OUT);
}
# ==========================================================================
sub print_memberMeronymRel {
	open (OUT, ">offset_memberMeronymRel.txt");

	print "Printing memberMeronymRel to file\n";

	foreach  (sort keys %memberMeronymRel) {
		 print OUT "$_ : $memberMeronymRel{$_}\n";
	}

	print "Finish printing memberMeronymRel to file\n";

	close(OUT);
}
# ==========================================================================
sub print_substanceMeronymRel {
	open (OUT, ">offset_substanceMeronymRel.txt");

	print "Printing substanceMeronymRel to file\n";

	foreach  (sort keys %substanceMeronymRel) {
		 print OUT "$_ : $substanceMeronymRel{$_}\n";
	}

	print "Finish printing substanceMeronymRel to file\n";

	close(OUT);
}
# ==========================================================================
sub print_partMeronymRel {
	open (OUT, ">offset_partMeronymRel.txt");

	print "Printing partMeronymRel to file\n";

	foreach  (sort keys %partMeronymRel) {
		 print OUT "$_ : $partMeronymRel{$_}\n";
	}

	print "Finish printing partMeronymRel to file\n";

	close(OUT);
}
# ==========================================================================
