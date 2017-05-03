import gensim
import logging, os
# from gensim.models.keyedvectors import KeyedVectors

logging.basicConfig(format='%(asctime)s : %(levelname)s : %(message)s', level=logging.INFO)
model = gensim.models.Word2Vec.load('/media/riset2014/42C0191EC01919AD/Skripsi-fix-data/model-word-embedding/wordembed-postag-lowcase')


def simAll(fname, opath):
    fw = open(opath, 'w')

    # process
    res = []
    with open(fname) as f:
        for line in f:
            rel1 = line.split(" # ")[0] # pair # vector
            rel2 = rel1[1:-1]
            rel3 = rel2.split(";") # hypo;hype
            hyponym = rel3[0].split("_")[0].lower() # hypo_X
            hypernym = rel3[1].split("_")[0].lower() # hype_X
            simScore = simSeed(hyponym, hypernym)
            
            rstr = "%s %s\n" % (line.rstrip(), simScore)
            print rstr
            fw.write(rstr)

    f.close()

def simSeed(s1, s2):
    ar1 = s1.split(" ")
    ar2 = s2.split(" ")

    w1 = ""
    w2 = ""
    for a1 in ar1:
        if (w1 == ""):
            w1 = a1
        else:
            w1 += "_" + a1
    for a2 in ar2:
        if (w2 == ""):
            w2 = a2
        else:
            w2 += "_" + a2

    hasil = 0
    if (w1 in model.vocab and w2 in model.vocab):
        hasil = model.similarity(w1, w2)
    return hasil


### MAIN
ipath = "tmpresult/iterasi-1-we1.seed"
opath = "tmpresult/iterasi-1-we2.seed"

simAll(ipath, opath)
