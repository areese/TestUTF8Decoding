
cd .. && gradle publishToMavenLocal
cd TestProject
gradle build
javah -cp build/classes/main com.yahoo.example.test.SampleJni
mv com_yahoo_example_test_SampleJni.h src/main/native/
