import { BaseModel } from '../../_base/crud';

export class JobInProgressModel extends BaseModel {
    id:number;
    jobId: string;
    jobStatus:string; 
 

    clear(id:number) {
        this.id             = undefined;
        this.jobId          = "";
        this.jobStatus      = "";
    }
}
