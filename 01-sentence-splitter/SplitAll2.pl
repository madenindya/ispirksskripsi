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

    print "Split $opath\n";
    my $line;
    while($line = <IN>) {
        # print "$line";
        if (length $line > 0) {
            
            if ($line =~ /(<\/doc>)/) {
                print OUT "$line\n";
                next;
            }

            $line =~ s/\[//g; # buang simbol siku
            $line =~ s/\]//g; # buang simbol siku
            $line =~ s/\(.*?\)//g; # buang yang ada dalam kurung. diasumsikan hanya kata/kalimat penjelas
            

            my $sentence = split_nonAlphanumerical($line);
            $sentence = "<start> $sentence";
            if ($sentence =~ /\s$/) {
                $sentence = "$sentence<end>";
            } else {
                $sentence = "$sentence <end>"
            }
            $line =~ s/[\s\t]+/\s/g;

            if (length($sentence) > 0) {
                print OUT "$sentence\n";
            }
        }
    }

    close(IN);
    close (OUT);
}
