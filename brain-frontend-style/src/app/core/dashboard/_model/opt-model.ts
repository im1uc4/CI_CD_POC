import { BaseModel } from '../../_base/crud';
import { OptModelConstraints } from './opt-model-constraints';
import { OptModelConstraints_param } from './opt-model-constraints-param';

export class OptModel extends BaseModel {
    id: number;
    model_name:string;
    model_description_header:string;
    model_description_details:string;
    person_in_charge:string;
    algorithm_id:number;
    model_status:boolean;
    created_by:string;
    updated_by:string;

    //_constraints:OptModelConstraints[]
    //_constraints_param:OptModelConstraints_param[]

    clear() {
        this.model_name = '';
        this.model_description_header = '';   
        this.model_description_details ='';
        this.person_in_charge='';
        this.algorithm_id=0;
        this.model_status=true;
        this.created_by='';
        this.updated_by='';
    }
}
