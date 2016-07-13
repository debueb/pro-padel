#!/bin/bash

cd
DISK_USAGE=`du -s 2>/dev/null |awk '{print $1}'`

if [ ${DISK_USAGE} -gt 800000 ]
then mail -s "Disk quota has reached 80%" d.wisskirchen@gmail.com <<EOF
Disk quota has reached ${DISK_USAGE}
EOF
fi
