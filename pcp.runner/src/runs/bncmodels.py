import fetcher

from config import datadir
from common import create_runs
from common import update_copy


def files_lowdens():
    return fetcher.Fetcher(datadir).fetch_files('benchdens', 'e0(2|4)n((90))\\.00(0|1|2)\\.in')

def files_highdens():
    return fetcher.Fetcher(datadir).fetch_files('benchdens', 'e0(6|8)n((90))\\.00(0|1|2)\\.in')

baseprops = {
             'cuts.enabled': 'true',
             'cuts.iterations.root.max': '100',
             'cuts.everynodes':'1',
             'cuts.maxdepth':'0',
             'cuts.local':'true',
             'cuts.onlyonup':'true',
                          
             'solver.maxTime': '5400',
             'solver.kind': 'PcpBranchAndCut',
             'branch.selection': '1',
             'branch.direction': '1',
             'branch.prios.enabled': 'true',
             'branch.prios.psize': '0',
             'branch.prios.psadjacent': '10',
             'branch.prios.colorindex': '-1',
             'branch.dynamic.dsatur.nodelb': '0.7',
             'branch.dynamic.fractional': 'false',
             'branch.dynamic.dsatur': 'true',
             'branch.singlevar': 'false',
             'branch.boundws': 'true',
             'solver.useCutCallback': 'true',
             'solver.useHeuristicCallback': 'true',
             'solver.useBranchingCallback': 'true',
             
             'primal.enabled': 'true',
             'primal.onlyonup': 'true',
             'primal.useub': 'true',
             'branch.enabled': 'true',
             
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
            'solver.useCplexPrimalHeuristic': 'false',
            
            'pruning.enabled': 'false',
            
              }

lowdens_runs = create_runs(baseprops, [
                {
                    'pruning.enabled': 'false',
                    'solver.probing': '1',
                    'solver.mipEmphasis': '3',
                },
                {
                    'pruning.enabled': 'false',
                    'solver.probing': '1',
                    'solver.mipEmphasis': '3',
                    'strategy.partition': 'PaintAtLeastOne',
                },
                {
                    'pruning.enabled': 'false',
                    'solver.probing': '-1',
                    'solver.mipEmphasis': '3',
                    'strategy.colorBound': 'UpperNodesSum',
                    'strategy.symmetry': 'UseLowerLabelFirst',                    
                },
                ])

lowdens_cuts = create_runs(baseprops, [
                {
                    'pruning.enabled': 'false',
                    'solver.probing': '1',
                    'solver.mipEmphasis': '3',
                    
                    'cuts.iterations.root.max': '100',
                    'cuts.iterations.nodes.max':'1',
                    'cuts.maxdepth':'0',
                },
                {
                    'pruning.enabled': 'false',
                    'solver.probing': '1',
                    'solver.mipEmphasis': '3',
                    
                    'cuts.iterations.root.max': '500',
                    'cuts.iterations.nodes.max':'1',
                    'cuts.maxdepth':'0',
                },
                {
                    'pruning.enabled': 'false',
                    'solver.probing': '1',
                    'solver.mipEmphasis': '3',
                    
                    'cuts.iterations.root.max': '100',
                    'cuts.iterations.nodes.max':'5',
                    'cuts.maxdepth':'15',
                },
                {
                    'pruning.enabled': 'false',
                    'solver.probing': '1',
                    'solver.mipEmphasis': '3',
                    
                    'cuts.iterations.root.max': '100',
                    'cuts.iterations.nodes.max':'20',
                    'cuts.local':'false',
                    'cuts.maxdepth':'10',
                },
                {
                    'pruning.enabled': 'false',
                    'solver.probing': '1',
                    'solver.mipEmphasis': '3',
                    
                    'cuts.iterations.root.max': '100',
                    'cuts.iterations.nodes.max':'20',
                    'cuts.local':'true',
                    'cuts.maxdepth':'10',
                },
                ])

lowdens_runs_ext = create_runs(baseprops, [
                {
                    'pruning.enabled': 'false',
                    'solver.probing': '-1',
                    'solver.mipEmphasis': '3',
                    'strategy.colorBound': 'UpperNodesSum',
                    'strategy.symmetry': 'UseLowerLabelFirst',
                    'branch.dynamic.dsatur': 'false',
                    'primal.onlyonup': 'false',
                    'primal.everynodes': '2',
                    'cuts.onlyonup': 'false'
                },
                {
                    'branch.dynamic.dsatur': 'false',
                    'primal.onlyonup': 'false',
                    'primal.everynodes': '2',  
                    'pruning.enabled': 'false',
                    'solver.probing': '-1',
                    'solver.mipEmphasis': '3',
                    'strategy.colorBound': 'UpperNodesSum',
                    'strategy.symmetry': 'UseLowerLabelFirst',
                    'cuts.onlyonup': 'false',
                    'primal.enabled': 'false',
                    'solver.useCplexPrimalHeuristic': 'true',
                },
                ])


lowdens_runs_phlfreq = create_runs(baseprops, [
                {
                    'pruning.enabled': 'false',
                    'solver.probing': '1',
                    'solver.mipEmphasis': '3',
                },
                {
                    'pruning.enabled': 'false',
                    'solver.probing': '1',
                    'solver.mipEmphasis': '3',
                    'strategy.partition': 'PaintAtLeastOne',
                },
                {
                    'pruning.enabled': 'false',
                    'solver.probing': '-1',
                    'solver.mipEmphasis': '3',
                    'strategy.colorBound': 'UpperNodesSum',
                    'strategy.symmetry': 'UseLowerLabelFirst',                    
                },
                ])


highdens_runs = create_runs(baseprops, [
                {
                    'pruning.enabled': 'false',
                    'solver.probing': '-1',
                    'solver.mipEmphasis': '0',
                },
                {
                    'pruning.enabled': 'false',
                    'solver.probing': '-1',
                    'solver.mipEmphasis': '0',
                    'strategy.colorBound': 'UpperNodesSumLowerSumPartition',
                    'strategy.symmetry': 'VerticesNumber',                    
                },
                {
                    'pruning.enabled': 'false',
                    'solver.probing': '-1',
                    'solver.mipEmphasis': '0',
                    'strategy.colorBound': 'UpperNodesSum',
                    'strategy.adjacency': 'AdjacentsLeqOne',
                    'strategy.symmetry': 'UseLowerLabelFirst',                    
                },
                {
                    'pruning.enabled': 'false',
                    'solver.probing': '-1',
                    'solver.mipEmphasis': '0',
                    'strategy.colorBound': 'UpperNodesSumLowerSumPartition',
                    'strategy.symmetry': 'VerticesNumber',
                    'strategy.adjacency': 'AdjacentsPartitionLeqColor',                    
                },
                {
                    'pruning.enabled': 'false',
                    'solver.probing': '-1',
                    'solver.mipEmphasis': '0',
                    'strategy.colorBound': 'UpperNodesSum',
                    'strategy.adjacency': 'AdjacentsPartitionLeqColor',
                    'strategy.symmetry': 'UseLowerLabelFirst',                    
                },
                ])


highdens_cuts_s3 = create_runs(update_copy(baseprops, {
                    'pruning.enabled': 'false',
                    'solver.probing': '-1',
                    'solver.mipEmphasis': '0',
                    'strategy.colorBound': 'UpperNodesSum',
                    'strategy.adjacency': 'AdjacentsPartitionLeqColor',
                    'strategy.symmetry': 'UseLowerLabelFirst',                         
                 }), [
                {
                    'cuts.iterations.root.max': '100',
                    'cuts.iterations.nodes.max':'1',
                    'cuts.maxdepth':'0',
                },
                {
                    'cuts.iterations.root.max': '500',
                    'cuts.iterations.nodes.max':'1',
                    'cuts.maxdepth':'0',
                },
                {
                    'cuts.iterations.root.max': '100',
                    'cuts.iterations.nodes.max':'5',
                    'cuts.maxdepth':'15',
                },
                {
                    'cuts.iterations.root.max': '100',
                    'cuts.iterations.nodes.max':'20',
                    'cuts.local':'false',
                    'cuts.maxdepth':'10',
                },
                {
                    'cuts.iterations.root.max': '100',
                    'cuts.iterations.nodes.max':'20',
                    'cuts.local':'true',
                    'cuts.maxdepth':'10',
                }
                 ])

highdens_cuts_s5 = create_runs(update_copy(baseprops, {
                    'pruning.enabled': 'false',
                    'solver.probing': '-1',
                    'solver.mipEmphasis': '0',
                    'strategy.colorBound': 'UpperNodesSumLowerSumPartition',
                    'strategy.symmetry': 'VerticesNumber',
                    'strategy.adjacency': 'AdjacentsPartitionLeqColor',              
                 }), [
                {
                    'cuts.iterations.root.max': '100',
                    'cuts.iterations.nodes.max':'1',
                    'cuts.maxdepth':'0',
                },
                {
                    'cuts.iterations.root.max': '500',
                    'cuts.iterations.nodes.max':'1',
                    'cuts.maxdepth':'0',
                },
                {
                    'cuts.iterations.root.max': '100',
                    'cuts.iterations.nodes.max':'5',
                    'cuts.maxdepth':'15',
                },
                {
                    'cuts.iterations.root.max': '100',
                    'cuts.iterations.nodes.max':'20',
                    'cuts.local':'false',
                    'cuts.maxdepth':'10',
                },
                {
                    'cuts.iterations.root.max': '100',
                    'cuts.iterations.nodes.max':'20',
                    'cuts.local':'true',
                    'cuts.maxdepth':'10',
                }
                 ])

highdens_runs_ext = create_runs(baseprops, [ {
                    'pruning.enabled': 'false',
                    'solver.probing': '-1',
                    'solver.mipEmphasis': '0',
                    'strategy.colorBound': 'UpperNodesSumLowerSumPartition',
                    'strategy.symmetry': 'MinimumNodeLabel',
                    'strategy.adjacency': 'AdjacentsPartitionLeqColor',                    
                }])


highdens_runs_phlfreq = create_runs(baseprops, [
                {
                    'pruning.enabled': 'false',
                    'solver.probing': '-1',
                    'solver.mipEmphasis': '0',
                },
                {
                    'pruning.enabled': 'false',
                    'solver.probing': '-1',
                    'solver.mipEmphasis': '0',
                    'strategy.colorBound': 'UpperNodesSumLowerSumPartition',
                    'strategy.symmetry': 'VerticesNumber',                    
                },
                {
                    'pruning.enabled': 'false',
                    'solver.probing': '-1',
                    'solver.mipEmphasis': '0',
                    'strategy.colorBound': 'UpperNodesSum',
                    'strategy.adjacency': 'AdjacentsLeqOne',
                    'strategy.symmetry': 'UseLowerLabelFirst',                    
                },
                {
                    'pruning.enabled': 'false',
                    'solver.probing': '-1',
                    'solver.mipEmphasis': '0',
                    'strategy.colorBound': 'UpperNodesSumLowerSumPartition',
                    'strategy.symmetry': 'VerticesNumber',
                    'strategy.adjacency': 'AdjacentsPartitionLeqColor',                    
                },
                {
                    'pruning.enabled': 'false',
                    'solver.probing': '-1',
                    'solver.mipEmphasis': '0',
                    'strategy.colorBound': 'UpperNodesSum',
                    'strategy.adjacency': 'AdjacentsPartitionLeqColor',
                    'strategy.symmetry': 'UseLowerLabelFirst',                    
                },
                ])


highdens_runs_fix = create_runs(baseprops, [
                {
                    'pruning.enabled': 'false',
                    'solver.probing': '-1',
                    'solver.mipEmphasis': '0',
                    'strategy.colorBound': 'UpperNodesSumLowerSumPartition',
                    'strategy.symmetry': 'VerticesNumber',                    
                },
                ])
