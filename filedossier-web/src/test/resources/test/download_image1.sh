set -e
trap 'echo TEST ERROR' ERR

curl -u ide:123 -o image1.jpg http://localhost:8080/filedossier-web/web/dossiers/teststorekey/testmodel/TEST/mode1/dossierfiles/image1
cmp image1.jpg page1.jpg
echo TEST OK
