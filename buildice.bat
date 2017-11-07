if not exist "c:/work/ice/%1" (mkdir "c:/work/ice/%1")
slice2%1 -I services/src/main/resources/zeroc --output-dir "C:/work/ice/%1" services/src/main/resources/zeroc/%2.ice