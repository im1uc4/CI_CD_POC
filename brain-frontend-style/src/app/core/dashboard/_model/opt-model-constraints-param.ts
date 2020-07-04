import { BaseModel } from '../../_base/crud';

export class OptModelConstraints_param extends BaseModel {
    id: number;
    constr_id:number;
    parm_name:string;
    parm_description:string;
    parm_mandatory:boolean;
    parm_editable:boolean;
    parm_datatype:string;
    constraint_parameter_value:string;
    param_status:string;    
    created_by:string;
    updated_by:string;


clear(id:number) {
    this.id = undefined;
    this.constr_id=0;
    this.parm_name='';
    this.parm_description='';
    this.parm_mandatory=false;
    this.parm_editable=true;
    this.parm_datatype='';
    this.constraint_parameter_value='';
    this.param_status='';    
    this.created_by='';
    this.updated_by='';
    }
}



