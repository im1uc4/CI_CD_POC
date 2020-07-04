import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'listfilter'
})
export class ListFilterPipe implements PipeTransform {
    transform(value: Array<any>, field: string): Array<any> {
        return value.filter((res, index) => {
            return value.findIndex((x) => {
            return x[field] == res[field];}) == index;
        });

       //Object.keys(filterObj).map(key => ({ key, value: filterObj[key] }));
    }
}