docker-compose build

docker-compose up -d

## Combined command
docker-compose up --build -d

##logs of container
docker logs containerid


##Enter in container 
docker exec -it containerid  sh

## Login default admin

curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"username":"admin","password":"P@ssw0rd123456"}' \
  http://localhost:3000/api/v1/signin



## Import users

curl --header "Content-Type: application/json" \
  --request POST \
  --data '{
  "userlist": [
    {
      "duty title": "string",
      "email": "string",
      "fullname": "string",
      "group": "general",
      "name": "string",
      "password": "sushil",
      "user role": [
        "Module User"
      ],
      "username": "sushil"
    }
  ]
}' \
  http://localhost:3000/api/v1/import_user



## Import Constraints Type

curl --header "Content-Type: application/json" --header "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImZ1bGxuYW1lIjoiQWRtaW5pc3RyYXRvciIsImV4cCI6MTU5NDA0NTUyMiwiaWF0IjoxNTkzOTU5MTIyLCJlbWFpbCI6ImVtYWlsQGVtYWlsLmNvbSIsImF1dGhvcml0aWVzIjpbIlJPTEVfTW9kdWxlIFVzZXIiLCJ1c2VybmFtZTphZG1pbiJdLCJ1c2VybmFtZSI6ImFkbWluIn0.Wt34kKdRCg7LyX0lH_WhIYsLvnJRtf534dGehBDQx6YUa2gw5UaazzdMdjjyejGnYHxZiGYqX0lP1VFNsGd9Ng"\
  --request POST \
  --data '{
	"constraintTypes":[
    {
        "constraintTypeId": "RESOURCE_SKILL",
        "constraintTypeName": "Resource Skill",
        "defaultDisplayName": "Check that the resource assigned to a task is skilled in the service required by the task.",
        "parameterList": [
            {
                "parameterType": "FIELD_VALUE_LIST",
                "parameterId": "TASK_FILTER",
                "parameterNum": 1,
                "name": "Task Filter",
                "parameterHiddenConditionList": null,
                "possibleTableNames": [],
                "possibleCustomFieldTypes": [],
                "fieldValueList": null
            }
        ]
    },
        {
        "constraintTypeId": "TASKS_OVERLAP",
        "constraintTypeName": "Tasks Overlap",
        "defaultDisplayName": "Check that the task assigned blah blah.",
        "parameterList": [
            {
              "parameterId": "OPERATION",
              "parameterNum": 1,
              "name": "Operation",
              "parameterType": "TEXT",
              "value": "Prefer Separate",
              "possibleValues": [
                "Prefer Overlap",
                "Prefer Separate"
              ]
            },
            {
              "parameterId": "TASK_FILTER",
              "parameterNum": 2,
              "name": "Task Filter",
              "parameterType": "FIELD_VALUE_LIST",
              "possibleTableNames": [],
              "possibleCustomFieldTypes": []
            },
            {
              "parameterId": "TASK_GROUPING",
              "parameterNum": 3,
              "name": "Task Grouping",
              "parameterType": "FIELD_NAME_LIST",
              "possibleTableNames": [],
              "possibleCustomFieldTypes": [],
              "fieldNameList": [
                {
                  "tableName": "Resource",
                  "fieldName": "resourceId"
                }
              ]
            }
          ]
    }
]
}' \
  http://localhost:3000/api/v1/import_constraint_types






## Import Model

curl --header "Content-Type: application/json" --header "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImZ1bGxuYW1lIjoiQWRtaW5pc3RyYXRvciIsImV4cCI6MTU5NDA0NTUyMiwiaWF0IjoxNTkzOTU5MTIyLCJlbWFpbCI6ImVtYWlsQGVtYWlsLmNvbSIsImF1dGhvcml0aWVzIjpbIlJPTEVfTW9kdWxlIFVzZXIiLCJ1c2VybmFtZTphZG1pbiJdLCJ1c2VybmFtZSI6ImFkbWluIn0.Wt34kKdRCg7LyX0lH_WhIYsLvnJRtf534dGehBDQx6YUa2gw5UaazzdMdjjyejGnYHxZiGYqX0lP1VFNsGd9Ng"\
  --request POST \
  --data '{
"optimisationModel": {
    "optimisationModelId":"ID- Biopolis 1",
    "versionNum": 1,
    "name": "Name is Biopolis 1",
    "titleDescription": "Title is Biopolis 1",
    "description": "Descreption is Biopolis 1",
    "customFieldName": {
      "person_in_charge": "Admin"
    },
    "constraintList": [
      {
        "constraintId": "Hard Constraint 1",
        "constraintNum": 1,
        "displayName": "Resources can only be assigned to Services that they are skilled in doing",
        "scoreLevel": "HARD",
        "scoreWeight": 1,
        "constraintType": {
          "constraintTypeId": "RESOURCE_SKILL",
          "constraintTypeName": "Resource Skill",
          "defaultDisplayName": "Check that the resource assigned to a task is skilled in the service required by the task.",
          "parameterList": [
            {
              "parameterId": "TASK_FILTER",
              "parameterNum": 1,
              "name": "Task Filter",
              "parameterType": "FIELD_VALUE_LIST",
              "possibleTableNames": [],
              "possibleCustomFieldTypes": []
            }
          ]
        }
      },
      {
        "constraintId": "Hard Constraint 2",
        "constraintNum": 2,
        "displayName": "Don't assign Resources to multiple Tasks concurrently",
        "scoreLevel": "HARD",
        "scoreWeight": 1,
        "constraintType": {
          "constraintTypeId": "TASKS_OVERLAP",
          "constraintTypeName": "Tasks Overlap",
          "defaultDisplayName": "Check if any pair of tasks are overlapping.",
          "parameterList": [
            {
              "parameterId": "OPERATION",
              "parameterNum": 1,
              "name": "Operation",
              "parameterType": "TEXT",
              "value": "Prefer Separate",
              "possibleValues": [
                "Prefer Overlap",
                "Prefer Separate"
              ]
            },
            {
              "parameterId": "TASK_FILTER",
              "parameterNum": 2,
              "name": "Task Filter",
              "parameterType": "FIELD_VALUE_LIST",
              "possibleTableNames": [],
              "possibleCustomFieldTypes": []
            },
            {
              "parameterId": "TASK_GROUPING",
              "parameterNum": 3,
              "name": "Task Grouping",
              "parameterType": "FIELD_NAME_LIST",
              "possibleTableNames": [],
              "possibleCustomFieldTypes": [],
              "fieldNameList": [
                {
                  "tableName": "Resource",
                  "fieldName": "resourceId"
                }
              ]
            }
          ]
        }
      }
    ]
  }
}' \
  http://localhost:3000/api/v1/import_model
