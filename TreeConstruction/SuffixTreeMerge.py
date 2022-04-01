import sys
from SimpleTries import SimpleTrie
import pickle, os, re
from TreeUtils import merge_trie_vertical, OUTPUTDIR
import resource


# def merge_trie_vertical(tree1, tree2, merged_node):
#     for child in tree1.children:
#         if not isinstance(child, UniqueEndChar):
#             newNode = SimpleNode(child, merged_node)
#             if 'e' not in child:
#                 if child in tree2.children:
#                     merge_trie_vertical(tree1.children[child], tree2.children[child], newNode)
#                 else:
#                     newNode = copy.deepcopy(tree1.children[child])
#                 newNode.parent = merged_node
#             merged_node.children[child] = newNode
#
#     for child in tree2.children:
#         if not isinstance(child, UniqueEndChar):
#             if 'end' not in child:
#                 if child not in tree1.children:  # and 'end' not in child:
#                     childNode = SimpleNode(child, merged_node)
#                     childNode.children = tree2.children[child].children
#                     # newNode = copy.deepcopy(tree2.children[child])
#                     # newNode.parent = merged_node
#                     merged_node.children[child] = childNode
#             else:
#                 merged_node.children[child] = tree2.children[child]
#                 merged_node.children[child].parent = merged_node

def main():
    run_name = sys.argv[1]

    # merge_type = int(sys.argv[3])  # 0==vertical,1=horizontal
    # output_file = sys.argv[4]
    # file1 = 'S_0_2_0_3_0'
    # file2 = 'S_2_2_0_3_0'
    character = sys.argv[2]  # 0==vertical,1=horizontal
    output_file = sys.argv[3]
    files = [f for f in os.listdir(OUTPUTDIR) if re.match(run_name + r'\_.*\_'+character, f)]
    if len(files) == 0:
        print("No files found")
        return
    file1 = files[0]
    with open(os.path.join(OUTPUTDIR, file1), 'rb') as f:
        trieRepresentation0 = pickle.load(f)
    for i in range(1, len(files)):
        file2 = files[i]

        with open(os.path.join(OUTPUTDIR, file2), 'rb') as f:
            trieRepresentation1 = pickle.load(f)
        merge_trie = SimpleTrie(file1 + '-' + file2)
        merge_trie_vertical(trieRepresentation0.root, trieRepresentation1.root, merge_trie.root)
        trieRepresentation0 = merge_trie

        # file1 = output_file
    # output_file = "merged_" + run_name  # file1 + '-' + file2
    with open(os.path.join(OUTPUTDIR, output_file+"_"+character), 'wb') as f:
        pickle.dump(trieRepresentation0, f)
    # dot = trieRepresentation0.to_GraphViz()
    # with open('suffix_tree_tt.dot', 'w') as tmp:
    #     tmp.write(dot)


if __name__ == "__main__":
    ##increasing recursion limit
    max_rec = 0x100000
    resource.setrlimit(resource.RLIMIT_STACK, [0x100 * max_rec, resource.RLIM_INFINITY])
    sys.setrecursionlimit(max_rec)

    main()
