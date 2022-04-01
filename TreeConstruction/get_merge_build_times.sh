#!/bin/bash
rm -rf tmp2
mkdir tmp2


python3 SuffixTreeCreateTime.py 0 125 0 125 0 1000 data/1000.txt run4_R0
python3 SuffixTreeCreateTime.py 0 125 0 125 1 1000 data/1000.txt run4_R0
python3 SuffixTreeCreateTime.py 375 125 0 125 0 1000 data/1000.txt run4_R3
python3 SuffixTreeCreateTime.py 375 125 0 125 1 1000 data/1000.txt run4_R3
python3 SuffixTreeCreateTime.py 125 125 0 125 0 1000 data/1000.txt run4_R1
python3 SuffixTreeCreateTime.py 125 125 0 125 1 1000 data/1000.txt run4_R1
python3 SuffixTreeCreateTime.py 250 125 0 125 0 1000 data/1000.txt run4_R2
python3 SuffixTreeCreateTime.py 250 125 0 125 1 1000 data/1000.txt run4_R2
python3 SuffixTreeMergeTime.py run4 0 merge_master
python3 SuffixTreeMergeTime.py run4 1 merge_master