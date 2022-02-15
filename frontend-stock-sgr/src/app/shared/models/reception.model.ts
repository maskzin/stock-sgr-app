import { FileUpload } from 'src/app/shared/models/file-upload.model';
import { ReceptionArticle } from './reception-article.model';
import { Fournisseur } from './fournisseur.model';
import { Employee } from './employee.model';
import { Article } from './article.model';
export class Reception {
  id: number
  numContrat: string
  dateContrat: Date
  dateReception: Date
  createdAt: Date
  updateAt: Date
  quantite:number
  receptionArticles: ReceptionArticle[]
  employees: Employee[]
  fournisseur: Fournisseur
  fileUploads:FileUpload[]
}
