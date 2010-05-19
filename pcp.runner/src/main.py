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

 
            ], 
            
            files = [ 
                        "rand30.in",
                        "rand40.in",
                        "rand50.in",
                    ],
                    
            dirs = [
                        ".\\..\\data\\rand025\\",
                        ".\\..\\data\\rand050\\",
                        ".\\..\\data\\rand075\\",
                    ])
