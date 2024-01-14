# Discussion

## Unit testing TreapMap

Due to the random nature of node priorities, it took a lot of time to develop tests 
for TreapMap. I had to choose an arbitrary number as the seed, write out the list of random numbers 
it would generate, then figure out in what order I should insert and remove nodes to get each 
specific rotation. For example, a seed of 2 would yield <-1154715079, 1260042744, -423279216>.

To design a test where insert() causes a left rotation, I added nodes in the following order: A, B, C.

    A (-1154715079)                 A (-1154715079)
     \                               \
      B (1260042744)    --->          C (-423279216)
       \                             /
        C (-423279216)              B (1260042744)

To design a test where remove() causes a right rotation, I first needed to insert three nodes with no rotations. 
I chose a seed of 0, which yielded priorities of <-1155484576, -723955400, 1033096058>, so I inserted the nodes 
in the following order: A, C, B, and then removed node C by setting its dpriority field (double priority) to 2, 
a number greater than the range of dpriorities that could be randomly assigned.

    A (-1155484576)                 A (-1155484576)             A (-1155484576)
     \                               \                           \
      C (dpriority = 2)    --->       B (1033096058)    --->      B (1033096058)
     /                                 \
    B (1033096058)                      C (dpriority = 2)

Designing a test for removing a leaf was easy, as no rotations were required. In order to isolate the test, 
I just needed to insert the nodes in a way that no rotations happened, then remove a leaf. I used the same seed 
as before (0), and called insert() in the following order: A, C, B. I then called remove() on B, which simply 
cut that node off without any other modifications.

## Benchmarking

For all data sets, AVL tree, BST, and Treap outperform Array. With the smaller files,
hotel_california.txt and federalist01.txt, this difference is not very noticeable. 
However, with much larger files, the difference between O(lgn) and O(n) becomes very
significant, with the binary trees being 30 times faster on moby_dick.txt and 10 times
faster on pride_and_prejudice.txt.

I expected AVL tree to be much faster than BST, especially on the larger data sets. 
However, this is not the case, as the runtimes are very similar on all files. I believe 
this may be due to the fact that as our tree depth increases, the maximum amount of rotations 
required per insertion or removal also increases. Even though each rotation is constant time, 
in the worst case we must perform O(lgn) rotations as we retrace up the tree. It is possible 
that in reality, we are not saving any time on these particular text files. Similarly, Treap
performs no better or worse than BST. Just how BSTs are worst-case O(n), so are Treaps. However, 
with random data sets such as these text files, the runtime ends up somewhere closer to O(lgn).

hotel_california.txt
Benchmark                  Mode  Cnt  Score   Error  Units
JmhRuntimeTest.arrayMap    avgt    2  0.159          ms/op
JmhRuntimeTest.avlTreeMap  avgt    2  0.119          ms/op
JmhRuntimeTest.bstMap      avgt    2  0.120          ms/op
JmhRuntimeTest.treapMap    avgt    2  0.149          ms/op

federalist01.txt
Benchmark                  Mode  Cnt  Score   Error  Units
JmhRuntimeTest.arrayMap    avgt    2  1.572          ms/op
JmhRuntimeTest.avlTreeMap  avgt    2  0.657          ms/op
JmhRuntimeTest.bstMap      avgt    2  0.616          ms/op
JmhRuntimeTest.treapMap    avgt    2  0.801          ms/op

moby_dick.txt
Benchmark                  Mode  Cnt     Score   Error  Units
JmhRuntimeTest.arrayMap    avgt    2  3444.684          ms/op
JmhRuntimeTest.avlTreeMap  avgt    2   108.657          ms/op
JmhRuntimeTest.bstMap      avgt    2   107.546          ms/op
JmhRuntimeTest.treapMap    avgt    2   129.737          ms/op

pride_and_prejudice.txt
Benchmark                  Mode  Cnt    Score   Error  Units
JmhRuntimeTest.arrayMap    avgt    2  615.224          ms/op
JmhRuntimeTest.avlTreeMap  avgt    2   56.653          ms/op
JmhRuntimeTest.bstMap      avgt    2   54.438          ms/op
JmhRuntimeTest.treapMap    avgt    2   66.706          ms/op
