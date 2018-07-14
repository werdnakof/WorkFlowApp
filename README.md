## Work Flow Console App

__How to Run:__  
* Buils jar file - "mvn clean compile assembly:single"  
* Run jar file  
* Enter the path of your task, see example [here](https://github.com/werdnakof/WorkFlowApp/blob/master/tasks/task2.json)

__Key Features:__
* Utilises RxJava library to handle async calls and thread handling
* Publisher Subscriber pattern
* Functional approach:
	* Easier unit testing (only simply testing for _objects going in and out_)
	* Side effects are avoided. But if they do occur, they can be contained in a single unit of function call.

Inspired by [Managing State with RxJava by Jake Wharton](https://www.youtube.com/watch?v=0IKHxjkgop4)

__Classes Descriptions:__
* App - entry point, compose functions together
* ConsoleReader - read console input and emitt ConsoleEvents 
* WorkFlow - data object for storing work flows
* WorkFlow - data object for storing work flows
* Executor - executing tasks (fake)
* Transformers - functions for converting data objects and/or side effects
	* getWorkFlow - currently read json file to create WorkFlow
	* saveWorkFlow - save a WorkFlow to db
	* saveWorkFlowExecution - save WorkFlowExecution to db
	* updateWorkFlowExecution - update WorkFlowExecution with response status to db
	* execute - carry out task in Executor
	* start - kick a series of executions from a WorkFlow, returns a stream of Responses
* DBService - a mock in-memory store for WorkFlow and WorkFlowExecution




<!--stackedit_data:
eyJoaXN0b3J5IjpbMTY2NjE5MDYxMCwtMTU4MDM3MDc2OSwxND
M2MzM2NDQzXX0=
-->