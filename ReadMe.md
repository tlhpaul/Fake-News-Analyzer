CIT 594 Data structures and software design final project, the collaborator is Sam Weinberg (https://www.linkedin.com/in/samuel-weinberg-8a126ab3/)

For our project, Fake News Analyzer, we designed an interface whereby users could ask 10 questions of the media. These questions were made more flexible by the ability for users to select a subset of the media to analyze. Many of our questions relied on statistical analyses as well. 
In order to run our project the attached file must be used at the first prompt after running main. One issue we ran into was that the actual building of the maps for analysis required many boolean arguments. This is because, depending on question and statistical analysis used, summing of values, sparseness of map or measurement on tweet or words in tweet were required. The map building methods could have been shorted but then the maps would have been built at a higher runtime and not with a single loop
Sam and Paul did an even breakdown with Paul producing the multivariate regression and the python pipeline while Sam produced the smaller statical methods. Together, they built the model, view and controller framework. Paul created the majority of the statistical adapter while sam did the majority of the map adapter. Sam added the JFreeChart and the interface. JFreeChart modified but replicated from existing examples since JFreeChart documentation is not free.

