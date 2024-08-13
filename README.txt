    Colorado Computational Pharmacology, University of Colorado School of Medicine  August 10, 2012


ESM is a subgraph matching tool for dependency graphs. It is the Java implementation 
of the exact subgraph matching algorithm we designed for matching dependency graphs. 
The subgraph matching problem (subgraph isomorphism) is NP-complete. We designed a 
simple exact subgraph matching (ESM) algorithm for dependency graphs using a backtracking 
approach. The total worst-case algorithm complexity is O(n^2 * k^n) where n is the number 
of vertices and k is the vertex degree. 

We have demonstrated the successful usage of our algorithm in three biomedical 
information extraction (relation and event) applications: BioNLP 2011 shared tasks on event 
extraction, Protein-Residue association detection and Protein-Protein interaction identification.

This directory contains the ESM tool developed by Haibin Liu <Haibin.Liu@ucdenver.edu>, 
Vlado Keselj <vlado@cs.dal.ca> and Christian Blouin <cblouin@cs.dal.ca>. It is developed 
in Java and is released as open source software to the NLP and text mining research 
communities to be used for research purposes only (see section 8 below for copyright 
information). It can be downloaded via http://esmalgorithm.sourceforge.net. If you make 
any changes, the authors would appreciate it if you can send the details of what you have done. 

Note: The ESM code requires Java version 6 or greater.


1. Files and Folders
---------------------

  README.txt                          this file
  
  esm-1.0.tar.gz	                  the source code, and test cases for the ESM
  


2. Usage
--------

The constructor of ESM takes two graphs as input, one is considered as subgraph 
(smaller) and the other is considered as a graph (bigger).

ESM provides three main public methods for different subgraph matching needs: 

(1) isSubgraphIsomorphism()

Determine if the input subgraph is subsumed by the input graph

(2) isGraphIsomorphism()

Determine if two input graphs are isomorphic to each other, based on the fact that 
if two graphs are subgraph isomorphic to each other, then they are isomorphic to 
each other

(3) getSubgraphMatchingMatches()

Retrieve specific matching results of all possible matchings between the input subgraph 
and the input graph because a subgraph can sometimes match different places of a graph.


The implementation of the underlying exact subgraph matching algorithm is declared 
as private method. See the source code for the detailed implementation.

For more details about our ESM algorithm, the analysis on time complexity, and the 
different applications of the algorithm, see the section "Related Publications" for
the complete list of our ESM-related publications.


Set the MAVEN_OPTS environment variable to provide the JVM enough memory to load 
the BioLemmatizer (this command only needs to be executed once):
  export MAVEN_OPTS="-Xmx1G"
  


3. Usage Examples
---------------------------------------------------------------------------------------------------------------------------

ESMTest.java contains a comprehensive set of unit test cases for each ESM public method.
Please see the test cases for specific usage examples before starting to build your own
subgraph matching applications. Generallly, the following 3 steps are involved:

(1)  Create two graphs 
     a createGraph() method is provided in the ESMTest.java to help with creating dependency 
     graphs directly from dependency representations. The "DirectedGraph" implementation in
     the JUNG library is adopted to facilitate the graph generation and operation. However, 
     separate definitions for graph vertex and edge are provided, along with the ESM, to cater 
     to the specific needs of dependency graphs. The definitions of vertex and edge can be 
     modified or extended according to users' special needs. In addition, the BioLemmatizer 
     is used to generate lemmas for graph node tokens for comparing node contents during the
     graph matching process.

(2)  Create ESM object 
     The ESM constructor takes two graphs as input parameters with the first parameter being 
     the subgraph (smaller) and the second parameter being the graph (bigger).
     
(3)  Gall graph(subgraph) isomorphism methods 
     The three public graph isomorphism-related methods can be then invoked on the ESM object.     



4. Related Publications
------------------------------------

This ESM implementation implements the subgraph matching algorithm proposed and applied in the 
following papers (in descending chronological order):
  
(1) Haibin Liu, Vlado Keselj, Christian Blouin, and Karin Verspoor.
    Subgraph Matching-based Literature Mining for Biomedical Relations and Events.
    In Proceedings of the AAAI 2012 Fall Symposium on Information Retrieval and Knowledge Discovery 
    in Biomedical Text. VA, USA, November 2012. 
 
(2) Ravikumar Komandur, Haibin Liu, Judith Cohn, Michael E. Wall and Karin Verspoor. 
    Literature Mining of Protein-Residue Associations with Graph Rules Learned through Distant Supervision.
    Journal of Biomedical Semantics, January 2012. in press.
 
(3) Ravikumar Komandur, Haibin Liu, Judith Cohn, Michael E. Wall and Karin Verspoor. 
    Pattern Learning Through Distant Supervision for Extraction of Protein-Residue Associations in the Biomedical Literature.
    In Proceedings of the Tenth International Conference on Machine Learning and Applications (ICMLA), 
    Hawaii, USA, December 2011.

(4) Haibin Liu, Ravikumar Komandur, and Karin Verspoor. 
    From Graphs to Events: A Subgraph Matching Approach for Information Extraction from Biomedical Text.
    In Proceedings of BioNLP Shared Task 2011 Workshop, Oregon, USA, June 2011.

(5) Haibin Liu, Vlado Keselj, and Christian Blouin. 
    Biological Event Extraction using Subgraph Matching. 
    Computational Intelligence, November 2010. in press.

(6) Haibin Liu, Christian Blouin, and Vlado Keselj.
    Biological Event Extraction using Subgraph Matching. 
    In Proceedings of the 4th International Symposium on Semantic Mining in Biomedicine (SMBM), 
    Cambridgeshire, UK, October 2010.
 


4. Copyright and License
------------------------------------
The software is released under the New BSD license 
(http://www.opensource.org/licenses/bsd-license.php).

Copyright (c) 2012, Regents of the University of Colorado
 All rights reserved.

 Redistribution and use in source and binary forms, with or without modification, 
 are permitted provided that the following conditions are met:

  * Redistributions of source code must retain the above copyright notice, this 
    list of conditions and the following disclaimer.
   
  * Redistributions in binary form must reproduce the above copyright notice, 
    this list of conditions and the following disclaimer in the documentation 
    and/or other materials provided with the distribution.
   
  * Neither the name of the University of Colorado nor the names of its 
    contributors may be used to endorse or promote products derived from this 
    software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON 
 ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

Any documentation, advertising materials, publications and other materials 
related to redistribution and use must acknowledge that the software was 
developed by Haibin Liu <Haibin.Liu@ucdenver.edu>, Vlado Keselj <vlado@cs.dal.ca> 
and Christian Blouin <cblouin@cs.dal.ca> and must refer to the following publication:

Haibin Liu, Vlado Keselj, and Christian Blouin. Biological Event Extraction using 
Subgraph Matching. Computational Intelligence, November 2010. in press.



5. Incorporated Library
---------------------------------------
This software incorporates the JUNG library (http://jung.sourceforge.net/).

JUNG is a Java-based open-source software library designed to support the modeling, 
analysis, and visualization of data that can be represented as graphs. Its focus is 
on mathematical and algorithmic graph applications pertaining to the fields of social 
network analysis, information visualization, knowledge discovery and data mining.

JUNG library license:

JUNG is licensed and made freely available under the Berkeley Software Distribution (BSD) license.



6. Acknowledgements
------------------------------------
Many thanks to William A Baumgartner Jr for his contribution to the software engineering and 
the release of the tool, and to Professor Lawrence Hunter and other members of the Colorado 
Computational Pharmacology group for providing valuable effort and suggestions related to this work.
