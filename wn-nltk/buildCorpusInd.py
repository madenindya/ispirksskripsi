# rel_code
# 1 -> hyponyms
# 2 -> member_meronyms
# 3 -> substance_meronyms
# 4 -> part_meronyms

from nltk.corpus import wordnet as wn

def create_rel(ind_lemmas, rel_code):
	# create mapping synset - noun_ind
	print "creating relation %s mapping" % (rel_code)
	filename = "ind_corpus%s.corpus" % (rel_code)
	f = open(filename, 'w')
	for lemma in ind_lemmas:
		synsets = wn.synsets(lemma, lang='ind')

		for syn in synsets:
			
			if rel_code == 1:
				rels = syn.hypernyms()
			elif rel_code == 2:
				rels = syn.member_meronyms()
			elif rel_code == 3:
				rels = syn.substance_meronyms()
			elif rel_code == 4: 
				rels = syn.part_meronyms()

			for rel in rels:
				ind_names = rel.lemma_names('ind')

				if len(ind_names)> 0:
					string_print = "%s/%s \n" % (lemma, ind_names)
					f.write(string_print)
				# ujian - [u'investigasi', u'memeriksa', u'pemeriksaan', u'penelitian', u'pengusutan', u'penyelidikan', u'selidik']
	print "done :)"


# MAIN PROGRAM
# get all lemma -- Noun
print "Retrieving all Indonesia lemma"
ind_lemmas = wn.all_lemma_names(pos='n', lang='ind')

create_rel(ind_lemmas, 1)
create_rel(ind_lemmas, 2)
create_rel(ind_lemmas, 3)
create_rel(ind_lemmas, 4)





# get relation

# wn.synsets('anjing', lang='ind')
# wn.lemmas('anjing', lang='ind')
# dog = wn.synset('dog.n.01')
# dog.hypernyms()
# wn.synset('canine.n.02').lemma_names('ind')

