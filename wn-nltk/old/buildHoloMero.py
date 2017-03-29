from nltk.corpus import wordnet as wn

globpath = "./../00-data/seed/ind/1-1/"

#############################################################################
def create_hmm(ind_lemmas):
	# create mapping synset - noun_ind
	print "creating relation member holonym/meronym mapping" 

	tmp_arr = []
	for lemma in ind_lemmas:
		synsets = wn.synsets(lemma, lang='ind')

		for syn in synsets:

			rels = syn.member_holonyms()			
			for rel in rels:
				ind_names = rel.lemma_names('ind')

				if len(ind_names)> 0:
					for name in ind_names:
						string_print = "%s/%s\n" % (name, lemma)
						tmp_arr.append(string_print)

			rels = syn.member_meronyms()
			for rel in rels: 
				ind_names = rel.lemma_names('ind')

				if len(ind_names) > 0:
					for name in ind_names:
						string_print = "%s/%s\n" % (lemma, name)
						tmp_arr.append(string_print)

	print "Printing..."
	filename = globpath + "ind_holo_mero_m.corpus"
	f = open(filename, 'w')

	pretty_print = set(tmp_arr)
	for p in pretty_print:
		f.write(p)

	f.close()
	print "done :)"


#############################################################################
def create_hms(ind_lemmas):
	# create mapping synset - noun_ind
	print "creating relation substance holonym/meronym mapping" 

	tmp_arr = []
	for lemma in ind_lemmas:
		synsets = wn.synsets(lemma, lang='ind')

		for syn in synsets:

			rels = syn.substance_holonyms()			
			for rel in rels:
				ind_names = rel.lemma_names('ind')

				if len(ind_names)> 0:
					for name in ind_names:
						string_print = "%s/%s\n" % (name, lemma)
						tmp_arr.append(string_print)

			rels = syn.substance_meronyms()
			for rel in rels: 
				ind_names = rel.lemma_names('ind')

				if len(ind_names) > 0:
					for name in ind_names:
						string_print = "%s/%s\n" % (lemma, name)
						tmp_arr.append(string_print)

	print "Printing..."
	filename = globpath + "ind_holo_mero_s.corpus"
	f = open(filename, 'w')

	pretty_print = set(tmp_arr)
	for p in pretty_print:
		f.write(p)

	f.close()
	print "done :)"


#############################################################################
def create_hmp(ind_lemmas):
	# create mapping synset - noun_ind
	print "creating relation part holonym/meronym mapping" 

	tmp_arr = []
	for lemma in ind_lemmas:
		synsets = wn.synsets(lemma, lang='ind')

		for syn in synsets:

			rels = syn.part_holonyms()			
			for rel in rels:
				ind_names = rel.lemma_names('ind')

				if len(ind_names)> 0:
					for name in ind_names:
						string_print = "%s/%s\n" % (name, lemma)
						tmp_arr.append(string_print)

			rels = syn.part_meronyms()
			for rel in rels: 
				ind_names = rel.lemma_names('ind')

				if len(ind_names) > 0:
					for name in ind_names:
						string_print = "%s/%s\n" % (lemma, name)
						tmp_arr.append(string_print)

	print "Printing..."
	filename = globpath + "ind_holo_mero_p.corpus"
	f = open(filename, 'w')

	pretty_print = set(tmp_arr)
	for p in pretty_print:
		f.write(p)

	f.close()
	print "done :)"

# wn.synsets('anjing', lang='ind')
# wn.lemmas('anjing', lang='ind')
# dog = wn.synset('dog.n.01')
# dog.hypernyms()
# wn.synset('canine.n.02').lemma_names('ind')

