'''
Created on 15/03/2008

@author: Santiago
'''

from subprocess import *

def main():
    #command = "java -Djava.library.path=${cplex.path} ${run.properties} -jar ${ant.project.name}.jar ${run.inputfile} ${build.number} ${run.parameters}"
    command = "java -classpath pcp/build/classes/pcp;pcp.lib/lib pcp/build/classes/pcp/Main"
    print "Invoking:"
    p = Popen(command, stdout=PIPE, cwd="E:\\Workspace")
    print "Communicating:"
    out, err = p.communicate()
    print "Output:"
    print out
    print "Error:"
    print err
    print "Finished!"
    #print pipe
    

if __name__ == '__main__':
    main()