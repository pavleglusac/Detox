import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { User } from 'src/app/model/user';
import { UserStateService } from 'src/app/services/store/user-state.service';
import { IconDefinition, faEnvelope } from '@fortawesome/free-solid-svg-icons';
import { DiagnosisEntry } from 'src/app/model/diagnose';
import { DiagnosisEntryComponent } from './diagnosis-entry/diagnosis-entry.component';
import { DiagnoseService } from 'src/app/services/diagnose.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: [],
  standalone: true,
  imports: [CommonModule, FontAwesomeModule, DiagnosisEntryComponent]
})
export class ProfileComponent implements OnInit{
  user: User | null = null;
  diagnosis: DiagnosisEntry[] = [];
  faEmail: IconDefinition = faEnvelope;

  constructor(private userStateService: UserStateService, private diagnoseService: DiagnoseService, private toastr: ToastrService) {
    this.userStateService.getUserState().subscribe((user: User | null) => {
      this.user = user;
    });
  }

  ngOnInit(): void {
    this.diagnoseService.loadDiagnosis(
      this.user!.email,
      (value: DiagnosisEntry[]) => {
        console.log(value);
        this.diagnosis = value;},
      (error: any) => {this.toastr.error(error.message)}
    );
  }

}
