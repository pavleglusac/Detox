import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { NavbarComponent } from '../navbar/navbar.component';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: [],
  standalone: true,
  imports: [CommonModule, NavbarComponent, RouterModule]
})
export class HomepageComponent {

}
