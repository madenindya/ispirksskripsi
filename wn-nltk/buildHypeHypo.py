from nltk.corpus import wordnet as wn

globpath = "./../00-data/seed/ind/1-1/"

def create_hh(ind_lemmas, rel_code):
	# create mapping synset - noun_ind

	if rel_code == 1:
		print "creating relation member holonym/meronym mapping"
	elif rel_code == 2:
		print "creating relation member holonym/meronym mapping" 
	elif rel_code == 3:
		print "creating relation substance holonym/meronym mapping" 
	elif rel_code == 4: 
		print "creating relation part holonym/meronym mapping" 

	tmp_arr = []
	for lemma in ind_lemmas:
		synsets = wn.synsets(lemma, lang='ind')

		for syn in synsets:

			lemma_names = syn.lemma_names('ind')


			if rel_code == 1:
				rels = syn.hypernyms()
			elif rel_code == 2:
				rels = syn.member_holonyms()
			elif rel_code == 3:
				rels = syn.substance_holonyms()
			elif rel_code == 4: 
				rels = syn.part_holonyms()			

			for rel in rels:
				ind_names = rel.lemma_names('ind')

				if len(ind_names)> 0:
					for name in ind_names:
						for lem_name in lemma_names:
							
							string_print = "%s##%s/%s##%s\n" % (rel, name, syn, lem_name)
							tmp_arr.append(string_print)


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

				if len(ind_names) > 0:
					for name in ind_names:
						for lem_name in lemma_names:

							string_print = "%s##%s/%s##%s\n" % (syn, lem_name, rel, name)
							tmp_arr.append(string_print)

	print "Printing..."
	
	filename = ""
	if rel_code == 1:
		filename = globpath + "ind_hype_hypo.corpus"
	elif rel_code == 2:
		filename = globpath + "ind_holo_mero_m.corpus"
	elif rel_code == 3:
		filename = globpath + "ind_holo_mero_s.corpus"
	elif rel_code == 4: 
		filename = globpath + "ind_holo_mero_p.corpus"

	f = open(filename, 'w')

	if rel_code == 1:
		f.write("syn##hype/syn##hypo\n")
	elif rel_code == 2:
		f.write("syn##m_holo/syn##m_mero\n")
	elif rel_code == 3:
		f.write("syn##s_holo/syn##s_mero\n")
	elif rel_code == 4: 
		f.write("syn##p_holo/syn##p_mero\n")
	
	pretty_print = set(tmp_arr)
	for p in pretty_print:
		f.write(p)

	f.close()
	print "done :)"


# get relation

# wn.synsets('anjing', lang='ind')
# wn.lemmas('anjing', lang='ind')
# dog = wn.synset('dog.n.01')
# dog.hypernyms()
# wn.synset('canine.n.02').lemma_names('ind')

