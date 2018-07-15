## Work Flow Console App

__How to Run:__  
* Buils jar file - "mvn clean compile assembly:single"  
* Run jar file  
* Enter the path of your task, see example [here](https://github.com/werdnakof/WorkFlowApp/blob/master/tasks/task2.json)

__Key Features:__
* Utilises RxJava library to handle async calls and thread handling
* Publisher Subscriber pattern
* Functional approach:
	* Easier unit testing (simply testing for _objects going in and out_)
	* Side effects are contained in a single unit of function call.

Inspired by [Managing State with RxJava by Jake Wharton](https://www.youtube.com/watch?v=0IKHxjkgop4)

__Classes Overview:__
* App - entry point, compose functions together
* ConsoleReader - read console input emitt ConsoleEvent 
* WorkFlow - data object for storing work flows, contain list of Task
* Task - where instructions of what to execute are stored (hypothetically)
* WorkFlowExecution - data object for storing executions
* Executor 
	* Start executing tasks e.g. call external api, save/read data to remote db  
	* For demo purposes, if a Task name is "force-error", then return Failure status (see [task2.json](https://github.com/werdnakof/WorkFlowApp/blob/master/tasks/task2.json) for example) In reality any tasks not executed properly should return Failure status
* Transformers _(functions for converting data objects or performing side effects)_
	* getWorkFlow - convert an event to create WorkFlow
	* saveWorkFlow - save a WorkFlow to db
	* saveWorkFlowExecution - save WorkFlowExecution to db
	* updateWorkFlowExecution - update WorkFlowExecution with response status to db
	* generate - create a new WorkFlowExecution by Task
	* execute - execute a task in Executor
	* start - kick start a series of executions from a WorkFlow, returns a stream of Responses
* DBService - a mock in-memory store for WorkFlow and WorkFlowExecution

*__DB currently delays (sleeps) for 500 milli seconds for every save/update, to disable it either comment out Thread.sleep or set `addDelay` to false in DBService.java, and recompile a jar file__*
<!--stackedit_data:
eyJoaXN0b3J5IjpbLTE2NzU4Njk4MjUsLTcxMTIxMjQwNCwtND
UzNzAwMzUwLC0xNTgwMzcwNzY5LDE0MzYzMzY0NDNdfQ==
-->