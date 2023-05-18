import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { HomepageComponent } from './components/homepage/homepage.component';
import { AuthGuard } from './shared/guards/auth.guard';
import { DiagnoseComponent } from './components/diagnose/diagnose.component';
import { QueryComponent } from './components/query/query.component';
import { ProfileComponent } from './components/profile/profile.component';

const routes: Routes = [
  {path: '', canActivate: [AuthGuard], component: HomepageComponent, 
  children: [
    {path: '', component: DiagnoseComponent},
    {path: 'query', component: QueryComponent},
    {path: 'profile', component: ProfileComponent}
  ]
  },
  {path: 'login', component: LoginComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
