group = "com.example"
version = "0.0.1-SNAPSHOT"

tasks{
    val buildAndDeploy by registering(){
        dependsOn( ":sample-func:shadowJar" )
        doLast{
            project( ":cdk" ).exec{
                commandLine( "cdk deploy CdkStack -v".split( " "))
            }

            project( "sample-func" )
        }
    }
}
