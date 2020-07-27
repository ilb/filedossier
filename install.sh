set -e
mkdir -p /home/filedossier/packages
mkdir -p /var/apps/filedossier/files
cp -r filedossier-web/src/main/resources/models/* /home/filedossier/packages
cp -r filedossier-web/src/main/resources/teststoreroot /var/apps/filedossier/files