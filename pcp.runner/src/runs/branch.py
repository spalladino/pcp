import fetcher

from config import datadir
from common import update_copy


def files():
    return fetcher.Fetcher(datadir).fetch_files('benchdens', '((e06n80)|(e02n90)|(e0(4|8)n100))\\.00(0|1|2)\\.in')

branchboundingruns = [
                      update_copy({
                 'solver.kind': 'PcpBranchAndCut',
                 'solver.useCutCallback': 'true',
                 'solver.useHeuristicCallback': 'true',
                 'solver.useBranchingCallback': 'true',
                 'pruning.enabled': 'false',
                 'primal.enabled': 'false',
                 'branch.enabled': 'true',
                 'cuts.enabled': 'false',
                 'solver.useCplexPrimalHeuristic': 'true',
                 'solver.maxTime': '900',
                 'branch.selection': '1',
                 'branch.direction': '0',
                 'branch.prios.enabled': 'true',
                 'branch.prios.psize': '0',
                 'branch.prios.psadjacent': '10',
                 'branch.prios.colorindex': '-1',
                 'branch.dynamic.dsatur.nodelb': '0.7',
                 'branch.dynamic.fractional': 'false',
                 'branch.dynamic.dsatur': 'true',
                        },d) for d in [
                      {
                       'branch.singlevar': 'true',
                       'branch.boundws': 'true',
                       },
                       {
                       'branch.singlevar': 'false',
                       'branch.boundws': 'false',
                       },
                       {
                       'branch.singlevar': 'true',
                       'branch.boundws': 'false',
                       },
                       {
                       'branch.singlevar': 'false',
                       'branch.boundws': 'true',
                       }
                      ]]

branchdynruns = [ update_copy({
                 'solver.kind': 'PcpBranchAndCut',
                 
                 'solver.useCutCallback': 'true',
                 'solver.useHeuristicCallback': 'true',
                 'solver.useBranchingCallback': 'true',
                 'pruning.enabled': 'false',
                 'primal.enabled': 'false',
                 'branch.enabled': 'true',
                 'cuts.enabled': 'false',
                  
                 'solver.useCplexPrimalHeuristic': 'true',
                 'solver.maxTime': '900',
                 
                 'branch.selection': '1',
                 'branch.direction': '0',

                 'branch.prios.enabled': 'true',
                 'branch.prios.psize': '0',
                 'branch.prios.psadjacent': '10',
                 'branch.prios.colorindex': '-1',
                 'branch.dynamic.dsatur.nodelb': '0.7',
                 
               },d) for d in [

                {
                    'branch.dynamic.fractional': 'true',
                    'branch.dynamic.fractional.most': 'true',
                    'branch.dynamic.dsatur': 'false',
                },
                {
                    'branch.dynamic.fractional': 'true',
                    'branch.dynamic.fractional.most': 'false',
                    'branch.dynamic.dsatur': 'false',
                },
                {
                    'branch.dynamic.fractional': 'false',
                    'branch.dynamic.dsatur': 'true',
                    'branch.direction': '0',
                },
                
                ]   
            ]

branchstaticruns = list([ update_copy({
                 'solver.useCutCallback': 'false', 
                 'solver.kind': 'PcpBranchAndCut',
                 'solver.useHeuristicCallback': 'true',
                 'solver.useBranchingCallback': 'true',
                 'pruning.enabled': 'false',
                 'primal.enabled': 'false',
                 'branch.enabled': 'false',
                 'cuts.enabled': 'false',
                 
                 'solver.useCplexPrimalHeuristic': 'true',
                 'solver.maxTime': '900',
                 
                 'branch.prios.enabled': 'true',                 
                 'branch.direction': '0',
                 'branch.selection': '1', 
               },d) for d in [
                { 'branch.prios.enabled': 'false'},       
                {
                 'branch.prios.psize': '0',
                 'branch.prios.psadjacent': '10',
                 'branch.prios.colorindex': '1',
                },
                
                {
                 'branch.prios.psize': '0',
                 'branch.prios.psadjacent': '-10',
                 'branch.prios.colorindex': '-1',
                },
                
                {
                 'branch.prios.psize': '0',
                 'branch.prios.psadjacent': '-10',
                 'branch.prios.colorindex': '1',
                },
                {
                 'branch.prios.psize': '0',
                 'branch.prios.psadjacent': '10',
                 'branch.prios.colorindex': '-1',
                },
                {
                 'branch.prios.psize': '0',
                 'branch.prios.psadjacent': '1',
                 'branch.prios.colorindex': '-10',
                },
                ]   
            ])

branchsosruns = list([ update_copy({
                 'solver.useCutCallback': 'false', 
                 'solver.kind': 'PcpBranchAndCut',
                 'solver.useHeuristicCallback': 'true',
                 'solver.useBranchingCallback': 'true',
                 'pruning.enabled': 'false',
                 'primal.enabled': 'false',
                 'branch.enabled': 'false',
                 'cuts.enabled': 'false',
                 
                 'solver.useCplexPrimalHeuristic': 'true',
                 'solver.maxTime': '900',
                 
                 'branch.prios.enabled': 'true',                 
                 'branch.direction': '0',
                 'branch.selection': '1', 
               },d) for d in [
                { 
                    'branch.prios.enabled': 'false'
                },       
                { 
                    'branch.prios.enabled': 'false',
                    'model.useSOS': 'true',
                },
                {
                 'branch.prios.psize': '0',
                 'branch.prios.psadjacent': '10',
                 'branch.prios.colorindex': '-1',
                },
                ]   
            ])
