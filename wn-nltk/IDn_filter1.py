from nltk.corpus import wordnet as wn
from IDn_util import get_filtered_ind_lemmas
from IDn_util import pretty_print

# kalo punya > 1 hypernym untuk SYNSET ataupun LEMMA -> buang!
def get_mapping_superStrict(ind_lemmas):
	hypohypes_dict = {}
	filename = "seed_superStrict"
	f = open(filename, 'w')

	for lemma in ind_lemmas:
		# FILTER-1: ambil lemma yg tidak ambigu.
		# 			kalo lemma termasuk dalam synsets yg bukan NOUN, skip. reduce disambiguation
		if len(wn.synsets(lemma, pos=wn.VERB, lang='ind')) > 0 or len(wn.synsets(lemma, pos=wn.ADJ, lang='ind')) > 0 or len(wn.synsets(lemma, pos=wn.ADV, lang='ind')) > 0:
			continue

		# retrive all noun synset
		synsets = wn.synsets(lemma, pos=wn.NOUN, lang='ind')
		for synset in synsets: # synset = hyponym
			hypernyms = synset.hypernyms()
			if len(hypernyms) != 1:
				continue # tidak punya hypernym / hypernym > 1, bye

			hypernym = hypernyms[0]
			ind_hyp_lemmas = get_filtered_ind_lemmas(lemma, hypernym)
			if len(ind_hyp_lemmas) < 1:
				continue # gak punya ind lemma, bye

			key = lemma
			value = (ind_hyp_lemmas, hypernym)

			# PRINT cek
			print_check(key, synset, value, f)

			# save
			if key in hypohypes_dict:
				current_value = hypohypes_dict[key]
				if current_value is None:
					continue # kalo udah NOne, abaikan

				if hypernym == current_value[1]:
					continue # kalo hypernym-nya sama, lanjut. (suka ke double)
				
				# ubah jadi None
				hypohypes_dict[key] = None
			else:
				hypohypes_dict[key] = value

	# PRINT smua
	print_dict(hypohypes_dict)

	return hypohypes_dict


def print_check(lemma, synset, value, f):
	key = (lemma, synset)
	print_str = "%s => %s\n" % (key, value)
	f.write(print_str)

def print_dict(hypohypes_dict):
	filename = "seed_superStrict2"
	f = open(filename, 'w')
	f2 = open("seed_superStrict_11", 'w')

	for key in hypohypes_dict:
		value = hypohypes_dict[key]
		if value is None:
			continue
		print_str = "%s => %s\n" % (key, value)
		f.write(print_str)

		# print with 1-1 format
		pretty_print(key, value[0], f2)
	
	f2.close()
	f.close()
