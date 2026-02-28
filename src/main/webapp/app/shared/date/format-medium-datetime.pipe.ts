import { Pipe, PipeTransform } from '@angular/core';

import dayjs from 'dayjs/esm';

@Pipe({
  name: 'formatMediumDatetime',
})
export default class FormatMediumDatetimePipe implements PipeTransform {
  transform(day: dayjs.Dayjs | Date | string | null | undefined): string {
    if (!day) {
      return '';
    }

    try {
      // Si c'est une string, la convertir en objet dayjs
      if (typeof day === 'string') {
        return dayjs(day).format('DD/MM/YYYY HH:mm:ss');
      }

      // Si c'est un objet Date
      if (day instanceof Date) {
        return dayjs(day).format('DD/MM/YYYY HH:mm:ss');
      }

      // Si c'est déjà un objet dayjs
      return day.format('DD/MM/YYYY HH:mm:ss');
    } catch (e) {
      console.error('Erreur de formatage:', e);
      return String(day);
    }
  }
}
