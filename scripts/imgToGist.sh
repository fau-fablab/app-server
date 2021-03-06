#!/bin/bash

token=$1
repo_id=$2
inputfile=$3
filename=$4

#configure git for image upload
git config --global user.email "fablab@i2.cs.fau.de"
git config --global user.name "fablab-server"

git clone https://$token@gist.github.com/$repo_id.git
mv $inputfile $repo_id/$filename
cd $repo_id
git add $filename
git commit -m "Added $filename"
git push
cd ../
rm -rf $repo_id/
