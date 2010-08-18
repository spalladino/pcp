import fetcher

from config import datadir
from common import create_runs
from common import update_copy


def files():
    return fetcher.Fetcher(datadir).fetch_files('benchdens', 'e0(2|4|6|8)n((100))\\.00(0|1|2)\\.in')     

baseprops = {
             'cuts.enabled': 'true',
             'cuts.iterations.root.max': '100',
             'cuts.everynodes':'1',
             'cuts.maxdepth':'0',
             'cuts.local':'true',
             'cuts.onlyonup':'true',
                          
             'solver.maxTime': '1800',
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
              }

pruning_runs = create_runs(baseprops, [
				{
					'pruning.enabled': 'false',
				},
				{
					'pruning.enabled': 'true',
		            'pruning.remaining': '20',
                    'pruning.useub': 'true',
                    'primal.useub': 'true',
				},
				{
					'pruning.enabled': 'true',
		            'pruning.remaining': '30',
                    'pruning.useub': 'true',
                    'primal.useub': 'true',
				},
				{
					'pruning.enabled': 'true',
		            'pruning.remaining': '40',
                    'pruning.useub': 'true',
                    'primal.useub': 'true',
				},
                 {
                    'pruning.useub': 'false',
                    'primal.useub': 'false',
                    'pruning.enabled': 'true',
                    'pruning.remaining': '20',
                },
                {
                    'pruning.useub': 'false',
                    'primal.useub': 'false',
                    'pruning.enabled': 'true',
                    'pruning.remaining': '30',
                },
                {
                    'pruning.useub': 'false',
                    'primal.useub': 'false',
                    'pruning.enabled': 'true',
                    'pruning.remaining': '40',
                },
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
