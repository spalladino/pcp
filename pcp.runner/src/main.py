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
#                {
#                    "dsatur.colorAdjPartitions": 'true',
#                    "dsatur.partition.weight.size": '10000',
#                    "dsatur.partition.weight.colorCount": '100',
#                    "dsatur.partition.weight.uncolored": '1', 
#                }, 
#                {
#                    "dsatur.colorAdjPartitions": 'true',
#                    "dsatur.partition.weight.size": '10000',
#                    "dsatur.partition.weight.colorCount": '1',
#                    "dsatur.partition.weight.uncolored": '100', 
#                }, 
#                {
#                    "dsatur.colorAdjPartitions": 'true',
#                    "dsatur.partition.weight.size": '100',
#                    "dsatur.partition.weight.colorCount": '10000',
#                    "dsatur.partition.weight.uncolored": '1', 
#                }, 
#                {
#                    "dsatur.colorAdjPartitions": 'true',
#                    "dsatur.partition.weight.size": '1',
#                    "dsatur.partition.weight.colorCount": '10000',
#                    "dsatur.partition.weight.uncolored": '100', 
#                }, 
#                {
#                    "dsatur.colorAdjPartitions": 'true',
#                    "dsatur.partition.weight.size": '100',
#                    "dsatur.partition.weight.colorCount": '1',
#                    "dsatur.partition.weight.uncolored": '10000', 
#                }, 
#                {
#                    "dsatur.colorAdjPartitions": 'true',
#                    "dsatur.partition.weight.size": '1',
#                    "dsatur.partition.weight.colorCount": '100',
#                    "dsatur.partition.weight.uncolored": '10000', 
#                }, 
#                
#                {
#                    "dsatur.colorAdjPartitions": 'false',
#                    "dsatur.partition.weight.size": '10000',
#                    "dsatur.partition.weight.colorCount": '100',
#                    "dsatur.partition.weight.uncolored": '1', 
#                }, 
#                {
#                    "dsatur.colorAdjPartitions": 'false',
#                    "dsatur.partition.weight.size": '10000',
#                    "dsatur.partition.weight.colorCount": '1',
#                    "dsatur.partition.weight.uncolored": '100', 
#                }, 
#                {
#                    "dsatur.colorAdjPartitions": 'false',
#                    "dsatur.partition.weight.size": '100',
#                    "dsatur.partition.weight.colorCount": '10000',
#                    "dsatur.partition.weight.uncolored": '1', 
#                }, 
#                {
#                    "dsatur.colorAdjPartitions": 'false',
#                    "dsatur.partition.weight.size": '1',
#                    "dsatur.partition.weight.colorCount": '10000',
#                    "dsatur.partition.weight.uncolored": '100', 
#                }, 
#                {
#                    "dsatur.colorAdjPartitions": 'false',
#                    "dsatur.partition.weight.size": '100',
#                    "dsatur.partition.weight.colorCount": '1',
#                    "dsatur.partition.weight.uncolored": '10000', 
#                }, 
#                {
#                    "dsatur.colorAdjPartitions": 'false',
#                    "dsatur.partition.weight.size": '1',
#                    "dsatur.partition.weight.colorCount": '100',
#                    "dsatur.partition.weight.uncolored": '10000', 
#                },

            ], 
            
            files = [ 
                        "rand100.in",
                        "rand120.in",
                        "rand140.in",
                        "rand160.in",
                        "rand180.in",
                        "rand200.in",
                    ])


#                {
#                    "strategy.coloring": 'DSaturEasyNode',
#                    "dsatur.colorAdjPartitions": 'true', 
#                }, {
#                    "strategy.coloring": 'DSaturEasyNode',
#                    "dsatur.colorAdjPartitions": 'false', 
#                }, {
#                    "strategy.coloring": 'DSaturHardPartition',
#                    "dsatur.colorAdjPartitions": 'true', 
#                }, {
#                    "strategy.coloring": 'DSaturHardPartition',
#                    "dsatur.colorAdjPartitions": 'false', 
#                }, 

