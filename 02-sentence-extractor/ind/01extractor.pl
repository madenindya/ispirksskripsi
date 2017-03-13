#!/usr/local/bin/perl

require "relationMapping.pl";
require "tagSentence.pl";

use strict;
use warnings;

open(OUT_LOG, ">tmp_log");

my $r = "hh";									# change this
my $infile = "ind_hype_hypo.corpus";			# and this
my $dump_file = "ind_map_$r.corpus";

my $globpath = "./../../00-data";
my $seed = "$globpath/seed/ind/1-1/$infile";
$dump_file = ">$globpath/dump-mapping/$dump_file";



# create relation mapping
print "create mapping $infile\n";
my $kecil_besar = create_mapping($seed, $dump_file, 0);


# process wikipedia data
print "process Wiki\n";
my $directory = "$globpath/wiki/wiki-ind-s";
opendir (DIR, $directory) or die $!;

while (my $file = readdir(DIR)) {
	if ($file =~ /[A-Z]+/) {	
		# my $file = "AE";
		
		my $subdir = "$directory/$file";
		print "$file --> $subdir\n";
		opendir (SUBDIR, $subdir) or die $!;

		while (my $sfile = readdir(SUBDIR)) {
			if ($sfile =~ /wiki_[0-9]+/) {
				print "$sfile\n";

				# print "tag sentences\n";
				tag_sentence($file, $sfile, $r, %$kecil_besar);
			}
		}

		close(SUBDIR);
	}
}

closedir(DIR);


close(OUT_LOG);
