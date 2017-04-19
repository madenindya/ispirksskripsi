use strict;
use warnings;

main();

sub main {

    # yang gak di postag
    my $directory = '../00-data/wiki/wiki-ind-s';
    opendir (DIR, $directory) or die $!;
    my $count = 0;
    while (my $file = readdir(DIR)) {
        if ($file =~ /[A-Z]+/) {    
            my $subdir = "../00-data/wiki/wiki-ind-s/$file/";
            print "$subdir\n";
            
            opendir (SUBDIR, $subdir) or die $!;

            while (my $sfile = readdir(SUBDIR)) {
                if ($sfile =~ /wiki_[0-9]+/) {
                    my $ipath = "$subdir/$sfile";
                    my $opath;
                    if($count < 10) {
                        $opath = "../00-data/wiki/wiki-ind-js-lowcase/wiki_00$count";
                    } elsif ($count < 100) {
                        $opath = "../00-data/wiki/wiki-ind-js-lowcase/wiki_0$count";
                    } else {
                        $opath = "../00-data/wiki/wiki-ind-js-lowcase/wiki_$count";
                    }
                    clean_file($ipath, $opath);

                    $count++;
                }
            }
            close(SUBDIR);
        }
    }
    close(DIR);

}

sub clean_file {
    my ($fpath, $opath) = @_;

    open (IN, $fpath) or die $!;
    open (OUT, ">$opath") or die $!;

    my $line;
    while ($line = <IN>) {
        chomp($line);
        my $stc = clean_line($line);
        if (length($stc) > 0) {
            print OUT "$stc\n";
        }
    }

    close(IN);
    close(OUT);
}

sub clean_line {

    my ($sentence) = @_;

    $sentence =~ s/^<start> //;
    $sentence =~ s/ <end>$//;
    $sentence =~ s/<\/doc>//;

    $sentence = lc $sentence;

    return $sentence;
}
