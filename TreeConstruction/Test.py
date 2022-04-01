import sys

start_line = int(sys.argv[1])
line_count = int(sys.argv[2])
start_pos = int(sys.argv[3])
char_count = int(sys.argv[4])
alphabet = sys.argv[5]
line_width = int(sys.argv[6])
filename = sys.argv[7]
temp_filename = sys.argv[8]
# line_width = 6
# start_line = 2
# line_count = 2
# start_pos = 0
# char_count = 3
# alphabet = '0'
add_subtree = False
if start_pos + char_count < line_width:
    add_subtree = True
line_width += 1  # for \n
input = dict()
input_rest = dict()
print("file reading")
with open("/import/helium-share/csgrad/azizmma/ParallelProject/"+filename, "r") as file:
    file.seek(line_width * (start_line))
    line = file.readline()
    index = 0
    while index < line_count:
        if len(line) >= char_count:
            line = line.strip()
            input['S' + str(index + start_line) + '_' + str(start_pos) + '_' + str(char_count)] = line[
                                                                                                  start_pos:char_count]
            if add_subtree:
                input_rest['S' + str(index + start_line) + '_' + str(start_pos) + '_' + str(char_count)] = line[
                                                                                                           start_pos + char_count:]
            index += 1
        line = file.readline()
# print(input)
suffixTries = []
suffixTrie = None
name = "S_" + str(start_line) + "_" + str(line_count) + "_" + str(start_pos) + "_" + str(
    char_count) + "_" + alphabet
tmpTrieRepresentation = SimpleTrie(name)

print("file read done")
