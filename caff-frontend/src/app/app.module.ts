import { BrowserModule } from '@angular/platform-browser';
import {  NgModule } from '@angular/core';
import { HttpClientModule } from "@angular/common/http";
import { RouterModule, Routes } from "@angular/router";
import { FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatToolbarModule} from '@angular/material/toolbar';
import { MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon"

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { HeaderComponent } from './components/header/header.component';
import { LogoutComponent } from './components/logout/logout.component';

import { RegisterComponent } from './components/register/register.component';
import { authInterceptorProviders } from './helpers/auth.interceptor';
import {CaffListComponent} from "./components/caff-list/caff-list.component";
import { CaffComponent } from './components/caff/caff.component';
import { AuthGuard } from './helpers/auth-guard';
import { CartComponent } from './components/cart/cart.component';
import { ProfileComponent } from './components/profile/profile.component';
import { AdminComponent } from './components/admin/admin.component';
const routers: Routes = [
  {path: 'header', component: HeaderComponent,},
  { path: 'home', component: HomeComponent, canActivate: [AuthGuard]},
  { path: 'login', component: LoginComponent},
  { path: 'register', component: RegisterComponent },
  { path: 'logout', component: LogoutComponent},
  { path: 'cart', component: CartComponent, canActivate: [AuthGuard]},
  { path: 'profile/:username', component: ProfileComponent, canActivate: [AuthGuard]},
  { path: 'admin', component: AdminComponent, canActivate: [AuthGuard]},

  { path: 'caffs', component: CaffListComponent, canActivate: [AuthGuard]},
  { path: 'caff/:id', component: CaffComponent, canActivate: [AuthGuard]},
  // otherwise redirect to home
  { path: '**', redirectTo: 'home' }
];

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    LogoutComponent,
    CaffListComponent,
    CaffComponent,
    CartComponent,
    ProfileComponent,
    AdminComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot(routers),
    FormsModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    ReactiveFormsModule,
    MatButtonModule,
  ],
  providers: [authInterceptorProviders, AuthGuard],
  bootstrap: [AppComponent]
})
export class AppModule { }

