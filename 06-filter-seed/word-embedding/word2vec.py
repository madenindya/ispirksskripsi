# import modules & set up logging
import gensim, logging, os
import numpy as np

logging.basicConfig(format='%(asctime)s : %(levelname)s : %(message)s', level=logging.INFO)

class MySentences(object):
    def __init__(self, dirname):
        self.dirname = dirname
 
    def __iter__(self):
        for fname in os.listdir(self.dirname):
            for line in open(os.path.join(self.dirname, fname)):
                yield line.lower().split() # lowercase-in
 
sentences = MySentences('new') # a memory-friendly iterato
model = gensim.models.Word2Vec(sentences, min_count=1, size=128, window=8)
model.save('first_lower_128')


# model = gensim.models.Word2Vec.load('third_model')
# text = 'eko budidharmaja team doktersehat.com'.split()
# default = [ 0, 0, 0, 0, 0, 0,
#  0, 0, 0, 0, 0, 0,
#  0, 0, 0, 0, 0, 0,
#  0, 0, 0, 0, 0, 0,
#  0, 0, 0, 0, 0, 0,
#  0, 0, 0, 0, 0, 0,
#  0, 0, 0, 0, 0, 0,
#  0, 0, 0, 0, 0, 0,
#  0, 0, 0, 0, 0, 0,
#  0, 0, 0, 0, 0, 0,
#  0, 0, 0, 0, 0, 0,
#  0, 0, 0, 0, 0, 0,
#  0, 0, 0, 0, 0, 0,
#  0, 0, 0, 0, 0, 0,
#  0, 0, 0, 0, 0, 0,
#  0, 0, 0, 0, 0, 0,
#  0, 0, 0, 0, 0, 0]

# default2 = np.array(default)
# numpy_array_text =[]
# for word in text:
# 	if word in model:
# 		numpy_array_text.append(np.array(model[word]))
# 	else:
# 		numpy_array_text.append(default2)

# print ((numpy_array_text))
