import { BaseModel } from '../../_base/crud';

export class Group extends BaseModel {
    id: number;
    group_title: string;
    group_description: string;


    clear(): void {
        this.id = undefined;
        this.group_title = '';
        this.group_description = '';
	}
}