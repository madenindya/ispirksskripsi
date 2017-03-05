from nltk.corpus import wordnet as wn

from buildCorpusInd import create_rel
from buildHypeHypo import create_hh
#from buildHoloMero import create_hmm, create_hms, create_hmp

# main
print "Please select what you want:"
print "1. Corpus all"
print "2. Corpus pair"
chosen1 = input()

if chosen1 == 1 or chosen1 == 2:
	print "Please choose:"
	print "1. all"
	print "2. hype/hypo"
	print "3. member holo/mero"
	print "4. substance holo/mero"
	print "5. part holo/mero"	

	chosen2 = input()

	if chosen2 > 0 or chosen2 < 6:
		print "Retrieving all Indonesia lemma"
		ind_lemmas = wn.all_lemma_names(pos='n', lang='ind')

		if chosen1 == 1 and chosen2 == 1:
			create_rel(ind_lemmas, 1)
			create_rel(ind_lemmas, 2)
			create_rel(ind_lemmas, 3)
			create_rel(ind_lemmas, 4)
		elif chosen1 == 1 and chosen2 == 2:
			create_rel(ind_lemmas, 1)
		elif chosen1 == 1 and chosen2 == 3:
			create_rel(ind_lemmas, 2)
		elif chosen1 == 1 and chosen2 == 4:
			create_rel(ind_lemmas, 3)
		elif chosen1 == 1 and chosen2 == 5:
			create_rel(ind_lemmas, 4)
		elif chosen1 == 2 and chosen2 == 1:
			create_hh(ind_lemmas, 1)
			create_hh(ind_lemmas, 2)
			create_hh(ind_lemmas, 3)
			create_hh(ind_lemmas, 4)
		elif chosen1 == 2 and chosen2 == 2:
			create_hh(ind_lemmas, 1)
		elif chosen1 == 2 and chosen2 == 3:
			create_hh(ind_lemmas, 2)
		elif chosen1 == 2 and chosen2 == 4:
			create_hh(ind_lemmas, 3)
		elif chosen1 == 2 and chosen2 == 5:
			create_hh(ind_lemmas, 4)
	else:
		print ":v"

else:
	print ":v"