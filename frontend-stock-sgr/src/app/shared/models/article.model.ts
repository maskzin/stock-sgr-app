import { Marque } from './marque.model';
import { Affectation } from './affectation.model';
import { Categorie } from "./categorie.model"


export class Article{
  id: number
  libelleArticle: string
  caracteristique: string
  niveauAlerte: number
  isSelected:boolean
  isReception:boolean
  commentaire: string
  stock: number
  createdAt: string
  updateAt: string
  receptionArticles: any
  affectationArticles:any
  affectations: Affectation[]
  categorie: Categorie
  marque:Marque
  type:string
}
