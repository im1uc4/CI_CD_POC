import { BaseModel } from '../../_base/crud';
//committed by kp due to obsolescence
//import { Address } from './address.model';
//import { SocialNetworks } from './social-networks.model';

export class User extends BaseModel {
    id: number;
    username: string;
    password: string;
    email: string;
    dislay_title:string;
    accessToken: string;
    refreshToken: string;
    roles: number[];
    groups: number[];    
    pic: string;
    fullname: string;   

    clear(): void {
        this.id = undefined;
        this.username = '';
        this.password = '';
        this.email = '';
        this.dislay_title='';
        this.roles = [];
        this.groups=[];
        this.fullname = '';
        this.accessToken = 'access-token-' + Math.random();
        this.refreshToken = 'access-token-' + Math.random();
        this.pic = './brain/assets/media/users/default.jpg';   
    }
}
