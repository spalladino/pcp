from fixture import *
import files

if __name__ == '__main__':
    #if not resume():
    newrun([
                {
                    'solver.kind': 'Heuristic',
                },
                {
                    'solver.kind': 'CplexDynamicSearch',
                    'solver.useCplexPrimalHeuristic': 'true',
                },
                {
                    'solver.kind': 'PcpCutAndBranch',
                    'primal.enabled': 'true',
                },
                {
                    'solver.kind': 'PcpBranchAndCut',
                    'primal.enabled': 'true',
                },
            ], 
            
                files = files.benchdens + files.benchnodes + files.dimacs,
                
                dirs = [ '.\\..\\data\\' ]
            )
