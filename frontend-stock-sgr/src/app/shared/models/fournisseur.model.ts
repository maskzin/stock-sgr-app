import { Reception } from "./reception.model"

export class Fournisseur {
  id: number
  nom: string
  prenom: string
  telehone: string
  nif:string
  adresse: string
  createdAt: Date
  updateAt: Date
  receptions: Reception[]
}
