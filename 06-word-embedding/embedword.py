import gensim
import logging, os
# from gensim.models.keyedvectors import KeyedVectors

logging.basicConfig(format='%(asctime)s : %(levelname)s : %(message)s', level=logging.INFO)

def simAll(fname, opath, model):
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

def simSeed(model, s1, s2):
    ar1 = s1.split(" ")
    ar2 = s2.split(" ")

    count = 0
    suma = 0.0
    for a1 in ar1:
        for a2 in ar2:
            if (a1 in model.vocab and a2 in model.vocab):
                count += 1
                sim = model.similarity(a1, a2) 
                suma += sim

    if (count > 0):
        return (suma / count)
    return 0

ipath = "pattern1_4"
opath = "pattern1_4_embed"
model = gensim.models.Word2Vec.load('/media/riset2014/42C0191EC01919AD/Skripsi-fix-data/model-word-embedding/wordembed-single-lowcase')

simAll(ipath, opath, model)
