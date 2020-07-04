import { CustomFieldList } from "./dataset-model";

export class TimeWindowList{
    timeWindowId: string;
    timeWindowName: string;
    startDateTime: string;
    endDateTime: string;
    customFieldList: CustomFieldList[];
}