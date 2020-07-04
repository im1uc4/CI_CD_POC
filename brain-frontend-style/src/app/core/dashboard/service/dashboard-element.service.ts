import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { BehaviorSubject } from 'rxjs';

import { DashboardElement } from '../element/dashboard-element';

export interface IDashboardService {
  add(dashElement: DashboardElement)
  queryInFolder(folderId: string): Observable<DashboardElement[]>
  get(id: string): DashboardElement
}

@Injectable({
  providedIn: 'root'
})
export class DashboardElement_Service implements  IDashboardService{
  private map = new Map<string, DashboardElement>()
  private querySubject: BehaviorSubject<DashboardElement[]>
  
  add(dashElement: DashboardElement) {
    this.map.set(dashElement.id, this.clone(dashElement));
    return dashElement;
  }
  queryInFolder(folderId: string) {
    const result: DashboardElement[] = []

    this.map.forEach(element => {
      if (element.location === folderId) {
        result.push(this.clone(element))
      }
    })
    if (!this.querySubject) {
      this.querySubject = new BehaviorSubject(result)
    } else {
      this.querySubject.next(result)
    }
    return this.querySubject.asObservable()
  }

  get(id: string) {
    return this.map.get(id)
  }    
 

  clone(element: DashboardElement) {
    return JSON.parse(JSON.stringify(element))
  }

  constructor() { }


}
