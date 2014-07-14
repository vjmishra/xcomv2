#/xpedx/python/Python-2.7.3

#--old !/usr/bin/python
#Written By: Ashad Dean - HP    - 5/17/2012
#Version: 0.3
#Transfers directories and contents from one host to the other via ftp.
#parameters: 1. transfer path   - required  --  absolute from root or relative from run directory.
#            2. change date     - required  --  change date to filter, eg: 2012-05-17 means any files changed from
#                                               5/17/2012 12am.
#            3. filter ext      - optional
#            4. --scan          - optional  --  scan only
# usage: ftpclient.py [-h] [-c CHGDATE] [-f FILTER] [--scan] path ftproot
#Example: to sync up files from /home/share/xpadmin/purgelogs/ to the ftp site below, call this command:
#1- To just scan what files will be uploaded:
#/xpedx/python/Python-2.7.3/bin/python2.7 /home/share/xpadmin/scripts/ftpclient.py -c '2012-07-01 00:00' -f ".jsp,.java,.txt" --scan /home/share/xpadmin/purgelogs /testing
#2- To actually do the upload
# /xpedx/python/Python-2.7.3/bin/python2.7 /xpedx/tmp/purgelogs/ftpclient.py -c '2012-07-01 00:00' -f ".jsp,.java,.txt,.py" /xpedx/tmp/purgelogs /testing
#/xpedx/python/Python-2.7.3/bin/python2.7 /xpedx/tmp/purgelogs/ftpclient_original.py -c '2012-07-01 00:00' -f ".jsp,.java,.txt,.py" /xpedx/tmp/purgelogs /testing

# for a quick python tutorial see this good resource http://www.tutorialspoint.com/python/python_quick_guide.htm
import os
import sys
import ftplib
import time
import argparse

#note: You must change the ftp information below
host="s29exvmbuild.na.ipaper.com"
port=21
login="xpedxqa"
passwd="password.1"
filelist=[]
fileprlist=[]

def visit(arg, dirname, names):
    global filelist,fileprlist
    #print dirname, arg
    chkdate = arg[0]
    ffilter = arg[1]
    for name in names:
        subname = os.path.join(dirname, name)
        if os.path.isdir(subname):
            print 'scanning  %s/' % name
        else:
            try:
                if os.path.getctime(subname) > chkdate and len(filter(lambda x: subname.endswith(x),ffilter)) == 0:
                    fileprlist += [subname + " --> updated: " + time.ctime(os.path.getctime(subname))]
                    filelist += [subname]
                #else:
                    #print "skipped " +  subname + " " + time.ctime(os.path.getmtime(subname))
            except:
                printError("Error accessing " + subname)

def ftpUpload():
    global filelist,ftproot
    curpath = ftproot
    print "ftproot = " + ftproot
    filename = ""
    try:
        #Logon to the FTP server.
        ftp = ftplib.FTP()
        ftp.connect(host,port)
        print ftp.getwelcome()
        try:
            ftp.login(login, passwd)
            print "ftpUpload() - logged in as " + login
            #Change to the root directory
            ftp.cwd(ftproot)
            #print "ftpUpload() - Changed directory to ftproot"
            #print "ftpUpload() - current directory is: " + ftp.pwd()
            #print "ftpUpload() - looping through files to upload..."
            for filepath in filelist:
                try:
                    print "ftpUpload() - Processing " + filepath + " ..."
                    try:
                        psplit = filepath.rsplit("/",1)
                        #print "finished split... "
                    except Exception, err:
                        #print "looks like rsplit is deprecated. "
                        sys.stderr.write('ftpUpload - ERROR trying to create directory: %s\n' % str(err))
                        return 1
                    #print "ftpUpload() - about to call len(psplit) "
                    #print "ftpUpload() - len(psplit) = " + str(len(psplit))
                    #print "ftpUpload() - finished call to len(psplit) "
                    #print "ftpUpload() - psplit[0] = " + psplit[0]

                    if len(psplit) == 2 and psplit[0] != curpath:
                        #print "ftpUpload() - in if - len(psplit)= 2"
                        #print "ftpUpload() - psplit[1] = " + psplit[1]
                        #Add "/" root qualifier to file name since walk function doesn't include / in the file names.
                        curpath = "/" + psplit[0]
                        filename = psplit[1]
                        #attempting to change to directory first time
                        try:
                            #print "ftpUpload() - curpath = " + curpath
                            #print "ftpUpload() - About to change directory from " + ftp.pwd() + " to " + curpath
                            #createChgFtpDir(ftp,curpath)
                            ftp.cwd(curpath) # dont change directories here since we already did in the above function
                            print "ftpUpload() - changed directory to " + curpath
                        except:
                            createChgFtpDir(ftp,curpath)
                    elif len(psplit) == 1:
                        filename = filepath
                        #print "ftpUpload() - len(psplit) = 1 and filename = " + filename
                    else:
                        #print "ftpUpload() - len(psplit) = " + len(psplit) + " and filename = " + str(psplit[1])
                        filename = psplit[1]
                    print "ftpUpload() - transfering " + filename + " ..."
                    f = open(filepath, "rb")
                    ftp.storbinary('STOR ' + filename, f)
                    print "ftpUpload() - finished transfering " + filename + " ..."
                except:
                    printError("Error Transfering " + filepath)
                try:
                    f.close()
                except:
                   pass

        except:
            printError("ftpUpload() - transfer failed")
    except:
        printError("ftpUpload() - connection failed")
    finally:
        ftp.close()

        
        
def createChgFtpDir(ftp,fpath):
    print "in createChgFtpDir function"
    global ftproot
    theParentpath=""
    theSubDir=""

    print "createChgFtpDir - ftproot = " + ftproot
    print "createChgFtpDir - fpath = " + fpath
    try:
        #First split the directories.
        psplit = fpath.split("/")
        #next change to root directory
        ftp.cwd(ftproot)
        #next: split the path and start creating directories starting from root
        for dirName in psplit:
            #print "createChgFtpDir - in for loop: processing dirName: " + dirName
            try:
                #first check if the directory exists by cd'ing to it
                ftp.cwd(dirName)
            except:
                #print "createChgFtpDir - for loop - except: directory" + dirName + " not found. Let's create it. "
                try:
                    #create the dir
                    ftp.mkd(dirName)
                    print "created directory: " + dirName
                    #cd to it immediately after having created it                    
                    ftp.cwd(dirName)
                    print "changed directory to: " + dirName
                    #print "createChgFtpDir - for loop - except: now directory is set to: " + ftp.pwd()
                except Exception, err:
                    sys.stderr.write('createChgFtpDir - except: ERROR trying to create directory:' + dirName + ' %s\n' % str(err))
                #finally:
                    #print "returning from createChgFtpDir..."   
    except Exception, err:
        sys.stderr.write('createChgFtpDir - except: ERROR trying to switch directory to :' + fpath + ' %s\n' % str(err))
def printError(e):
    on = True
    if on:
        print e
        #print "Exception--> " + str(sys.exc_info()[0])
         


parser = argparse.ArgumentParser(description='Upload directory structure to an FTP location.')
parser.add_argument('path', metavar='path', help='absolute or relative directory path to upload')
parser.add_argument('ftproot',metavar='ftproot', help="ftp root the upload should be placed")
parser.add_argument('-c', dest='chgdate', default=time.strftime("%Y-%m-%d %H:%M"),
    help='Enter the date filter - any file changed after <yyyy-mm-dd> 12am')
parser.add_argument('-f', dest='filter', default=" ",
    help='Enter the file filter to exclude, file extensions seperated by a comma - eg: ".txt,.jpg,.jsp"')
parser.add_argument('--scan', dest="scan_only", const=True, default=False, action="store_const",
    help='add this if you want just to scan changed files without uploading to ftp')

args = parser.parse_args()
path = args.path
ffilter = args.filter.split(",")
lastupdate = time.mktime(time.strptime(args.chgdate,"%Y-%m-%d %H:%M"))
ftproot= args.ftproot

if len(path.split("/")) > 1:
    try:
        print "main() - about to chg path to " + path
        os.chdir(path.rsplit("/",1)[0])
        path = path.rsplit("/",1)[1]
        print "main() - path was set to:  " + path
    except:
        printError("Invalid directory - please specify a absolute path from root or relative path including " +
                   "the director you want to transfer")
        exit(1)
print "** scanning started **"
os.path.walk(path,visit,[lastupdate,ffilter])
print "** scanning complete **"
for f in fileprlist: print f
print str(len(filelist)) + " Files need uploading"
if len(filelist) > 0:
    if args.scan_only:
        print "Scan only - no ftp upload"
    else:
        print "Uploading to FTP"
        ftpUpload()
        print "FTP uploading completed"
