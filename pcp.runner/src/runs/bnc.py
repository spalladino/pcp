import fetcher

from config import datadir
from common import create_runs
from common import update_copy


def files():
    return fetcher.Fetcher(datadir).fetch_files('benchdens', 'e0(2|4|6|8)n((100))\\.00(0|1|2)\\.in')     

def filesp1():
    return fetcher.Fetcher(datadir).fetch_files('singlepart', 'e0(2|4|6|8)n((100))p1\\.00(0|1|2)\\.in')     

def files80():
    return fetcher.Fetcher(datadir).fetch_files('benchdens', 'e0(2|4|6|8)n((80))\\.00(0|1|2)\\.in')

def files90():
    return fetcher.Fetcher(datadir).fetch_files('benchdens', 'e0(2|4|6|8)n((90))\\.00(0|1|2)\\.in')

def fileshk():
    return fetcher.Fetcher(datadir).fetch_files('holme', 'n100d0(1|2|3|4)\\.00(3|4)\\.in')

def filesvlow():
    return fetcher.Fetcher(datadir).fetch_files('benchdens', 'e0(2)n((100))\\.00(3|4)\\.in')
def fileslow():
    return fetcher.Fetcher(datadir).fetch_files('benchdens', 'e0(4)n((100))\\.00(3|4)\\.in')
def fileshigh():
    return fetcher.Fetcher(datadir).fetch_files('benchdens', 'e0(6|8)n((100))\\.00(3|4)\\.in')

def fileslow90():
    return fetcher.Fetcher(datadir).fetch_files('benchdens', 'e0(2|4)n((90))\\.00(0|1|2|3|4)\\.in')
def fileshigh90():
    return fetcher.Fetcher(datadir).fetch_files('benchdens', 'e0(6|8)n((90))\\.00(0|1|2|3|4)\\.in')

def filesn90d60fixedpart():
    return fetcher.Fetcher(datadir).fetch_files('fixedpart', 'p(\d)n90d0(6)\\.00(0|1)\\.in')

def fileslowhk90():
    return fetcher.Fetcher(datadir).fetch_files('holmefinal', 'n90d(2|4)0p(\\d\\d)\\.00(0|1|2|3|4)\\.in')
def fileshighhk90():
    return fetcher.Fetcher(datadir).fetch_files('holmefinal', 'n90d(6|8)0p(\\d\\d)\\.00(0|1|2|3|4)\\.in')

baseprops = {
             'cuts.enabled': 'true',
             'cuts.iterations.root.max': '100',
             'cuts.everynodes':'1',
             'cuts.maxdepth':'0',
             'cuts.local':'true',
             'cuts.onlyonup':'true',
                          
             'solver.maxTime': '7200',
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
            'pruning.minset': '1',
            'pruning.remaining': '1',
            'pruning.frac': '1.0',
            'pruning.useub': 'true',

			'clique.enabled': 'true',
			'path.enabled': 'true',
			'gprime.path.enabled': 'true',
			'blockColor.enabled': 'true',
			'solver.useCplexCuttingPlanes': 'false',
            'solver.useCplexPrimalHeuristic': 'false',
              }

lowdens_final = create_runs(baseprops, [
                {
                    'pruning.enabled': 'false',
                    'solver.probing': '1',
                    'solver.mipEmphasis': '3',
                    'cuts.iterations.root.max': '500',
                    'cuts.iterations.nodes.max':'1',
                    'cuts.maxdepth':'0',
                }])

lowdens_final_pruning = create_runs(baseprops, [               
                {
                    'pruning.enabled': 'true',
                    'pruning.minset': '5',
                    'pruning.remaining': '20',
                    'pruning.frac': '1.0',
                    'pruning.useub': 'true',
                         
                    'solver.probing': '1',
                    'solver.mipEmphasis': '3',
                    'cuts.iterations.root.max': '500',
                    'cuts.iterations.nodes.max':'1',
                    'cuts.maxdepth':'0',
                }])

lowdens_final_pruning_more = create_runs(baseprops, [               
                {
                    'pruning.enabled': 'true',
                    'pruning.minset': '5',
                    'pruning.remaining': '30',
                    'pruning.frac': '1.0',
                    'pruning.useub': 'true',
                         
                    'solver.probing': '1',
                    'solver.mipEmphasis': '3',
                    'cuts.iterations.root.max': '500',
                    'cuts.iterations.nodes.max':'1',
                    'cuts.maxdepth':'0',
                }])


highdens_final = create_runs(update_copy(baseprops, {
                    'pruning.enabled': 'false',
                    'solver.probing': '-1',
                    'solver.mipEmphasis': '0',
                    'strategy.colorBound': 'UpperNodesSum',
                    'strategy.adjacency': 'AdjacentsPartitionLeqColor',
                    'strategy.symmetry': 'UseLowerLabelFirst',                         
                 }), [
                {
                    'cuts.iterations.root.max': '500',
                    'cuts.iterations.nodes.max':'1',
                    'cuts.maxdepth':'0',
                }])

highdens_final_vs_cplex = create_runs(update_copy(baseprops, {
                    'pruning.enabled': 'false',
                    'strategy.colorBound': 'UpperNodesSum',
                    'strategy.adjacency': 'AdjacentsPartitionLeqColor',
                    'strategy.symmetry': 'UseLowerLabelFirst',                         
                 }), [
                {
                    'solver.probing': '-1',
                    'solver.mipEmphasis': '0',
                    'cuts.iterations.root.max': '500',
                    'cuts.iterations.nodes.max':'1',
                    'cuts.maxdepth':'0',
                },
                {
                 'solver.kind': 'CplexBranchAndCutSearch',
                 'solver.useCplexPrimalHeuristic': 'false',
                 'solver.useCplexPreprocess': 'true',
                 'solver.useCplexCuttingPlanes': 'false',
                 },
                  {
                 'solver.kind': 'CplexBranchAndCutSearch',
                 'solver.useCplexPrimalHeuristic': 'false',
                 'solver.useCplexPreprocess': 'true',
                 'solver.useCplexCuttingPlanes': 'false',
                 'model.variables.boundOnDegree': 'false',
                 'model.variables.boundOnPartitionIndex': 'false',
                 'model.variables.fixClique': 'false',
                 }
               ])


highdens_final_pruning = create_runs(update_copy(baseprops, {
                    'pruning.enabled': 'false',
                    'solver.probing': '-1',
                    'solver.mipEmphasis': '0',
                    'strategy.colorBound': 'UpperNodesSum',
                    'strategy.adjacency': 'AdjacentsPartitionLeqColor',
                    'strategy.symmetry': 'UseLowerLabelFirst',                         
                 }), [
                {
                    'pruning.enabled': 'true',
                    'pruning.minset': '5',
                    'pruning.remaining': '20',
                    'pruning.frac': '1.0',
                    'pruning.useub': 'true',
                 
                    'cuts.iterations.root.max': '500',
                    'cuts.iterations.nodes.max':'1',
                    'cuts.maxdepth':'0',
                }])

highdens_final_pruning_more = create_runs(update_copy(baseprops, {
                    'pruning.enabled': 'false',
                    'solver.probing': '-1',
                    'solver.mipEmphasis': '0',
                    'strategy.colorBound': 'UpperNodesSum',
                    'strategy.adjacency': 'AdjacentsPartitionLeqColor',
                    'strategy.symmetry': 'UseLowerLabelFirst',                         
                 }), [
                {
                    'pruning.enabled': 'true',
                    'pruning.minset': '5',
                    'pruning.remaining': '30',
                    'pruning.frac': '1.0',
                    'pruning.useub': 'true',
                 
                    'cuts.iterations.root.max': '500',
                    'cuts.iterations.nodes.max':'1',
                    'cuts.maxdepth':'0',
                }])



hopefully_final_runs_vlow = create_runs(baseprops, [
                           {
                            'solver.maxTime': '7200',
                            'pruning.enabled': 'true',
                            'pruning.remaining': '20',
                            'pruning.useub': 'true',
                            'primal.useub': 'true',
                            'solver.probing': '-1',
                            'solver.mipEmphasis': '3',
                            'cuts.iterations.root.max': '300',
                            
                            'strategy.colorBound': 'UpperNodesSumLowerSumPartition',
                            'strategy.adjacency': 'AdjacentsLeqOne',
                            'strategy.symmetry': 'MinimumNodeLabel',    
                            'strategy.partition': 'PaintAtLeastOne',               
                            },
                            {
                             'solver.maxTime': '7200',
                             'solver.kind': 'CplexBranchAndCutSearch',
                             'solver.useCplexPrimalHeuristic': 'false',
                             'solver.useCplexPreprocess': 'true',
                             'solver.useCplexCuttingPlanes': 'false',
                             },
                              {
                             'solver.maxTime': '7200',
                             'solver.kind': 'CplexBranchAndCutSearch',
                             'solver.useCplexPrimalHeuristic': 'false',
                             'solver.useCplexPreprocess': 'true',
                             'solver.useCplexCuttingPlanes': 'false',
                             'model.variables.boundOnDegree': 'false',
                             'model.variables.boundOnPartitionIndex': 'false',
                             'model.variables.fixClique': 'false',
                             }
                           ])

hopefully_final_runs_low = create_runs(baseprops, [
                           {
                            'solver.maxTime': '7200',
                            'pruning.enabled': 'true',
                            'pruning.remaining': '20',
                            'pruning.useub': 'true',
                            'primal.useub': 'true',
                            'solver.probing': '1',
                            'solver.mipEmphasis': '3',
                            'cuts.iterations.root.max': '300',
                            
                            'strategy.colorBound': 'UpperNodesSumLowerSumPartition',
                            'strategy.adjacency': 'AdjacentsNeighbourhood',
                            'strategy.symmetry': 'MinimumNodeLabel',    
                            'strategy.partition': 'PaintExactlyOne',               
                            },
                            {
                             'strategy.colorBound': 'UpperNodesSumLowerSumPartition',
                            'strategy.adjacency': 'AdjacentsNeighbourhood',
                            'strategy.symmetry': 'MinimumNodeLabel',    
                            'strategy.partition': 'PaintExactlyOne',     
                            
                             'solver.maxTime': '7200',
                             'solver.kind': 'CplexBranchAndCutSearch',
                             'solver.useCplexPrimalHeuristic': 'true',
                             'solver.useCplexPreprocess': 'true',
                             'solver.useCplexCuttingPlanes': 'true',
                             },
                              {
                               'strategy.colorBound': 'UpperNodesSumLowerSumPartition',
                            'strategy.adjacency': 'AdjacentsNeighbourhood',
                            'strategy.symmetry': 'MinimumNodeLabel',    
                            'strategy.partition': 'PaintExactlyOne',     
                            
                             'solver.maxTime': '7200',
                             'solver.kind': 'CplexBranchAndCutSearch',
                             'solver.useCplexPrimalHeuristic': 'true',
                             'solver.useCplexPreprocess': 'true',
                             'solver.useCplexCuttingPlanes': 'true',
                             'model.variables.boundOnDegree': 'false',
                             'model.variables.boundOnPartitionIndex': 'false',
                             'model.variables.fixClique': 'false',
                             }
                           ])

hopefully_final_runs_high = create_runs(baseprops, [
                           {
                            'solver.maxTime': '7200',
                            'pruning.enabled': 'true',
                            'pruning.remaining': '20',
                            'pruning.useub': 'true',
                            'primal.useub': 'true',
                            'solver.probing': '1',
                            'solver.mipEmphasis': '0',
                            'cuts.iterations.root.max': '300',
                            
                            'strategy.colorBound': 'UpperNodesSumLowerSumPartition',
                            'strategy.adjacency': 'AdjacentsNeighbourhood',
                            'strategy.symmetry': 'MinimumNodeLabel',    
                            'strategy.partition': 'PaintExactlyOne',               
                            },
                            {
                             'strategy.colorBound': 'UpperNodesSumLowerSumPartition',
                            'strategy.adjacency': 'AdjacentsNeighbourhood',
                            'strategy.symmetry': 'MinimumNodeLabel',    
                            'strategy.partition': 'PaintExactlyOne',               
                            
                             'solver.maxTime': '7200',
                             'solver.kind': 'CplexBranchAndCutSearch',
                             'solver.useCplexPrimalHeuristic': 'true',
                             'solver.useCplexPreprocess': 'true',
                             'solver.useCplexCuttingPlanes': 'true',
                             },
                              {
                               'strategy.colorBound': 'UpperNodesSumLowerSumPartition',
                            'strategy.adjacency': 'AdjacentsNeighbourhood',
                            'strategy.symmetry': 'MinimumNodeLabel',    
                            'strategy.partition': 'PaintExactlyOne',               
                            
                             'solver.maxTime': '7200',
                             'solver.kind': 'CplexBranchAndCutSearch',
                             'solver.useCplexPrimalHeuristic': 'true',
                             'solver.useCplexPreprocess': 'true',
                             'solver.useCplexCuttingPlanes': 'true',
                             'model.variables.boundOnDegree': 'false',
                             'model.variables.boundOnPartitionIndex': 'false',
                             'model.variables.fixClique': 'false',
                             }
                           ])



hopefully_final_runs_vlow_2 = create_runs(baseprops, [
                           {
                            'solver.maxTime': '7200',
                            'pruning.enabled': 'true',
                            'pruning.remaining': '20',
                            'pruning.useub': 'true',
                            'primal.useub': 'true',
                            'solver.probing': '1',
                            'solver.mipEmphasis': '3',
                            'cuts.iterations.root.max': '300',
                            
                            'strategy.colorBound': 'UpperNodesSumLowerSumPartition',
                            'strategy.adjacency': 'AdjacentsNeighbourhood',
                            'strategy.symmetry': 'MinimumNodeLabel',    
                            'strategy.partition': 'PaintAtLeastOne',               
                            },
                            {
                             'solver.maxTime': '7200',
                             'solver.kind': 'CplexDynamicSearch',
                             'solver.useCplexPrimalHeuristic': 'false',
                             'solver.useCplexPreprocess': 'true',
                             'solver.useCplexCuttingPlanes': 'false',
                             },
                              {
                             'solver.maxTime': '7200',
                             'solver.kind': 'CplexDynamicSearch',
                             'solver.useCplexPrimalHeuristic': 'false',
                             'solver.useCplexPreprocess': 'true',
                             'solver.useCplexCuttingPlanes': 'false',
                             'model.variables.boundOnDegree': 'false',
                             'model.variables.boundOnPartitionIndex': 'false',
                             'model.variables.fixClique': 'false',
                             }
                           ])

hopefully_final_runs_low_2 = create_runs(baseprops, [
                           {
                            'solver.maxTime': '7200',
                            'pruning.enabled': 'true',
                            'pruning.remaining': '20',
                            'pruning.useub': 'true',
                            'primal.useub': 'true',
                            'solver.probing': '1',
                            'solver.mipEmphasis': '3',
                            'cuts.iterations.root.max': '300',
                            
                            'strategy.colorBound': 'UpperNodesSumLowerSumPartition',
                            'strategy.adjacency': 'AdjacentsNeighbourhood',
                            'strategy.symmetry': 'MinimumNodeLabel',    
                            'strategy.partition': 'PaintExactlyOne',               
                            },
                            {
                             'strategy.colorBound': 'UpperNodesSumLowerSumPartition',
                            'strategy.adjacency': 'AdjacentsNeighbourhood',
                            'strategy.symmetry': 'MinimumNodeLabel',    
                            'strategy.partition': 'PaintExactlyOne',     
                            
                             'solver.maxTime': '7200',
                             'solver.kind': 'CplexDynamicSearch',
                             'solver.useCplexPrimalHeuristic': 'true',
                             'solver.useCplexPreprocess': 'true',
                             'solver.useCplexCuttingPlanes': 'true',
                             },
                              {
                               'strategy.colorBound': 'UpperNodesSumLowerSumPartition',
                            'strategy.adjacency': 'AdjacentsNeighbourhood',
                            'strategy.symmetry': 'MinimumNodeLabel',    
                            'strategy.partition': 'PaintExactlyOne',     
                            
                             'solver.maxTime': '7200',
                             'solver.kind': 'CplexDynamicSearch',
                             'solver.useCplexPrimalHeuristic': 'true',
                             'solver.useCplexPreprocess': 'true',
                             'solver.useCplexCuttingPlanes': 'true',
                             'model.variables.boundOnDegree': 'false',
                             'model.variables.boundOnPartitionIndex': 'false',
                             'model.variables.fixClique': 'false',
                             }
                           ])

hopefully_final_runs_high_2 = create_runs(baseprops, [
                           {
                            'solver.maxTime': '7200',
                            'pruning.enabled': 'true',
                            'pruning.remaining': '20',
                            'pruning.useub': 'true',
                            'primal.useub': 'true',
                            'solver.probing': '-1',
                            'solver.mipEmphasis': '0',
                            'cuts.iterations.root.max': '300',
                            
                            'strategy.colorBound': 'UpperNodesSumLowerSumPartition',
                            'strategy.adjacency': 'AdjacentsNeighbourhood',
                            'strategy.symmetry': 'MinimumNodeLabel',    
                            'strategy.partition': 'PaintExactlyOne',               
                            },
                            {
                             'strategy.colorBound': 'UpperNodesSumLowerSumPartition',
                            'strategy.adjacency': 'AdjacentsNeighbourhood',
                            'strategy.symmetry': 'MinimumNodeLabel',    
                            'strategy.partition': 'PaintExactlyOne',               
                            
                             'solver.maxTime': '7200',
                             'solver.kind': 'CplexDynamicSearch',
                             'solver.useCplexPrimalHeuristic': 'true',
                             'solver.useCplexPreprocess': 'true',
                             'solver.useCplexCuttingPlanes': 'true',
                             },
                              {
                               'strategy.colorBound': 'UpperNodesSumLowerSumPartition',
                            'strategy.adjacency': 'AdjacentsNeighbourhood',
                            'strategy.symmetry': 'MinimumNodeLabel',    
                            'strategy.partition': 'PaintExactlyOne',               
                            
                             'solver.maxTime': '7200',
                             'solver.kind': 'CplexDynamicSearch',
                             'solver.useCplexPrimalHeuristic': 'true',
                             'solver.useCplexPreprocess': 'true',
                             'solver.useCplexCuttingPlanes': 'true',
                             'model.variables.boundOnDegree': 'false',
                             'model.variables.boundOnPartitionIndex': 'false',
                             'model.variables.fixClique': 'false',
                             }
                           ])

hopefully_final_runs_dimacs = create_runs(baseprops, [
                           # inserted from vlow, low and high
                            {
                            'solver.maxTime': '7200',
                            'pruning.enabled': 'true',
                            'pruning.remaining': '20',
                            'pruning.useub': 'true',
                            'primal.useub': 'true',
                            'solver.probing': '1',
                            'solver.mipEmphasis': '3',
                            'cuts.iterations.root.max': '300',
                            
                            'strategy.colorBound': 'UpperNodesSumLowerSumPartition',
                            'strategy.adjacency': 'AdjacentsNeighbourhood',
                            'strategy.symmetry': 'MinimumNodeLabel',    
                            'strategy.partition': 'PaintAtLeastOne',               
                            },
                            {
                            'solver.maxTime': '7200',
                            'pruning.enabled': 'true',
                            'pruning.remaining': '20',
                            'pruning.useub': 'true',
                            'primal.useub': 'true',
                            'solver.probing': '1',
                            'solver.mipEmphasis': '3',
                            'cuts.iterations.root.max': '300',
                            
                            'strategy.colorBound': 'UpperNodesSumLowerSumPartition',
                            'strategy.adjacency': 'AdjacentsNeighbourhood',
                            'strategy.symmetry': 'MinimumNodeLabel',    
                            'strategy.partition': 'PaintExactlyOne',               
                            },
                           {
                            'solver.maxTime': '7200',
                            'pruning.enabled': 'true',
                            'pruning.remaining': '20',
                            'pruning.useub': 'true',
                            'primal.useub': 'true',
                            'solver.probing': '-1',
                            'solver.mipEmphasis': '0',
                            'cuts.iterations.root.max': '300',
                            
                            'strategy.colorBound': 'UpperNodesSumLowerSumPartition',
                            'strategy.adjacency': 'AdjacentsNeighbourhood',
                            'strategy.symmetry': 'MinimumNodeLabel',    
                            'strategy.partition': 'PaintExactlyOne',               
                            },
                           # the previous ones
                           {
                            'solver.maxTime': '7200',
                            'pruning.enabled': 'true',
                            'pruning.remaining': '20',
                            'pruning.useub': 'true',
                            'primal.useub': 'true',
                            'solver.probing': '1',
                            'solver.mipEmphasis': '3',
                            'cuts.iterations.root.max': '300',
                            
                            'strategy.colorBound': 'UpperNodesSumLowerSumPartition',
                            'strategy.adjacency': 'AdjacentsNeighbourhood',
                            'strategy.symmetry': 'MinimumNodeLabel',    
                            'strategy.partition': 'PaintExactlyOne',               
                            },
                           {
                            'solver.maxTime': '7200',
                            'pruning.enabled': 'true',
                            'pruning.remaining': '20',
                            'pruning.useub': 'true',
                            'primal.useub': 'true',
                            'solver.probing': '1',
                            'solver.mipEmphasis': '0',
                            'cuts.iterations.root.max': '300',
                            
                            'strategy.colorBound': 'UpperNodesSumLowerSumPartition',
                            'strategy.adjacency': 'AdjacentsNeighbourhood',
                            'strategy.symmetry': 'MinimumNodeLabel',    
                            'strategy.partition': 'PaintExactlyOne',               
                            },
                            {
                             'strategy.colorBound': 'UpperNodesSumLowerSumPartition',
                            'strategy.adjacency': 'AdjacentsNeighbourhood',
                            'strategy.symmetry': 'MinimumNodeLabel',    
                            'strategy.partition': 'PaintExactlyOne',               
                            
                             'solver.maxTime': '7200',
                             'solver.kind': 'CplexBranchAndCutSearch',
                             'solver.useCplexPrimalHeuristic': 'true',
                             'solver.useCplexPreprocess': 'true',
                             'solver.useCplexCuttingPlanes': 'true',
                             },
                              {
                               'strategy.colorBound': 'UpperNodesSumLowerSumPartition',
                            'strategy.adjacency': 'AdjacentsNeighbourhood',
                            'strategy.symmetry': 'MinimumNodeLabel',    
                            'strategy.partition': 'PaintExactlyOne',               
                            
                             'solver.maxTime': '7200',
                             'solver.kind': 'CplexBranchAndCutSearch',
                             'solver.useCplexPrimalHeuristic': 'true',
                             'solver.useCplexPreprocess': 'true',
                             'solver.useCplexCuttingPlanes': 'true',
                             'model.variables.boundOnDegree': 'false',
                             'model.variables.boundOnPartitionIndex': 'false',
                             'model.variables.fixClique': 'false',
                             }
                           ])

pruning_runs_on_remaining = create_runs(baseprops, [
                {
                    'pruning.enabled': 'false',
                },
                {
                    'pruning.enabled': 'true',
                    'pruning.remaining': '10'
                },
                {
                    'pruning.enabled': 'true',
                    'pruning.remaining': '20'
                },
                {
                    'pruning.enabled': 'true',
                    'pruning.remaining': '30'
                },
                {
                    'pruning.enabled': 'true',
                    'pruning.remaining': '40'
                }
                ])

pruning_runs = create_runs(baseprops, [
				{
					'pruning.enabled': 'false',
				},
                {
                    'pruning.enabled': 'true',
                    'pruning.frac': '0.2'
                },
				{
					'pruning.enabled': 'true',
                    'pruning.frac': '0.4'
				},
                {
                    'pruning.enabled': 'true',
                    'pruning.frac': '0.6'
                },
                {
                    'pruning.enabled': 'true',
                    'pruning.frac': '0.8'
                }
				])

pruning_primalnoub_runs = create_runs(baseprops, [
                {
                    'pruning.enabled': 'false',
                    'primal.useub': 'false'
                },
                {
                    'pruning.enabled': 'true',
                    'pruning.frac': '0.4',
                    'primal.useub': 'false'
                },
                {
                    'pruning.enabled': 'true',
                    'pruning.frac': '0.5',
                    'primal.useub': 'false'
                },
                {
                    'pruning.enabled': 'true',
                    'pruning.frac': '0.6',
                    'primal.useub': 'false'
                },
                {
                    'pruning.enabled': 'true',
                    'pruning.frac': '0.7',
                    'primal.useub': 'false'
                },
                {
                    'pruning.enabled': 'true',
                    'pruning.frac': '0.8',
                    'primal.useub': 'false'
                }
                ])

emph_runs = create_runs(baseprops, [
                {
                    'solver.mipEmphasis': '0',
                },
                {
                    'solver.mipEmphasis': '1',
                },
                {
                    'solver.mipEmphasis': '2',
                },
                {
                    'solver.mipEmphasis': '3',
                },
                {
                    'solver.mipEmphasis': '4',
                },
                ])


prob_runs = create_runs(baseprops, [
                {
                    'solver.probing': '-1',
                },
                {
                    'solver.probing': '1',
                },
                {
                    'solver.probing': '2',
                },
                {
                    'solver.probing': '3',
                },
                ])

primal_runs = create_runs(baseprops, [
                {
                 'primal.onlyonup': 'true',
                 'primal.runoncutcallback': 'true'
                },
                {
                 'primal.onlyonup': 'true',
                },
                {
                 'primal.enabled': 'true',
                 'primal.everynodes': '1',
                 'primal.onlyonup': 'false',
                 'primal.runoncutcallback': 'true',
                 'cuts.onlyonup':'false'
                },
                {
                 'primal.enabled': 'true',
                 'primal.everynodes': '1',
                 'primal.onlyonup': 'false'
                },
                {
                 'primal.enabled': 'true',
                 'primal.everynodes': '2',
                 'primal.onlyonup': 'false'
                },
                {
                 'primal.enabled': 'true',
                 'primal.everynodes': '4',
                 'primal.onlyonup': 'false'
                },
                ])
