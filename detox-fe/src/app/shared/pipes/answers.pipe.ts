import { Pipe, PipeTransform } from '@angular/core';


@Pipe({name: 'toSerbian', standalone: true})
export class AnswersPipe implements PipeTransform {
  transform(value: any, ...args: any[]) {
    value = value.toLowerCase();
    switch(value) {
      case 'true': return 'Pozitivno';
      case 'false': return 'Negativno';
      case 'lower': return 'Donji disajni putevi';
      case 'upper': return 'Gornji disajni putevi';
      case 'medicine': return 'Lekovi';
      case 'drugs': return 'Psihoaktivne-kontrolisane supstance';
      default: return `${value[0].toUpperCase()}${value.slice(1).toLowerCase()}`
 }
  }
}