import { Division } from './division.model';
import { Reception } from './reception.model';
import { Affectation } from './affectation.model';
export class Employee {
  id: number
  nom: string
  prenom: string
  createdAt: Date
  updateAt: Date
  affectations: Affectation[]
  receptions: Reception[]
  division: Division
  telephone:string
  adresse:string
}
