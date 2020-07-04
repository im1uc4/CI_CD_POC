import { CustomFieldList } from "./dataset-model";

export class ServiceList{
    serviceId: string;
    serviceName: string;
    defaultTaskDuration: number;
    customFieldList: CustomFieldList[];
    skillIdList: string[];
    
}