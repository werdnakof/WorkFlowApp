## Work Flow Console App
Key Features of the app:
* Utilise RxJava library to handle async calls and thread handling
* Publisher Subscriber pattern, events driven
* Functional approach 
	* Allow easier unit testing as you are simply testing for _things going in and out_
	* Side effects are avoided as often as possible. But if they do occur, they can be contained in a single unit of function call.

Classes descriptions:
* App - entry point, compose functions together
* ConsoleReader - read console input and emitt ConsoleEvents 
* Transformers - contains functions for converting data objects and side effects
	* getWorkFlow - currently read json file to create WorkFlow
	* saveWorkFlow - save a WorkFlow to db
	* saveWorkFlowExecution - save WorkFlowExecution to db
	* nextSteps - convert a WorkFlow to a sequence of WorkFlowExecutions to be executed
* DBService - a mock in-memory store for WorkFlow and WorkFlowExecution




<!--stackedit_data:
eyJoaXN0b3J5IjpbLTE1ODAzNzA3NjksMTQzNjMzNjQ0M119
-->