from fixture import *

import files

if __name__ == '__main__':
    newrun([
                {
                    'solver.kind': 'CplexBranchAndCutSearch',
                    'solver.useCplexPrimalHeuristic': 'true',
                    'primal.enabled': 'false',
                    'callback.branching.enabled': 'false',
                    'callback.heuristic.enabled': 'false',
                },
                {
                    'solver.kind': 'PcpCutAndBranch',
                },
                {
                    'solver.kind': 'PcpBranchAndCut',
                },
            ], 
            
                files = files.benchparts_single + files.benchdens_single + files.benchnodes_single,
                
                dirs = [ '.\\..\\data\\' ]
            )
    
    if False:
        newrun([
                {
                    'branch.dynamic.fractional': 'false',
                    'branch.dynamic.dsatur': 'false',
                    'branch.prios.enabled': 'false',
                    'branch.prios.psize': '-10',
                    'branch.prios.psadjacent': '100'
                },
                {
                    'branch.dynamic.fractional': 'true',
                    'branch.dynamic.dsatur': 'false',
                    'branch.prios.enabled': 'true',
                    'branch.prios.psize': '-10',
                    'branch.prios.psadjacent': '100'
                },
                {
                    'branch.dynamic.fractional': 'true',
                    'branch.dynamic.dsatur': 'false',
                    'branch.prios.enabled': 'false',
                    'branch.prios.psize': '10',
                    'branch.prios.psadjacent': '100'
                },
                {
                    'branch.dynamic.fractional': 'false',
                    'branch.dynamic.dsatur': 'true',
                    'branch.prios.enabled': 'true',
                    'branch.prios.psize': '-10',
                    'branch.prios.psadjacent': '100'
                },
                {
                    'branch.dynamic.fractional': 'false',
                    'branch.dynamic.dsatur': 'true',
                    'branch.prios.enabled': 'false',
                    'branch.prios.psize': '-10',
                    'branch.prios.psadjacent': '-100'
                },
            ], 
            
                files = ['n50.000.in', 'n50.001.in', 'n50.002.in' ],
                
                dirs = [ '.\\..\\data\\benchnodes\\' ]
            )
