import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { FormsModule } from '@angular/forms';
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { IconDefinition, faSearch, faChevronDown, faChevronUp } from "@fortawesome/free-solid-svg-icons";
import { ToastrService } from "ngx-toastr";
import { QueryService } from "src/app/services/query.service";

interface QueryOptions {
  query: string;
  queryInput: string | null;
  result: string[] | null;
  show: boolean;
  showErrMsg?: boolean;
  apiPath: string;
  reqParamName?: string;
}


@Component({
  selector: 'app-query',
  templateUrl: './query.component.html',
  styleUrls: [],
  standalone: true,
  imports: [CommonModule, FormsModule, FontAwesomeModule]
})
export class QueryComponent {

  constructor(private queryService: QueryService, private toastr: ToastrService) { }
  
  options: QueryOptions[] = [
  { query: "Pregled svih testova neophodnih za otkrivanje toksina:", queryInput: "", result: null, show: false, showErrMsg: false, apiPath: '', reqParamName: '' },
    { query: "Pregled svih daljih testova za određenog pacijenta:", queryInput: "", result: null, show: false, showErrMsg: false, apiPath: 'further-tests',reqParamName: 'username' },
    { query: "Pregled svih pacijenata koji su mogući korisnici teških droga (Kokain, Amfetamini)", queryInput: null, result: null, show: false, apiPath: 'potential-heavy-drugs-users' },
    { query: "Pregled svih životno ugroženih radnika u industriji (Ugljen-disulfid, Cijanid)", queryInput: null, result: null, show: false, apiPath: '' },
  ]

  faView: IconDefinition = faSearch;
  faShowResult: IconDefinition = faChevronDown;
  faHideResult: IconDefinition = faChevronUp;

  search = (option: QueryOptions) => {
    let reqParam = option.reqParamName ? `?${option.reqParamName}=${option.queryInput}` : '';
    if (option.queryInput === '') {
      option.showErrMsg = true;
      return;
    } else option.showErrMsg = false;
    
    option.result = null;

    this.queryService.search(
      option.apiPath,
      reqParam,
      (resultList: any) => {
        if (resultList.length === 0) {
          this.toastr.info('Nema rezultata za zadati upit'); 
          return;
        }
        option.result = resultList;
        option.show = true;
      },
      (error: any) => {
        this.toastr.error(error.message)
      },
    );
  }

  toggleShowResult = (option: QueryOptions) => {
    option.show = !option.show;
  }

}
