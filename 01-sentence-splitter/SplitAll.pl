# Split all paragraf in corpus from wikipedia extractor

require "ID_SentenceSplitter.pl";

use strict;
use warnings;

print "Start\n";
main();
print "End, bye\n";

sub main {

	# read all files in directory
	my $directory = '../00-data/wiki/wiki-ind';
	# opendir (DIR, $directory) or die $!;
	# while (my $file = readdir(DIR)) {
	# 	if ($file =~ /[A-Z]+/) {	
	# 		print "$file\n";
			my $file = "AC";
			my $subdir = "../00-data/wiki/wiki-ind/$file/";
			print "$subdir\n";
			opendir (SUBDIR, $subdir) or die $!;

			while (my $sfile = readdir(SUBDIR)) {
				if ($sfile =~ /wiki_[0-9]+/) {
					# print "$sfile\n";

					split_all($file, $sfile);
				}
			}

			close(SUBDIR);
	# 	}
	# }
	# closedir(DIR);
}


sub split_all {
	my ($folder, $filename) = (@_);

	my $opath = "../00-data/wiki/wiki-ind/$folder/$filename";
	my $cpath = "../00-data/wiki/wiki-ind-s/$folder/$filename";
	$cpath = ">$cpath";

	open (IN, $opath);
	open (OUT, $cpath);


	my $line;

	print "Split\n";
	while($line = <IN>) {
		# print "$line";
		if (length $line > 0) {
			
			if ($line =~ /<doc.+>/) {
				next;
			}

			my $results = get_sentences($line);

			foreach my $s (@$results) {
			    next if not defined $s;
			    print OUT "$s\n";
			}
		}
	}

	close(IN);
	close (OUT);
}
