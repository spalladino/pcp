import numpy
import pylab

def simplegraphic(x,y, labelx=None, labely=None):
    pylab.clf()
    pylab.scatter(x,y)
    pylab.plot(x,y)
    if labelx: pylab.xlabel(labelx)
    if labely: pylab.ylabel(labely)
    if labelx and labely: pylab.title("%s vs %s" % (labelx, labely))
    
def show():
    pylab.show()
    
def save(filename):
    pylab.savefig(filename)