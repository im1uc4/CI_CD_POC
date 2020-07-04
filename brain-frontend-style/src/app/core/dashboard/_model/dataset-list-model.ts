import { BaseModel } from '../../_base/crud';

export class DataSetList extends BaseModel {
    id?: number;
    model_id:number;  
    dataset_name:string;
    dataset_description:string;
    dataset_scheduledatetime:string;
    isMasterdataset:boolean; 
    status:boolean;
    created_by:string;
    updated_by:string;
    createdAt:string;
    updatedAt:string;

    clear() {
        this.id=undefined;
        this.model_id=undefined;       
        this.dataset_name='';
        this.dataset_description='';
        this.dataset_scheduledatetime='';
        this.isMasterdataset=true;
        this.status=true;
         this.created_by='';
        this.updated_by='';
        this.createdAt='';
        this.updatedAt='';
    }
}
