# http://www.nltk.org/howto/wordnet.html
# 1 SYNSET bisa punya > 1 hypernym --> ambil yg lemma-nya sama ajah
# 1 LEMMA  bisa punya > 1 synset -> > 1 hypernym --> ambil yg lemmanya sama ajah

from nltk.corpus import wordnet as wn
from nltk.corpus import wordnet_ic
from IDn_filter1 import get_mapping_superStrict
from IDn_filter2 import get_mapping_sameLemma
from IDn_filter3 import get_mapping_simmilarity

# ======== MAIN
# get all indonesian lemma noun
ind_lemmas = wn.all_lemma_names(pos=wn.NOUN, lang='ind')

get_mapping_superStrict(ind_lemmas)
print("done, bye 1")

get_mapping_sameLemma(ind_lemmas)
print("done, bye 2")

get_mapping_simmilarity(ind_lemmas)
print("done, bye 3")
