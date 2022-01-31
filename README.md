# RubbleRaiders
# Blades & Tomes

Blades & Tomes is a turn-based dungeon crawl game made with the libGDX engine
where you play as an adventurer, wishing to strike his fortune within the deep depths
of dark dungeons. To learn more about the project, please refer to the project
description on the wiki [here.](https://github.com/SCCapstone/RubbleRaiders/wiki/Project-Description)

## Requirements

To be able to run the app, please install the following programs:

* [Latest Version of Java](https://www.java.com/en/)
* [JDK version 12.0.2](https://www.oracle.com/java/technologies/javase/jdk12-archive-downloads.html)
* [IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/download/#section=Windows)

Please make sure you have a folder dedicated to containing the project as well as a willingness
to get "down and dirty" into Gradle and Java, as well as possibly XML and JSON.

**_IF YOU ARE RUNNING ON LINUX_**

Please install OpenJDK 8 using the following library path typed for Ubuntu derivatives:

`sudo apt install openjdk-8-jdk`

If you have another version already installed, after installing OpenJDK 8, type in the following commands:

* Gets all installed versions Java JDK <br>

`update-java-alternatives --list`

* Changes default library to selected version <br>

`sudo update-java-alternatives --set /path/to/altenative/version`

If the above does not work, please copy and paste the following commands into terminal before running:

`export JAVA_HOME=/path/to/JDK
export PATH=$JAVA_HOME/bin:$PATH`

Please refer to [this stack overflow page (comment by snesgx)](https://stackoverflow.com/questions/55847497/how-do-i-troubleshoot-inconsistency-detected-dl-lookup-c-111-java-result-12) for further information.

## Setup

To SetUp the local branch, first clone the branch to your workspace directory (let's say "~/my_work_directory").
After this step, then run IntelliJ and select "Open". Move to the "BladeAndTomes" folder and open the
"build.gradle" file. After this step, wait for the program to build the project before doing anything else.
Once the project has built, make sure to click the build icon in IntelliJ to make sure it is built.

## Running

To be able to run the program, please install the latest release of Java, as this utilizes the .jar extension.
A link to install Java is provided below:

https://www.java.com/en/

To install Java, click on the link and follow the instructions provided to get the latest version of Java SE.

For running the program for the first time, click on the "Gradle" side menu, then go to "desktop/other" and
click "Run". This will run the game and automatically update it for IntelliJ, so that each successive time you
run the game, all that needs to be done is click on the "Run" icon and the project will build and attempt to
run.

## Testing

In order to properly test, please make sure that the correct branch and correct version of the code is
available on your computer. Once everything is verified, check RubbleRaiders/BladeAndTomes/tests
to make sure it is present as well as to make sure GdxTestRunner.java (Made by Thomas Pronold)
(Source: https://github.com/TomGrill/gdx-testing) is within that branch as well as the
modules 'examples', 'UnitTests', and 'BehaviorTests'. Once all files are verified, run the
command "./gradlew tests:test" in the BladeAndTomes directory in order to run all tests at once through your CLI. The results will
be displayed within the console output, and if there are any failures, Gradle will tell you which ones failed if you
scroll up. If you wish to test individual methods, IntelliJ provides a simple testing GUI
that allows you to do single tests if you open the test file in IntelliJ and left click on the
green play button next to the method or class.

### Example Test
* Navigate to RubbleRaiders/BladeAndTomes/tests and check to see if the following are present:
  * Modules: 'examples', 'UnitTests', 'BehaviorTests'
  * GdxTestRunner.java above all classes
* Run the following command in BladeAndTomes directory in terminal:

`./gradlew tests:test`

* Alternatively, go into IntelliJ, navigate to 'tests' folder
* Select a test in it's modules
* Near the line numbers, a green play button will be next to the class and the function
* Click the class play button to run the whole class test.
* Or click the method test to test the individual method

# Authors

Aidan Emmons - akemmons@email.sc.edu

Brent Able - bpable@email.sc.edu

V.N Anirudh Oruganti - oruganti@email.sc.edu

Alex Facer - afacer@email.sc.edu

Miller Banford - banford@email.sc.edu
