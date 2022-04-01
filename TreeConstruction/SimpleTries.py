import copy


class SimpleNode():
    # Left = None#0
    # Right = None#1
    children = None  # for all
    parent = None
    name = ""

    # def __init__(self, name, children, parent):
    #     self.name = name
    #     self.children = children
    #     self.parent = parent

    def __init__(self, name, parent):
        self.name = name
        self.children = dict()
        self.parent = parent

    def to_GraphViz(self, a, level):
        a.append('"%s:%s" [color=red];\n' % (str(self.name), str(level)))
        # super ().to_dot (a)
        if self.children != (None):
            for key in self.children:
                child = self.children[key]
                a.append('"%s:%s" -> "%s:%s" [label="%s"];\n' % (
                    str(self.name), str(level), str(child.name), str(level + 1), str(key)))

                child.to_GraphViz(a, level + 1)

    def remove_end_terms(self):
        children = list(self.children.keys())

        for child in children:
            if child != '0' and child != '1':
                del self.children[child]
            else:
                self.children[child].remove_end_terms()

    def merge(self, subtree):
        # print(subtree)
        if subtree.children!=None:
            if len(subtree.children) > 0:
                child = list(subtree.children.keys())[0]
                if child in self.children:
                    self.children[child].merge(subtree.children[child])
                else:
                    tmpNode = subtree.children[child]
                    self.children[child] = tmpNode
                    tmpNode.parent = self

    def add_tree(self, tree_to_add):
        children = list(self.children.keys())
        for child in children:
            if child != '0' and child != '1':
                del self.children[child]
                tmpNode = copy.copy(tree_to_add)
                if tree_to_add.name not in children:
                    self.children[tree_to_add.name] = tmpNode
                    tmpNode.parent = self
                else:
                    self.children[tree_to_add.name].merge(tmpNode)
            # else:
            #      self.children[child].add_tree(tree_to_add)


class SimpleTrie():
    root = None

    def __init__(self, root_name):
        self.root = SimpleNode(root_name, None)

    def remove_end_terms(self):
        self.root.remove_end_terms()

    def add_tree(self, subtree):

        subchild = list(subtree.root.children.keys())[0]
        tree_to_add = subtree.root.children[subchild]

        node = self.root
        children = list(node.children.keys())
        for child in children:
            if child != '0' and child != '1':
                del node.children[child]
                tmpNode = copy.copy(tree_to_add)
                if tree_to_add.name not in node.children:
                    node.children[tree_to_add.name] = tmpNode
                    tmpNode.parent = node
                else:
                    node.children[tree_to_add.name].merge(tmpNode)
            else:
                node.children[child].add_tree(tree_to_add)

    def from_string(self, sequence):
        node = self.root
        for character in sequence:
            newNode = SimpleNode(character, node)
            node.children[character] = newNode
            node = newNode
        node.children['e' + ''.join(self.root.name.split("_")[0:2])] = SimpleNode('e' + self.root.name, node)
        node.children['e' + ''.join(self.root.name.split("_")[0:2])].children = None

    def from_GraphViz(self, filename):
        with open(filename, 'r') as file:
            relation = dict()
            nodes = []
            for line in file:
                if '}' not in line and '{' not in line:
                    print(line)

    def to_GraphViz(self):
        """ Output the tree in GraphViz .dot format. """
        dot = []
        dot.append('strict digraph G {\n')
        self.root.to_GraphViz(dot, 0)
        dot.append('}\n')
        return ''.join(dot)
