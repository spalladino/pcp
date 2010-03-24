from fixture import *

if __name__ == '__main__':
    if not resume():
        newrun([{ "run.filename": "rand30.in" }, 
                { "run.filename": "rand20.in" },
                { "run.filename": "rand10.in" }])
