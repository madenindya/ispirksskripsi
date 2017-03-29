# rel_code
# 1 -> hyponyms
# 2 -> member_meronyms
# 3 -> substance_meronyms
# 4 -> part_meronyms

from nltk.corpus import wordnet as wn

def create_rel(ind_lemmas, rel_code):
	# create mapping synset - noun_ind
	print "creating relation %s mapping" % (rel_code)
	filename = "./../00-data/seed/ind/n-n/ind_corpus%s.corpus" % (rel_code)
	f = open(filename, 'w')
	for lemma in ind_lemmas:
		synsets = wn.synsets(lemma, lang='ind')

		for syn in synsets:

			print_lemma  = syn.lemma_names('ind')
			
			if rel_code == 1:
				rels = syn.hyponyms()
			elif rel_code == 2:
				rels = syn.member_meronyms()
			elif rel_code == 3:
				rels = syn.substance_meronyms()
			elif rel_code == 4: 
				rels = syn.part_meronyms()

			for rel in rels:
				ind_names = rel.lemma_names('ind')

				if len(ind_names)> 0:
					string_print = "%s##%s/%s##%s \n" % (syn, print_lemma, rel, ind_names)
					f.write(string_print)
				# ujian - [u'investigasi', u'memeriksa', u'pemeriksaan', u'penelitian', u'pengusutan', u'penyelidikan', u'selidik']
	f.close()
	print "done :)"

# get relation

# wn.synsets('anjing', lang='ind')
# wn.lemmas('anjing', lang='ind')
# dog = wn.synset('dog.n.01')
# dog.hypernyms()
# wn.synset('canine.n.02').lemma_names('ind')

