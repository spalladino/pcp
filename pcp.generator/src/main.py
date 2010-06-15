import generator, os

folder='D:/Tesis/Workspace/data/'

def main():
    basegen = generator.Generator(os.path.join(folder, 'holme'))
    for density in range(1,5,1):
        for instance in range(5):
            nodes = 80
            gen = basegen.with_name('n%dd%02d.%03d' % (nodes, density, instance))
            gen.holme_kim(nodes,float(density)/10.0)

if __name__ == '__main__':
    main()