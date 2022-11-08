git add ./ && git commit -m "#" && git push origin master

if [ $? -ne 0 ]; then
read -n 1
exit
fi

echo "Done"
echo "Have a nice day :)"
sleep 2