export class QueryResultsModel {
	// fields
	items: any[];
	totalCount: number;
	errorMessage: string;
	recordId:number;
	dataModified:boolean;
	lastAffectedTable:string

	constructor(_items: any[] = [], _totalCount: number = 0,  _recordId?, _datamodified?,  _errorMessage: string = '', _lastAffectedTable?) {
		this.items = _items;
		this.totalCount = _totalCount;
		this.recordId= _recordId;
		this.dataModified= _datamodified;
		this.lastAffectedTable = _lastAffectedTable
	}
}