from fixture import *
from config import *

import fetcher
import files
import os

datadir = ".\\..\\..\\data\\"

if __name__ == '__main__':
    files = fetcher.Fetcher(datadir).fetch_files('benchdens', 'e0(2|4|6|8)n100\\.00[\\d]\\.in') + fetcher.Fetcher(datadir).fetch_files('holme', 'n100d0(1|2|3|4)\\.00[\\d]\\.in')   
    print 'Processing: ', files
   
    Fixture().newrun(
            [
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
                    'model.adjacentsNeighbourhood.useCliqueCover': 'false'
                },
                {
                    'strategy.adjacency': 'AdjacentsPartitionLeqColor',
                },
                {
                    'strategy.symmetry': 'None',
                },
                {
                    'strategy.symmetry': 'VerticesNumber',
                },
                {
                    'strategy.symmetry': 'MinimumNodeLabel',
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
                    'strategy.symmetry': 'MinimumNodeLabel',
                    'strategy.colorBound': 'UpperNodesSumLowerSumPartition',
                },
                {
                    'strategy.objective': 'Linear',
                },
                {
                    'strategy.objective': 'Geometric',
                },
            ], 
            
                files = files,
                dirs = [ ".\\..\\data\\" ],
                runid = '20100629MODELS',
            )
    
