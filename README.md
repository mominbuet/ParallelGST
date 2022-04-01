# Privacy Preserving Queries using GST and Reverse Merkle Tree

The repository has two parts: `TreeConstruction` and `PrivacySection`. In the `data` folder we have the variable size data from `50x50` to `1000x1000` size (`nxm`)

##TreeConstruction 
The program for generating the Suffix Trees are in `MainProgram.c` file. Check the call arguments in the pbs file. To comile and run: 
```
make
# to generate tree for 1000x1000 file
/usr/bin/time -v mpirun -np 1 ./MainProgram 1000
```
The c programs have the python ninstructions which will automatically call them to generate the trees.

## PrivacySection

The `SearchClient.java` and `SearchServer.java` are the two programs needed for the privacy-preserving query execution. The garbled circuit version of the code is named as `SearchClientGC.java` and `SearchServerGC.java`. All four programs are in the `Query` package. 

For the ip address, change the corresponding variable in the client codes and run them simultaneously. We use the emp-toolkit as the garbled circuit toolkit which is available in https://github.com/emp-toolkit/emp-sh2pc. Change the path valriable if needed. 

For any queries please email azizmma@cs.umanitoba.ca
