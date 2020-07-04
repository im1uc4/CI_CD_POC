import { resourceListLocation } from "./dataset-model-resourceList-location";
import { CustomFieldList } from "./dataset-model";

export class TaskList{
    taskId: string;
    taskName: string;
    startDateTime: string;
    taskDuration: number;
    resourceLocked: boolean;
    startDateTimeLocked: boolean;
    customFieldList:  CustomFieldList[];
    serviceId: string;
    timeWindowId: string;
    resourceId: string;
    location: resourceListLocation;

}