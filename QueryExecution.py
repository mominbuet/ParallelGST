from suffix_tree import Tree
import datetime, pickle, resource, sys, os
from suffix_tree import ukkonen
from suffix_tree import ukkonen_gusfield
import numpy as np

BUILDERS = [
    ['ukkonen', ukkonen.Builder],
    ['gusfield', ukkonen_gusfield.Builder]
]


def findPos(suffixTrie, q, tmp_length, original_start):
    _, pos = (suffixTrie.findPosition(q))
    if pos == None:
        return False
    for ind, pth in pos:
        if pth.start == original_start and pth.end == original_start + tmp_length:
            return True
        # else:
        #     res = False

    return False


def main():
    filename = '500.txt'

    input = dict()
    index = 1
    suffixTrie = None
    if not os.path.isfile('queryTree_' + filename):
        with open(os.path.join('data', filename), "r") as file:
            line = file.readline()
            while line != "":
                input['S' + str(index)] = line
                index += 1
                line = file.readline()
        a = datetime.datetime.now()
        suffixTrie = Tree(input, builder=ukkonen.Builder)
        b = datetime.datetime.now()
        print("Tree building time :" + str(b - a))
        with open('queryTree_' + filename, 'wb') as f:
            pickle.dump(suffixTrie, f)
    else:
        with open('queryTree_' + filename, 'rb') as f:
            suffixTrie = pickle.load(f)

    query_length = [100, 200, 300, 400, 500, 1000]

    for query_size in query_length:
        print(query_size)
        threshold = (query_size * 3) // 4
        np.random.seed(0);query = (np.random.randint(2, size=query_size))
        q = ''.join(str(x) for x in query)
        # print(q)
        a = datetime.datetime.now()
        res = (suffixTrie.find(q))
        b = datetime.datetime.now()
        # print(str(res))
        print("Exact Q Execution time :" + str(b - a))
        res = False
        a = datetime.datetime.now()
        tmp_length = query_size
        while not res and tmp_length > 0:
            tmp_length -= 1
            res = findPos(suffixTrie, q[-tmp_length:], tmp_length, query_size - tmp_length) and findPos(suffixTrie,
                                                                                                        q[:tmp_length],
                                                                                                        tmp_length, 0)

        b = datetime.datetime.now()
        print("Exact SetMaximal Q Execution time :" + str(b - a))

        res = False
        a = datetime.datetime.now()
        while not res and tmp_length > threshold:
            tmp_length -= 1
            res = findPos(suffixTrie, q[-tmp_length:], tmp_length, query_size - tmp_length) and findPos(suffixTrie,
                                                                                                        q[:tmp_length],
                                                                                                        tmp_length, 0)

        b = datetime.datetime.now()
        print("Threshold Q Execution time :" + str(b - a))

        res = False
        a = datetime.datetime.now()
        tmp_length = query_size
        while not res and tmp_length > 0:
            tmp_length -= 1

            res = (suffixTrie.find(q[-tmp_length:])) and (suffixTrie.find(q[-tmp_length:]))
        b = datetime.datetime.now()
        print("PosInvariant SetMaximal Q Execution time :" + str(b - a))
        res=False


if __name__ == "__main__":
    ##increasing recursion limit
    max_rec = 0x1000000
    resource.setrlimit(resource.RLIMIT_STACK, [0x100 * max_rec, resource.RLIM_INFINITY])
    sys.setrecursionlimit(max_rec)
    main()
