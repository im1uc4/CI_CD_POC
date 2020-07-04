import { ResourceList } from "./dataset-model-resourceList";
import { ServiceList } from "./dataset-model-serviceList";
import { SkillList } from "./dataset-model-skillList";
import { TaskList } from "./dataset-model-taskList";
import { TimeWindowList } from "./dataset-model-timeWindowList";

export class DataSetModel{

    datasetId: string;
    name: string;
    description: string;
    scheduleDateTime: string;
    isMasterDataset: boolean;
    resourceList: ResourceList[];
    serviceList: ServiceList[];
    skillList: SkillList[];
    taskList: TaskList[];
    timeWindowList: TimeWindowList;

}


export class CustomFieldList{
    type: string;
    customFieldNum: number;
    name: string;
    customFieldType: string;
    mandatory: boolean;
    textValue: string;
}