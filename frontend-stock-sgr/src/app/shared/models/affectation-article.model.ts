import { Article } from 'src/app/shared/models/article.model';
import { Affectation } from './affectation.model';
export class AffectationArticle {
  id: number
  isAffectation: boolean
  quantite: number
  reference:string
  createdAt: Date
  updatedAt: Date
  article: Article
  affectations: Affectation[]
  validAt:Date
  isValidity:boolean
}
