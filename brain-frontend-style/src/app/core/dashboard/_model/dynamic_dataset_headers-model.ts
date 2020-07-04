import { BaseModel } from '../../_base/crud';


export class DynamicHeader extends BaseModel {
    id: number;
    model_id: number;
    dataset_id: number;   
    table_name:string;
    column_name:string;
    fieldSeqNo:number;
    display_name:string;
    column_type:string;
    column_format:string;
    is_editable:boolean;
    is_mandatory:boolean;
    is_visible:boolean;
    status:boolean;
    created_by:string;
    updated_by:string;
    createdAt:string;
    updatedAt:string;

    clear(id:number) {
        this.id = undefined;
        this.model_id = undefined;
        this.dataset_id= undefined; 
        this.table_name="";
        this.column_name="";
        this.fieldSeqNo=undefined;
        this.display_name="";
        this.column_type="";
        this.column_format="";
        this.is_editable=true;
        this.is_mandatory=true;
        this.is_visible=true;
        this.status=true;
        this.created_by="";
        this.updated_by="";
        this.createdAt="";
        this.updatedAt="";
    }
}
