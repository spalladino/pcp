import generator, os

folder='F:/Tesis/Workspace/data/'

def main():
    holme_basegen = generator.Generator(os.path.join(folder, 'holmefinal'))
    barabasi_basegen = generator.Generator(os.path.join(folder, 'barabasidens'))

    densities = {10: 20, 25: 40, 45: 60, 70: 80}
    for m in [10,25,45,70]:
        for p in range(0,10,2):
            for instance in range(1):
                for nodes in [90]:
                   
                    h_gen = holme_basegen.with_name('n%dd%02dp%02d.%03d' % (nodes, densities[m], p,instance))
                    h_gen.holme_kim(nodes,(m,float(p)/10.0,float(m)/float(nodes)))
                    
                    #b_gen = barabasi_basegen.with_name('n%dd%02d.%03d' % (nodes, 0.2, instance))
                    #b_gen.barabasi_albert(nodes,m)

if __name__ == '__main__':
    main()