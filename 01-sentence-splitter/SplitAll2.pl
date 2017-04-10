require "ID_SplitNonAlphanumerical.pl";

use strict;
use warnings;

my $directory = '../00-data/wiki/wiki-ind-s';
opendir (DIR, $directory) or die $!;
while (my $file = readdir(DIR)) {
    if ($file =~ /[A-Z]+/) {    
        my $subdir = "../00-data/wiki/wiki-ind-s/$file/";
        print "$subdir\n";
        
        opendir (SUBDIR, $subdir) or die $!;

        while (my $sfile = readdir(SUBDIR)) {
            if ($sfile =~ /wiki_[0-9]+/) {
                split_all($file, $sfile);
            }
        }
        close(SUBDIR);
    }
}
closedir(DIR);


sub split_all {
    my ($folder, $filename) = (@_);

    my $opath = "../00-data/wiki/wiki-ind-s/$folder/$filename";
    my $cpath = "../00-data/wiki/wiki-ind-s2/$folder/$filename";
    $cpath = ">$cpath";

    open (IN, $opath);
    open (OUT, $cpath);


    my $line;

    print "Split $opath\n";
    while($line = <IN>) {
        # print "$line";
        if (length $line > 0) {
            
            if ($line =~ /<doc.+>/) {
                next;
            }

            my $sentence = split_nonAlphanumerical($line);

            if (length($sentence) > 0) {
                print OUT "$sentence\n";
            }
        }
    }

    close(IN);
    close (OUT);
}
