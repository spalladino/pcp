import fetcher

from config import datadir
from common import create_runs
from common import update_copy


def files():
    return fetcher.Fetcher(datadir).fetch_files('benchdens', 'e0(2|4|6|8)n120\\.00(0|1|2)\\.in') + \
        fetcher.Fetcher(datadir).fetch_files('benchnodes', 'n((100)|(120)|(140)|(160)|(180)|(200))\\.00(0|1|2)\\.in') + \
        fetcher.Fetcher(datadir).fetch_files('holme', 'n120d0(1|2|3|4)\\.00[\\d]\\.in') 
    

baseprops = {
             'cuts.enabled': 'true',
             'cuts.iterations.root.max': '100',
             'solver.maxTime': '1800',
             'solver.kind': 'PcpBranchAndCut',
             'branch.selection': '1',
             'branch.direction': '0',
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
             'branch.enabled': 'true',
             
            'clique.colorsAsc': 'false',
            'clique.backtrackBrokenIneqs': 'false',
            'clique.backtrackLastCandidate': 'false',
            'blockColor.usePool': 'false',
            'cuts.iset.usePathsAlgorithm': 'true',
            'cuts.iset.useBreakingSymmetry': 'false',
            
            'solver.mipEmphasis': '0',
            'solver.probing': '0',
			'cuts.minCliques': '40'
			
            'pruning.enabled': 'false',
            'pruning.minset': '10',
            'pruning.remaining': '20',
            'pruning.frac': '1.0',

			'clique.enabled': 'true',
			'path.enabled': 'true',
			'gprime.path.enabled': 'true',
			'blockColor.enabled': 'true',
			'solver.useCplexCuttingPlanes': 'true',

              }
			  
pruning_runs = create_runs(baseprops, [
				{
					'pruning.enabled': 'false',

				},
				{
					'pruning.enabled': 'true',
		            'pruning.remaining': '10',
				},
				{
					'pruning.enabled': 'true',
		            'pruning.remaining': '20',
				},
				{
					'pruning.enabled': 'true',
		            'pruning.remaining': '30',
				},
				{
					'pruning.enabled': 'true',
		            'pruning.remaining': '40',
				}
				])