
# Flickwiz
The purpose of this project is to extract movies information by using movie poster.<br/><br/>
# Installation
<p>Prerequisites</p>
1. Maven (version > 3.0.5)<br>


# Adding dependencies
<p>Flickwiz have some dependencies, so before building the application you've to add them first.</p>
#### Note:
<p>Clone the Project to directory whose path doesn't contain blank spaces.</p>
1. clone the flickwiz repository on your machine.<br/>
`https://github.com/skunkworks002/flickwiz.git`<br/> 
2. <p>To add dependencies run the following commands from the root directory of flickwiz</p>
  <p>`mvn install:install-file -Dfile=src/lib/opencv-2411.jar -DgroupId=org.opencv -DartifactId=opencv-java -Dversion=2.4.11 -Dpackaging=jar`</p>

# How to build the application
1. <p>navigate to the Flickwiz directory and run the following command</p>
  `mvn clean compile package`<br>

# Running the application

### Running at the command line
<p>run the following command from the root directory</p>
`java -jar target/flickwiz-v1.0.jar`

### Running in your IDE (Eclipse)
1. Import Flickwiz project
<p>`File -> Import -> Existing Maven Projects`</p>
2. <p>Run `org.orbitsoft.flickwiz.service.Application.java`</p>

### Open your favorite web browser
Navigate to `http://localhost:9000/fickwiz/uploadImage`




## License
The code is licensed under the [Apache License Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
=======
# flickwiz

