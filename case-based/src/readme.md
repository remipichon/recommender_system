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

how to evaluate diversity
=> CaseSimiliraty between recommendation dans l'ordre de la liste triée de sortie
graph : similarity to user = f ( diversity with all others element )


http://hci.epfl.ch/teaching/advanced-hci/slides/Diversity%20vs%20Accuracy.pdf
similarity = f ( size )


java impl
https://github.com/ykaragol/checkersmaster/blob/master/CheckersMaster/src/checkers/algorithm/GreedyAlgorithm.java


  
