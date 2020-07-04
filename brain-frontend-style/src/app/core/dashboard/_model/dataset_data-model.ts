import { BaseModel } from '../../_base/crud';

export class DataSetDataModel extends BaseModel {
    id: number;
    record_id:number;    
    table_name:string;  
    table_display_name:string;
    dataset_id:number;
    field_name:string;
    value:string;
    status:boolean;
    created_by:string;
    updated_by:string;
    createdAt:string;
    updatedAt:string;   

    clear(id:number) {
        this.id             = undefined;
        this.record_id      = undefined;
        this.table_name     = "";
        this.table_display_name="";
        this.dataset_id     = undefined;
        this.field_name     ="";
        this.value          ="";
        this.status         =true;
        this.created_by     ="";
        this.updated_by     ="";
        this.createdAt      ="";
        this.updatedAt      ="";
    }
}
