# Sentence Splitter Module
#-------------------------------------
# Author Ken Nabila Setya 1306413252
# Fasilkom UI 2013
use strict;
use warnings;

my $EOS = "<EOS>";
my $P = q/[\.!?]/;         

#abbreviations
my $abbr_1 = "S.Ked. S.Kom. S.Kep. S.Agr. S.Psi. S.Sos. S.Hut. S.STP. S.Par. S.Ars. S.Kel. M.Kom. M.Hut. M.Psi. M.MPd. S.Hum. M.Hum. M.Stat. M.AB. M.AP. S.Arl. M.Arl. B.Eng. B.Com. M.Eng. M.Th. M.Com. M.Mar.";

my $abbr_2 = "S.KG. S.Gz. S.Pt. S.IP. S.In. S.AB. S.Sy. S.Si. S.Th. S.Sn. S.Ds. S.AP. S.Mb. S.Ag. S.Pd. S.IK. S.KH. S.SI. M.Cs. M.Ag. M.Si. M.TI. M.Pd. M.Ak. M.Sn. M.Kn. B.Sc. M.Sc. D.Th.";

my $abbr_3 = "S.E.I. S.K.M. S.H.I S.T.P. M.P.H. M.B.A.";

my $abbr_4 = "S.E. S.H. S.P. S.T S.S. M.A. M.M. M.T. M.H. M.E. B.A.";

my $abbr_5 = "S.I.Kom. M.I.Kom.";

my $abbr_6 = "S.Pd.I S.Th.I";

my $abbr_7 = "Teol. MMSI.";

my $abbr_8 = "S.Farm. M.Farm. S.SArl. M.SArl. M.Phil.";

my $abbr_9 = "M.A.R.S. S.A.R.S.";

my $abbr_10 = "Dr. dr.";

my $abbr_11 = "K.H. R.A. R.M.";

my $abbr_12 = "Muh. Kms. Hj. Nn. Ny. Tn. M. H. Mr. Ms.";

my $abbr_13 = "a.i. a.n. a.s. adm. b.d. dkk. dll. dsb. dst. ed. hlm. jln. jl. rhs. reg. red. ybs. yth. ytc. n.b.";

my $abbr_14 = "No.";

my $abbr_15 = "Ph.D. Th.M. Ed.D";

my $abbr_16 = "Ka. Kab. Kel. Kec. Kp.";

my $abbr_17 = "PT. CV.";

my $abbr_18 = "Jr. Sr.";

my $abbr_19 = "Drs. Dra. drg. drh. Ir. Prof.";

#my $texts = "halo semua warga u.s.a. dan Tony Jr. My hand Teol wo Dr. Ken Nabila Setya,s.Kom itu. Dari hanya 1.00.00. Aku \"Wong Kito galo! hai pa kabarnya deh\"! Finally, \"Kita udah selesai nih\" PT. apadeh PT. Karya Ciptas nya haeduh. Bikin pusing yaelah (Est. 354).. ya gak sih. RA Kartini pernah blg kalau R.M Adi Tiro no 345 ";

#my $sref = get_sentences($texts);
#foreach my $sentence (@$sref)
#{
#  print $sentence."\n";
#}

#print abbreviations_tag($texts);


sub get_sentences {
  my ($text)=@_;
  return [] unless defined $text;
  my $abbr_text = abbreviations_tag($text);
  my $marked_text = first_sentence_breaking($abbr_text);
  my $fixed_marked_text = remove_false_end_of_sentence($marked_text);
  my @sentences = split(/$EOS/,$fixed_marked_text);
  my $cleaned_sentences = clean_sentences(\@sentences);
  return $cleaned_sentences;
}

sub first_sentence_breaking {
  my ($text) = @_;
  $text=~s/([\n\r]+)/$EOS/gs; ## new-line means a different sentence.
  $text=~s/(\s(?:[a-z0-9]+)$P(?:\s+))([a-z])/$1.uc($2)/gse; #normalize the first letter of sentence that is non-capital 
  $text=~s/($P(?:\s+))([^a-z])/$1$EOS$2/gs; #it will never replace an abbr
  return $text;
}

sub remove_false_end_of_sentence {
  my ($marked_segment) = @_;
##  ## don't do u.s.a., but do 1.000.000.
  $marked_segment=~s/(\.(?:\w+)$P\s)$EOS/$1/sg;        
  $marked_segment=~s/([\.0-9]\.\s)/$1$EOS/sg;         

  # fix: bla bla... yada yada
  $marked_segment=~s/(\.\.\. )$EOS([[:lower:]])/$1$2/sg; 

  # don't break in brackets
  $marked_segment=~s/(\((?:[^\)]*?))$EOS((?:[^\)]*?)\))/$1$2/sg;
  $marked_segment=~s/(\[(?:[^\]]*?))$EOS((?:[^\]]*?)\])/$1$2/sg;
  $marked_segment=~s/(\{(?:[^\}]*?))$EOS((?:[^\}]*?)\})/$1$2/sg;

  # don't break in double quote
  $marked_segment=~s/(\s"(?:[^"]*?))$EOS((?:[^"]*?)")/$1$2/sg;
  $marked_segment=~s/(^"(?:[^"]*?))$EOS((?:[^"]*?)")/$1$2/sg;


  # don't break in single quote
  $marked_segment=~s/(\s'(?:[^']*?))$EOS((?:[^']*?)')/$1$2/sg;
  $marked_segment=~s/(^'(?:[^']*?))$EOS((?:[^']*?)')/$1$2/sg;
  

  # don't break: text . . some more text.
  $marked_segment=~s/(\s\.\s)$EOS(\s*)/$1$2/sg;
  $marked_segment=~s/(["']\s*)$EOS(\s*[[:lower:]])/$1$2/sg;

  $marked_segment=~s/(\s$P\s)$EOS/$1/sg;
  return $marked_segment;
}

sub clean_sentences {
  my ($sentences) = @_;
    my $cleaned_sentences;
    foreach my $s (@$sentences) {
      next if not defined $s;
      next if $s!~m/\w+/;
      $s=~s/^\s*//;
      $s=~s/\s*$//;
      $s=~s/\s+/ /g;
      #remove abbr tag
      $s=~s/<abbr>//g;
      $s=~s/<\/abbr>//g;
      push @$cleaned_sentences,$s;
    }
  return $cleaned_sentences;
}

#normalize abbreviation
sub abbreviations_tag {
  my ($text) = @_;
  my $all_abbr;
  my @all_abbr = ();

  #Rule 1,2,4,8,15
  #example Ken Nabila Setya,s.Kom -> Ken Nabila Setya, S.Kom.
  $all_abbr = $abbr_1." ".$abbr_2." ".$abbr_4." ".$abbr_8." ".$abbr_15;
  @all_abbr = split / /, $all_abbr;

  foreach my $abbr (@all_abbr){
    my @parts = split /\./, $abbr;

    my $uc_1 = uc($parts[0]); 
    my $uc_2 = uc($parts[1]); 
    my $lc_1 = lc($parts[0]); 
    my $lc_2 = lc($parts[1]); 
    $text=~s/([\s,])($uc_1|$lc_1|$parts[0])\.($uc_2|$lc_2|$parts[1])\.?/$1 <abbr>$abbr<\/abbr>/sg;
  }

  #Rule 12, 13
  $all_abbr = $abbr_12." ".$abbr_13;
  @all_abbr = split / /, $all_abbr;

  foreach my $abbr (@all_abbr){
    my @parts = split /\./, $abbr;    
    my $lc_1 = lc($parts[0]); 
    my $norm = $parts[0]; 
    $text=~s/\s(?:$lc_1|$norm)\.?(\s[A-Z<])/ <abbr>$abbr<\/abbr>$1/sg;
    $text=~s/^(?:$lc_1|$norm)\.?(\s[A-Z<])/<abbr>$abbr<\/abbr>$1/sg;
  }

  #Rule 7
  $all_abbr = $abbr_7;
  @all_abbr = split / /, $all_abbr;

  foreach my $abbr (@all_abbr){
    my @parts = split /\./, $abbr;
    $text=~s/([\s,])$parts[0]\.?/$1 <abbr>$abbr<\/abbr>/sg;
  }

  #Rule 3,5,6
  $all_abbr = $abbr_3." ".$abbr_5." ".$abbr_6;
  @all_abbr = split / /, $all_abbr;

  foreach my $abbr (@all_abbr){
    my @parts = split /\./, $abbr;

    my $uc_1 = uc($parts[0]); 
    my $uc_2 = uc($parts[1]); 
    my $uc_3 = uc($parts[2]); 
    my $lc_1 = lc($parts[0]); 
    my $lc_2 = lc($parts[1]); 
    my $lc_3 = lc($parts[2]);
    $text=~s/([\s,])($uc_1|$lc_1|$parts[0])\.($uc_2|$lc_2|$parts[1])\.($uc_3|$lc_3|$parts[2])\.?/$1 <abbr>$abbr<\/abbr>/sg;
  }

  #Rule 10,17
  $all_abbr = $abbr_10." ".$abbr_17;
  @all_abbr = split / /, $all_abbr;

  foreach my $abbr (@all_abbr){
    my @parts = split /\./, $abbr;
    $text=~s/\s$parts[0]\.?(\s[A-Z<])/ <abbr>$abbr<\/abbr>$1/sg;    
    $text=~s/^$parts[0]\.?(\s[A-Z<])/<abbr>$abbr<\/abbr>$1/sg;
  }

  #Rule 11
  $all_abbr = $abbr_11;
  @all_abbr = split / /, $all_abbr;

  foreach my $abbr (@all_abbr){
    my @parts = split /\./, $abbr;

    $text=~s/\s$parts[0](?:\.?)$parts[1](?:\.?)(\s[A-Z<])/ <abbr>$abbr<\/abbr>$1/sg;
    $text=~s/^$parts[0](?:\.?)$parts[1](?:\.?)(\s[A-Z<])/<abbr>$abbr<\/abbr>$1/sg;
  }

  #Rule 14
  $all_abbr = $abbr_14;
  @all_abbr = split / /, $all_abbr;

  foreach my $abbr (@all_abbr){
    my @parts = split /\./, $abbr;
    my $uc_1 = uc($parts[0]); 
    my $lc_1 = lc($parts[0]); 
    $text=~s/\s(?:$lc_1|$uc_1|$parts[0])\.?(\s[0-9])/ <abbr>$abbr<\/abbr>$1/sg;
    $text=~s/^(?:$lc_1|$uc_1|$parts[0])\.?(\s[0-9])/<abbr>$abbr<\/abbr>$1/sg;
  }

  #Rule 16,19
  $all_abbr = $abbr_16." ".$abbr_19;
  @all_abbr = split / /, $all_abbr;

  foreach my $abbr (@all_abbr){
    my @parts = split /\./, $abbr;
    my $lc_1 = lc($parts[0]); 
    my $ucf_1 = ucfirst($parts[0]);
    $text=~s/\s(?:$lc_1|$ucf_1)\.?(\s[dA-Z<])/ <abbr>$abbr<\/abbr>$1/sg;
    $text=~s/^(?:$lc_1|$ucf_1)\.?(\s[dA-Z<])/<abbr>$abbr<\/abbr>$1/sg;
  }

  #Rule 9
  $all_abbr = $abbr_9;
  @all_abbr = split / /, $all_abbr;

  foreach my $abbr (@all_abbr){
    my $without_dot = $abbr;
    $without_dot =~ s/\.//g;
    my $without_end_dot = $abbr;
    $without_end_dot =~ s/\.$//g;
    $text=~s/([\s,])$abbr/$1 <abbr>$abbr<\/abbr>/sg;
    $text=~s/([\s,])(?:$without_dot|$without_end_dot)/$1 <abbr>$abbr<\/abbr>/sg;
  }

  #Rule 18
  $all_abbr = $abbr_18;
  @all_abbr = split / /, $all_abbr;
  foreach my $abbr (@all_abbr){
    $text=~s/\s$abbr/ <abbr>$abbr<\/abbr>/sg;
  }

  return $text;
}

sub normalize_sentence
{
  my($str, $model_type) = @_;
  if($model_type == 1){
    #clean the unusual punctuations in front of a sentence
    $str =~ s/^([\.\]\}\)\:\=\+\!\?\\\/\-\*\#]+)//g; 
    #clean the unusual punctuations in front of a word 
    $str =~ s/(\s)([\.\]\}\)\:\=\+\!\?\-\*]+)([a-zA-Z])/$1$2/g; 
    $str =~ s/^((?!$P)[[:punct:]])/$1 /g;  
    $str =~ s/((?!$P)[[:punct:]]$)/ $1/g; 
    $str =~ s/([^A-Za-z0-9])((?!\.)[[:punct:]])/$1 $2 /g;   
    $str =~ s/((?!\.)[[:punct:]])([^A-Za-z0-9])/ $1 $2/g; 
    $str =~ s/($P)(\s*)$/ $1/g;    
  }
  elsif($model_type == 2){
    $str =~ s/^((?!$P)[[:punct:]])//g;  
    $str =~ s/((?!$P)[[:punct:]]$)//g; 
    $str =~ s/([^A-Za-z0-9])(?:(?!\.)[[:punct:]])/$1 /g;   
    $str =~ s/(?:(?!\.)[[:punct:]])([^A-Za-z0-9])/ $1/g; 
    $str =~ s/($P)(\s*)$//g; 
  }
  #delete quotation
  $str =~ s/["'] / /g;
  $str =~ s/ ["']/ /g;

  $str =~ s/\s+/ /g;
  $str =~ s/^[[:space:]]+//g;
  if($model_type == 1){
    #fix the hyperlink
    $str =~ s/http\s:\s\/\s/http:\//g;  
    $str =~ s/https\s:\s\/\s/https:\//g;      
    $str =~ s/Http\s:\s\/\s/http:\//g;  
    $str =~ s/Https\s:\s\/\s/https:\//g; 
  }else{
    #fix the hyperlink
    $str =~ s/http\s/http:\//g;  
    $str =~ s/https\s/https:\//g;      
    $str =~ s/Http\s/http:\//g;  
    $str =~ s/Https\s/https:\//g; 
  }
  return $str;
}