export const environment = {
	production: true,
	isMockEnabled: false, // You have to switch this, when your real back-end is done
	authTokenKey: 'authce9d77b308c149d5992a80073637e4d5',
	is_ssl:false,
	get apiURL(){
		return ((this.is_ssl)?'https':'http')+'://localhost/OptEngineBackEnd'
   },
   get engineAPI(){
	return ((this.is_ssl)?'https':'http')+'://localhost/OptEngREST'
   }
};
