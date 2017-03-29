from nltk.corpus import wordnet as wn
import numpy as np
from functools import reduce
from IDn_util import get_filtered_ind_lemmas
from IDn_util import pretty_print

# cuma ambil lemma ind yang sama ajah
def get_mapping_sameLemma(ind_lemmas):
	hypohypes_dict = {}
	filename = "seed_sameLemma"
	f = open(filename, 'w')
	for lemma in ind_lemmas:

		# FILTER-1: ambil lemma yg tidak ambigu.
		# 			kalo lemma termasuk dalam synsets yg bukan NOUN, skip. reduce disambiguation
		if len(wn.synsets(lemma, pos=wn.VERB, lang='ind')) > 0 or len(wn.synsets(lemma, pos=wn.ADJ, lang='ind')) > 0 or len(wn.synsets(lemma, pos=wn.ADV, lang='ind')) > 0:
			continue

		# retrive all noun synset
		synsets = wn.synsets(lemma, pos=wn.NOUN, lang='ind')
		for synset in synsets:
			
			hypernyms = synset.hypernyms()
			# SYNSET tidak punya hypernym, bye
			if len(hypernyms) < 1:
				continue 
			# SYNSET punya lebih dari (>) 1 hypernym
			new_value = None
			if len(hypernyms) > 1:
				all_ind_hype_lemmas = []
				for hypernym in hypernyms:
					# FILTER-2: ambil lemmas yg tidak ambigu 
					ind_hypernym_lemmas = get_filtered_ind_lemmas(lemma, hypernym)
					if len(ind_hypernym_lemmas) > 0: # kalo ada lemma indo, masukan ke array tmp untuk di intersect 
						all_ind_hype_lemmas.append(ind_hypernym_lemmas) 
					else: # kalo gada lemma ind, buang hypernym-nya 
						hypernyms.remove(hypernym)

				# FILTER-3: ambil seluruh lemma yg beririsan
				all_ind_hype_lemmas_reduce = []
				if len(all_ind_hype_lemmas) > 0:
					all_ind_hype_lemmas_reduce = reduce(np.intersect1d, all_ind_hype_lemmas)
				
				# jika hasil filter ada
				if len(all_ind_hype_lemmas_reduce) > 0:
					# print all_ind_hype_lemmas_reduce
					new_value = (all_ind_hype_lemmas_reduce, hypernyms) # simpan (all) array of hypernym
			# SYNSET punya tepat (==) 1 hypernym
			else:
				# FILTER-2: ambil lemmas yg tidak ambigu 
				ind_hypernym_lemmas = get_filtered_ind_lemmas(lemma, hypernyms[0])
				if len(ind_hypernym_lemmas) < 1: # kalo gada lemma indo, skip
						continue
				new_value = (ind_hypernym_lemmas, hypernyms) # simpan array of hypernym

			if new_value is None:
				continue

			# PRINT check 
			print_check(lemma, synset, new_value, f)

			# save
			key = lemma
			# FILTER-4: kalo lemma-hypo udah ada, ambil lemma-hype yang beririsan
			# update new_value
			if key in hypohypes_dict:
				# kalo udah pernah jadi none, skip
				if hypohypes_dict[key] is None:
					continue
				# else
				new_hype = []
				new_hype = np.intersect1d(hypohypes_dict[key][0], new_value[0])
				# kalo ada yg beririsan, update new_value
				if len(new_hype) > 0:
					new_synsets = hypohypes_dict[key][1]
					new_synsets.extend(new_value[1])
					new_value = (new_hype, new_synsets)
				# kalo gada, new_value null
				else:
					new_value = None				
				
			# update new_value ke dict
			hypohypes_dict[key] = new_value
	f.close()

	# PRINT dict
	print_dict(hypohypes_dict)


def print_dict(hypohypes_dict):
	filename = "seed_sameLemma2"
	f = open(filename, 'w')
	f2 = open("seed_sameLemma_11", 'w')

	for key in hypohypes_dict:
		hype_tup = hypohypes_dict[key]
		if hype_tup is not None and len(hype_tup[0]) > 0:
			print_str = "%s => %s\n" % (key, hype_tup)
			f.write(print_str)
	
			# print with 1-1 format
			pretty_print(key, hype_tup[0], f2)

	f2.close()
	f.close()


def print_check(lemma, synset, new_value, f):
	key = (lemma, synset)
	print_str = "%s => %s\n" % (key, new_value)
	f.write(print_str)