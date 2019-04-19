from suffix_tree import ukkonen
from suffix_tree import ukkonen_gusfield
from SimpleTries import SimpleTrie, SimpleNode
import copy
from suffix_tree.util import UniqueEndChar

BUILDERS = [
    ['ukkonen', ukkonen.Builder],
    ['gusfield', ukkonen_gusfield.Builder]
]
OUTPUTDIR = 'tmp'

def get_last_char(node_label):
    if '$' in node_label:
        return node_label.split('$')[0].strip()[-1:]
    return node_label


def handleLeafNodes(treeNode, simpleTreeNode):
    last_char = get_last_char(treeNode.__str__())
    nextNode = SimpleNode(last_char, simpleTreeNode)

    endNode = SimpleNode('e' + treeNode.getInfo(), nextNode)
    nextNode.children['e' + treeNode.getInfo()] = endNode
    return nextNode


def convertSimpleTree(treeNode, simpleTreeNode):
    # if not isinstance(treeNode, str):

    if treeNode.is_internal():
        # node = SimpleNode(treeNode.__str__(), dict(), simpleTreeNode)
        for children in treeNode.children:

            #
            newNode = None
            if isinstance(children, UniqueEndChar):
                newNode = SimpleNode('e' + (treeNode.children[children].getInfo()), simpleTreeNode)

            else:
                newNode = SimpleNode(children, simpleTreeNode)
                if treeNode.children[children].is_leaf():
                    if len(treeNode.children[children].__str__().split("$")[0].strip().split(" ")) > int(
                            treeNode.children[children].__str__().split(":")[1]):
                        name = get_last_char(treeNode.children[children].__str__())
                        newNode.children[name] = handleLeafNodes(treeNode.children[children], simpleTreeNode)
                    else:
                        newNode.name = 'e' + treeNode.children[children].getInfo()
                        # simpleTreeNode.children[get_last_char(children)] = treeNode.__str__().split('$')[1].strip()
                else:
                    # nextNode = SimpleNode(children, simpleTreeNode)
                    # simpleTreeNode.children[children] = nextNode
                    convertSimpleTree(treeNode.children[children], newNode)
            simpleTreeNode.children[children] = newNode

    else:
        simpleTreeNode.children[get_last_char(treeNode.__str__())] = handleLeafNodes(treeNode, simpleTreeNode)
    # last_char = get_last_char(treeNode.__str__())
    # nextNode = SimpleNode(last_char, simpleTreeNode)
    # simpleTreeNode.children[last_char] = nextNode
    # endNode = SimpleNode(treeNode.getInfo(), nextNode)
    # nextNode.children['end'] = endNode

    # else:
    #     nextNode = SimpleNode(treeNode, simpleTreeNode)
    #     simpleTreeNode.children[treeNode] = nextNode


# def merge_trie_horizontal(tree1, tree2, merged_node):

def merge_trie_vertical(tree1, tree2, merged_node):
    for child in tree1.children:
        if not isinstance(child, UniqueEndChar):
            newNode = SimpleNode(child, merged_node)
            if 'e' not in child:
                if child in tree2.children:
                    merge_trie_vertical(tree1.children[child], tree2.children[child], newNode)
                else:
                    newNode = copy.copy(tree1.children[child])
                newNode.parent = merged_node
                merged_node.children[child] = newNode
            else:
                merged_node.children[child] = newNode
            # merged_node.children[child].parent = merged_node

    for child in tree2.children:
        if not isinstance(child, UniqueEndChar):
            if 'e' not in child:
                if child not in tree1.children:  # and 'end' not in child:
                    childNode = SimpleNode(child, merged_node)
                    childNode.children = tree2.children[child].children
                    # newNode = copy.deepcopy(tree2.children[child])
                    # newNode.parent = merged_node
                    merged_node.children[child] = childNode
            else:
                merged_node.children[child] = tree2.children[child]
                merged_node.children[child].parent = merged_node
