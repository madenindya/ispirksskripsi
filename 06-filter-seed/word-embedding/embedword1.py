import gensim
import logging, os
# from gensim.models.keyedvectors import KeyedVectors

logging.basicConfig(format='%(asctime)s : %(levelname)s : %(message)s', level=logging.INFO)
model = gensim.models.Word2Vec.load('/media/riset2014/42C0191EC01919AD/Skripsi-fix-data/model-word-embedding/wordembed-single-lowcase')


def simAll(fname, opath):
    fw = open(opath, 'w')

    # process
    res = []
    with open(fname) as f:
        for line in f:
            rel1 = line.split(" ; ")[0]
            rel2 = rel1[1:-1]
            rel3 = rel2.split(",")
            hyponym = rel3[0].split("_")[0].lower()
            hypernym = rel3[1].split("_")[0].lower()
            simScore = simSeed(model, hyponym, hypernym)
            
            rstr = "%s %s\n" % (line.rstrip(), simScore)
            print rstr
            fw.write(rstr)

    f.close()

def simSeed(s1, s2):
    ar1 = s1.split(" ")
    ar2 = s2.split(" ")

    v1 = 0
    v2 = 0
    for a1 in ar1:
        if (a1 in model.vocab):
            if (v1 == 0):
                v1 = model[a1]
            else:
                v1 += model[a1]
    for a2 in ar2:
        if (a2 in model.vocab):
            if (v2 == 0):
                v2 = model[a2]
            else:
                v2 += model[a2]
    hasil = model.similarity(v1,v2)
    return hasil


### MAIN
ipath = "../../05-pattern-matching/suffix-tree/tmpseedspool/iterasi-1-filter1.seed"
opath = "tmpresult/iterasi-1-we1.seed"

simAll(ipath, opath, model)
