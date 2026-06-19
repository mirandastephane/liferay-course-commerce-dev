\# Liferay Commerce Developer



\## Classroom Training Prerequisites



Please review the course prerequisites \[here](https://www.liferay.com/training/classroom-prerequisites).



\## Setting up environments



Here are some simple instructions to prepare your environment before the training starts.



1\. Clone the repository to your computer.



&#x20;   ```

&#x20;   git clone https://github.com/liferay/liferay-course-commerce-dev.git

&#x20;   ```



&#x20;  Once cloned, navigate into the project's folder.



&#x20;   ```

&#x20;   cd liferay-course-commerce-dev

&#x20;   ```



1\. Perform a git fetch, a day before the training starts, to ensure you have the latest version.



&#x20;   ```

&#x20;   git fetch origin

&#x20;   ```



1\. Checkout the main branch.



&#x20;   ```

&#x20;   git checkout main

&#x20;   ```



1\. To ensure you have the most recent content for main branch, run the following command.



&#x20;   ```

&#x20;   git pull origin main

&#x20;   ```



1\. In the terminal, navigate to the root folder of the repository and run the following command.



&#x20;   ```

&#x20;   cd liferay-course-commerce-dev

&#x20;   ```



&#x20;   ```

&#x20;   blade gw initBundle

&#x20;   ```



&#x20;  Or



&#x20;   ```

&#x20;   ./gradlew initBundle

&#x20;   ```



1\. After a successful build, run the 'blade server run' command to start your server.



&#x20;   ```

&#x20;   blade server run

&#x20;   ```



&#x20;  In case you don't have blade installed, run the following command from the workspace's root folder.



&#x20;   ```

&#x20;   ./bundles/tomcat/bin/catalina.sh run

&#x20;   ```



Now, a clean Liferay environment should have started.



1\. When finished, access your Liferay DXP instance by going to `localhost:8080` in your browser.



1\. Sign in using these credentials.



&#x20;   \* Username: `admin@clarityvisionsolutions.com`

&#x20;   \* Password: `learn`



Once logged in, access the Control Panel \&rarr; Search \&rarr; Index Actions \&rarr; Execute Full Reindex.



And voilà! You're ready to dive into the classroom training!



<!-- If you get curious and take a look at the repository, you'll notice it contains a branch named `final`. 

&#x20;    This branch has been created to show you the finished version of the course. -->





