import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import * as jspreadsheet from "jspreadsheet-ce";
import * as XLSX from 'xlsx'


@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: [],
  standalone: true,
  imports: [CommonModule, NgxDatatableModule]
})
export class SettingsComponent {

  
  @ViewChild("spreadsheet") spreadsheet!: ElementRef;
  title = "CodeSandbox";
  selected:number = 1;
 
  data = [

  ];

  columns = [
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

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    
  }

  ngAfterViewInit() {
    

    this.loadData();
  }

  changeSelect(num: number) {
    this.selected = num;
  }

  setUpJsSpreadsheet() {
    jspreadsheet(this.spreadsheet.nativeElement, {
      data: this.data,
        columns: this.columns,
      minDimensions: [10, 10]
    });
  }


  save() {
    let columnTitles = this.columns.map((column) => column.title);
    let data = [columnTitles, ...this.data];
    console.log(data);
    let worksheet = XLSX.utils.aoa_to_sheet(data);
    let workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, "Sheet1");
    let wopts = { bookType: 'xlsx', bookSST: false, type: "array" } as XLSX.WritingOptions;
    let wbout = XLSX.write(workbook, wopts);

    let blob = new Blob([wbout], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'});

    let formData = new FormData();
    formData.append('file', blob, 'spreadsheet.xlsx');
    
    this.http.post('api/controlled-substances/configure', formData).subscribe((response) => {
      console.log(response);
    });
  }

  loadData() {
    
    this.http.get('api/controlled-substances/configure', { responseType: 'blob' }).subscribe((response: any) => {
      const reader: FileReader = new FileReader();
      reader.onloadend = (e) => {
        const data = new Uint8Array(e.target!.result as ArrayBuffer);
        const workbook = XLSX.read(data, {type: 'array'});
        var firstSheetName = workbook.SheetNames[0];
        var worksheet = workbook.Sheets[firstSheetName];
        var sheetData = XLSX.utils.sheet_to_json(worksheet, {header: 1});
  
        var headers = sheetData.shift();
        this.data = sheetData as any;
        console.log(this.data);
        this.setUpJsSpreadsheet();
        
      };
      reader.readAsArrayBuffer(response);
      // read blob data

      

    });
  }
  
}
