import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { User } from 'src/app/model/user';
import { UserStateService } from 'src/app/services/store/user-state.service';
import { IconDefinition, faEnvelope } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: [],
  standalone: true,
  imports: [CommonModule, FontAwesomeModule]
})
export class ProfileComponent {
  user: User | null = null;
  faEmail: IconDefinition = faEnvelope;

  constructor(private userStateService: UserStateService) {
    this.userStateService.getUserState().subscribe((user: User | null) => {
      this.user = user;
    });
  }

}
