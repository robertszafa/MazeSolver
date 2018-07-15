# MazeSolver
![solution image](mazes/SOLUTION-maze7.gif)
![statistics image](assets/maze7-statistics-screenshot.png)


## About
A maze solver written in Java. The goal of this project was to implement various search algorithms. For now, this are A*, Breadth First Search and Depth First Search. I will try to implement more algorithms in the future.<br>

## How to run
### Compile
- download the project
- compile the src folder
### Run
- from the src directory type <b>java MazeSolver ../mazes/<i>"maze-file-name"</i></b>
- if you want to run only individual algorithms, add a second parameter:<br>
  d - Depth First Search<br>
  b - Breadth First Search<br>
  a - A* Search<br>
  For example:<br>
  <b>java MazeSolver ../mazes/<i>"maze-file-name"</i> bd</b> - runs only the BFS and DFS search

## What mazes can it solve?
I have included a couple of maze examples in the mazes folder, however, feel free to add your own mazes.<br>
<b>Use <a href="http://hereandabove.com/maze/mazeorig.form.html">this website</a> to generate your own mazes and use these parameters:</b><br>
- path width: 1
- wall width: 1
- wall color: 0 0 0
- path color: 255 255 255
