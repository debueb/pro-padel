#!/bin/bash 

HOUR=$(TZ="Europe/Berlin" date +"%H")
#if [ $HOUR == 11 ]
#then
TODAY=$(date +"%Y_%m_%d");

mysqldump -h $OPENSHIFT_MYSQL_DB_HOST -P $OPENSHIFT_MYSQL_DB_PORT -u $OPENSHIFT_MYSQL_DB_USERNAME --password=$OPENSHIFT_MYSQL_DB_PASSWORD $OPENSHIFT_APP_NAME > ${OPENSHIFT_TMP_DIR}/backup.sql
gzip -f ${OPENSHIFT_TMP_DIR}/backup.sql

${OPENSHIFT_REPO_DIR}/dropbox_uploader.sh -f ${OPENSHIFT_REPO_DIR}/dropbox_uploader.config mkdir ${TODAY}
./dropbox_uploader.sh upload ${OPENSHIFT_TMP_DIR}/backup.sql.gz ${TODAY}/${OPENSHIFT_APP_NAME}_mysql_${TODAY}.sql.gz
rm ${OPENSHIFT_TMP_DIR}/backup.sql.gz

mkdir ${OPENSHIFT_TMP_DIR}/data
cp -R ${OPENSHIFT_DATA_DIR} ${OPENSHIFT_TMP_DIR}/data
tar -zcf ${OPENSHIFT_TMP_DIR}/data_${TODAY}.tar.gz ${OPENSHIFT_TMP_DIR}/data

${OPENSHIFT_REPO_DIR}/dropbox_uploader.sh -f ${OPENSHIFT_REPO_DIR}/dropbox_uploader.config upload ${OPENSHIFT_TMP_DIR}/data_${TODAY}.tar.gz ${TODAY}/${OPENSHIFT_APP_NAME}_data_${TODAY}.tar.gz
rm ${OPENSHIFT_TMP_DIR}/data_${TODAY}.tar.gz
rm -rf ${OPENSHIFT_TMP_DIR}/data

#fi
