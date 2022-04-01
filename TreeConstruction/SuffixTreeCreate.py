from suffix_tree import Tree
from suffix_tree import ukkonen
import sys
from SimpleTries import SimpleTrie, SimpleNode
import pickle
from TreeUtils import OUTPUTDIR, convertSimpleTree
import os, resource


def convertSuffixTreeToTrie(SuffixTree, Trie, child):
    if child in SuffixTree.root.children:
        convertSimpleTree(SuffixTree.root.children[child], Trie.root)


def main():
    start_line = int(sys.argv[1])
    line_count = int(sys.argv[2])
    start_pos = int(sys.argv[3])
    char_count = int(sys.argv[4])
    alphabet = sys.argv[5]
    line_width = int(sys.argv[6])
    filename = sys.argv[7]
    temp_filename = sys.argv[8]  # output file name/rank if used
    # line_width = 6
    # start_line = 2
    # line_count = 2
    # start_pos = 0
    # char_count = 3
    # alphabet = '0'
    add_subtree = False
    if start_pos + char_count < line_width:
        add_subtree = True
        # print("add_subtree " + add_subtree)
    line_width += 1  # for \n
    input = dict()
    input_rest = dict()
    with open(filename, "r") as file:
        file.seek(line_width * (start_line))
        line = file.readline()
        index = 0
        while index < line_count and line != "":
            if len(line) >= char_count:
                line = line.strip()
                if char_count - start_pos < (line_width - 1):
                    input['S' + str(index + start_line) + '_' + str(start_pos) + '_' + str(char_count)] = line[
                                                                                                          start_pos:char_count]
                else:
                    input['S' + str(index + start_line) + '_' + str(start_pos) + '_' + str(char_count)] = line
                if add_subtree:
                    input_rest['S' + str(index + start_line) + '_' + str(start_pos) + '_' + str(char_count)] = line[
                                                                                                               start_pos + char_count:]
                index += 1
                # print(index)
            line = file.readline()
    # print(input)
    suffixTries = []
    suffixTrie = None
    name = temp_filename + "S_" + str(start_line) + "_" + str(line_count) + "_" + str(start_pos) + "_" + str(
        char_count) + "_" + alphabet
    tmpTrieRepresentation = SimpleTrie(name)
    # if add_subtree:
    #     for key in input:
    #         tmp=Tree({key:input[key]}, builder=ukkonen.Builder)
    #         tmpTrie = SimpleTrie(key)
    #         convertSuffixTreeToTrie(tmp, tmpTrie, alphabet)
    #         rest_sequence = SimpleTrie(key)
    #         rest_sequence.from_string(input_rest[key])
    #         tmpTrie.add_tree(rest_sequence)
    #         suffixTries.append(tmpTrie)
    # else:
    suffixTrie = Tree(input, builder=ukkonen.Builder)
    convertSuffixTreeToTrie(suffixTrie, tmpTrieRepresentation, alphabet)
    if add_subtree:
        # suffixTrie = suffixTries[0]
        for key in input_rest:
            rest_sequence = SimpleTrie(key)
            rest_sequence.from_string(input_rest[key])
            tmpTrieRepresentation.add_tree(rest_sequence)

    # trieRepresentations = []
    outputfile = os.path.join(OUTPUTDIR, name)

    with open(outputfile, 'wb') as f:
        pickle.dump(tmpTrieRepresentation, f)
    return
    # dot = tmpTrieRepresentation.to_GraphViz()
    # with open('suffix_tree_tt.dot', 'w') as tmp:
    #     tmp.write(dot)


# with open(name0, 'wb') as f:
#     pickle.dump(trieRepresentation0, f)
# with open(name1, 'wb') as f:
#     pickle.dump(trieRepresentation1, f)

# with open(name1, 'rb') as f:
#     trieRepresentation0 = pickle.load(f)
# print(trieRepresentation0)


if __name__ == "__main__":
    ##increasing recursion limit
    max_rec = 0x1000000
    resource.setrlimit(resource.RLIMIT_STACK, [0x100 * max_rec, resource.RLIM_INFINITY])
    sys.setrecursionlimit(max_rec)
    main()
