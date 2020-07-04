import { BaseModel } from '../../_base/crud';

export class JobsList extends BaseModel {
    id?: number;
    jobId: string;
    model_id:number;
    dataset_id:number;
    jod_description:string;
    dataset_name:string;
    dataset_description:string;
    dataset_scheduledatetime:string;
    completeddatetime:string;
    submitteddatetime:string;
    created_by:string;
    updated_by:string;
    createdAt:string;
    updatedAt:string;

    clear() {
        this.id=undefined;
        this.jobId=undefined;
        this.model_id=undefined;
        this.dataset_id=undefined;
        this.jod_description='';
        this.dataset_name='';
        this.dataset_description='';
        this.dataset_scheduledatetime='';
        this.completeddatetime='';
        this.submitteddatetime='';
        this.created_by='';
        this.updated_by='';
        this.createdAt='';
        this.updatedAt='';
    }
}
