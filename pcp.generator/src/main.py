import generator, os

folder='E:/Workspace/data/'

def main():
    holme_basegen = generator.Generator(os.path.join(folder, 'holme'))
    barabasi_basegen = generator.Generator(os.path.join(folder, 'barabasi'))
    
    for density in range(1,5,1):
        for instance in range(5):
            nodes = 20
            
            h_gen = holme_basegen.with_name('n%dd%02d.%03d' % (nodes, density, instance))
            h_gen.holme_kim(nodes,float(density)/10.0)
            
            b_gen = barabasi_basegen.with_name('n%dd%02d.%03d' % (nodes, density, instance))
            b_gen.barabasi_albert(nodes,float(density)/10.0)

if __name__ == '__main__':
    main()