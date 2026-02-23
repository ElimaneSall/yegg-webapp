import dayjs from 'dayjs/esm';
import { IOperateur } from 'app/entities/operateur/operateur.model';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { ReportType } from 'app/entities/enumerations/report-type.model';
import { ReportFormat } from 'app/entities/enumerations/report-format.model';

export interface IRapport {
  id: number;
  nom?: string | null;
  type?: keyof typeof ReportType | null;
  periodeDebut?: dayjs.Dayjs | null;
  periodeFin?: dayjs.Dayjs | null;
  format?: keyof typeof ReportFormat | null;
  contenu?: string | null;
  dateGeneration?: dayjs.Dayjs | null;
  generePar?: string | null;
  operateur?: Pick<IOperateur, 'id'> | null;
  admin?: Pick<IUtilisateur, 'id'> | null;
}

export type NewRapport = Omit<IRapport, 'id'> & { id: null };
