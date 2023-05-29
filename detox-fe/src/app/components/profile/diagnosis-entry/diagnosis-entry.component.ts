import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition, faCheckDouble, faArrowsRotate } from '@fortawesome/free-solid-svg-icons';
import { DiagnosisEntry } from 'src/app/model/diagnose';

@Component({
  selector: 'app-diagnosis-entry',
  templateUrl: './diagnosis-entry.component.html',
  styleUrls: [],
  standalone: true,
  imports: [CommonModule, FontAwesomeModule]
})
export class DiagnosisEntryComponent {

  @Input() diagnosis: DiagnosisEntry | null = null;

  faDone: IconDefinition = faCheckDouble;
  faNotDone: IconDefinition = faArrowsRotate;

}
