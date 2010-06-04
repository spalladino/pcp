from fixture import *

import files

if __name__ == '__main__':
    newrun([
                {
                    'solver.kind': 'CplexBranchAndCutSearch',
                },
                {
                    'solver.kind': 'PcpCutAndBranch',
                },
                {
                    'solver.kind': 'PcpBranchAndCut',
                },
            ], 
            
                files = files.benchdens80_single + files.benchparts_single + files.benchnodes_single,
                
                dirs = [ '.\\..\\data\\' ]
            )
    
