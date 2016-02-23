# Task 1

implements max similarity

test and plots 4 grah, keep the best one


# Task 2

compute movies data
* for each movies
  * mean ratings
  * count ratings
  
  
use computed data


# Tast 3

W = 1- #distinct genres /(#profilemovies)


#distinct genres is how many genres the movie has

#profilemovies is how many review the user has


For one movie, if the user has a lots reviews '#distinct genres /(#profilemovies)' will be low and the weight high (close to 1)

The more a user has reviews, the more its profile genres will be taken into account. Quite understandable as well


For one user, if the movie has a lots of genres,  '#distinct genres /(#profilemovies)' will be high and the weight low.

Which means that the more a movie has genres, the less its genre is usefull. Easily understandable


# Task 4

new recommeder class : instead of pick 10, we pick 100 (*10) and pick the 10 most diverstity



distribution of diversity


java impl
https://github.com/ykaragol/checkersmaster/blob/master/CheckersMaster/src/checkers/algorithm/GreedyAlgorithm.java


C is our set
k 10
b 100

distribution :
#user = f ( F1metric ) ou plutot l'inverse ?



1. define BoundedGreedySelection(t, C, k, b)
2. begin
3. C’:= bk cases in C that are most similar to t
4. R := {}
5. for i = 1 to k
6. Sort C’ by Quality(t, c, R) for each c in C’
7. R := R + First(C’)
8. C’:= C’ – First(C’)
9. end
10. return R
11. end


#task 5
look for every set of genres of movies
X => y
! X => Y
compare ratio




  
