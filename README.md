# art
## Unix command line to rename all files by removing prefix
`for f in TestSR*; do mv "$f" "CL${f#TestSR}"; done`
## JPEG Optimization Linux
`apt-get install jpegoptim`
`jpegoptim filename.jpeg`
