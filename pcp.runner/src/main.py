from fixture import *

import files

if __name__ == '__main__':
    newrun([
                {
                    'solver.kind': 'PcpCutAndBranch',
                    'solver.mipEmphasis': '0' 
                },
                {
                    'solver.kind': 'PcpBranchAndCut',
                    'solver.mipEmphasis': '0' 
                },
                {
                    'solver.kind': 'PcpBranchAndCut',
                    'solver.mipEmphasis': '2' 
                },
            ], 
            
                files = files.benchdens80_single_001 + files.benchnodes_single_001,
                
                dirs = [ '.\\..\\data\\' ],
            )
    
