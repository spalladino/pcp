from fixture import *

if __name__ == '__main__':
    #if not resume():
    newrun([
                {
                    "path.enabled": 'false', 
                    "clique.enabled": 'false',
                    "blockColor.enabled": 'false',
                    "gprime.enabled": 'false',
                    "holes.enabled": 'false',
                    "branch.prios.enabled": 'false',
                    "primal.enabled": 'false',
                }, 
                {
                    "path.enabled": 'false', 
                    "clique.enabled": 'true',
                    "blockColor.enabled": 'true',
                    "gprime.enabled": 'false',
                    "holes.enabled": 'false',
                    "branch.prios.enabled": 'false',
                    "primal.enabled": 'false',
                },
                {
                    "path.enabled": 'false', 
                    "clique.enabled": 'false',
                    "blockColor.enabled": 'false',
                    "gprime.enabled": 'false',
                    "holes.enabled": 'false',
                    "branch.prios.enabled": 'true',
                    "primal.enabled": 'true',
                }, 
                {
                    "path.enabled": 'false', 
                    "clique.enabled": 'false',
                    "blockColor.enabled": 'false',
                    "gprime.enabled": 'false',
                    "holes.enabled": 'false',
                    "branch.prios.enabled": 'true',
                    "primal.enabled": 'false',
                }, 

                {
                    "path.enabled": 'false', 
                    "clique.enabled": 'true',
                    "blockColor.enabled": 'true',
                    "gprime.enabled": 'false',
                    "holes.enabled": 'false',
                    "branch.prios.enabled": 'true',
                    "primal.enabled": 'true',
                }, 

                {
                    "path.enabled": 'false', 
                    "clique.enabled": 'true',
                    "blockColor.enabled": 'true',
                    "gprime.enabled": 'false',
                    "holes.enabled": 'false',
                    "branch.prios.enabled": 'true',
                    "primal.enabled": 'false',
                }, 

                {
                    "path.enabled": 'true', 
                    "clique.enabled": 'true',
                    "blockColor.enabled": 'true',
                    "gprime.enabled": 'true',
                    "holes.enabled": 'true',
                    "branch.prios.enabled": 'false',
                    "primal.enabled": 'false',
                }, 

                {
                    "path.enabled": 'true', 
                    "clique.enabled": 'false',
                    "blockColor.enabled": 'false',
                    "gprime.enabled": 'true',
                    "holes.enabled": 'true',
                    "branch.prios.enabled": 'false',
                    "primal.enabled": 'false',
                }, 

                {
                    "path.enabled": 'true', 
                    "clique.enabled": 'true',
                    "blockColor.enabled": 'true',
                    "gprime.enabled": 'true',
                    "holes.enabled": 'true',
                    "branch.prios.enabled": 'true',
                    "primal.enabled": 'true',
                }, 

                {
                    "path.enabled": 'true', 
                    "clique.enabled": 'false',
                    "blockColor.enabled": 'false',
                    "gprime.enabled": 'true',
                    "holes.enabled": 'true',
                    "branch.prios.enabled": 'true',
                    "primal.enabled": 'true',
                }, 

 
            ], 
            
            files = [ 
                        "rand50.in",
                        "rand60.in",
                        "rand70.in",
                        "rand80.in",
                        "rand90.in",
                        "rand100.in",
                        "rand110.in",
                        "rand120.in",
                    ],
                    
            dirs = [
                        ".\\..\\data\\rand025\\",
                        ".\\..\\data\\rand050\\",
                        ".\\..\\data\\rand075\\",
                    ])
