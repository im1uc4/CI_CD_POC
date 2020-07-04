export class PageConfig {
	public defaults: any = {
		dashboard: {
			page: {
				'title': 'Brain Dashboard',
				'desc': 'Generate optimised roster'
			},
		},
	
	};

	public get configs(): any {
		return this.defaults;
	}
}
