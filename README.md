
### How to run this application?

To run this Dockerfile, follow the steps below:

- **Step 1**: Install Docker on your system    
Before running this Dockerfile, you must have Docker installed on your system. To do so, follow the official Docker documentation for your operating system.

- **Step 2**: Navigate to the directory containing the Dockerfile.     
Open a terminal and navigate to the root directory of the project where Dockerfile is saved. 

- **Step 3**: Build the Docker image.    
Run the following command to build the Docker image (Make sure to include the dot (.) at the end of the command, which indicates the current directory!):    
``` docker build -t wolt-app . ```
- **Step 4**: Run the Docker container     
  After the image is built, run the following command to start the Docker container:      
  ``` docker run -p 8080:8080 wolt-app ```

Alternatively, to run the app, you can use the `docker-run.sh` script. [Click here](./docker-run.sh) to download the script.
This script builds the image and runs it for you. To make it executable using the ```chmod +x docker-run.sh``` command, and then run it using the ```./docker-run.sh``` command. 
Note that you need to be in the directory that contains the Dockerfile for this script to work.

And Postman collection for sample requests (restaurant_opening_hours.postman_collection.json) is located in the root directory of the project