from fixture import *

if __name__ == '__main__':
    #if not resume():
    newrun([
                {
                    "branch.prios.enabled": "false" },
                    
                {   "branch.prios.psize": 0,
                    "branch.prios.psadjacent": 1000,
                    "branch.prios.colorindex": 0,
                    "branch.prios.reversecolorindex": 0 },
                    
                {   "branch.prios.psize": 0,
                    "branch.prios.psadjacent": 1000,
                    "branch.prios.colorindex": 1,
                    "branch.prios.reversecolorindex": 0 },

                {   "branch.prios.psize": 0,
                    "branch.prios.psadjacent": 1000,
                    "branch.prios.colorindex": 0,
                    "branch.prios.reversecolorindex": 1 },
                

                {   "branch.prios.psize": 1,
                    "branch.prios.psadjacent": 5,
                    "branch.prios.colorindex": 0,
                    "branch.prios.reversecolorindex": 0 },

                ], 
            files = [ 
                        "rand30.in",
                        "rand35.in",
                        "rand36.in"
                    ])
