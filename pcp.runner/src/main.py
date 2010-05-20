from fixture import *

if __name__ == '__main__':
    #if not resume():
    newrun([
                {
                    'cuts.minCliques': '0',
                    'iterations.root.max': '5',
                    'primal.enabled': 'true',
                    'pruning.remaining': '0'
                },
                {
                    'cuts.minCliques': '50',
                    'iterations.root.max': '5',
                    'primal.enabled': 'true',
                    'pruning.remaining': '0'
                },
                {
                    'cuts.minCliques': '50',
                    'iterations.root.max': '20',
                    'primal.enabled': 'true',
                    'pruning.remaining': '0'
                },
                {
                    'cuts.minCliques': '50',
                    'iterations.root.max': '20',
                    'primal.enabled': 'true',
                    'pruning.remaining': '15'
                },
                {
                    'cuts.minCliques': '50',
                    'iterations.root.max': '20',
                    'primal.enabled': 'true',
                    'pruning.remaining': '25'
                },
                {
                    'cuts.minCliques': '50',
                    'iterations.root.max': '20',
                    'primal.enabled': 'false',
                    'pruning.remaining': '15'
                    
                },
                {
                    'cuts.minCliques': '0',
                    'iterations.root.max': '20',
                    'primal.enabled': 'false',
                    'pruning.remaining': '0'
                },
                {
                    'cuts.minCliques': '0',
                    'iterations.root.max': '20',
                    'primal.enabled': 'false',
                    'pruning.remaining': '15'
                    
                },    
 
 
            ], 
            
            files = [ 
                        "rand100.in",
                        "rand110.in",
                        "rand120.in",
                        "rand130.in",
                        "rand140.in",
                        "rand150.in",
                    ],
                    
            dirs = [
                        ".\\..\\data\\rand025\\",
                        ".\\..\\data\\rand050\\",
                        ".\\..\\data\\rand075\\",
                    ])
