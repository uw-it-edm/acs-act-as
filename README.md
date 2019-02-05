# Alfresco Platform JAR Module - SDK 3

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/d708fdf2a35342eda0fbe8d690e2cb69)](https://app.codacy.com/app/uw-it-edm/acs-act-as?utm_source=github.com&utm_medium=referral&utm_content=uw-it-edm/acs-act-as&utm_campaign=Badge_Grade_Dashboard)

# Create release 

Travis will create a new GitHub release when a new tag is detected.
To create new tag, run these commands on your local machine : 

    git flow release start x.x.x
    mvn versions:set -DnewVersion=x.x.x -DgenerateBackupPoms=false
    git add pom.xml
    git commit -m "Bump version"
    git flow feature finish 
    git push origin master develop
    
Once this is done, prepare develop for the next devolpment cycle by updating the pom to use a `SNAPSHOT` version

    mvn versions:set -DnewVersion=x.x.x-SNAPSHOT -DgenerateBackupPoms=false
    git add pom.xml
    git commit -m  "Prepare for next development cycle"
    git push origin develop 


# Configure access to Alfresco Private Repository  

follow this to create a master password and encrypt the alfresco password : https://maven.apache.org/guides/mini/guide-encryption.html

Then add the repository in your `.m2/settings.xml` following this : https://docs.alfresco.com/4.2/tasks/dev-extensions-maven-sdk-tutorials-configure-maven-enterprise.html




#Alfresco SDK does not support Alfresco 6.X, the directions below are out of date


To run use `mvn clean install -DskipTests=true alfresco:run` or `./run.sh` and verify that it 

 * Runs the embedded Tomcat + H2 DB 
 * Runs Alfresco Platform (Repository)
 * Runs Alfresco Solr4
 * Packages both as JAR and AMP assembly
 
 Try cloning it, change the port and play with `enableShare`, `enablePlatform` and `enableSolr`. 
 
 Protip: This module will work just fine as a Share module if the files are changed and 
 if the enablePlatform and enableSolr is disabled.
 
# Few things to notice

 * No parent pom
 * WAR assembly is handled by the Alfresco Maven Plugin configuration
 * Standard JAR packaging and layout
 * Works seamlessly with Eclipse and IntelliJ IDEA
 * JRebel for hot reloading, JRebel maven plugin for generating rebel.xml, agent usage: `MAVEN_OPTS=-Xms256m -Xmx1G -agentpath:/home/martin/apps/jrebel/lib/libjrebel64.so`
 * AMP as an assembly
 * [Configurable Run mojo](https://github.com/Alfresco/alfresco-sdk/blob/sdk-3.0/plugins/alfresco-maven-plugin/src/main/java/org/alfresco/maven/plugin/RunMojo.java) in the `alfresco-maven-plugin`
 * Resources loaded from META-INF
 * Web Fragment (this includes a sample servlet configured via web fragment)
 
# TODO
 
  * Abstract assembly into a dependency so we don't have to ship the assembly in the archetype
  * Purge, 
  * Functional/remote unit tests
   
  
 
# Configure Intellij : 

https://docs.alfresco.com/5.2/tasks/sdk-develop-intellij.html

