from fixture import *

if __name__ == '__main__':
    if not resume():
        newrun([
                { "holes.maxPerColor": 1 },
                { "holes.maxPerColor": 5 },
                { "holes.maxPerColor": 10 }
                ], 
            files = [ 
                    "rand10.in", 
                    "rand20.in", 
                    "rand30.in" 
                    ])
