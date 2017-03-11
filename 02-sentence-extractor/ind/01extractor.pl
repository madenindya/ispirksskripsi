#!/usr/local/bin/perl

require "relationMapping.pl";

use strict;
use warnings;

open(OUT_LOG, ">tmp_log");

my $infile = "ind_hype_hypo.corpus";
my $dump_file = "ind_map_hh.corpus";

my $globpath = "./../../00-data";
my $seed = "$globpath/seed/ind/1-1/$infile";
$dump_file = ">$globpath/dump-mapping/$dump_file";



# create relation mapping
print "create mapping $infile\n";
my $kecil_besar = create_mapping($seed, $dump_file, 0);

# process wikipedia data
# print "process Wiki\n";
# my $directory = "$globpath/wiki/wiki-ind-s";
# opendir (DIR, $directory) or die $!;

# # while (my $file = readdir(DIR)) {
# # 	if ($file =~ /[A-Z]+/) {	
# 		my $file = "AE";
# 		my $subdir = "$directory/$file";
# 		print "$subdir\n";
# 		opendir (SUBDIR, $subdir) or die $!;

# 		while (my $sfile = readdir(SUBDIR)) {
# 			if ($sfile =~ /wiki_[0-9]+/) {
# 				print "$sfile\n";

# 				find_sentences($file, $sfile);
# 			}
# 		}

# # 		close(SUBDIR);
# # 	}
# # }

# closedir(DIR);


close(OUT_LOG);


#########################################################################################
# Functions
#
#########################################################################################

#
# benerin ini !!!!!!!!!!!!!!!!!
#########################################################################################
# sub find_sentences {
# 	my ($dir, $filename) = (@_);

# 	my $path = "$globpath/wiki/wiki-ind-s/$dir/$filename";
# 	print "$path\n";
# 	my $opath = "$globpath/wiki/wiki-ind-tagged/hh/sentences_hh_$dir.corpus";
# 	print "$opath\n";

# 	open(IN, $path) or die $!;
# 	open(OUT, ">>$opath") or die $!;

# 	my $count = 0;
# 	while(my $l = <IN>) {
# 		$count += 1;
# 		print "$count \n";

# 		chop($l);
# 	    $l = lc $l;

# 	    # iterate kecil
# 		# harus merupakan word
# 		foreach my $kecil (keys %kecil_besar){

# 			if ($l =~ /$kecil/) {

# 				foreach my $besar (@{$kecil_besar{$kecil}}) {
					
# 					if ($l =~ /$besar/) {

# 						my $line = $l;

# 						$line =~ s/$kecil/<hyponym>$kecil<hyponym>/g;
# 						if ($line =~ /[a-z\.\-0-9]<hyponym>[a-z\.\-0-9]+<hyponym>/) {
# 							next;
# 						}
# 						if ($line =~ /<hyponym>[a-z\.\-0-9]+<hyponym>[a-z\.\-0-9]/) {
# 							next;
# 						}


# 						$line =~ s/$besar/<hypernym>$besar<hypernym>/g;
# 						if ($line =~ /[a-z\.\-0-9]<hypernym>[a-z\.\-0-9]+<hypernym>/) {
# 							next;
# 						}
# 						if ($line =~ /<hypernym>[a-z\.\-0-9]+<hypernym>[a-z\.\-0-9]/) {
# 							next;
# 						}

# 						if ($line =~ /></) {
# 							next;
# 						}

# 						print OUT_LOG "found mero:$kecil! & holo:$besar!\n";

# 						print OUT "$line\n";
# 					}
# 				}

# 			}
# 		}
# 	}

# 	close(IN);
# 	close(OUT);
# }