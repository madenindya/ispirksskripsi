#!/usr/local/bin/perl

use strict;
use warnings;


open(OUT_LOG, ">tmp_log");

my %kecil_besar;
create_mapping();

print "WIKIPEDIA...\n";

my $directory = '../../data/wiki-ind';
# opendir (DIR, $directory) or die $!;
# while (my $file = readdir(DIR)) {
# 	if ($file =~ /[A-Z]+/) {	
		# print "$file\n";
		my $file = "AC";
		my $subdir = "../../data/wiki-ind/$file/";
		print "$subdir\n";
		opendir (SUBDIR, $subdir) or die $!;

		while (my $sfile = readdir(SUBDIR)) {
			if ($sfile =~ /wiki_[0-9]+/) {
				# print "$sfile\n";

				find_sentences($file, $sfile);
			}
		}

		close(SUBDIR);
# 	}

# }
# closedir(DIR);
close(OUT_LOG);


sub create_mapping {
	open(IN, "tmp_seed"); 												# change isi this
	open(OUT, ">../../data/dump-mapping/ind_map_hmm.corpus");			# change this
	my $line;
	while($line = <IN>) {
	chop($line);

	$line =~ s/Synset\('[a-z\.0-9\_]*'\)##//g;
	$line =~ s/\_/ /g;

	my @tokens = split(/\//, $line);

	my $besar = $tokens[0];
	my $kecil = $tokens[1];

	push @{$kecil_besar{$kecil}}, $besar;
	}

	print OUT_LOG "\n\n\n";
	my $total_hypo = scalar (keys %kecil_besar);
	print OUT_LOG "Total unique: $total_hypo\n";

	use Data::Dumper;
	print OUT Dumper \%kecil_besar;
	close(IN);
	close(OUT);	
}


sub find_sentences {
	my ($folder, $filename) = (@_);

	my $path = "../../data/wiki-ind/$folder/$filename";
	print $path;

	open(IN, $path);
	open(OUT, ">>sentences/sentences_hmm_$folder.corpus");

	my $count = 0;
	while(my $l = <IN>) {
		$count += 1;
		print "$count \n";

		chop($l);
	    $l = lc $l;

	    # iterate kecil
		# harus merupakan word
		foreach my $kecil (keys %kecil_besar){

			if ($l =~ /$kecil/) {

				foreach my $besar (@{$kecil_besar{$kecil}}) {
					
					if ($l =~ /$besar/) {

						my $line = $l;

						$line =~ s/$kecil/<meronym>$kecil<meronym>/g;
						if ($line =~ /[a-z\.\-0-9]<meronym>[a-z\.\-0-9]+<meronym>/) {
							next;
						}
						if ($line =~ /<meronym>[a-z\.\-0-9]+<meronym>[a-z\.\-0-9]/) {
							next;
						}


						$line =~ s/$besar/<holonym>$besar<holonym>/g;
						if ($line =~ /[a-z\.\-0-9]<holonym>[a-z\.\-0-9]+<holonym>/) {
							next;
						}
						if ($line =~ /<holonym>[a-z\.\-0-9]+<holonym>[a-z\.\-0-9]/) {
							next;
						}

						if ($line =~ /></) {
							next;
						}

						print OUT_LOG "found mero:$kecil! & holo:$besar!\n";

						print OUT "$line\n";
					}
				}

			}
		}


	}

	close(IN);
}