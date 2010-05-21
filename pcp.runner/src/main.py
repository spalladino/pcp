from fixture import *

if __name__ == '__main__':
    #if not resume():
    newrun([
                {
                    'solver.kind': 'Heuristic'
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
                        ".\\..\\data\\rand075\\",
                    ])
