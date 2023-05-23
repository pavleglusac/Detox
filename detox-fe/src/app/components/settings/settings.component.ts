import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { TemplateComponent } from './template/template.component';


@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: [],
  standalone: true,
  imports: [CommonModule, NgxDatatableModule, TemplateComponent]
})
export class SettingsComponent {

  selected:number = 1;

  columnsGcSubstances = [
    { title: 'Naziv', width: 100 },
    { title: 'Min Kokain', width: 100 },
    { title: 'Max Kokain', width: 100 },
    { title: 'Vreme Kokain', width: 100 },
    { title: 'Min Opioid', width: 100 },
    { title: 'Max Opioid', width: 100 },
    { title: 'Time Opioid', width: 100 },
    { title: 'Min Meth', width: 100 },
    { title: 'Max Meth', width: 100 },
    { title: 'Time Meth', width: 100 },
    { title: 'Min Benz', width: 100 },
    { title: 'Max Benz', width: 100 },
    { title: 'Time Benz', width: 100 },
    { title: 'Min Syn. Can.', width: 100 },
    { title: 'Max Syn. Can.', width: 100 },
    { title: 'Time Syn. Can.', width: 100 },
]

columnsGcIndustry = [
  { title: 'Naziv', width: 100 },
  { title: 'Min Benzen', width: 100 },
  { title: 'Max Benzen', width: 100 },
  { title: 'Vreme Benzen', width: 100 },
  { title: 'Min Toluen', width: 100 },
  { title: 'Max Toluen', width: 100 },
  { title: 'Time Toluen', width: 100 },
  { title: 'Min Formaldehid', width: 100 },
  { title: 'Max Formaldehid', width: 100 },
  { title: 'Time Formaldehid', width: 100 },
  { title: 'Min Polihlorovani bifenili', width: 100 },
  { title: 'Max Polihlorovani bifenili  ', width: 100 },
  { title: 'Time Polihlorovani bifenili  ', width: 100 },
  { title: 'Min Vinil hlorid', width: 100 },
  { title: 'Max Vinil hlorid', width: 100 },
  { title: 'Time Vinil hlorid', width: 100 },
]


  changeSelect(num: number) {
    this.selected = num;
  }

  get columns() {
    if (this.selected === 1 ) {
      return this.columnsGcSubstances;
    } else if (this.selected === 2) {
      return this.columnsGcIndustry;
    }
    return this.columnsGcIndustry;
  }

  get api() {
    if (this.selected === 1 ) {
      return 'api/controlled-substances/configure';
    } else if (this.selected === 2) {
      return 'api/industry/gas-chromatography/configure';
    }
    return 'api/industry/spectophotometria/configure';
  }
  
  get title() {
    if (this.selected === 1 ) {
      return 'Parametari gasne hromatografije kod kontrolisanih supstanci:';
    } else if (this.selected === 2) {
      return 'Parametari gasne hromatografije kod industrijskih toksina:';
    }
    return 'Parametari spektofotometrije kod industrijskih toksina:';
  }

}
