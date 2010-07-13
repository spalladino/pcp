from config import *

import fetcher

def update_copy(d1, d2):
    d = d1.copy()
    d.update(d2)
    return d

def fetch_heur_files():
    return fetcher.Fetcher(datadir).fetch_files('benchdens', 'e0(2|4|6|8)n140\\.00[\\d]\\.in') + \
        fetcher.Fetcher(datadir).fetch_files('benchnodes', 'n((60)|(80)|(100)|(120)|(140)|(160)|(180)|(200))\\.00[\\d]\\.in') + \
        fetcher.Fetcher(datadir).fetch_files('benchpart', 'p[\\d]t[\\d]e05n140\\.00[\\d]\\.in') + \
        fetcher.Fetcher(datadir).fetch_files('holme', 'n140d0(1|2|3|4)\\.00[\\d]\\.in')

def fetch_model_files():
    return fetcher.Fetcher(datadir).fetch_files('benchdens', 'e0(2|4|6|8)n100\\.00[\\d]\\.in') + \
        fetcher.Fetcher(datadir).fetch_files('holme', 'n100d0(1|2|3|4)\\.00[\\d]\\.in')

def fetch_branch_files():
    return fetcher.Fetcher(datadir).fetch_files('benchdens', '((e06n80)|(e02n90)|(e0(4|8)n100))\\.00(0|1|2)\\.in')

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
                {
                    'branch.dynamic.fractional': 'false',
                    'branch.dynamic.dsatur': 'true',
                    'branch.direction': '1',
                },
                {
                    'branch.dynamic.fractional': 'false',
                    'branch.dynamic.dsatur': 'true',
                    'branch.direction': '-1',
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

dsaturruns = [
              {
                'strategy.coloring': 'DSaturHardPartition',
                'dsatur.partition.weight.size': '1',
                'dsatur.partition.weight.colorCount': '10000',
                'dsatur.partition.weight.uncolored': '100'
              },
              {
                'strategy.coloring': 'DSaturHardPartition',
                'dsatur.partition.weight.size': '10000',
                'dsatur.partition.weight.colorCount': '1',
                'dsatur.partition.weight.uncolored': '100'
              },
              {
                'strategy.coloring': 'DSaturHardPartition',
                'dsatur.partition.weight.size': '1',
                'dsatur.partition.weight.colorCount': '100',
                'dsatur.partition.weight.uncolored': '10000'
              },
              {
                'strategy.coloring': 'DSaturHardPartition',
                'dsatur.partition.weight.size': '10000',
                'dsatur.partition.weight.colorCount': '100',
                'dsatur.partition.weight.uncolored': '1'
              },
                            {
                'strategy.coloring': 'DSaturHardPartition',
                'dsatur.partition.weight.size': '100',
                'dsatur.partition.weight.colorCount': '1',
                'dsatur.partition.weight.uncolored': '10000'
              },
              {
                'strategy.coloring': 'DSaturHardPartition',
                'dsatur.partition.weight.size': '100',
                'dsatur.partition.weight.colorCount': '10000',
                'dsatur.partition.weight.uncolored': '1'
              },
              {
                'strategy.coloring': 'DSaturEasyNodeRandomized'
              },
              {
                'strategy.coloring': 'DSaturEasyNode'
              },
             ]

modelruns = [{
                'strategy.partition': 'PaintAtLeastOne',
            },
            {
                'strategy.partition': 'PaintExactlyOne',
            },
            {
                'strategy.adjacency': 'AdjacentsLeqColor',
            },
            {
                'strategy.adjacency': 'AdjacentsLeqOne',
            },
            {
                'model.adjacentsNeighbourhood.useCliqueCover': 'false'
            },
            {
                'strategy.adjacency': 'AdjacentsPartitionLeqColor',
            },
            {
                'strategy.symmetry': 'None',
            },
            {
                'strategy.symmetry': 'VerticesNumber',
            },
            {
                'strategy.symmetry': 'MinimumNodeLabel',
            },
            {
                'strategy.colorBound': 'UpperNodesSum',
            },
            {
                'strategy.colorBound': 'UpperNodesSumLowerSum',
            },
            {
                'strategy.colorBound': 'UpperNodesSumLowerSumPartition',
            },
            {
                'strategy.symmetry': 'MinimumNodeLabel',
                'strategy.colorBound': 'UpperNodesSumLowerSumPartition',
            },
            {
                'strategy.objective': 'Linear',
            },
            {
                'strategy.objective': 'Geometric',
            }]
