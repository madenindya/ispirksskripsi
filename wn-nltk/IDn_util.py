from nltk.corpus import wordnet as wn

def get_filtered_ind_lemmas(lemma, synset):
	# ambil semua lemma bahasa indonesia dari synset
	ind_lemmas = synset.lemma_names('ind')

	# kalo nama-nya sama buang
	# kalo lemma termasuk dalam synsets yg bukan NOUN, buang. reduce disambiguation
	for ind_lemma in ind_lemmas:
		if ind_lemma == lemma or len(wn.synsets(ind_lemma, pos=wn.VERB, lang='ind')) > 0 or len(wn.synsets(ind_lemma, pos=wn.ADJ, lang='ind')) > 0 or len(wn.synsets(ind_lemma, pos=wn.ADV, lang='ind')) > 0:
				ind_lemmas.remove(ind_lemma)

	return ind_lemmas

def pretty_print(key, arr_hype, f):
	for hype in arr_hype:
		print_str = "%s ## %s\n" % (key, hype)
		f.write(print_str)
