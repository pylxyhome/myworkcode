jlib="./bin";
pro=$2;

for jarfile in $1/*.jar;
do
	jlib=$jlib:$jarfile
done
echo $jlib
java -cp $jlib $pro
