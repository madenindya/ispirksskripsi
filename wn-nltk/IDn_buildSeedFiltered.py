# http://www.nltk.org/howto/wordnet.html
# 1 SYNSET bisa punya > 1 hypernym --> ambil yg lemma-nya sama ajah
# 1 LEMMA  bisa punya > 1 synset -> > 1 hypernym --> ambil yg lemmanya sama ajah

from nltk.corpus import wordnet as wn
from pprint import pprint
import numpy as np
from functools import reduce


# ======== GLOBAL VAR
hypohypes_dict = {}



# ======== FUNCTIONs
def get_filtered_ind_lemmas(lemma, synset):
	# ambil semua lemma bahasa indonesia dari synset
	ind_lemmas = synset.lemma_names('ind')

	# kalo nama-nya sama buang
	# kalo lemma termasuk dalam synsets yg bukan NOUN, buang. reduce disambiguation
	for ind_lemma in ind_lemmas:
		if ind_lemma == lemma or len(wn.synsets(ind_lemma, pos=wn.VERB, lang='ind')) > 0 or len(wn.synsets(ind_lemma, pos=wn.ADJ, lang='ind')) > 0 or len(wn.synsets(ind_lemma, pos=wn.ADV, lang='ind')) > 0:
				ind_lemmas.remove(ind_lemma)

	return ind_lemmas

def filter_lemma_synsets(n, key):
	# FAIL! -> DON'T USE THIS
	sim_score = -1
	hype = None
	detail = ""
	print_str = ""

	# FILTER-4: kalo lemma punya lebih dari 1 synset, ambil yg paling similar ajah
	if n > 0:
		for hype_tup in hypohypes_dict[key]:
			hype_syn = hype_tup[0]
			score = hype_tup[1]

			detail += "   %s -- %s\n" % (hype_syn, score)
			if score > sim_score:
				sim_score = score
				hype = hype_syn
		print_str = "%s => %s\n%s" % (key, hype, detail)
	else:
		print_str = "%s => %s\n" % (key, hypohypes_dict[key])

	return print_str



# ======== MAIN
filename = "tmp"
f = open(filename, 'w')

# get all indonesian lemma noun
ind_lemmas = wn.all_lemma_names(pos=wn.NOUN, lang='ind')

for lemma in ind_lemmas:

	# FILTER-1: ambil lemma yg tidak ambigu.
	# 			kalo lemma termasuk dalam synsets yg bukan NOUN, skip. reduce disambiguation	
	if len(wn.synsets(lemma, pos=wn.VERB, lang='ind')) > 0 or len(wn.synsets(lemma, pos=wn.ADJ, lang='ind')) > 0 or len(wn.synsets(lemma, pos=wn.ADV, lang='ind')) > 0:
		continue

	# retrive all noun synset
	synsets = wn.synsets(lemma, pos=wn.NOUN, lang='ind')

	for synset in synsets:
		
		hypernyms = synset.hypernyms()
		if len(hypernyms) < 1:
			continue # tidak punya hypernym, bye

		# SYNSET punya lebih dari (>) 1 hypernym
		new_value = None
		if len(hypernyms) > 1:

			all_ind_hype_lemmas = []
			for hypernym in hypernyms:
				# FILTER-2: ambil lemmas yg tidak ambigu 
				ind_hypernym_lemmas = get_filtered_ind_lemmas(lemma, hypernym)
				if len(ind_hypernym_lemmas) > 0: # kalo ada lemma indo, masukan ke array tmp untuk di intersect 
					all_ind_hype_lemmas.append(ind_hypernym_lemmas) 
				else: # kalo gada, buang hypernym-nya 
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

			new_value = (ind_hypernym_lemmas, hypernyms) # simpan array of hypernyms


		# kalo new_value isinya
		if new_value is not None:

			# print
			key1 = (lemma, synset)
			print_str = "%s => %s\n" % (key1, new_value)
			f.write(print_str)

			# save
			key = lemma

			# FILTER-4: kalo lemma-hypo udah ada, ambil lemma-hype yang beririsan
			# update new_value
			if key in hypohypes_dict:

				if hypohypes_dict[key] is not None:
					
					new_hype = []
					new_hype = np.intersect1d(hypohypes_dict[key][0], new_value[0])
					# kalo ada yg beririsan, update new_value
					if len(new_hype) > 0:
						# print(new_hype)
						new_synsets = hypohypes_dict[key][1]
						new_synsets.extend(new_value[1])
						new_value = (new_hype, new_synsets)
						print new_value[0]
						# print new_value
					# kalo gada, new_value nulll
					else:
						# print(new_hype)
						new_value = None
				else:
				# kalo udah pernah jadi none, skip
					continue
			
			hypohypes_dict[key] = new_value
			

f.close()
print "50% -- sampai disini SYNSET-SYNSET hyponym-hypernym udah 1-1"



filename = "tmp2"
f = open(filename, 'w')
for key in hypohypes_dict:

	hype_tup = hypohypes_dict[key]

	if hype_tup is not None and len(hype_tup[0]) > 0:
		print_str = "%s => %s # %s\n" % (key, hype_tup[0], hype_tup[1])
		f.write(print_str)

f.close()
print "100% -- sampai disini LEMMA-SYNSET hyponym-hypernym udah 1-1"

print("done, bye")


		

# 			# kalo punya lemma bahasa indonesia baru di cek lebih lanjut
# 			if len(ind_hypernym_lemmas) > 0:

# 				# FILTER-3: jika SYNSET punya lebih dari 1 hypernym --> ambil yg paling similar ajah'
# 				# sim_score = synset.path_similarity(hypernym) # FAIL -> Nilai selalu 0.5
# 				# sim_score = synset.lch_similarity(hypernym) # FAIL -> Nilai selalu 2.944..

				


# 				sim_score = synset.
# 				print sim_score
# 				if sim_score > similarity_score:
# 					hype = hypernym
# 					similarity_score = sim_score
# 					ind_hype_lemmas = ind_hypernym_lemmas


# 				# save
# 				key = lemma
# 				new_value = (hype, similarity_score)

# 				if key in hypohypes_dict:
# 					if new_value in hypohypes_dict[key]:
# 						continue
# 					hypohypes_dict[key].append(new_value)				
# 				else:
# 					hypohypes_dict[key] = [new_value]


# 				# print
# 				key1 = (lemma, synset)
# 				new_value1 = (ind_hype_lemmas, hype)
				
# 				printstr = "%s => %s\n" % (key1, new_value1)
# 				f.write(printstr)




