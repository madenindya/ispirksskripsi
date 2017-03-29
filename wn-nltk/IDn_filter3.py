from nltk.corpus import wordnet as wn
from nltk.corpus import wordnet_ic
from IDn_util import get_filtered_ind_lemmas
from IDn_util import pretty_print

# ambil berdasarkan similarity
def get_mapping_simmilarity(ind_lemmas):
	hypohypes_dict = {}
	filename = "seed_similarityScore"
	f = open(filename, 'w')
	brown_ic = wordnet_ic.ic('ic-brown.dat')
	for lemma in ind_lemmas:
		# FILTER-1: ambil lemma yg tidak ambigu.
		# 			kalo lemma termasuk dalam synsets yg bukan NOUN, skip. reduce disambiguation
		if len(wn.synsets(lemma, pos=wn.VERB, lang='ind')) > 0 or len(wn.synsets(lemma, pos=wn.ADJ, lang='ind')) > 0 or len(wn.synsets(lemma, pos=wn.ADV, lang='ind')) > 0:
			continue

		# retrive all noun synset
		synsets = wn.synsets(lemma, pos=wn.NOUN, lang='ind')
		for synset in synsets: # synset = hyponym
			hypernyms = synset.hypernyms()
			if len(hypernyms) < 1:
				continue # tidak punya hypernym, bye

			new_value = None
			hype_syns = []
			ind_hype_lemmas_choosen = []
			max_score = -1 

			for hypernym in hypernyms:
				# FILTER: ambil yg punya ind_lemma ajah
				ind_hyp_lemmas = get_filtered_ind_lemmas(lemma, hypernym)
				if len(ind_hyp_lemmas) < 1:
					continue # kalo gak punya ind_lemma, bye!

				# FILTER: ambil synset yg paling similar dengan synset hyponym		
				sim_score = synset.res_similarity(hypernym, brown_ic)
				if sim_score > max_score:
					max_score = sim_score
					hype_syns = [] # reset array
				elif sim_score < max_score:
					continue
				hype_syns.append(hypernym) # kalo ==, bakal: array size > 0

				# update isi synset choosen
				# kalo punya lebih dari 1 hypernym, bakal dibuang jadi ini gak ngaruh
				ind_hype_lemmas_choosen = ind_hyp_lemmas 

			# FILTER: kalo ada >1 synset hypernym yg nilai-nya sama, buang!
			if len(hype_syns) != 1:
				continue
			
			new_value = (ind_hype_lemmas_choosen, hype_syns[0])
			
			# print			
			print_check(lemma, synset, new_value, f)
			
			#save
			key = lemma
			new_value = (ind_hype_lemmas_choosen, hype_syns[0], max_score)
			
			if key in hypohypes_dict:
				# FILTER: jika udah ada, ambil yg similirity-nya terbesar
				old_value = hypohypes_dict[key][0]

				if new_value[2] > old_value[2]:
					hypohypes_dict[key] = [new_value]
				elif new_value[2] == old_value[2]:
					# kalo sama, array di append. ini bakal dibuang dibawah
					hypohypes_dict[key].append(new_value)
			else:
				hypohypes_dict[key] = [new_value]
	f.close()

	# PRINT all
	print_dict(hypohypes_dict)


def print_dict(hypohypes_dict):
	filename = "seed_similarityScore2"
	f = open(filename, 'w')
	f2 = open("seed_similarityScore_11", 'w')

	for key in hypohypes_dict:
		value_list = hypohypes_dict[key]
		
		# FILTER: kalo 1 lemma dengan 2 synset yg score-nya sama, buang!
		if len(value_list) > 1:
			continue

		# entah kenapa ini suka jadi gak punya nilai .-.
		if len(value_list[0][0]) < 1:
			continue

		print_str = "%s => %s\n" % (key, value_list[0])
		f.write(print_str) 

		# print with 1-1 format
		pretty_print(key, value_list[0][0], f2)

	f2.close
	f.close()

def print_check(lemma, synset, new_value, f):
	key = (lemma, synset)
	print_str = "%s => %s\n" % (key, new_value)
	f.write(print_str)
