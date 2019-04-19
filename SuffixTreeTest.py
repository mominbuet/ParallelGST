from suffix_trees_mc import STree

from suffix_tree import Tree
from suffix_tree.node import Internal, Leaf
from suffix_tree import ukkonen
from suffix_tree import ukkonen_gusfield
from SimpleTries import SimpleTrie, SimpleNode
from TreeUtils import merge_trie_vertical, convertSimpleTree
from suffix_tree.util import Path, UniqueEndChar

BUILDERS = [
    ['ukkonen', ukkonen.Builder],
    ['gusfield', ukkonen_gusfield.Builder]
]


def print_tree(sTreeRoot):
    for links in sTreeRoot.transition_links:
        # for link in links:
        print(links[0])
        if len(links[0].transition_links) > 0:
            print_tree(links[0])


child_list = ['0', '1']

#
# def convertSimpleTreeFromDot(dot_file):
#     with open(dot_file, 'r') as file:
#         relation = dict()
#         nodes = []
#         for line in file:
#             if ['{', '}'] not in line:
#                 if '->' in line:
#                     parts = line.split('->')
#                     relation[parts[0].strip()] = get_last_char(parts[1].split(' ')[0])
#
#                 else:
#                     node_label = line.split(" ")[0]
#                     node_label = get_last_char(node_label)
#                     node = SimpleNode(node_label, dict(), None)
#                     nodes.append(node)

#
# def merge_tree_vertical(tree1, tree2, merged_tree):
#     tree1_child = None
#     tree2_child = None
#     # if not merged_tree.parent == None:
#     if not tree1.is_leaf():
#         tree1_child = tree1.children  # key=0/1/$
#     else:
#         tree1_child = tree1.__str__().split('$')[0].strip()[-1:]
#         merged_tree.children[tree1_child] = SimpleNode(tree1.__str__().split('$')[1].strip(), merged_tree)
#     if not tree2.is_leaf():
#         tree2_child = tree2.children
#     else:
#         tree2_child = tree2.__str__().split('$')[0].strip()[-1:]
#         merged_tree.children[tree2_child] = SimpleNode(tree2.__str__().split('$')[1].strip(), merged_tree)
#
#     # if ('0' in tree1_child and '0' in tree2_child):
#     for child in child_list:
#         copied = None
#         if child in tree1_child and child in tree2_child:
#             copied = SimpleNode(child, merged_tree)
#             merged_tree.children[child] = copied
#             # merge_tree_vertical(tree1_child[child], tree2_child[child], copied)
#             if (isinstance(tree1_child, dict) and isinstance(tree2_child, dict)):
#                 merge_tree_vertical(tree1_child[child], tree2_child[child], copied)
#             else:
#                 if isinstance(tree1_child, dict):
#                     # copied = SimpleNode(child, dict(), merged_tree)
#                     convertSimpleTree(tree1_child[child], copied)
#                 else:
#                     convertSimpleTree(tree2_child[child], copied)
#
#         elif child not in tree1_child and child in tree2_child:
#             # copied = copy.deepcopy(tree2_child[child])
#             copied = SimpleNode(child, merged_tree)
#             if isinstance(tree2_child, dict):
#                 convertSimpleTree(tree2_child[child], copied)
#             else:
#                 convertSimpleTree(tree2_child, copied)
#             # merged_tree[child] = copied
#         elif child in tree1_child and child not in tree2_child:
#             copied = SimpleNode(child, merged_tree)
#             if isinstance(tree1_child, dict):
#                 convertSimpleTree(tree1_child[child], copied)
#             else:
#                 convertSimpleTree(tree1_child, copied)
#             # merged_tree[child] = copied
#         else:
#             return


#
# # Suffix-Tree example.
# # st = STree.STree("abcdefghab")
# # print(st.find("abc"))  # 0
# # print(st.find_all("ab"))  # [0, 8]
#
# # Generalized Suffix-Tree example.
# # a = ["nonsense", "sense", "offense"]
# a = "nonsense"
# st = STree.STree(a)
# # print(st.find("sen"))
# #
# print_tree(st.root)
# # print(st.lcs())  # "abc"
tree1 = Tree({'S_0_3': '010'}, builder=ukkonen.Builder)
# tree2 = Tree({'S21': '111', 'S31': '110'}, builder=ukkonen.Builder)
tree2 = Tree({'S_4_3': '001'}, builder=ukkonen.Builder)

testTree10 = SimpleTrie("S_4_3")
convertSimpleTree(tree2.root.children['0'], testTree10.root)

testTree00 = SimpleTrie("S_0_3")
convertSimpleTree(tree1.root.children['0'], testTree00.root)
rest_sequence = SimpleTrie("S_0_3")
rest_sequence.from_string('101')
testTree00.add_tree(rest_sequence)


testTree00_10 = SimpleTrie("S_0")
# merge_trie_horizontal(testTree00.root, testTree10.root, testTree00_10.root)
merge_trie_vertical(testTree00.root, testTree10.root, testTree00_10.root)

# merged_tree = SimpleTree("R(0+1)1")
# merge_tree_vertical(tree1.root.children['0'], tree2.root.children['0'], merged_tree.root)
#
# dot = testTree00_10.to_GraphViz()
# with open('suffix_tree.dot', 'w') as tmp:
#     tmp.write(dot)
#
# dot = testTree10.to_GraphViz()
# with open('suffix_tree2.dot', 'w') as tmp:
#     tmp.write(dot)
dot = testTree00.to_GraphViz()
with open('suffix_tree2.dot', 'w') as tmp:
    tmp.write(dot)

# print(tree.find('sense'))
# tree_file = SimpleTree("file").from_GraphViz('suffix_tree1.dot')
