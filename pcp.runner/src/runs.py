from config import *

import fetcher

def fetch_heur_files():
    return fetcher.Fetcher(datadir).fetch_files('benchdens', 'e0(2|4|6|8)n140\\.00[\\d]\\.in') + \
        fetcher.Fetcher(datadir).fetch_files('benchnodes', 'n((60)|(80)|(100)|(120)|(140)|(160)|(180)|(200))\\.00[\\d]\\.in') + \
        fetcher.Fetcher(datadir).fetch_files('benchpart', 'p[\\d]t[\\d]e05n140\\.00[\\d]\\.in') + \
        fetcher.Fetcher(datadir).fetch_files('holme', 'n140d0(1|2|3|4)\\.00[\\d]\\.in')

def fetch_model_files():
    return fetcher.Fetcher(datadir).fetch_files('benchdens', 'e0(2|4|6|8)n100\\.00[\\d]\\.in') + \
        fetcher.Fetcher(datadir).fetch_files('holme', 'n100d0(1|2|3|4)\\.00[\\d]\\.in')

def fetch_branch_files():
    return fetcher.Fetcher(datadir).fetch_files('benchdens', 'e0(2|4|6|8)n60\\.00(0|1|2)\\.in')

branchdynruns = [ {
                 'solver.useCutCallback': 'false', 
                 'solver.kind': 'PcpBranchAndCut',
                 'solver.useHeuristicCallback': 'false',
                 'solver.useCplexPrimalHeuristic': 'true',
                 'solver.useBranchingCallback': 'true',
                 'solver.maxTime': '900',
                 
                 'branch.prios.enabled': 'true',
                 'branch.selection': '1', # TODO: CHECK CPLEX DEFAULTS,
                 
                 'branch.prios.psize': '0',
                 'branch.prios.psadjacent': '10',
                 'branch.prios.colorindex': '1',
                 'branch.dynamic.dsatur.nodelb': '0.7',
                 
               }.copy().update(d) for d in [

                {
                    'branch.dynamic.fractional': 'true',
                    'branch.dynamic.dsatur': 'false',
                    'branch.direction': '0',
                },
                
                {
                    'branch.dynamic.fractional': 'false',
                    'branch.dynamic.dsatur': 'true',
                    'branch.direction': '0',
                },
                
                {
                    'branch.dynamic.fractional': 'true',
                    'branch.dynamic.dsatur': 'false',
                    'branch.direction': '1',
                },
                
                {
                    'branch.dynamic.fractional': 'false',
                    'branch.dynamic.dsatur': 'true',
                    'branch.direction': '1',
                },
                
                {
                    'branch.dynamic.fractional': 'true',
                    'branch.dynamic.dsatur': 'false',
                    'branch.direction': '-1',
                },
                
                {
                    'branch.dynamic.fractional': 'false',
                    'branch.dynamic.dsatur': 'true',
                    'branch.direction': '-1',
                },
                
                ]   
            ]

branchstaticruns = [ {
                 'solver.useCutCallback': 'false', 
                 'solver.kind': 'PcpBranchAndCut',
                 'solver.useHeuristicCallback': 'false',
                 'solver.useCplexPrimalHeuristic': 'true',
                 'solver.useBranchingCallback': 'false',
                 'solver.maxTime': '900',
                 
                 'branch.prios.enabled': 'true',                 
                 'branch.direction': '0',
                 'branch.selection': '1', # TODO: CHECK CPLEX DEFAULTS
               }.copy().update(d) for d in [
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
                ]   
            ]

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
