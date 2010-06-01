from fixture import *

import files

if __name__ == '__main__':
    newrun([
                {
                    'solver.kind': 'Heuristic',
                },
                {
                    'solver.kind': 'CplexDynamicSearch',
                    'solver.useCplexPrimalHeuristic': 'true',
                    'primal.enabled': 'false',
                    'callback.branching.enabled': 'false',
                    'callback.heuristic.enabled': 'false',
                },
                {
                    'solver.kind': 'PcpCutAndBranch',
                    'primal.enabled': 'true',
                    'pruning.frac': '0.4'
                },
                {
                    'solver.kind': 'PcpBranchAndCut',
                    'primal.enabled': 'true',
                    'pruning.frac': '0.2'
                },
            ], 
            
                files = files.benchnodes_single + files.benchdens_single,
                
                dirs = [ '.\\..\\data\\' ]
            )
