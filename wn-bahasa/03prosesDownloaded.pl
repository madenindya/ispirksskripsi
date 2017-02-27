#!/usr/local/bin/perl


open (IN, "tmp-n.html");
open (OUT, ">tmp-data-check-ind");

my $line;
my $synset = "";
my $relation = "";

while ($line = <IN>) {
	chop($line);

	if ($line =~ /xml:lang="ind"/) {
		$line = <IN>;
		$line = <IN>;
		$line =~ s/^\s+//;
		$line =~ s/<\/td>\s*$//;
		$synset = $synset . "," . $line;
	} 
	elsif ($line =~ /ontology#hyponym/) {
		$line = <IN>;
		$line = <IN>;
		$line = <IN>;
		$line =~ s/<\/a>\s*$//;
		$line =~ s/^\s+<.*>//;
		$relation = $relation . "hyponym\n";
		$relation = $relation . "$line\n";
	}
	elsif ($line =~ /ontology#(.*holonym)"/) {
		my ($rel) = $line =~ /ontology#(.*holonym)"/;
		$line = <IN>;
		$line = <IN>;
		$line = <IN>;
		$line =~ s/<\/a>\s*$//;
		$line =~ s/^\s+<.*>//;
		$relation = $relation . "$rel\n";
		$relation = $relation . "$line\n";
	}
}

$synset =~ s/^,//;

print OUT "$synset\n";
print OUT "$relation";

close(IN);
close(OUT);