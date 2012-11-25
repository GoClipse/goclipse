#!/usr/bin/env python
#
# A script which will build cross-platforms copies of gocode.
#
# Usage: build_gocode.py [clean]

import platform
import os
import shutil
import subprocess
import sys

from os.path import join, realpath, exists

GOROOT = realpath('goroot')
GOCODE = realpath('gocode')

def BuildGoRoot():
  ExecCmd(["hg", "clone", "-u", "release",
           "https://code.google.com/p/go", GOROOT])
  
  ExecCmd(["./all.bash", "--no-clean"],
          {"GOARCH": "386"},
          join(GOROOT, 'src'))
  ExecCmd(["./all.bash", "--no-clean"],
          {"GOARCH": "amd64"},
          join(GOROOT, 'src'))
  
  ExecCmd(["./make.bash", "--no-clean"],
          {"CGO_ENABLED":"0", "GOOS":"darwin", "GOARCH":"386"},
          join(GOROOT, 'src'))
  ExecCmd(["./make.bash", "--no-clean"],
          {"CGO_ENABLED":"0", "GOOS":"darwin", "GOARCH":"amd64"},
          join(GOROOT, 'src'))
  ExecCmd(["./make.bash", "--no-clean"],
          {"CGO_ENABLED":"0", "GOOS":"linux", "GOARCH":"386"},
          join(GOROOT, 'src'))
  ExecCmd(["./make.bash", "--no-clean"],
          {"CGO_ENABLED":"0", "GOOS":"linux", "GOARCH":"amd64"},
          join(GOROOT, 'src'))
  ExecCmd(["./make.bash", "--no-clean"],
          {"CGO_ENABLED":"0", "GOOS":"windows", "GOARCH":"386"},
          join(GOROOT, 'src'))
  ExecCmd(["./make.bash", "--no-clean"],
          {"CGO_ENABLED":"0", "GOOS":"windows", "GOARCH":"amd64"},
          join(GOROOT, 'src'))


def BuildGoCode():
  if not exists(GOCODE):
    os.mkdir(GOCODE)
  
  ExecGo(["get", "-u", "github.com/nsf/gocode"], {}, GOCODE)

  BuildGoCodePlatform('darwin', '386')
  BuildGoCodePlatform('darwin', 'amd64')
  
  BuildGoCodePlatform('linux', '386')
  BuildGoCodePlatform('linux', 'amd64')
  
  BuildGoCodePlatform('windows', '386')
  BuildGoCodePlatform('windows', 'amd64')


def BuildGoCodePlatform(goOs, goArch):
  print "Building gocode for %s/%s" % (goOs, goArch)
  
  if goOs is 'windows':
    binaryName = 'gocode.exe'
  else:
    binaryName = 'gocode'
  
  ExecGo(["build",
          "-o",
          join('..', 'tools', goOs + '_' + goArch, binaryName),
          "github.com/nsf/gocode"],
         {"CGO_ENABLED": "0", "GOOS": goOs, "GOARCH": goArch},
         GOCODE)


def Clean():
  print "cleaning goroot and gocode"
  shutil.rmtree(GOROOT, ignore_errors=True)
  shutil.rmtree(GOCODE, ignore_errors=True)


def IsWindows():
  return platform.system() is 'Windows'


def ExecGo(arguments, environment=None, directory=None):
  arguments.insert(0, 'go')
  
  if environment is None:
    environment = {}
    
  environment['GOROOT'] = GOROOT
  environment['GOPATH'] = GOCODE
  environment['PATH'] = join(GOROOT, 'bin') + ':' + os.getenv('PATH')
  
  ExecCmd(arguments, environment, directory)
  
  
def ExecCmd(arguments, environment=None, directory=None):
  if environment is None:
    e = os.environ
  else:
    e = os.environ.copy()
    e.update(environment)
    
  p = subprocess.Popen(arguments,
                       shell=IsWindows(),
                       cwd=directory,
                       env=e)
  retCode = p.wait()
  
  if retCode != 0:
    raise Exception("error running %s" % arguments)
  

def Main(argv):
  # handle args
  if len(argv) > 1:
    if len(argv) == 2 and argv[1] == 'clean':
      Clean()
      return 0
    else:
      print "only the 'clean' arg is allowed"
      return 1

  # check for a rebuild of goroot
  if not exists(GOROOT):
    BuildGoRoot()

  BuildGoCode()
  

if __name__ == '__main__':
  sys.exit(Main(sys.argv))

