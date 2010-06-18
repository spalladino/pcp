from fixture import *
from config import *

import fetcher
import files
import os

datadir = ".\\..\\..\\data\\"

if __name__ == '__main__':
    files = fetcher.Fetcher(datadir).fetch_files('benchdens', 'e0(2|4|6|8)n100\\.00[\\d]\\.in') + fetcher.Fetcher(datadir).fetch_files('holme', 'n100d0(1|2|3|4)\\.00[\\d]\\.in')   
    print 'Processing: ', files
   
    newrun([
                {
                    'strategy.partition': 'PaintAtLeastOne',
                },
                {
                    'strategy.partition': 'PaintExactlyOne',
                },
                {
                    'strategy.adjacency': 'AdjacentsLeqColor',
                },
                {
                    'strategy.adjacency': 'AdjacentsLeqOne',
                },
                {
                    'strategy.adjacency': 'AdjacentsNeighbourhood',
                },
                {
                    'strategy.adjacency': 'AdjacentsPartitionLeqColor',
                },
                {
                    'strategy.symmetry': 'None',
                },
                {
                    'strategy.symmetry': 'UseLowerLabelFirst',
                },
                {
                    'strategy.symmetry': 'MinimumNodeLabel',
                },
                {
                    'strategy.colorBound': 'None',
                },
                {
                    'strategy.colorBound': 'UpperNodesSum',
                },
                {
                    'strategy.colorBound': 'UpperNodesSumLowerSum',
                },
                {
                    'strategy.colorBound': 'UpperNodesSumLowerSumPartition',
                },
                {
                    'strategy.objective': 'Linear',
                },
            ], 
            
                files = files,
                dirs = [ datadir ],
            )
    
