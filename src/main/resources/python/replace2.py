import os
import re
import shutil
import sys

def subFile(filePath,oldWord,newWord) :
    #(1) subFileName
    subFileName(filePath,oldWord,newWord);
    #(2) subContent
    fopen=open(filePath,'r')
    w_str=''
    for line in fopen:
        if re.search(oldWord,line):
            line=re.sub(oldWord,newWord,line)
            w_str+=line
        else:
            w_str+=line
    fopen.close()
    wopen=open(filePath,'w')
    wopen.write(w_str)
    wopen.close()

#subFileName
def subFileName(filePath,oldWord,newWord) :
    parentPath = os.path.split(filePath)[0]
    fileName = os.path.split(filePath)[1]
   # if oldWord in fileName :
    if re.search(oldWord,fileName) :
        newFileName = re.sub(oldWord,newWord,fileName)
        newPath = parentPath + "/" + newFileName
        os.rename(filePath,newPath)
        return newPath
    else :
        return filePath

def subDir(dirPath,oldWord,newWord) :
    list = os.listdir(dirPath)
    for x in list :
        subPath = dirPath+"/"+x
        subPath = subFileName(subPath,oldWord,newWord)
        if os.path.isdir(subPath) :
            if x.startswith('.') :
                shutil.rmtree(subPath)
            else :
                subDir(subPath,oldWord,newWord)
        else :
            subFile(subPath,oldWord,newWord)

def checkDir(path) :
    if not path :
        return
    elif path.endswith("/") :
        return
    elif not os.path.isdir(path) :
        return
    else :
        return "true"

def deploy(dirPath,appName) :
    oldAppName = "\{APPNAME\}"
    subDir(dirPath,oldAppName,appName)

argSize = len(sys.argv)
if argSize < 3 :
    print "please entry apppath and oldappname and newappname!"
elif not checkDir(sys.argv[1]) :
    print "apppath is wrone,don't end '/'"
elif not sys.argv[2] :
    print "oldappname not allow blank!"
elif not sys.argv[3] :
    print "newappname not allow blank!"
else :
    subDir(sys.argv[1],sys.argv[2],sys.argv[3])
    #deploy(sys.argv[1],sys.argv[2])
    print "replace oldWord:"+sys.argv[2]+" to newWord:"+sys.argv[3]+" ok"







