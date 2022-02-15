import { Reception } from './reception.model';
import { Article } from 'src/app/shared/models/article.model';
export class ReceptionArticle {

  id: number
  isReception: boolean
  quantite: number
  createdAt: Date
  updatedAt: Date
  article: Article
  reception: Reception
}
