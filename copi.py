import shutil
import sys
argv = sys.argv
in_file = argv[1]
shutil.copy("temp.txt",in_file)