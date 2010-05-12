from fixture import *

if __name__ == '__main__':
    #if not resume():
    newrun([
                {
                    "strategy.coloring": 'DSaturEasyNode',
                    "dsatur.colorAdjPartitions": 'true', 
                }, {
                    "strategy.coloring": 'DSaturEasyNode',
                    "dsatur.colorAdjPartitions": 'false', 
                }, {
                    "strategy.coloring": 'DSaturHardPartition',
                    "dsatur.colorAdjPartitions": 'true', 
                }, {
                    "strategy.coloring": 'DSaturHardPartition',
                    "dsatur.colorAdjPartitions": 'false', 
                }, 

            ], 
            
            files = [ 
                        "rand10.in",
                        "rand11.in",
                        "rand12.in",
                        "rand13.in",
                    ])
