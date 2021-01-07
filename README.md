Algorithm to compute the average ammount of open spaces needed in an n * n square for percolation. 

How much of cable should be made of conductive material to insure current passes through? How much of a rain jacket material can be made of rain non resistant stuff? to find out we need to understand as a matter of statistical observation, what is the minimum ration or random open/conductive spaces for an area to percolate? 
This very important scientific question can only be reliable answered through a computer simulation(as of yet no math proof) so it makes for a great coding exersise. 

Percolation occurs when a square in the bottom row is connected through neighboring open squares to the top row. so below image percolates:
x = closed;  0 = open;  
x x x x 0  
x x 0 0 0  
x x 0 x x  
x 0 0 x x  
x 0 x x x  

While this one does not:  
x x x x 0  
x x x x 0  
x x 0 x x  
x 0 0 x x  
x 0 x x x  

To figure out the ratio of open to closed squares needed for percolation, we simply need to run sufficianly large ammount of experiments (at least thousands) on a sufficiently large squares(at least thousands). for each of the experiments we need to open a random square, and check if it percolates. Once the system percolates, we divide open squares by the total number of squares to get our ratio. 

Brute force aproach of recursively checking: for every square in top row, does it have open neighbors that ultimetly connect it to bottom row would take hundeds of years on the super computer. 

This implimentation uses [Union Find data structure](https://en.wikipedia.org/wiki/Disjoint-set_data_structure#:~:text=In%20computer%20science%2C%20a%20disjoint,a%20set%20into%20disjoint%20subsets.) Specificly weighted quick union variant connect and keep track of percolating branches. my first aproach (PercolationOld.java) assigned arbitrarrily high weight to the squares representing top row so that these squares would always be at the top of the tree. This way for every bottom row, I could quickly check if its root is a square at the top row. While the individual checks were very very fast O(n) = 32(max tree height), to check if the whole system percolates we still need O(n) = n^2 time.(every bottom square for every top square) And this check needs to be performed after every opened square.  
This is still way to slow for this computer to be done with calculation this year, so I figured out a way to create two virtual nodes representing one node thats connected to all top squares, and one node thats connected to all the bottom squares. 
    v1
x x x x 0
x x x x 0
x x 0 x x
x 0 0 x x
x 0 x x x
    v2
That way, to check if system percolates we can simply check whether v1 is connected to v2 with O(n)=32. This technique allowed us to run our required number of samples, insuring 
confidence intervals way above 95% (see PercolationStats.java) with diviation in 0.01 range.  

This aproach does introduce backwash: once the percolation occurs, every node thats connected to the bottom row will compute as full(connected to top row) but this can be solved by implmenting another weighted union find with a single virtual node at the top, so that only if elements are 'full' in both of these UF classes, do we mark them as 'full'. Although this doubles out computational and memory cost, the end result is still 2N memory usage and constant worst case percolates(), and open() and find() calculations.   
