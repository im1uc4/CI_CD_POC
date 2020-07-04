import { AbstractControl, ValidatorFn} from '@angular/forms';
import { DataSetDataModel } from '../../../../core/dashboard/_model/dataset_data-model';

export function UniquenessValidator(Items: DataSetDataModel[], tableName:string, fieldName:string): ValidatorFn {
        return (control: AbstractControl): { [key: string]: any } | null  => {
            let count=0;
           let metaDataElement= Items.filter(res => res.table_name === tableName && res.field_name===fieldName && res.value===control.value);
            if (metaDataElement.length>1) {
                return { 'idUniq': true};
            }
            return null; 
        }
}
 
 
