import { AffectationArticle } from './affectation-article.model';
import { Article } from './article.model';
import { Employee } from './employee.model';
import { FileUpload } from './file-upload.model';
export class Affectation {
  id: number
  dateAffectation: Date
  quantite: number
  nom: string
  prenom: string
  createdAt: Date
  updateAt: Date
  employee: Employee
  externe:boolean
  structure:string
  affectant:string
  articles: Article[]
  affectationArticles:AffectationArticle[]
  fileUploads:FileUpload[]

}
