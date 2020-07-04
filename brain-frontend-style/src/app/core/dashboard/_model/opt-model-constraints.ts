import { BaseModel } from '../../_base/crud';

export class OptModelConstraints extends BaseModel {
    id: number;  
    model_id:number;  
    constraintName:string;
    constraintNum:number;
    constraint_type_id:number;
    scoreLevel:string;
    scoreLeveldescription:string;
    scoreWeight:number;
    status:boolean;
    created_by:string;
    updated_by:string;
    createdAt:string;
    updatedAt:string;

clear(id:number) {
    this.id = undefined;
    this.constraintName='';
    this.constraintNum=null;
    this.constraint_type_id=0;
    this.model_id=0;
    this.scoreLevel='';
    this.scoreLeveldescription='';
    this.scoreWeight=0;
    this.status=false;
    this.created_by='';
    this.updated_by='';
    this.createdAt='';
    this.updatedAt='';
    }
}

