import fetcher

from config import datadir
from common import create_runs
from common import update_copy



def files():
    return fetcher.Fetcher(datadir).fetch_files('benchdens', 'e0(2|4|6|8)n90\\.00(0|1|2)\\.in') + \
        fetcher.Fetcher(datadir).fetch_files('holme', 'n100d0(1|2|3|4)\\.00(0|1|2)\\.in')


baseprops = {
             'cuts.enabled': 'false',
             'cuts.iterations.root.max': '100',
             'cuts.everynodes':'1',
             'cuts.maxdepth':'0',
             'cuts.local':'true',
             'cuts.onlyonup':'true',
                          
             'solver.maxTime': '600',
             'solver.kind': 'CplexBranchAndBound',
             'branch.selection': '1',
             'branch.direction': '1',
             'branch.prios.enabled': 'false',
             'branch.prios.psize': '0',
             'branch.prios.psadjacent': '10',
             'branch.prios.colorindex': '-1',
             'branch.dynamic.dsatur.nodelb': '0.7',
             'branch.dynamic.fractional': 'false',
             'branch.dynamic.dsatur': 'false',
             'branch.singlevar': 'false',
             'branch.boundws': 'true',
             
             'solver.useCutCallback': 'false',
             'solver.useHeuristicCallback': 'false',
             'solver.useBranchingCallback': 'false',
             
             'primal.enabled': 'false',
             'primal.onlyonup': 'true',
             'branch.enabled': 'false',
             
            'clique.colorsAsc': 'false',
            'clique.backtrackBrokenIneqs': 'false',
            'clique.backtrackLastCandidate': 'false',
            'blockColor.usePool': 'false',
            'cuts.iset.usePathsAlgorithm': 'true',
            'cuts.iset.useBreakingSymmetry': 'false',
            
            'solver.mipEmphasis': '0',
            'solver.probing': '0',
            'cuts.minCliques': '100',
            
            'pruning.enabled': 'false',
            'pruning.minset': '5',
            'pruning.remaining': '10',
            'pruning.frac': '1.0',

            'clique.enabled': 'true',
            'path.enabled': 'true',
            'gprime.path.enabled': 'true',
            'blockColor.enabled': 'true',
            'solver.useCplexCuttingPlanes': 'false',
            'solver.useCplexPrimalHeuristic': 'true',
              }

modelruns = create_runs(baseprops, [{
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
            }])