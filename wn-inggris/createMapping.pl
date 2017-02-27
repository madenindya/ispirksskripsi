#!/usr/local/bin/perl

open (IN, "index.noun.modif");
open (OUT, ">result.txt");

my %offsetToSynset;

create_offsetToSynset();
print_offsetToSynset();

close(IN);
close(OUT);


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
sub print_offsetToSynset {

	print "Printing result to file\n";

	foreach  (sort keys %offsetToSynset) {
		 print OUT "$_ : $offsetToSynset{$_}\n";
	}

	print "Finish printing result to file\n";
}
# ==========================================================================
