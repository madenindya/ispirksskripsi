use strict;
use warnings;

my $EOS = "<EOS>";
my $P = q/[\.!?]/;         

#split symbol
# kalo bukan postag, tambahin tahap ini

# my $kata1 = "Sebagian besar DNA (lebih dari 98% pada manusia) bersifat non-kode, yang berarti bagian ini tidak berfungsi menyandikan protein.";
# my $kata2 = "Utah merupakan pusat dari Gereja Jesus Christ of Latter-day Saints (LDS atau Gereja Mormon), yang diperkirakan 60% dari penduduk adalah anggotanya, Gereja LDS memiliki pengaruh budaya yang kuat, termasuk salah satu dari dua negara bagian di mana perjudian ilegal.";

# my $hasil1 = split_nonAlphanumerical($kata1);
# print "$hasil1\n";
# my $hasil2 = split_nonAlphanumerical($kata2);
# print "$hasil2\n";

sub split_nonAlphanumerical {

    my($sentence) = @_;
    my @tokens = split(/\s+/, $sentence);
    my $line = "";

    foreach my $token (@tokens) {

        my $back = "";

        while ($token =~ /^([^A-z0-9])/) {
            $line = "$line $1";
            $token =~ s/^[^A-z0-9]//;   
        }
        while ($token =~ /([^A-z0-9])$/) {
            $back = "$1 $back";
            $token =~ s/[^A-z0-9]$//;   
        }

        # update line
        $line = "$line $token";
        if (length $back > 0) {
            $line = "$line $back";
        }

        $line =~ s/\s+/ /g;
        $sentence = $1 if ($line =~ /^\s(.*)/);
    }

    return $sentence;
}
