import { resourceListLocation } from "./dataset-model-resourceList-location";
import { CustomFieldList } from "./dataset-model";

export class ResourceList{
    resourceId: string;
    resourceName: string;
    customFieldList: CustomFieldList[];
    skillIdList: string[];
    timeWindowIdList: string[];
    location: resourceListLocation;

}