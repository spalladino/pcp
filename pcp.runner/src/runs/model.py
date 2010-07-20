from config import datadir

import fetcher


def files():
    return fetcher.Fetcher(datadir).fetch_files('benchdens', 'e0(2|4|6|8)n100\\.00[\\d]\\.in') + \
        fetcher.Fetcher(datadir).fetch_files('holme', 'n100d0(1|2|3|4)\\.00[\\d]\\.in')



modelruns = [{
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
            }]