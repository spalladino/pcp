from fixture import *

if __name__ == '__main__':
    #if not resume():
    newrun([
                {
                    'solver.kind': 'CplexDynamicSearch', 
                    'solver.useHeuristicCallback': 'false',
                    'solver.useBranchingCallback': 'false',
                    'solver.useCplexPrimalHeuristic': 'true'
                },
                {
                    'solver.kind': 'Heuristic',
                },
                {
                    'solver.kind': 'PcpCutAndBranch',
                    'primal.enabled': 'true',
                    'strategy.adjacency': 'AdjacentsNeighbourhood'
                },
                {
                    'solver.kind': 'PcpCutAndBranch',
                    'primal.enabled': 'true',
                },
                {
                    'solver.kind': 'PcpCutAndBranch',
                    'primal.enabled': 'false',
                },
                {
                    'solver.kind': 'PcpBranchAndCut',
                    'primal.enabled': 'true',
                },
                {
                    'solver.kind': 'PcpBranchAndCut',
                    'primal.enabled': 'false',
                },
 
            ], 
            
            files = [ 
                        "rand100.in",
                        "rand120.in",
                        "rand140.in",
                        "rand160.in",
                        "rand180.in",
                        "rand200.in",
                    ],
                    
            dirs = [
                        ".\\..\\data\\rand025\\",
                        ".\\..\\data\\rand050\\",
                        ".\\..\\data\\rand075\\",
                    ])
