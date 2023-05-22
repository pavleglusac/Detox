import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { FormsModule } from '@angular/forms';
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { IconDefinition, faSearch, faChevronDown, faChevronUp } from "@fortawesome/free-solid-svg-icons";

interface QueryOptions {
  query: string;
  queryInput: string | null;
  result: string[] | null;
  show: boolean;
}


@Component({
  selector: 'app-query',
  templateUrl: './query.component.html',
  styleUrls: [],
  standalone: true,
  imports: [CommonModule, FormsModule, FontAwesomeModule]
})
export class QueryComponent {
  
  options: QueryOptions[] = [
  { query: "Pregled svih testova neophodnih za otkrivanje toksina:", queryInput: "", result: null, show: false },
    { query: "Pregled svih daljih testova za određenog pacijenta:", queryInput: "", result: null, show: false },
    { query: "Pregled svih pacijenata koji su mogući korisnici teških droga (Kokain, Amfetamini)", queryInput: null, result: null, show: false },
    { query: "Pregled svih životno ugroženih radnika u industriji (Ugljen-disulfid, Cijanid)", queryInput: null, result: null, show: false },
  ]

  faView: IconDefinition = faSearch;
  faShowResult: IconDefinition = faChevronDown;
  faHideResult: IconDefinition = faChevronUp;

  search = (option: QueryOptions) => {
    option.show = true;
    option.result = ["Rezultat 1", "Rezultat 2", "Rezultat 3"]
  }

  toggleShowResult = (option: QueryOptions) => {
    option.show = !option.show;
  }

}
